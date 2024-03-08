package org.app.client.util.driver;

import com.profesorfalken.jpowershell.PowerShell;

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
    public static void removerDriversInvalidos(){
        driversInvalidos().forEach(drive -> PowerShell.executeSingleCommand("(New-Object -comObject Shell.Application).Namespace(17).ParseName(\"%s:\").InvokeVerb(\"Eject\")".formatted(drive)));
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
