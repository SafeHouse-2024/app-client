package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.conexao.ConexaoSql;
import org.springframework.jdbc.core.JdbcTemplate;

public class RegistroComponenteController {
    Conexao conexao = new Conexao();
//    ConexaoSql conexaoSql = new ConexaoSql();

    public void adicionarRegistro(String nome, String valor, Integer fkComponente){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();
//        JdbcTemplate getConexaoSql = conexaoSql.getJdbcTemplate();

        getConexao.update("INSERT INTO RegistroComponente(nome, valor, fkComponente) VALUES (?,?,?)", nome, valor, fkComponente);
//        getConexaoSql.update("INSERT INTO RegistroComponente(nome, valor, fkComponente) VALUES (?,?,?)", nome, valor, fkComponente);
    }

}
