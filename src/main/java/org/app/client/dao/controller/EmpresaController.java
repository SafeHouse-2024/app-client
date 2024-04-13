package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.Empresa;
import org.springframework.jdbc.core.*;

public class EmpresaController {
  Conexao conexao = new Conexao();

  public void cadastrarEmpresa() {
    JdbcTemplate getConexao = conexao.getJdbcTemplate();

    // List<Empresa> empresas = getConexao.query("SELECT * FROM empresa", new
    // BeanPropertyRowMapper<Empresa>(Empresa.class));

    // int alteracoes = getConexao.update("INSERT INTO Empresa(nome, email,
    // razaoSocial, cnpj) VALUES ('Spectra', 'spectra@sptech.school', 'Spectra
    // Machine', '12345678901234')");
    Empresa empresa = getConexao.queryForObject("SELECT * FROM Empresa where nome = 'Rappi'",
        new BeanPropertyRowMapper<>(Empresa.class));

    System.out.println("""
        Nome da Empresa: %s
        CPNJ da Empresa: %s
        Telefone da Empresa: %s""".formatted(empresa.getNome(), empresa.getCnpj(), empresa.getTelefone()));

  }
}
