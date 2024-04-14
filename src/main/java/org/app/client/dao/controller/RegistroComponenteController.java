package org.app.client.dao.controller;

import com.mysql.cj.jdbc.JdbcConnection;
import org.app.client.conexao.Conexao;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RegistroComponenteController {
    Conexao conexao = new Conexao();

    public void adicionarRegistro(String nome, String valor, Integer fkComponente){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();

        getConexao.update("INSERT INTO RegistroComponente(nome, valor, fkComponente) VALUES (?,?,?)", nome, valor, fkComponente);
    }
}
