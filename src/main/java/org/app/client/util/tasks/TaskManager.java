package org.app.client.util.tasks;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import org.app.client.dao.controller.DarkStoreController;
import org.app.client.dao.controller.EmpresaController;
import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.NomeProcesso;
import org.app.client.util.ExecutarPrograma;
import org.app.client.util.websocket.Websocket;
import org.buildobjects.process.ExternalProcessFailureException;
import org.buildobjects.process.ProcBuilder;
import org.springframework.jdbc.core.JdbcTemplate;

import java.net.URISyntaxException;
import java.util.List;

public class TaskManager {

    // para descobrir o valor da imagem de um processo use no seu cmd o comando tasklist;
    private static String comandoWindows = "cmd /c taskkill /F /IM %s /T";
    private static String comandoLinux = "killall -KILL %s";

    public static void taskKill(String so, Computador computador, List<NomeProcesso> processos){
        if(so.toUpperCase().contains("win".toUpperCase())){
            tasksKillWindows(computador, processos);
        } else if (so.contains("nux")) {
            taskKillLinux(computador, processos);
        }
    }

    private static void tasksKillWindows(Computador computador, List<NomeProcesso> listaProcessosProibidosWindows){
        JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
        listaProcessosProibidosWindows.forEach(processo -> {
            PowerShellResponse response = PowerShell.executeSingleCommand(comandoWindows.formatted(processo.getNome()));

            if(response.getCommandOutput().contains("ÊXITO:")){
                getConexao.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "O processo %s foi fechado".formatted(processo.getNome()), computador.getIdComputador());
                try {
                    Websocket.defineEventMessage("O processo %s foi fechado da %s".formatted(processo.getNome(), computador.getNome()), EmpresaController.fetchEmpresa(computador.getIdComputador()), DarkStoreController.fetchDarkStore(computador.getIdComputador()), computador.getMacAddress());
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

        });
    }

    private static void taskKillLinux(Computador computador, List<NomeProcesso> listaProcessosProibidosLinux){
        JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
        listaProcessosProibidosLinux.forEach(processo -> {
            String textoInvalido = "";
            try{
                new ProcBuilder("killall")
                        .withArgs("-KILL", processo.getNome()).run();
            }catch (ExternalProcessFailureException e){
                textoInvalido = e.getStderr().trim();
            }
            if(textoInvalido.contains("%s: no process found".formatted(processo.getNome()))) return;
            getConexao.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "O processo %s foi fechado".formatted(processo.getNome()), computador.getIdComputador());
            try {
                Websocket.defineEventMessage("O processo %s foi fechado da %s".formatted(processo.getNome(), computador.getNome()), EmpresaController.fetchEmpresa(computador.getIdComputador()), DarkStoreController.fetchDarkStore(computador.getIdComputador()), computador.getMacAddress());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
