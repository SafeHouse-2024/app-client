package org.app.client.dau.controller;

import org.app.client.conexao.Conexao;
import org.app.client.dau.entity.Empresa;
import org.springframework.jdbc.core.*;

import java.util.List;

public class EmpresaController {
  Conexao conexao = new Conexao();

  public void cadastrarEmpresa() {
    JdbcTemplate getConexao = conexao.getJdbcTemplate();

//    List<Empresa> empresas = getConexao.query("SELECT * FROM empresa", new BeanPropertyRowMapper<Empresa>(Empresa.class));

//    int alteracoes = getConexao.update("INSERT INTO empresa(nome, email, razaoSocial, cnpj) VALUES ('Spectra', 'spectra@sptech.school', 'Spectra Machine', '12345678901234')");
    Object spectra = getConexao.queryForObject("SELECT * FROM empresa where nome = 'Spectra'", new BeanPropertyRowMapper(Empresa.class));
    Empresa empresa = new Empresa(spectra);


    System.out.println("""
            Nome da Empresa: %s
            CPNJ da Empresa: %s
            Telefone da Empresa: %s""".formatted(empresa.getNome(), empresa.getCnpj(), empresa.getTelefone()));

  }
}
