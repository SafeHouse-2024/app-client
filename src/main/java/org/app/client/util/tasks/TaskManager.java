package org.app.client.util.tasks;

import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.Processo;
import org.app.client.util.ExecutarPrograma;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {

    // para descobrir o valor da imagem de um processo use no seu cmd o comando tasklist;
    private static String comandoWindows = "cmd /c taskkill /F /IM %s /T";
    private static String comandoLinux = "killall -KILL %s";

    public static void taskKill(String so, Computador computador, List<Processo> processos){
        if(so.contains("win")){
            tasksKillWindows(computador, processos);
        } else if (so.contains("nux")) {
            taskKillLinux(computador, processos);
        }
    }

    private static void tasksKillWindows(Computador computador, List<Processo> listaProcessosProibidosWindows){
        JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
        listaProcessosProibidosWindows.forEach(processo -> {
            try {
                Process process = Runtime.getRuntime().exec(String.format(comandoWindows, processo.getNomeWindows()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String textoInvalido = reader.readLine();
                if(textoInvalido != null || textoInvalido.equals("%s: no process found".formatted(processo.getNomeWindows()))) return;
                getConexao.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "O processo %s foi fechado".formatted(processo.getNomeWindows()), computador.getIdComputador());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static void taskKillLinux(Computador computador, List<Processo> listaProcessosProibidosLinux){
        JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
        listaProcessosProibidosLinux.forEach(processo -> {
            try{
                Process process = Runtime.getRuntime().exec(String.format(comandoLinux, processo.getNomeLinux()));
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String textoInvalido = reader.readLine();
                if(textoInvalido != null && textoInvalido.equals("ERRO: o processo \"%s\" não foi encontrado.".formatted(processo.getNomeLinux()))) return;
                getConexao.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "O processo %s foi fechado".formatted(processo.getNomeLinux()), computador.getIdComputador());
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        });

    }
}
