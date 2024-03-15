package org.app.client.util;

import org.app.client.util.driver.DriverManagerLinux;
import org.app.client.util.driver.DriverManagerWindows;
import org.app.client.util.tasks.TaskManager;

public class ExecutarPrograma {

    public static void executarPrograma(String so, String user){

        TaskManager.taskKill(so);
        if(so.contains("win")){
            DriverManagerWindows.removerDriversInvalidos();
        }else if(so.contains("nux")){
            DriverManagerLinux.removerDriversInvalidos(user);
        }
    }
}
