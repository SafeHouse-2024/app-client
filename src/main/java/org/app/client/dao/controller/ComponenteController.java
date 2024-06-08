package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.conexao.ConexaoSql;
import org.app.client.dao.entity.Componente;
import org.app.client.util.ExecutarPrograma;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;


public class ComponenteController {
    Conexao conexao = new Conexao();
    ConexaoSql conexaoSql = new ConexaoSql();

    public Componente adicionarComponente(String nome, Integer fkComputador){

        try{
            JdbcTemplate getConexaoSql = conexaoSql.getJdbcTemplate();
            getConexaoSql.update("INSERT INTO Componente(nome, fkComputador) VALUES(?,?)", nome, fkComputador);
        }catch (Exception e){
            System.out.println("Houve um problema com a conexão remota");
        }


         return pegarUltimoComponenteInserido(nome, fkComputador);
    }

    private Componente pegarUltimoComponenteInserido(String nome, Integer fkComputador) {
        // Trocar pela requisição remota
        try {
            JdbcTemplate jdbcTemplate = conexaoSql.getJdbcTemplate();
            return jdbcTemplate.queryForObject(
                    "SELECT TOP 1 * FROM Componente WHERE nome = ? AND fkComputador = ? ORDER BY idComponente DESC",
                    new BeanPropertyRowMapper<>(Componente.class),
                    nome, fkComputador
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Componente> listarComponentes(Integer fkComputador) {
        // Trocar para requisição remota
        try {
            JdbcTemplate jdbcTemplate = ExecutarPrograma.conexaoSql.getJdbcTemplate();
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

    public static void adicionarComponenteLocalmente(String nome, Integer fkComputador, Integer idComponente){

        try{
            JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
            getConexao.update("INSERT INTO Componente(idComponente, nome, fkComputador) VALUES(?,?,?)", idComponente ,nome, fkComputador);
        }catch (Exception e){
            System.out.println("Houve um problema com a conexão local");
        }

    }
}

