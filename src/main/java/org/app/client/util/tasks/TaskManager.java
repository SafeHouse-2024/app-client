package org.app.client.util.tasks;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import org.app.client.LogType;
import org.app.client.dao.controller.DarkStoreController;
import org.app.client.dao.controller.EmpresaController;
import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.NomeProcesso;
import org.app.client.util.ExecutarPrograma;
import org.app.client.util.websocket.Websocket;
import org.app.client.Log;
import org.app.client.util.notificacoes.NotificacaoSlack;
import org.buildobjects.process.ExternalProcessFailureException;
import org.buildobjects.process.ProcBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import java.net.URISyntaxException;
import java.io.IOException;
import java.util.List;

public class TaskManager {

    private static String comandoWindows = "cmd /c taskkill /F /IM %s /T";
    private static String comandoLinux = "killall -KILL %s";

    private final Log log;

    public TaskManager(Log log) {
        this.log = new Log(LogType.SEGURANCA);
    }

    public static void taskKill(String so, Computador computador, List<NomeProcesso> processos){
        if(so.toUpperCase().contains("win".toUpperCase())){
            tasksKillWindows(computador, processos);
        } else if (so.contains("nux")) {
            taskKillLinux(computador, processos);
        }
    }

    private static void tasksKillWindows(Computador computador, List<NomeProcesso> listaProcessosProibidosWindows){
        listaProcessosProibidosWindows.forEach(processo -> {
            PowerShellResponse response = PowerShell.executeSingleCommand(comandoWindows.formatted(processo.getNome()));

            if(response.getCommandOutput().contains("ÊXITO:")){
                try{
                    JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
                    getConexao.update("INSERT INTO Log (descricao, fkComputador) VALUES (?,?)", "O processo %s foi fechado".formatted(processo.getNome()), computador.getIdComputador());
                }catch(Exception e){
                  System.out.println("Houve um problema de conexão Local");
                }
                try {
                    JdbcTemplate getConexaoSql = ExecutarPrograma.conexaoSql.getJdbcTemplate();
                    getConexaoSql.update("INSERT INTO Log (descricao, fkComputador) VALUES (?,?)", "O processo %s foi fechado".formatted(processo.getNome()), computador.getIdComputador());
                } catch (Exception e) {
                    System.out.println("Houve um problema de conexão");
                } finally {
                    try {
                        NotificacaoSlack.EnviarNotificacaoSlack("O processo %s foi fechado".formatted(processo.getNome()));
                        Log.generateLog("O processo %s foi fechado".formatted(processo.getNome()));
                        Websocket.defineEventMessage("O processo %s foi fechado da %s".formatted(processo.getNome(), computador.getNome()), EmpresaController.fetchEmpresa(computador.getIdComputador()), DarkStoreController.fetchDarkStore(computador.getIdComputador()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        });
    }

    private static void taskKillLinux(Computador computador, List<NomeProcesso> listaProcessosProibidosLinux){
        listaProcessosProibidosLinux.forEach(processo -> {
            String textoInvalido = "";
            try{
                new ProcBuilder("killall")
                        .withArgs("-KILL", processo.getNome()).run();
            }catch (ExternalProcessFailureException e){
                textoInvalido = e.getStderr().trim();
            }
            if(textoInvalido.contains("%s: no process found".formatted(processo.getNome()))) return;
            
            try{
                JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
                getConexao.update("INSERT INTO Log (descricao, fkComputador) VALUES (?,?)", "O processo %s foi fechado".formatted(processo.getNome()), computador.getIdComputador());
            }catch(Exception e){
                System.out.println("Houve um problema de conexão local");
            }
          
            try {
                JdbcTemplate getConexaoSql = ExecutarPrograma.conexaoSql.getJdbcTemplate();
                getConexaoSql.update("INSERT INTO Log (descricao, fkComputador) VALUES (?,?)", "O processo %s foi fechado".formatted(processo.getNome()), computador.getIdComputador());
            } catch (Exception e) {
                System.out.println("Houve um problema de conexão");
            } finally {
                try {
                    NotificacaoSlack.EnviarNotificacaoSlack("O processo %s foi fechado".formatted(processo.getNome()));
                    Log.generateLog("O processo %s foi fechado".formatted(processo.getNome()));
                    Websocket.defineEventMessage("O processo %s foi fechado da %s".formatted(processo.getNome(), computador.getNome()), EmpresaController.fetchEmpresa(computador.getIdComputador()), DarkStoreController.fetchDarkStore(computador.getIdComputador()));
                }catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }
}
