package org.app.client.dao.controller;

import java.util.List;

import org.app.client.conexao.Conexao;
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

  public Computador buscarMaquina(String codigoAcesso){
    JdbcTemplate getConexao = conexao.getJdbcTemplate();
    Computador computadorEncontrado = getConexao.queryForObject("SELECT * FROM Computador WHERE codigoAcesso = '%s'".formatted(codigoAcesso), new BeanPropertyRowMapper<>(Computador.class));

    return computadorEncontrado;
  }

  public List<String> listarCodigosExistentes(){
    JdbcTemplate getConexao = conexao.getJdbcTemplate();
    List<String> codigosExistentes = getConexao.queryForList("SELECT codigoAcesso FROM Computador", String.class);

    return codigosExistentes;
  }
}
