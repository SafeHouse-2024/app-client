package org.app.client.util.driver;

import com.profesorfalken.jpowershell.PowerShell;
import com.profesorfalken.jpowershell.PowerShellResponse;
import org.app.client.dao.entity.Computador;
import org.app.client.util.ExecutarPrograma;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DriverManagerWindows {

    private static Map<String, Boolean> driverPermitidos = new HashMap<>(); // Mapa para armazenar os drivers permitidos

    // Verifica se o driver existe
    private static boolean verificarDriver(String nomeDriver){
        return new File(nomeDriver).exists();
    }

    // Atualiza a lista de drivers conectados
    private static void atualizarDriversConectados(){
        for (int i = 0; i <= 25; i++){
            driverPermitidos.put(String.valueOf((char)('A' + i)), verificarDriver( (char)('A' + i) + ":/"));
        }
    }

    // Mostra os drivers permitidos
    public static void mostrarDrivers(){
        for (int i = 0; i <= 25; i++) {
            System.out.println(driverPermitidos.get(String.valueOf((char)('A' + i))));
        }
    }

    // Remove os drivers inválidos
    public static void removerDriversInvalidos(Computador computador){
        JdbcTemplate getConexao = ExecutarPrograma.conexao.getJdbcTemplate();
        driversInvalidos().forEach(drive -> {
            PowerShellResponse response = comandoPowerShell(drive);
            if(response.getCommandOutput().isEmpty()){
                getConexao.update("INSERT INTO Log(descricao, fkComputador) VALUES (?,?)", "Um pendrive foi ejetado da %s".formatted(computador.getNome()), computador.getIdComputador());
            }
        });
    }

    private static PowerShellResponse comandoPowerShell(String driver){
        return PowerShell.executeSingleCommand("(New-Object -comObject Shell.Application).Namespace(17).ParseName(\"D:\").InvokeVerb(\"Eject\")".formatted(driver));
    }

    // Retorna uma lista de drivers inválidos
    private static List<String> driversInvalidos(){
        atualizarDriversConectados();
        // Drivers que virão do banco de dados;
        List<String> driversValidos = List.of("C"); // Lista de drivers válidos
        List<String> driversExistentes = new ArrayList<>(); // Lista de drivers existentes
        driverPermitidos.forEach((key, value) -> {
            if (value) {
                driversExistentes.add(key);
            }
        });
        return driversExistentes.stream()
                .filter(driver -> !driversValidos.contains(driver)).collect(Collectors.toList());
    }

}
