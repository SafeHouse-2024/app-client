package org.app.client.conexao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConexaoSql {

    private JdbcTemplate jdbcTemplate;

    public ConexaoSql() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://100.26.238.36:1433;encrypt=false;databaseName=spectra");
        dataSource.setUsername("sa");
        dataSource.setPassword("sptech1234@");

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
