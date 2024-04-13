package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.Componente;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class ComponenteController {
    Conexao conexao = new Conexao();

    public void adicionarComponente(String nome, Integer fkComputador){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();

        getConexao.update("Insert Into Componente(nome, fkComputador) VALUES(?,?)", nome, fkComputador);
    }

    public Componente pegarComponente(Integer fkComputador){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();

        return getConexao.queryForObject("Select * FROM componente JOIN computador ON fkComputador = idComputador WHERE fkComputador = ? ORDER BY idComponente desc LIMIT 1", new BeanPropertyRowMapper<>(Componente.class), fkComputador);
    }

}
