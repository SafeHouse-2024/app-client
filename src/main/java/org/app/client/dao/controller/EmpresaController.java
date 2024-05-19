package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.Empresa;
import org.app.client.util.ExecutarPrograma;
import org.springframework.jdbc.core.*;

public class EmpresaController {

  public static Empresa fetchEmpresa(Integer idComputador) {
    JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
    return getConexao.queryForObject("SELECT e.* FROM Empresa e JOIN DarkStore d ON d.fkEmpresa = e.idEmpresa JOIN Computador c ON c.fkDarkStore = d.idDarkStore WHERE c.idComputador = ?",
        new BeanPropertyRowMapper<>(Empresa.class), idComputador);
    }
}
