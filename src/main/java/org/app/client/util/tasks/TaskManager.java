package org.app.client.util.tasks;

import java.io.IOException;
import java.util.List;

public class TaskManager {

    // para descobrir o valor da imagem de um processo use no seu cmd o comando tasklist;
    private static String comandoWindows = "cmd /c taskkill /F /IM %s /T";
    private static String comandoLinux = "killall -KILL %s";

    private static List<String> processosProibidosWindows(){
        return List.of("WhatsApp.exe", "firefox.exe");
    }

    private static List<String> processosProibidosLinux(){
        return List.of("glaclient-linux", "Discord");
    }

    public static void tasksKillWindows(){
        processosProibidosWindows().forEach(processo -> {
            try {
                Runtime.getRuntime().exec(String.format(comandoWindows, processo));
                System.out.println("Matando o processo %s".formatted(processo));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public static void taskKillLinux(){
        processosProibidosLinux().forEach(processo -> {
            try{
                Runtime.getRuntime().exec(String.format(comandoLinux, processo));
                System.out.println("Matando o processo %s".formatted(processo));
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        });

    }
}
