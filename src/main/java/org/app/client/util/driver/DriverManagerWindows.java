package org.app.client.util.driver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverManagerWindows {

    private static Map<String, Boolean> driverPermitidos = new HashMap<>();

    private static boolean verificarDriver(String nomeDriver){
        return new File(nomeDriver).exists();
    }

    private static void atualizarDriversConectados(){
        for (int i = 0; i <= 25; i++){
            driverPermitidos.put(String.valueOf('A' + i), verificarDriver( (char)('A' + i) + ":/"));
        }
    }

    public static void mostrarDrivers(){
        for (int i = 0; i <= 25; i++) {
            System.out.println(driverPermitidos.get(String.valueOf('A' + i)));
        }
    }

    public static void removerDriversInvalidos(){

    }

    private static List<String> driversInvalidos(){
        atualizarDriversConectados();
        // Drivers que virão do banco de dados;
        List<String> driversValidos = List.of("C", "D");
        return driverPermitidos.keySet().stream()
                .filter(driver -> !driversValidos.contains(driver)).toList();
    }

}
