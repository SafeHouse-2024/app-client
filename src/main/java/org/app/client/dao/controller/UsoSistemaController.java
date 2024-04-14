package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.UsoSistema;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.time.LocalDateTime;

public class UsoSistemaController {

    Conexao conexao = new Conexao();

    public void adicionarUsoSistema(Instant tempoInicializacao, Long tempoMinutos, Integer fkSistemaOperacional, Computador computador){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();

        getConexao.update("INSERT INTO UsoSistema (dataInicializacao, tempoAtividadeMinutos, fkSistemaOperacional, fkComputador) VALUES (?, ?, ?, ?)", tempoInicializacao, tempoMinutos, fkSistemaOperacional, computador.getIdComputador());
    }

    public void atualizarUsoSistema(Long tempoMinutos, UsoSistema usoSistema){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();

        getConexao.update("UPDATE UsoSistema SET tempoAtividadeMinutos = ? WHERE idUsoSistema = ?", tempoMinutos, usoSistema.getIdUsoSistema());
    }

    public UsoSistema pegarUsoSistema(Computador computador){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();

        return getConexao.queryForObject("SELECT * FROM UsoSistema WHERE fkComputador = ? ORDER BY idUsoSistema DESC LIMIT 1",
                new BeanPropertyRowMapper<>(UsoSistema.class),computador.getIdComputador());
    }
}
