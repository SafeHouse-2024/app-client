package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.Processo;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ProcessoController {

    Conexao conexao = new Conexao();

    public List<Processo> listarProcessos(Integer fkSistemaOperacional){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();
        if(fkSistemaOperacional == 1){
            return getConexao.query("SELECT nomeWindows FROM Processo", new BeanPropertyRowMapper<>(Processo.class));
        }
        return getConexao.query("SELECT nomeLinux FROM Processo", new BeanPropertyRowMapper<>(Processo.class));
    }
}
