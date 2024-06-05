package org.app.client.util.driver;

import org.app.client.Log;
import org.app.client.conexao.Conexao;
import org.app.client.dao.controller.DarkStoreController;
import org.app.client.dao.controller.EmpresaController;
import org.app.client.dao.entity.Computador;
import org.app.client.util.ExecutarPrograma;
import org.app.client.util.websocket.Websocket;
import org.app.client.util.notificacoes.NotificacaoSlack;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class DriverManagerLinux {
  
    String mensagem = "Um pendrive foi ejetado da %s";

    // Remove os drivers inválidos
    public static void removerDriversInvalidos(String user, Computador computador, String sudo){

        File pendrivesLinux = new File("/media/%s".formatted(user));
        if (pendrivesLinux.list() != null){
            List<String> pendrives = List.of(pendrivesLinux.list());
            pendrives.forEach(pendrive -> {
                try {
                    Process p = Runtime.getRuntime().exec("sudo -S eject /media/%s/%s".formatted(user, pendrive));
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                    //Passar a senha do sudo
                    bw.write(sudo);
                    bw.flush();
                    bw.close();
                    
                    try{
                      JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
                      getConexao.update("INSERT INTO Log (descricao, fkComputador) VALUES (?, ?)", mensagem.formatted(computador.getNome()), computador.getIdComputador());
                    }catch(Exception e){
                      System.out.println("Houve um problema na conexão do banco de dados local");
                    }
                    try{
                        JdbcTemplate getConexaoSql = ExecutarPrograma.conexaoSql.getJdbcTemplate();
                        getConexaoSql.update("INSERT INTO Log (descricao, fkComputador) VALUES (?, ?)", mensagem.formatted(computador.getNome()), computador.getIdComputador());
                    }catch(Exception e){
                        System.out.println("Houve um problema na conexão do banco de dados remoto");
                    }finally{
                      try{
                        Log.generateLog(mensagem.formatted(computador.getNome()));
                        NotificacaoSlack.EnviarNotificacaoSlack(mensagem.formatted(computador.getNome()));
                        Websocket.defineEventMessage(mensagem.formatted(computador.getNome()), EmpresaController.fetchEmpresa(computador.getIdComputador()), DarkStoreController.fetchDarkStore(computador.getIdComputador()));
                      }catch(Exception e){
                        throw new RuntimeException(e);
                      }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

}
