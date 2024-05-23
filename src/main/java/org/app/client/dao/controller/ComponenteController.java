package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.conexao.ConexaoSql;
import org.app.client.dao.entity.Componente;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;


public class ComponenteController {
    Conexao conexao = new Conexao();
    ConexaoSql conexaoSql = new ConexaoSql();

    public Componente adicionarComponente(String nome, Integer fkComputador){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();
//        JdbcTemplate getConexaoSql = conexaoSql.getJdbcTemplate();

        getConexao.update("INSERT INTO Componente(nome, fkComputador) VALUES(?,?)", nome, fkComputador);
//        getConexaoSql.update("INSERT INTO Componente(nome, fkComputador) VALUES(?,?)", nome, fkComputador);

         return pegarUltimoComponenteInserido(nome, fkComputador);
    }

    private Componente pegarUltimoComponenteInserido(String nome, Integer fkComputador) {
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM Componente WHERE nome = ? AND fkComputador = ? ORDER BY idComponente DESC LIMIT 1",
                    new BeanPropertyRowMapper<>(Componente.class),
                    nome, fkComputador
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Componente> listarComponentes(Integer fkComputador) {
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();
        try {
            return jdbcTemplate.query(
                    "SELECT * FROM Componente WHERE fkComputador = ?",
                    new BeanPropertyRowMapper<>(Componente.class),
                    fkComputador
            );
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}

