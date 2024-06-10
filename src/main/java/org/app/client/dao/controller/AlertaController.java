package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.conexao.ConexaoSql;
import org.app.client.dao.entity.Alerta;
import org.app.client.dao.entity.Computador;
import org.app.client.util.captura.AtualizarAlerta;
import org.app.client.util.notificacoes.NotificacaoSlack;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class AlertaController {

    ConexaoSql conexao = new ConexaoSql();

    public void getAllAlertasCPU(Computador computador){
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        List<Alerta> alertas = jdbcTemplate.query("SELECT DISTINCT pc.idComputador,\n" +
                "                        COUNT(rc.valor) AS 'quantidade'\n" +
                "           FROM RegistroComponente rc\n" +
                "           JOIN Componente c ON c.idComponente = rc.fkComponente\n" +
                "           JOIN Computador pc ON c.fkComputador = pc.idComputador\n" +
                "           JOIN DarkStore d ON d.idDarkStore = pc.fkDarkStore\n" +
                "           WHERE dataRegistro >= DATEADD(MINUTE, -30000, GETDATE())\n" +
                "           AND c.nome LIKE 'Processador'\n" +
                "           AND rc.valor > '80'\n" +
                "           AND pc.idComputador = ?\n" +
                "           GROUP BY pc.idComputador;", new BeanPropertyRowMapper<>(Alerta.class), computador.getIdComputador());


        alertas.stream().filter(Objects::nonNull).forEach(alerta -> inserindoAlertaCPU(alerta, computador));
    }

    public void getAllAlertasRAM(Computador computador){
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        List<Alerta> alertas = jdbcTemplate.query("SELECT DISTINCT pc.idComputador,\n" +
                "       COUNT(rc.valor) AS 'quantidade'\n" +
                "FROM RegistroComponente rc\n" +
                "JOIN Componente c ON c.idComponente = rc.fkComponente\n" +
                "JOIN Computador pc ON c.fkComputador = pc.idComputador\n" +
                "JOIN DarkStore d ON d.idDarkStore = pc.fkDarkStore\n" +
                "WHERE dataRegistro >= DATEADD(MINUTE, -30000, GETDATE())\n" +
                "  AND c.nome LIKE 'Memória'\n" +
                "  AND CAST(REPLACE(rc.valor, ',', '.') as FLOAT) > CAST(\n" +
                "                         REPLACE(SUBSTRING(\n" +
                "                             (SELECT TOP 1 ca.valor\n" +
                "                              FROM CaracteristicaComponente ca\n" +
                "                              JOIN Componente c2 ON c2.idComponente = ca.fkComponente\n" +
                "                              JOIN Computador pc2 ON pc2.idComputador = c2.fkComputador\n" +
                "                              WHERE pc2.idComputador = ?\n" +
                "                                AND c2.nome LIKE 'Memória'\n" +
                "                                AND ca.nome LIKE 'Memória Total'), \n" +
                "                             1, \n" +
                "                             CHARINDEX(' ', \n" +
                "                                       (SELECT TOP 1 ca.valor\n" +
                "                                        FROM CaracteristicaComponente ca\n" +
                "                                        JOIN Componente c2 ON c2.idComponente = ca.fkComponente\n" +
                "                                        JOIN Computador pc2 ON pc2.idComputador = c2.fkComputador\n" +
                "                                        WHERE pc2.idComputador = ?\n" +
                "                                          AND c2.nome LIKE 'Memória'\n" +
                "                                          AND ca.nome LIKE 'Memória Total'\n" +
                "                                       ) + ' '\n" +
                "                             )), ',', '.') AS float\n" +
                "                         ) * 0.8\n" +
                "  AND pc.idComputador = ?\n" +
                "GROUP BY pc.idComputador;", new BeanPropertyRowMapper<>(Alerta.class), computador.getIdComputador(), computador.getIdComputador(), computador.getIdComputador());

        alertas.stream().filter(Objects::nonNull).forEach(a -> inserindoAlertaRAM(a, computador));
    }

    public void getAllAlertasDisco(Computador computador){
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        List<Alerta> alertas = jdbcTemplate.query("SELECT DISTINCT pc.idComputador\n" +
                "        FROM Componente c\n" +
                "        JOIN Computador pc ON c.fkComputador = pc.idComputador\n" +
                "        JOIN CaracteristicaComponente ca ON ca.fkComponente = c.idComponente\n" +
                "        WHERE c.nome LIKE 'Disco'\n" +
                "        AND CAST(REPLACE(SUBSTRING(\n" +
                "        (SELECT TOP 1 ca.valor\n" +
                "                FROM CaracteristicaComponente ca\n" +
                "                JOIN Componente c ON c.idComponente = ca.fkComponente\n" +
                "                JOIN Computador pc ON pc.idComputador = c.fkComputador\n" +
                "                WHERE pc.idComputador = ?\n" +
                "                AND c.nome LIKE 'Disco'\n" +
                "                AND ca.nome LIKE 'Memória Disponível'),\n" +
                "                1, CHARINDEX(' ', (SELECT TOP 1 ca.valor\n" +
                "       FROM CaracteristicaComponente ca\n" +
                "                    JOIN Componente c ON c.idComponente = ca.fkComponente\n" +
                "                    JOIN Computador pc ON pc.idComputador = c.fkComputador\n" +
                "                    WHERE pc.idComputador = ?\n" +
                "                    AND c.nome LIKE 'Disco'\n" +
                "                    AND ca.nome LIKE 'Memória Disponível') + ' ') - 1), ',', '.') AS FLOAT) < CAST(REPLACE(SUBSTRING(\n" +
                "          (SELECT TOP 1 ca.valor FROM CaracteristicaComponente ca JOIN Componente c ON c.idComponente = ca.fkComponente\n" +
                "       JOIN Computador pc ON pc.idComputador = c.fkComputador\n" +
                "       WHERE pc.idComputador = ?\n" +
                "       AND c.nome LIKE 'Disco'\n" +
                "       AND ca.nome LIKE 'Memória Total'),\n" +
                "       1, CHARINDEX(' ', (SELECT TOP 1 ca.valor\n" +
                "                    FROM CaracteristicaComponente ca\n" +
                "                   JOIN Componente c ON c.idComponente = ca.fkComponente\n" +
                "                   JOIN Computador pc ON pc.idComputador = c.fkComputador\n" +
                "                   WHERE pc.idComputador = ?\n" +
                "                   AND c.nome LIKE 'Disco'\n" +
                "                   AND ca.nome LIKE 'Memória Total') + ' ') - 1), ',', '.') AS FLOAT) * 0.2\n" +
                "      AND pc.idComputador = ?;", new BeanPropertyRowMapper<>(Alerta.class), computador.getIdComputador(), computador.getIdComputador(), computador.getIdComputador(), computador.getIdComputador(), computador.getIdComputador());

        alertas.stream().filter(Objects::nonNull).forEach(alerta -> inserindoAlertaDisco(alerta, computador));
    }

    public void getAllAlertasRede(Computador computador){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();

        List<Alerta> alertas = getConexao.query("SELECT DISTINCT(pc.idComputador), COUNT(rc.valor) as 'quantidade' \n" +
                "FROM Componente c \n" +
                "JOIN Computador pc ON c.fkComputador = pc.IdComputador \n" +
                "JOIN CaracteristicaComponente ca ON ca.fkComponente = c.idComponente \n" +
                "JOIN RegistroComponente rc ON rc.fkComponente = c.idComponente \n" +
                "WHERE WHERE rc.dataRegistro >= DATEADD(MINUTE, -5, GETDATE()) \n" +
                "  AND (c.nome LIKE 'Rede' \n" +
                "       AND ((rc.nome LIKE 'Ping' AND rc.valor > '100') \n" +
                "            OR (rc.nome LIKE 'Download' AND rc.valor < '5') \n" +
                "            OR (rc.nome LIKE 'Upload' AND rc.valor < '5'))\n" +
                "      ) \n" +
                "  AND pc.idComputador = ? \n" +
                "GROUP BY pc.idComputador;", new BeanPropertyRowMapper<>(Alerta.class), computador.getIdComputador());

            alertas.forEach(alerta -> inserindoAlertaRede(alerta, computador));

    }

    public void inserindoAlertaCPU(Alerta alerta, Computador computador){

        String status = "";

        if(alerta.getQuantidade() > 25){
            status = "Crítico";
        }else if(alerta.getQuantidade() >= 15){
            status = "Alerta";
        }else{
            status = "Normal";
        }

        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();
        List<AtualizarAlerta> ultimaMedicao = jdbcTemplate.query("SELECT TOP 1 l.dataLog as 'data' FROM Log l JOIN Computador c ON l.fkComputador = c.idComputador WHERE c.idComputador = ? AND (l.descricao LIKE '%estado%' AND l.descricao LIKE '%CPU%') ORDER BY l.idLog DESC", new BeanPropertyRowMapper<>(AtualizarAlerta.class), computador.getIdComputador());

        if(ultimaMedicao.isEmpty()){
            ultimaMedicao.add(new AtualizarAlerta(LocalDateTime.now(ZoneId.of("UTC"))));
        }else if(ultimaMedicao.isEmpty() && !status.equals("Normal")){
            jdbcTemplate.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "Uso excessivo de CPU na máquina %s com estado %s".formatted(computador.getNome(), status), computador.getIdComputador());
            System.out.println("Valor de ocorrências: %.2f, status %s".formatted(alerta.getQuantidade(), status));
            try {
                NotificacaoSlack.EnviarNotificacaoSlack("Uso excessivo de CPU na máquina %s com estado %s".formatted(computador.getNome(), status));
            } catch (Exception e) {
                System.out.println("Houve um problema de conexão com o slack");
            }
        }

        if((status.equals("Crítico") || status.equals("Alerta")) && (ChronoUnit.MINUTES.between(ultimaMedicao.get(0).getData(), LocalDateTime.now(ZoneId.of("UTC"))) > 5)){
            jdbcTemplate.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "Uso excessivo de CPU na máquina %s com estado %s".formatted(computador.getNome(), status), computador.getIdComputador());
            System.out.println("Valor de ocorrências: %.2f, status %s".formatted(alerta.getQuantidade(), status));
            try {
                NotificacaoSlack.EnviarNotificacaoSlack("Uso excessivo de CPU na máquina %s com estado %s".formatted(computador.getNome(), status));
            } catch (Exception e) {
                System.out.println("Houve um problema de conexão com o slack");
            }
        }


    }

    public void inserindoAlertaRAM(Alerta alerta, Computador computador){

        String status = "";

        if(alerta.getQuantidade() > 25){
            status = "Crítico";
        }else if(alerta.getQuantidade() >= 15){
            status = "Alerta";
        }else{
            status = "Normal";
        }

        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();
        List<AtualizarAlerta> ultimaMedicao = jdbcTemplate.query("SELECT TOP 1 l.dataLog as 'data' FROM Log l JOIN Computador c ON l.fkComputador = c.idComputador WHERE c.idComputador = ? AND (l.descricao LIKE '%estado%' AND l.descricao LIKE '%RAM%') ORDER BY l.idLog DESC", new BeanPropertyRowMapper<>(AtualizarAlerta.class), computador.getIdComputador());

        if(ultimaMedicao.isEmpty()){
            ultimaMedicao.add(new AtualizarAlerta(LocalDateTime.now(ZoneId.of("UTC"))));
        }else if(ultimaMedicao.isEmpty() && !status.equals("Normal")){
            jdbcTemplate.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "Uso excessivo de RAM na máquina %s com estado %s".formatted(computador.getNome(), status), computador.getIdComputador());
            try {
                NotificacaoSlack.EnviarNotificacaoSlack("Uso excessivo de RAM na máquina %s com estado %s".formatted(computador.getNome(), status));
            } catch (Exception e) {
                System.out.println("Houve um problema de conexão com o slack");
            }
        }

        if((status.equals("Crítico") || status.equals("Alerta")) && (ChronoUnit.MINUTES.between(ultimaMedicao.get(0).getData(), LocalDateTime.now(ZoneId.of("UTC"))) > 5)){

            jdbcTemplate.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "Uso excessivo de RAM na máquina %s com estado %s".formatted(computador.getNome(), status), computador.getIdComputador());
            try {
                NotificacaoSlack.EnviarNotificacaoSlack("Uso excessivo de RAM na máquina %s com estado %s".formatted(computador.getNome(), status));
            } catch (Exception e) {
                System.out.println("Houve um problema de conexão com o slack");
            }
        }
    }

    public void inserindoAlertaDisco(Alerta alerta, Computador computador){

        String status = "";

        if(alerta.getIdComputador() != null){
            status = "Crítico";
        }else{
            status = "Normal";
        }

        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();
        List<AtualizarAlerta> ultimaMedicao = jdbcTemplate.query("SELECT TOP 1 l.dataLog as 'data' FROM Log l JOIN Computador c ON l.fkComputador = c.idComputador WHERE c.idComputador = ? AND (l.descricao LIKE '%estado%' AND l.descricao LIKE '%Disco%') ORDER BY l.idLog DESC", new BeanPropertyRowMapper<>(AtualizarAlerta.class), computador.getIdComputador());

        if(ultimaMedicao.isEmpty()){
            ultimaMedicao.add(new AtualizarAlerta(LocalDateTime.now(ZoneId.of("UTC"))));
        }else if(ultimaMedicao.isEmpty() && !status.equals("Normal")){
            jdbcTemplate.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "Uso excessivo de Disco na máquina %s com estado %s".formatted(computador.getNome(), status), computador.getIdComputador());

            try {
                NotificacaoSlack.EnviarNotificacaoSlack("Uso excessivo de Disco na máquina %s com estado %s".formatted(computador.getNome(), status));
            } catch (Exception e) {
                System.out.println("Houve um problema de conexão com o slack");
            }
        }

        if(status.equals("Crítico") && ChronoUnit.MINUTES.between(ultimaMedicao.get(0).getData(), LocalDateTime.now(ZoneId.of("UTC"))) > 5){
            jdbcTemplate.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "Uso excessivo de Disco na máquina %s com estado %s".formatted(computador.getNome(), status), computador.getIdComputador());

            try {
                NotificacaoSlack.EnviarNotificacaoSlack("Uso excessivo de Disco na máquina %s com estado %s".formatted(computador.getNome(), status));
            } catch (Exception e) {
                System.out.println("Houve um problema de conexão com o slack");
            }
        }

    }

    public void inserindoAlertaRede(Alerta alerta, Computador computador){

        String status = "";

        if(alerta.getQuantidade() > 25){
            status = "Crítico";
        } else if (alerta.getQuantidade() > 15) {
            status = "Alerta";
        }else{
            status = "Normal";
        }

        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();
        List<AtualizarAlerta> ultimaMedicao = jdbcTemplate.query("SELECT TOP 1 l.dataLog as 'data' FROM Log l JOIN Computador c ON l.fkComputador = c.idComputador WHERE c.idComputador = ? AND (l.descricao LIKE '%estado%' AND l.descricao LIKE '%Rede%') ORDER BY l.idLog DESC", new BeanPropertyRowMapper<>(AtualizarAlerta.class), computador.getIdComputador());

        if(ultimaMedicao.isEmpty()){
            ultimaMedicao.add(new AtualizarAlerta(LocalDateTime.now(ZoneId.of("UTC"))));
        }else if(ultimaMedicao.isEmpty() && !status.equals("Normal")){
            jdbcTemplate.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "Uso excessivo de Disco na máquina %s com estado %s".formatted(computador.getNome(), status), computador.getIdComputador());

            try {
                NotificacaoSlack.EnviarNotificacaoSlack("Uso excessivo de Disco na máquina %s com estado %s".formatted(computador.getNome(), status));
            } catch (Exception e) {
                System.out.println("Houve um problema de conexão com o slack");
            }
        }

        if(status.equals("Crítico") || status.equals("Alerta") && ChronoUnit.MINUTES.between(ultimaMedicao.get(0).getData(), LocalDateTime.now(ZoneId.of("UTC"))) > 5){

            jdbcTemplate.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "Rede instável na máquina %s com estado %s".formatted(computador.getNome(), status), computador.getIdComputador());

            try {
                NotificacaoSlack.EnviarNotificacaoSlack("Rede instável na máquina %s com estado %s".formatted(computador.getNome(), status));
            } catch (Exception e) {
                System.out.println("Houve um problema de conexão com o slack");
            }

        }
    }
}
