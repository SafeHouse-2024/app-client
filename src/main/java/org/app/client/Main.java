package org.app.client;

import org.app.client.login.Login;
import org.app.client.util.driver.DriverManagerWindows;
import org.app.client.util.tasks.TaskManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Login.autenticar(scan);
        while(true){
            TaskManager.tasksKillWindows();
            DriverManagerWindows.removerDriversInvalidos();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}