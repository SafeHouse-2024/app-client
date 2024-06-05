package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.Alerta;
import org.app.client.dao.entity.Computador;
import org.app.client.util.notificacoes.NotificacaoSlack;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Objects;

public class AlertaController {

    Conexao conexao = new Conexao();

    public void getAllAlertasCPU(Computador computador){
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        // SELECT DISTINCT pc.idComputador,
        //                COUNT(rc.valor) AS quantidade
        //FROM RegistroComponente rc
        //JOIN Componente c ON c.idComponente = rc.fkComponente
        //JOIN Computador pc ON c.fkComputador = pc.idComputador
        //JOIN DarkStore d ON d.idDarkStore = pc.fkDarkStore
        //WHERE dataRegistro >= DATEADD(MINUTE, -30000, GETDATE())
        //  AND c.nome LIKE 'Processador'
        //  AND rc.valor > 80
        //  AND pc.idComputador = @computadorId
        //GROUP BY pc.idComputador;

        List<Alerta> alertas = jdbcTemplate.query("SELECT DISTINCT(pc.idComputador), count(rc.valor) as 'quantidade'\n" +
                "                        FROM RegistroComponente rc \n" +
                "                        JOIN Componente c ON c.idComponente = rc.fkComponente \n" +
                "                        JOIN Computador pc ON c.fkComputador = pc.IdComputador \n" +
                "                        JOIN DarkStore d ON d.idDarkStore = pc.fkDarkStore \n" +
                "                        WHERE dataRegistro >= NOW() - INTERVAL 30000 MINUTE \n" +
                "                        AND c.nome LIKE 'Processador'\n" +
                "                        AND rc.valor > 80\n" +
                "                        AND pc.idComputador = ?\n" +
                "                        GROUP BY pc.idComputador;", new BeanPropertyRowMapper<>(Alerta.class), computador.getIdComputador());


        alertas.stream().filter(Objects::nonNull).forEach(alerta -> inserindoAlertaCPU(alerta, computador));
    }

    public void getAllAlertasRAM(Computador computador){
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        // SELECT DISTINCT pc.idComputador,
        //                COUNT(rc.valor) AS quantidade
        //FROM RegistroComponente rc
        //JOIN Componente c ON c.idComponente = rc.fkComponente
        //JOIN Computador pc ON c.fkComputador = pc.idComputador
        //JOIN DarkStore d ON d.idDarkStore = pc.fkDarkStore
        //WHERE dataRegistro >= DATEADD(MINUTE, -30000, GETDATE())
        //  AND c.nome LIKE 'Memória'
        //  AND rc.valor > CAST(SUBSTRING((SELECT TOP 1 ca.valor
        //                                 FROM CaracteristicaComponente ca
        //                                 JOIN Componente c ON c.idComponente = ca.fkComponente
        //                                 JOIN Computador pc ON pc.idComputador = c.fkComputador
        //                                 WHERE pc.idComputador = @computadorId
        //                                   AND c.nome LIKE 'Memória'
        //                                   AND ca.nome LIKE 'Memória Total'),
        //                                 1,
        //                                 CHARINDEX(' ', (SELECT TOP 1 ca.valor
        //                                                 FROM CaracteristicaComponente ca
        //                                                 JOIN Componente c ON c.idComponente = ca.fkComponente
        //                                                 JOIN Computador pc ON pc.idComputador = c.fkComputador
        //                                                 WHERE pc.idComputador = @computadorId
        //                                                   AND c.nome LIKE 'Memória'
        //                                                   AND ca.nome LIKE 'Memória Total') + ' ')) AS FLOAT) * 0.8
        //  AND pc.idComputador = @computadorId
        //GROUP BY pc.idComputador;
        List<Alerta> alertas = jdbcTemplate.query("SELECT DISTINCT(pc.idComputador), count(rc.valor) as 'quantidade' FROM RegistroComponente rc JOIN Componente c ON c.idComponente = rc.fkComponente JOIN Computador pc ON c.fkComputador = pc.IdComputador JOIN DarkStore d ON d.idDarkStore = pc.fkDarkStore WHERE dataRegistro >= NOW() - INTERVAL 30000 MINUTE AND c.nome LIKE 'Memória' AND rc.valor > SUBSTRING_INDEX((SELECT ca.valor FROM CaracteristicaComponente ca JOIN Componente c ON c.idComponente = ca.fkComponente JOIN Computador pc ON pc.idComputador = c.fkComputador WHERE pc.idComputador = ? AND c.nome LIKE 'Memória' AND ca.nome LIKE 'Memória Total'), \" \", 1) * 0.8 AND pc.idComputador = ? GROUP BY pc.idComputador;\n", new BeanPropertyRowMapper<>(Alerta.class), computador.getIdComputador(), computador.getIdComputador());

        alertas.stream().filter(Objects::nonNull).forEach(a -> inserindoAlertaRAM(a, computador));
    }

    public void getAllAlertasDisco(Computador computador){
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        // SELECT DISTINCT pc.idComputador
        //FROM Componente c
        //JOIN Computador pc ON c.fkComputador = pc.idComputador
        //JOIN CaracteristicaComponente ca ON ca.fkComponente = c.idComponente
        //WHERE c.nome LIKE 'Disco'
        //AND CAST(SUBSTRING(
        //    (SELECT TOP 1 ca.valor
        //     FROM CaracteristicaComponente ca
        //     JOIN Componente c ON c.idComponente = ca.fkComponente
        //     JOIN Computador pc ON pc.idComputador = c.fkComputador
        //     WHERE pc.idComputador = @computadorId1
        //     AND c.nome LIKE 'Disco'
        //     AND ca.nome LIKE 'Memória Disponível'),
        //    1,
        //    CHARINDEX(' ', (SELECT TOP 1 ca.valor
        //                    FROM CaracteristicaComponente ca
        //                    JOIN Componente c ON c.idComponente = ca.fkComponente
        //                    JOIN Computador pc ON pc.idComputador = c.fkComputador
        //                    WHERE pc.idComputador = @computadorId1
        //                    AND c.nome LIKE 'Disco'
        //                    AND ca.nome LIKE 'Memória Disponível') + ' ') - 1) AS FLOAT)
        //< CAST(SUBSTRING(
        //    (SELECT TOP 1 ca.valor
        //     FROM CaracteristicaComponente ca
        //     JOIN Componente c ON c.idComponente = ca.fkComponente
        //     JOIN Computador pc ON pc.idComputador = c.fkComputador
        //     WHERE pc.idComputador = @computadorId2
        //     AND c.nome LIKE 'Disco'
        //     AND ca.nome LIKE 'Memória Total'),
        //    1,
        //    CHARINDEX(' ', (SELECT TOP 1 ca.valor
        //                    FROM CaracteristicaComponente ca
        //                    JOIN Componente c ON c.idComponente = ca.fkComponente
        //                    JOIN Computador pc ON pc.idComputador = c.fkComputador
        //                    WHERE pc.idComputador = @computadorId2
        //                    AND c.nome LIKE 'Disco'
        //                    AND ca.nome LIKE 'Memória Total') + ' ') - 1) AS FLOAT) * 0.2
        //AND pc.idComputador = @computadorId3;

        List<Alerta> alertas = jdbcTemplate.query("SELECT DISTINCT(pc.idComputador) FROM Componente c JOIN Computador pc ON c.fkComputador = pc.IdComputador JOIN CaracteristicaComponente ca ON ca.fkComponente = c.idComponente WHERE c.nome LIKE 'Disco' AND (SUBSTRING_INDEX((SELECT ca.valor FROM CaracteristicaComponente ca JOIN Componente c ON c.idComponente = ca.fkComponente JOIN Computador pc ON pc.idComputador = c.fkComputador WHERE pc.idComputador = ? AND c.nome LIKE 'Disco' AND ca.nome LIKE 'Memória Disponível'), \" \", 1)) < (SUBSTRING_INDEX((SELECT ca.valor FROM CaracteristicaComponente ca JOIN Componente c ON c.idComponente = ca.fkComponente JOIN Computador pc ON pc.idComputador = c.fkComputador WHERE pc.idComputador = ? AND c.nome LIKE 'Disco' AND ca.nome LIKE 'Memória Total'), \" \", 1) * 0.2) AND pc.idComputador = ?;\n", new BeanPropertyRowMapper<>(Alerta.class), computador.getIdComputador(), computador.getIdComputador(), computador.getIdComputador());

        alertas.stream().filter(Objects::nonNull).forEach(alerta -> inserindoAlertaDisco(alerta, computador));
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

        jdbcTemplate.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "Uso excessivo de CPU na máquina %s com estado %s".formatted(computador.getNome(), status), computador.getIdComputador());

        try {
            NotificacaoSlack.EnviarNotificacaoSlack("Uso excessivo de CPU na máquina %s com estado %s".formatted(computador.getNome(), status));
        } catch (Exception e) {
            System.out.println("Houve um problema de conexão com o slack");
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

        jdbcTemplate.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "Uso excessivo de RAM na máquina %s com estado %s".formatted(computador.getNome(), status), computador.getIdComputador());

        try {
            NotificacaoSlack.EnviarNotificacaoSlack("Uso excessivo de RAM na máquina %s com estado %s".formatted(computador.getNome(), status));
        } catch (Exception e) {
            System.out.println("Houve um problema de conexão com o slack");
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

        jdbcTemplate.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "Uso excessivo de Disco na máquina %s com estado %s".formatted(computador.getNome(), status), computador.getIdComputador());

        try {
            NotificacaoSlack.EnviarNotificacaoSlack("Uso excessivo de Disco na máquina %s com estado %s".formatted(computador.getNome(), status));
        } catch (Exception e) {
            System.out.println("Houve um problema de conexão com o slack");
        }
    }

    public void inserindoAlertaRede(){}
}
