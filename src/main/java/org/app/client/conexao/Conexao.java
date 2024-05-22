package org.app.client.conexao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class Conexao {
  private JdbcTemplate jdbcTemplate;

  public Conexao() {
    BasicDataSource dataSource = new BasicDataSource();

    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://localhost:3306/spectra");
    dataSource.setUsername("root");
    dataSource.setPassword("@Senha2506*");

    jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }
}
