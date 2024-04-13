package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.springframework.jdbc.core.JdbcTemplate;

public class ComputadorController {
  Conexao conexao = new Conexao();

  public void ativarMaquina(Integer idComputador){
    JdbcTemplate getConexao = conexao.getJdbcTemplate();
    Integer alteracoes = getConexao.update("UPDATE Computador SET ativo = 'Ativo' WHERE idComputador = ?", idComputador);
    System.out.println("Máquina ativada com sucesso!");
  }
}
