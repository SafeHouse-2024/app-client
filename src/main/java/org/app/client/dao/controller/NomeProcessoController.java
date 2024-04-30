package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.NomeProcesso;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class NomeProcessoController {

    Conexao conexao = new Conexao();

    public List<NomeProcesso> listarProcessos(Integer fkSistemaOperacional){
        JdbcTemplate getConexao = conexao.getJdbcTemplate();
        if(fkSistemaOperacional == 1){
            return getConexao.query("SELECT np.nome FROM ProcessoSistema ps JOIN Processo p ON p.idProcesso = ps.fkProcesso JOIN NomeProcesso np " +
                    "ON np.idNome = p.fkNomeProcesso WHERE ps.fkSistemaOperacional = ?", new BeanPropertyRowMapper<>(NomeProcesso.class), fkSistemaOperacional);
        }
        return getConexao.query("SELECT np.nome FROM ProcessoSistema ps JOIN Processo p ON p.idProcesso = ps.fkProcesso JOIN NomeProcesso np " +
                "ON np.idNome = p.fkNomeProcesso WHERE ps.fkSistemaOperacional = ?", new BeanPropertyRowMapper<>(NomeProcesso.class), fkSistemaOperacional);
    }
}
