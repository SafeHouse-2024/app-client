package org.app.client;

import java.util.List;
import java.util.Scanner;

import org.app.client.dao.controller.*;
import org.app.client.dao.entity.Componente;
import org.app.client.dao.entity.Computador;
import org.app.client.login.Login;
import org.app.client.util.ExecutarPrograma;

import com.github.britooo.looca.api.core.Looca;
import org.app.client.dao.entity.CaracteristicaComponente;

public class Main {
    public static void main(String[] args) {
        ComponenteController componenteController = new ComponenteController();
        ComputadorController computadorController = new ComputadorController();
        CaracteristicaComponenteController caracteristicaComponenteController = new CaracteristicaComponenteController();
        Scanner scanner = new Scanner(System.in);
        Looca looca = new Looca();

        // Ativar maquina
        String macAddress = looca.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getEnderecoMac();
        Computador computador = computadorController.buscarMaquina("00:1B:44:11:3A:B7");
        String codigoMaquina = computador.getCodigoAcesso();
        String codigoAcesso;

        do {
            System.out.println("Digite o código de acesso: ");
            codigoAcesso = scanner.nextLine();
            if (!codigoMaquina.equals(codigoAcesso)) {
                System.out.println("Código de acesso inválido!");
            } else {
                System.out.println("Código de acesso válido!");
            }
        } while (!codigoMaquina.equals(codigoAcesso));
        computadorController.ativarMaquina(computador.getIdComputador());
        computador.setAtivo("Ativo");

        // // Adicionando componentes

        // Processador
        while (computador.getAtivo() == "Ativo") {

            Componente processador = componenteController.adicionarComponente("Processador",
                    computador.getIdComputador());
            caracteristicaComponenteController.adicionarCaracteristica("Fabricante",
                    looca.getProcessador().getFabricante(), processador.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("Nome", looca.getProcessador().getNome(),
                    processador.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("Frequencia",
                    looca.getProcessador().getFrequencia().toString(), processador.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("Nucleos",
                    looca.getProcessador().getNumeroCpusFisicas().toString(), processador.getIdComponente());

            Componente memoria = componenteController.adicionarComponente("Memoria", computador.getIdComputador());
            caracteristicaComponenteController.adicionarCaracteristica("Total",
                    looca.getMemoria().getTotal().toString(), memoria.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("Em uso",
                    looca.getMemoria().getEmUso().toString(), memoria.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("Disponivel",
                    looca.getMemoria().getDisponivel().toString(), memoria.getIdComponente());

            Componente disco = componenteController.adicionarComponente("Disco", computador.getIdComputador());
            caracteristicaComponenteController.adicionarCaracteristica("Total",
                    looca.getGrupoDeDiscos().getQuantidadeDeDiscos().toString(0), disco.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("Disponivel",
                    looca.getGrupoDeDiscos().getVolumes().get(0).getVolume(), disco.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("Utilizado",
                    looca.getGrupoDeDiscos().getTamanhoTotal().toString(0), disco.getIdComponente());

        }

        // Listando componentes
        // List<Componente> componentes = componenteController.listarComponentes(computador.getIdComputador());
        // for (Componente componente : componentes) {
        //     System.out.println("Componente: " + componente.getNome());
        //     List<CaracteristicaComponente> caracteristicas = caracteristicaComponenteController
        //             .listarCaracteristicas(componente.getIdComponente());
        //     for (CaracteristicaComponente caracteristica : caracteristicas) {
        //         System.out.println("Caracteristica: " + caracteristica.getNome() + " - " + caracteristica.getValor());
        //     }
        // }

    }
}