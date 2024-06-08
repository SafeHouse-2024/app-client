package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.conexao.ConexaoSql;
import org.springframework.jdbc.core.JdbcTemplate;

public class RegistroComponenteController {
    Conexao conexao = new Conexao();
    ConexaoSql conexaoSql = new ConexaoSql();

    public void adicionarRegistro(String nome, String valor, Integer fkComponente){

        try {
            JdbcTemplate getConexao = conexao.getJdbcTemplate();
            getConexao.update("INSERT INTO RegistroComponente(nome, valor, fkComponente) VALUES (?,?,?)", nome, valor, fkComponente);
        }catch (Exception e){
            System.out.println("Houve problemas de conexão com o banco local");
        }

        try{
            JdbcTemplate getConexaoSql = conexaoSql.getJdbcTemplate();
            getConexaoSql.update("INSERT INTO RegistroComponente(nome, valor, fkComponente) VALUES (?,?,?)", nome, valor, fkComponente);
        }catch (Exception e){
            System.out.println("Houve problemas de conexão com o banco remoto");
        }

    }

}
