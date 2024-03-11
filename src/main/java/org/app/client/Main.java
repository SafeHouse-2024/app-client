package org.app.client;

import org.app.client.login.Login;
import org.app.client.util.driver.DriverManagerLinux;
import org.app.client.util.driver.DriverManagerWindows;
import org.app.client.util.tasks.TaskManager;



public class Main {
    public static void main(String[] args) {
        Login.autenticar();
        while(true){
            DriverManagerLinux.removerDriversInvalidos();
//            TaskManager.tasksKillWindows();
//            DriverManagerWindows.removerDriversInvalidos();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}