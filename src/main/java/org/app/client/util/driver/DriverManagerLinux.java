package org.app.client.util.driver;

import org.app.client.Log;
import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.Computador;
import org.app.client.util.ExecutarPrograma;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

public class DriverManagerLinux {

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
                        getConexao.update("INSERT INTO Log (descricao, fkComputador) VALUES (?, ?)", "Um pendrive foi ejetado da %s".formatted(computador.getNome()), computador.getIdComputador());
                        JdbcTemplate getConexaoSql = ExecutarPrograma.conexaoSql.getJdbcTemplate();
                        getConexaoSql.update("INSERT INTO Log (descricao, fkComputador) VALUES (?, ?)", "Um pendrive foi ejetado da %s".formatted(computador.getNome()), computador.getIdComputador());
                    }catch(Exception e){
                        System.out.println("Houve um problema na conexão do banco de dados");
                    }finally{
                        Log.generateLog("Um pendrive foi ejetado da máquina");
                    }


                        Log.generateLog("Um pendrive foi ejetado da máquina");

                }catch (IOException e) {
                    throw new RuntimeException(e);
            }
        });
        }
    }
}
