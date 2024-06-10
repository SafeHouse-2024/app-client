package org.app.client.util.driver;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import org.app.client.LogType;
import org.app.client.dao.controller.DarkStoreController;
import org.app.client.dao.controller.EmpresaController;
import org.app.client.util.websocket.Websocket;
import java.io.File;
import java.net.URISyntaxException;
import org.app.client.Log;
import org.app.client.dao.entity.Computador;
import org.app.client.util.ExecutarPrograma;
import org.app.client.util.notificacoes.NotificacaoSlack;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DriverManagerWindows {

    private static Map<String, Boolean> driverPermitidos = new HashMap<>();

    private static boolean verificarDriver(String nomeDriver){
        return new File(nomeDriver).exists();
    }

    private static void atualizarDriversConectados(){
        for (int i = 0; i <= 25; i++){
            driverPermitidos.put(String.valueOf((char)('A' + i)), verificarDriver( (char)('A' + i) + ":/"));
        }
    }

    public static void mostrarDrivers(){
        for (int i = 0; i <= 25; i++) {
            System.out.println(driverPermitidos.get(String.valueOf((char)('A' + i))));
        }
    }

    public static void removerDriversInvalidos(Computador computador){
        driversInvalidos().forEach(drive -> {
            PowerShellResponse response = comandoPowerShell(drive);
            if(response.getCommandOutput().isEmpty()){
                try{
                    JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
                    getConexao.update("INSERT INTO Log (descricao, fkComputador) VALUES (?,?)", "Um pendrive foi ejetado da %s".formatted(computador.getNome()), computador.getIdComputador());
                }catch(Exception e){
                  throw new RuntimeException(e);
                }
                try {
                    JdbcTemplate getConexaoSql = ExecutarPrograma.conexaoSql.getJdbcTemplate();
                    getConexaoSql.update("INSERT INTO Log (descricao, fkComputador) VALUES (?,?)", "Um pendrive foi ejetado da %s".formatted(computador.getNome()), computador.getIdComputador());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        NotificacaoSlack.EnviarNotificacaoSlack("Um pendrive foi ejetado da %s".formatted(computador.getNome()));
                        Log.generateLog("Um pendrive foi ejetado da máquina", LogType.SEGURANCA);
                        Websocket.defineEventMessage("Um pendrive foi ejetado da %s".formatted(computador.getNome()), EmpresaController.fetchEmpresa(computador.getIdComputador()), DarkStoreController.fetchDarkStore(computador.getIdComputador()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private static PowerShellResponse comandoPowerShell(String driver){
        return PowerShell.executeSingleCommand("(New-Object -comObject Shell.Application).Namespace(17).ParseName(\"D:\").InvokeVerb(\"Eject\")".formatted(driver));
    }

    private static List<String> driversInvalidos(){
        atualizarDriversConectados();
        List<String> driversValidos = List.of("C");
        List<String> driversExistentes = new ArrayList<>();
        driverPermitidos.forEach((key, value) -> {
            if (value) {
                driversExistentes.add(key);
            }
        });
        return driversExistentes.stream()
                .filter(driver -> !driversValidos.contains(driver)).collect(Collectors.toList());
    }

}
