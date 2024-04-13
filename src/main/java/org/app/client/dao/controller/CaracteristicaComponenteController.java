package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.springframework.jdbc.core.JdbcTemplate;

public class CaracteristicaComponenteController {
    Conexao conexao = new Conexao();

    public void adicionarCaracteristica(String nome, String valor, Integer fkComponente){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();

        getConexao.update("Insert into CaracteristicaComponente(nome, valor, fkComponente) VALUES (?,?,?)", nome, valor, fkComponente);
    }
}
