package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.app.client.dao.entity.CaracteristicaComponente;
import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class CaracteristicaComponenteController {
    Conexao conexao = new Conexao();

    public void adicionarCaracteristica(String nome, String valor, Integer fkComponente){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();

        getConexao.update("Insert into CaracteristicaComponente(nome, valor, fkComponente) VALUES (?,?,?)", nome, valor, fkComponente);
    }

    public List<CaracteristicaComponente> listarCaracteristicas(Integer fkComponente){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();
        List<CaracteristicaComponente> caracteristicas = getConexao.query("SELECT * FROM CaracteristicaComponente WHERE fkComponente = ?", new BeanPropertyRowMapper<>(CaracteristicaComponente.class), fkComponente);

        return caracteristicas;
    }
}
