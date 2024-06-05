package org.app.client.dao.controller;

import org.app.client.dao.entity.DarkStore;
import org.app.client.util.ExecutarPrograma;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class DarkStoreController {

    public static DarkStore fetchDarkStore(Integer idComputador){
        JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
        return getConexao.queryForObject("SELECT d.* FROM DarkStore d JOIN Computador c ON c.fkDarkStore = d.idDarkStore WHERE c.idComputador = ?",
                new BeanPropertyRowMapper<>(DarkStore.class), idComputador);
    }
}
