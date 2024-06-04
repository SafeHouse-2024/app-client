package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.Alerta;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class AlertaController {

    Conexao conexao = new Conexao();

    public List<Alerta> getAllAlertasCPU(Integer idComputador){
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        return jdbcTemplate.query("SELECT DISTINCT(pc.idComputador), count(rc.valor) as 'quantidade'\n" +
                "                        FROM RegistroComponente rc \n" +
                "                        JOIN Componente c ON c.idComponente = rc.fkComponente \n" +
                "                        JOIN Computador pc ON c.fkComputador = pc.IdComputador \n" +
                "                        JOIN DarkStore d ON d.idDarkStore = pc.fkDarkStore \n" +
                "                        WHERE dataRegistro >= NOW() - INTERVAL 30000 MINUTE \n" +
                "                        AND c.nome LIKE 'Processador'\n" +
                "                        AND rc.valor > 80\n" +
                "                        AND pc.idComputador = ?\n" +
                "                        GROUP BY pc.idComputador;", new BeanPropertyRowMapper<>(Alerta.class), idComputador);
    }

    public List<Alerta> getAllAlertasRAM(Integer idComputador){
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        return jdbcTemplate.query("SELECT DISTINCT(pc.idComputador), count(rc.valor) as 'quantidade' FROM RegistroComponente rc JOIN Componente c ON c.idComponente = rc.fkComponente JOIN Computador pc ON c.fkComputador = pc.IdComputador JOIN DarkStore d ON d.idDarkStore = pc.fkDarkStore WHERE dataRegistro >= NOW() - INTERVAL 30000 MINUTE AND c.nome LIKE 'Memória' AND rc.valor > SUBSTRING_INDEX((SELECT ca.valor FROM CaracteristicaComponente ca JOIN Componente c ON c.idComponente = ca.fkComponente JOIN Computador pc ON pc.idComputador = c.fkComputador WHERE pc.idComputador = ? AND c.nome LIKE 'Memória' AND ca.nome LIKE 'Memória Total'), \" \", 1) * 0.8 AND pc.idComputador = ? GROUP BY pc.idComputador;\n", new BeanPropertyRowMapper<>(Alerta.class), idComputador, idComputador);
    }

    public List<Alerta> getAllAlertasDisco(Integer idComputador){
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        return jdbcTemplate.query("SELECT DISTINCT(pc.idComputador) FROM Componente c JOIN Computador pc ON c.fkComputador = pc.IdComputador JOIN CaracteristicaComponente ca ON ca.fkComponente = c.idComponente WHERE c.nome LIKE 'Disco' AND (SUBSTRING_INDEX((SELECT ca.valor FROM CaracteristicaComponente ca JOIN Componente c ON c.idComponente = ca.fkComponente JOIN Computador pc ON pc.idComputador = c.fkComputador WHERE pc.idComputador = ? AND c.nome LIKE 'Disco' AND ca.nome LIKE 'Memória Disponível'), \" \", 1)) < (SUBSTRING_INDEX((SELECT ca.valor FROM CaracteristicaComponente ca JOIN Componente c ON c.idComponente = ca.fkComponente JOIN Computador pc ON pc.idComputador = c.fkComputador WHERE pc.idComputador = ? AND c.nome LIKE 'Disco' AND ca.nome LIKE 'Memória Total'), \" \", 1) * 0.2) AND pc.idComputador = ?;\n", new BeanPropertyRowMapper<>(Alerta.class), idComputador, idComputador, idComputador);
    }
}
