package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.conexao.ConexaoSql;
import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.UsoSistema;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.time.LocalDateTime;

public class UsoSistemaController {

    Conexao conexao = new Conexao();
    ConexaoSql conexaoSql = new ConexaoSql();

    public void adicionarUsoSistema(Instant tempoInicializacao, Long tempoMinutos, Integer fkSistemaOperacional, Computador computador){
        try{
            JdbcTemplate getConexao = conexao.getJdbcTemplate();
            getConexao.update("INSERT INTO UsoSistema (dataInicializacao, tempoAtividadeMinutos, fkSistemaOperacional, fkComputador) VALUES (?, ?, ?, ?)", tempoInicializacao, tempoMinutos, fkSistemaOperacional, computador.getIdComputador());
        }catch (Exception e){
            System.out.println("Houve problema de conexão com o banco local");
        }
        try{
            JdbcTemplate getConexaoSql = conexaoSql.getJdbcTemplate();
            getConexaoSql.update("INSERT INTO UsoSistema (dataInicializacao, tempoAtividadeMinutos, fkSistemaOperacional, fkComputador) VALUES (?, ?, ?, ?)", tempoInicializacao, tempoMinutos, fkSistemaOperacional, computador.getIdComputador());
        }catch (Exception e){
            System.out.println("Houve problema de conexão com o banco remoto");
        }

    }

    public void atualizarUsoSistema(Long tempoMinutos, UsoSistema usoSistema){

        try{
            JdbcTemplate getConexao = conexao.getJdbcTemplate();
            getConexao.update("UPDATE UsoSistema SET tempoAtividadeMinutos = ? WHERE idUsoSistema = ?", tempoMinutos, usoSistema.getIdUsoSistema());

        }catch (Exception e){
            System.out.println("Houve problema de conexão com o banco local");
        }

        try{
            JdbcTemplate getConexaoSql = conexaoSql.getJdbcTemplate();
            getConexaoSql.update("UPDATE UsoSistema SET tempoAtividadeMinutos = ? WHERE idUsoSistema = ?", tempoMinutos, usoSistema.getIdUsoSistema());
        }catch (Exception e){
            System.out.println("Houve problema de conexão com o banco remoto");
        }

    }

    public UsoSistema pegarUsoSistema(Computador computador){
        UsoSistema usoSistema = new UsoSistema();
        // deve ser remoto
        try{
            JdbcTemplate getConexao = conexao.getJdbcTemplate();
            usoSistema = getConexao.queryForObject("SELECT * FROM UsoSistema WHERE fkComputador = ? ORDER BY idUsoSistema DESC LIMIT 1",
                    new BeanPropertyRowMapper<>(UsoSistema.class),computador.getIdComputador());
        }catch (Exception e){
            System.out.println("Houve problema de conexão com o banco local");
            System.out.println();
        }
        return usoSistema;
    }
}
