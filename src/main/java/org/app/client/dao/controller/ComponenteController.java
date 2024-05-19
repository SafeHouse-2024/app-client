package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.conexao.ConexaoSql;
import org.app.client.dao.entity.Componente;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;


public class ComponenteController {
    Conexao conexao = new Conexao();
    ConexaoSql conexaoSql = new ConexaoSql();

    public Componente adicionarComponente(String nome, Integer fkComputador){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();
        JdbcTemplate getConexaoSql = conexaoSql.getJdbcTemplate();

        getConexao.update("INSERT INTO Componente(nome, fkComputador) VALUES(?,?)", nome, fkComputador);
        getConexaoSql.update("INSERT INTO Componente(nome, fkComputador) VALUES(?,?)", nome, fkComputador);

        return pegarComponente(fkComputador);
    }

    private Componente pegarComponente(Integer fkComputador){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();

        return getConexao.queryForObject("Select * FROM Componente JOIN Computador ON fkComputador = idComputador WHERE fkComputador = ? ORDER BY idComponente desc LIMIT 1", new BeanPropertyRowMapper<>(Componente.class), fkComputador);
    }

    public List<Componente> listarComponentes(Integer fkComputador){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();
        List<Componente> componentes = getConexao.query("SELECT * FROM Componente WHERE fkComputador = ?", new BeanPropertyRowMapper<>(Componente.class), fkComputador);
    
        return componentes;
      }

}
