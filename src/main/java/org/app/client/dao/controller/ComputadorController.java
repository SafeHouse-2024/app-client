package org.app.client.dao.controller;

import java.util.List;

import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.Componente;
import org.app.client.dao.entity.Computador;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class ComputadorController {
  Conexao conexao = new Conexao();

  public void ativarMaquina(Integer idComputador){
    JdbcTemplate getConexao = conexao.getJdbcTemplate();
    Integer alteracoes = getConexao.update("UPDATE Computador SET ativo = 'Ativo' WHERE idComputador = ?", idComputador);
    System.out.println("Máquina ativada com sucesso!");
  }

  public Computador buscarMaquina(String macAddress){
    JdbcTemplate getConexao = conexao.getJdbcTemplate();
    Computador computadorEncontrado = getConexao.queryForObject("SELECT * FROM Computador WHERE macAddress = '%s'".formatted(macAddress), new BeanPropertyRowMapper<>(Computador.class));

    return computadorEncontrado;
  }

  public void inserirSistemaOperacional(Integer fkSistemaOperacional, Computador computador){
      JdbcTemplate getConexao = conexao.getJdbcTemplate();


    getConexao.update("INSERT INTO SistemaComputador(fkSistemaOperacional, fkComputador) VALUES (?,?)", fkSistemaOperacional, computador.getIdComputador());
  }

}
