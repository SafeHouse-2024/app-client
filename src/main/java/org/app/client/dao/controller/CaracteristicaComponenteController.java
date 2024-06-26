package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.conexao.ConexaoSql;
import org.springframework.jdbc.core.JdbcTemplate;
import org.app.client.dao.entity.CaracteristicaComponente;
import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class CaracteristicaComponenteController{
    Conexao conexao = new Conexao();
    ConexaoSql conexaoSql = new ConexaoSql();

    public void adicionarCaracteristica(String nome, String valor, Integer fkComponente){
        try{
            JdbcTemplate getConexaoSql = conexaoSql.getJdbcTemplate();
            getConexaoSql.update("INSERT INTO CaracteristicaComponente(nome, valor, fkComponente) VALUES (?,?,?)", nome, valor, fkComponente);
        }catch (Exception e){
            System.out.println("Houve um problema de conexão com o banco remoto");
        }

        try{
            JdbcTemplate getConexao = conexao.getJdbcTemplate();
            getConexao.update("INSERT INTO CaracteristicaComponente(nome, valor, fkComponente) VALUES (?,?,?)", nome, valor, fkComponente);
        }catch (Exception e){
            System.out.println("Houve um problema de conexão com o banco local");
        }
    }

    public List<CaracteristicaComponente> listarCaracteristicas(Integer fkComponente){
        JdbcTemplate getConexao = conexaoSql.getJdbcTemplate();

        return getConexao.query("SELECT * FROM CaracteristicaComponente WHERE fkComponente = ?", new BeanPropertyRowMapper<>(CaracteristicaComponente.class), fkComponente);
    }

    public void atualizarValorCaracteristica(String valor, Integer idCaracteristica){

        try {
            JdbcTemplate getConexaoSql = conexaoSql.getJdbcTemplate();
            getConexaoSql.update("UPDATE CaracteristicaComponente SET valor = ? WHERE idCaracteristicaComponente = ?", valor, idCaracteristica);
        }catch (Exception e){
            System.out.println("Houve um problema de conexão remoto");
        }

        try {
            JdbcTemplate getConexao = conexao.getJdbcTemplate();
            getConexao.update("UPDATE CaracteristicaComponente SET valor = ? WHERE idCaracteristicaComponente = ?", valor, idCaracteristica);
        }catch (Exception e){
            System.out.println("Houve um problema de conexão local");
        }
    }
}
