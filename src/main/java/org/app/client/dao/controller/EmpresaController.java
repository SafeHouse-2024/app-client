package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.Empresa;
import org.springframework.jdbc.core.*;

public class EmpresaController {
  Conexao conexao = new Conexao();

  public void cadastrarEmpresa() {
    // Trocar para remoto
    try{
      JdbcTemplate getConexao = conexao.getJdbcTemplate();
      Empresa empresa = getConexao.queryForObject("SELECT * FROM Empresa where nome = 'Rappi'",
              new BeanPropertyRowMapper<>(Empresa.class));
    }catch (Exception e){
      System.out.println("Houve um problema na conexão do banco.");
    }
  }
}
