package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.conexao.ConexaoSql;
import org.app.client.dao.entity.Computador;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

public class ComputadorController {
  Conexao conexao = new Conexao();
  ConexaoSql conexaoSql = new ConexaoSql();

  public void ativarMaquina(Integer idComputador){
    try{
      JdbcTemplate getConexao = conexao.getJdbcTemplate();
      getConexao.update("UPDATE Computador SET ativo = 'Ativo' WHERE idComputador = ?", idComputador);
    }catch (Exception e){
      System.out.println("Houve um problema de conexão com o banco local");
    }

    try {
      JdbcTemplate getConexaoSql = conexaoSql.getJdbcTemplate();
      getConexaoSql.update("UPDATE Computador SET ativo = 'Ativo' WHERE idComputador = ?", idComputador);
    }catch (Exception e){
      System.out.println("Houve um problema na conexão remota");
    }

    System.out.println("Máquina ativada com sucesso!");
  }

  public Computador buscarMaquina(String macAddress){
    // Trocar para conexão remota
    Computador computador = new Computador();
    try{
      JdbcTemplate getConexao = conexao.getJdbcTemplate();
      computador = getConexao.queryForObject("SELECT * FROM Computador WHERE macAddress = '%s'".formatted(macAddress), new BeanPropertyRowMapper<>(Computador.class));

        if (computador != null) {
            System.out.println("O computador encontrado foi o: "+ computador.getIdComputador());
        }
    }catch (Exception e){
      System.out.println("Houve um problema de conexão remoto");
    }

    return computador;
  }

  public void inserirSistemaOperacional(Integer fkSistemaOperacional, Computador computador){
      try {
        JdbcTemplate getConexao = conexao.getJdbcTemplate();
        getConexao.update("INSERT INTO SistemaComputador(fkSistemaOperacional, fkComputador) VALUES (?,?)", fkSistemaOperacional, computador.getIdComputador());
      }catch (Exception e){
        System.out.println("Houve um problema de conexão local");
      }

      try {
        JdbcTemplate getConexaoSql = conexaoSql.getJdbcTemplate();
        getConexaoSql.update("INSERT INTO SistemaComputador(fkSistemaOperacional, fkComputador) VALUES (?,?)", fkSistemaOperacional, computador.getIdComputador());
      }catch (Exception e){
        System.out.println("Houve um problema de conexão remoto");
      }
  }

}
