package org.app.client.util.driver;

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

    public static void removerDriversInvalidos(String user, Computador computador){
        JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
        File pendrivesLinux = new File("/media/%s".formatted(user));
        List<String> pendrives = List.of(pendrivesLinux.list());
        pendrives.forEach(pendrive -> {
            try {
                Process p = Runtime.getRuntime().exec("sudo -S eject /media/%s/%s".formatted(user, pendrive));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
                //Passar a senha do sudo
                bw.write("159634801480");
                bw.flush();
                bw.close();
                getConexao.update("INSERT INTO Log (descricao, fkComputador) VALUES (?, ?)", "Um pendrive foi ejetado da %s".formatted(computador.getNome()), computador.getIdComputador());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
