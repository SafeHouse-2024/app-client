package org.app.client.dao.controller;

import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.NomeProcesso;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class NomeProcessoController {

    Conexao conexao = new Conexao();

    public List<NomeProcesso> listarProcessos(Integer fkSistemaOperacional){
        // trocar para banco remoto
        List<NomeProcesso> nomeProcessos = new ArrayList<>();
        try{
            JdbcTemplate getConexao = conexao.getJdbcTemplate();
            if(fkSistemaOperacional == 1){
                nomeProcessos = getConexao.query("SELECT np.nome FROM ProcessoSistema ps JOIN NomeProcesso np ON np.idNome = ps.fkNomeProcesso WHERE ps.fkSistemaOperacional = ?", new BeanPropertyRowMapper<>(NomeProcesso.class), fkSistemaOperacional);
            }
            nomeProcessos = getConexao.query("SELECT np.nome FROM ProcessoSistema ps JOIN NomeProcesso np ON np.idNome = ps.fkNomeProcesso WHERE ps.fkSistemaOperacional = ?", new BeanPropertyRowMapper<>(NomeProcesso.class), fkSistemaOperacional);
        }catch (Exception e){
            System.out.println("Houve um problema de conexão com o banco");
        }
        return nomeProcessos;
    }
}
