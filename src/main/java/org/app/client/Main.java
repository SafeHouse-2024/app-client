package org.app.client;

import java.util.Scanner;

import org.app.client.dao.controller.ComputadorController;
import org.app.client.dao.controller.EmpresaController;
import org.app.client.dao.entity.Computador;
import org.app.client.login.Login;
import org.app.client.util.ExecutarPrograma;

import com.github.britooo.looca.api.core.Looca;



public class Main {
    public static void main(String[] args) {
        Looca looca = new Looca();
        Scanner scanner = new Scanner(System.in);
        EmpresaController empresaController = new EmpresaController();
        ComputadorController computadorController = new ComputadorController();
        empresaController.cadastrarEmpresa();
        computadorController.ativarMaquina(1);
        computadorController.buscarMaquina("00:1B:44:11:3A:B7");

        String sistemaOperacional = System.getProperty("os.name");
        String user = sistemaOperacional.contains("nux") ? System.getProperty("user.name") : null;
        
        Login.autenticar(scanner);
        System.out.println("Vamos passar informações do seu computador ->");
        // String sistemaOperacional = looca.getSistema().getSistemaOperacional();
        String processador = looca.getProcessador().getNome();
        String memoriaRam = looca.getMemoria().getTotal().toString();

        System.out.println("Sistema Operacional: " + sistemaOperacional);
        System.out.println("Processador: " + processador);
        System.out.println("Memória RAM: " + memoriaRam);

        System.out.println("Vamos passar informações do seu computador <-");

        System.out.println("Você quer ver mais detalhes sobre cada item? (S/N)");
        String detalhes = scanner.nextLine();

        while (detalhes != null && detalhes.equalsIgnoreCase("S")) {
            System.out.println("Qual item você quer ver mais detalhes? (Sistema Operacional, Processador, Memória RAM)");
            String item = scanner.nextLine();

            if (item.equalsIgnoreCase("Sistema Operacional")) {
                System.out.println("Sistema Operacional: " + looca.getSistema());
            } else if (item.equalsIgnoreCase("Processador")) {
                System.out.println("Processador: " + looca.getProcessador());
            } else if (item.equalsIgnoreCase("Memoria RAM")) {
                System.out.println("Memória RAM: " + looca.getMemoria());
            } else {
                System.out.println("Item não encontrado");
            }

            System.out.println("Você quer ver mais detalhes sobre cada item? (S/N)");
            detalhes = scanner.nextLine();
            
        }
        System.out.println("Fim da execução");

        System.out.println("Vamos agora verificar se há tarefas e drivers inválidos para serem removidos");
        ExecutarPrograma.executarPrograma(sistemaOperacional, user);
        scanner.close();

    }
}