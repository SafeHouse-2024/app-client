package org.app.client;

import java.util.List;
import java.util.Scanner;

import org.app.client.dao.controller.*;
import org.app.client.dao.entity.Componente;
import org.app.client.dao.entity.Computador;
import org.app.client.login.Login;
import org.app.client.util.ExecutarPrograma;

import com.github.britooo.looca.api.core.Looca;
import org.checkerframework.checker.units.qual.C;


public class Main {
    public static void main(String[] args) {
        // Instanciando a classe ComputadorController
        ComputadorController computadorController = new ComputadorController();
        // Listando os codigos existentes no banco de dados
        List<String> codigosExistentes = computadorController.listarCodigosExistentes();
        String codigoAcesso = "";
        Scanner scanner = new Scanner(System.in);

        // Loop para validar o codigo de acesso
        do {
            System.out.println("Digite o código de acesso: ");
            codigoAcesso = scanner.nextLine();
            if (!codigosExistentes.contains(codigoAcesso)) {
                System.out.println("Código de acesso inválido!");
            }else{
                System.out.println("Código de acesso válido!");
            }
        } while (!codigosExistentes.contains(codigoAcesso));

        // Instanciando a classe Computador
        Computador computador = computadorController.buscarMaquina(codigoAcesso);
        // Instanciando a classe ComponenteController
        ComponenteController componenteController = new ComponenteController();
        // Instanciando a classe Looca
        Looca looca = new Looca();
        // Instanciando a classe ExecutarPrograma
        ExecutarPrograma executarPrograma = new ExecutarPrograma();
        
    }
}