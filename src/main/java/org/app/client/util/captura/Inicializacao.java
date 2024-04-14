package org.app.client.util.captura;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import org.app.client.dao.controller.CaracteristicaComponenteController;
import org.app.client.dao.controller.ComponenteController;
import org.app.client.dao.controller.ComputadorController;
import org.app.client.dao.controller.RegistroComponenteController;
import org.app.client.dao.entity.CaracteristicaComponente;
import org.app.client.dao.entity.Componente;
import org.app.client.dao.entity.Computador;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.stream.Collectors;

public class Inicializacao {

    public static Computador adicionarEstruturaMaquina(Looca looca, String codigoAcesso) {

        ComponenteController componenteController = new ComponenteController();
        ComputadorController computadorController = new ComputadorController();
        CaracteristicaComponenteController caracteristicaComponenteController = new CaracteristicaComponenteController();

        List<RedeInterface> redes = looca.getRede().getGrupoDeInterfaces().getInterfaces().stream().filter(rede -> rede.getNomeExibicao().toUpperCase().contains("Wireless Network Adapter".toUpperCase())).collect(Collectors.toList());
        String macAddress = redes.get(0).getEnderecoMac();
        Computador computador = null;
        try{
            computador = computadorController.buscarMaquina("00:1B:44:11:3A:B8");
        }catch (EmptyResultDataAccessException e){
            System.out.println("Máquina não cadastrada!");
        }

        if (computador.getAtivo().equals("Inativo") && codigoAcesso.equals(computador.getCodigoAcesso())) {

            List<Disco> discosLooca = looca.getGrupoDeDiscos().getDiscos();

            // // Adicionando componentes

            // Processador
            Componente processador = componenteController.adicionarComponente("Processador",
                    computador.getIdComputador());
            caracteristicaComponenteController.adicionarCaracteristica("Fabricante",
                    looca.getProcessador().getFabricante(), processador.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("Nome", looca.getProcessador().getNome(),
                    processador.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("Frequencia",
                    looca.getProcessador().getFrequencia().toString(), processador.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("Nucleos Físicos",
                    looca.getProcessador().getNumeroCpusFisicas().toString(), processador.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("Núcleos Lógicos",
                    looca.getProcessador().getNumeroCpusLogicas().toString(), processador.getIdComponente());

            //Memoria RAM
            Componente memoria = componenteController.adicionarComponente("Memoria", computador.getIdComputador());
            caracteristicaComponenteController.adicionarCaracteristica("Memoria Total",
                    String.valueOf("%.2fGB".formatted(looca.getMemoria().getTotal() / Math.pow(10, 9))), memoria.getIdComponente());
            //Disco Rígido
            for (Disco discos : discosLooca) {
                if (discos.getTamanho() > 0) {
                    Componente disco = componenteController.adicionarComponente("Disco", computador.getIdComputador());
                    if (discos.getTamanho() >= Math.pow(10, 12)) {
                        caracteristicaComponenteController.adicionarCaracteristica("Memoria Total", String.valueOf("%.2f TB".formatted(discos.getTamanho() / Math.pow(10, 12))), disco.getIdComponente());
                    } else {
                        caracteristicaComponenteController.adicionarCaracteristica("Memoria Total", String.valueOf("%.2f GB".formatted(discos.getTamanho() / Math.pow(10, 9))), disco.getIdComponente());
                    }

                }

            }
            Componente rede = componenteController.adicionarComponente("Rede", computador.getIdComputador());
            caracteristicaComponenteController.adicionarCaracteristica("IPV4", redes.get(0).getEnderecoIpv4().get(0), rede.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("IPV6", redes.get(0).getEnderecoIpv6().get(0), rede.getIdComponente());

            computadorController.ativarMaquina(computador.getIdComputador());
            computador.setAtivo("Ativo");
        }

        return computador;
    }

    public static boolean verificarMaquina(Computador computador, String codigoAcesso) {
        return computador.getCodigoAcesso().equals(codigoAcesso);
    }

    public static void capturarRegistros(RegistroComponenteController registroComponenteController, List<Componente> componentes, Looca looca) {
        Componente processador = componentes.stream().filter(componente -> componente.getNome().equalsIgnoreCase("Processador")).findFirst().get();
        Componente memoria = componentes.stream().filter(componente -> componente.getNome().equalsIgnoreCase("Memoria")).findFirst().get();
        List<Componente> discos = componentes.stream().filter(componente -> componente.getNome().equalsIgnoreCase("Disco")).toList();
        discos.forEach(disco -> pegarDisco(disco, looca));
        registroComponenteController.adicionarRegistro("Taxa de Uso", String.valueOf("%.2f".formatted(looca.getProcessador().getUso())), processador.getIdComponente());
        registroComponenteController.adicionarRegistro("Taxa de Uso", String.valueOf("%.2f".formatted(looca.getMemoria().getEmUso() / Math.pow(10, 9))), memoria.getIdComponente());
    }

    public static void pegarDisco(Componente componente, Looca looca) {
        CaracteristicaComponenteController caracteristicaComponenteController = new CaracteristicaComponenteController();
        List<CaracteristicaComponente> caracteristicaComponentes = caracteristicaComponenteController.listarCaracteristicas(componente.getIdComponente());
        List<Disco> discos = looca.getGrupoDeDiscos().getDiscos();
        for (int i = 0; i < caracteristicaComponentes.size(); i++) {
            if (caracteristicaComponentes.get(i).getNome().equals("Memoria Total")) {
                if (caracteristicaComponentes.get(i).getValor().equals(String.valueOf("%.2f TB".formatted(discos.get(i).getTamanho() / Math.pow(10, 12))))) {
                    if (discos.get(i).getTamanho() > 0) {
                        if (discos.get(i).getTamanho() >= Math.pow(10, 12)) {
                            caracteristicaComponenteController.atualizarValorCaracteristica(String.valueOf("%.2f TB".formatted(discos.get(i).getTamanho() / Math.pow(10, 12))), caracteristicaComponentes.get(i).getIdCaracteristicaComponente());
                        } else {
                            caracteristicaComponenteController.atualizarValorCaracteristica(String.valueOf("%.2f GB".formatted(discos.get(i).getTamanho() / Math.pow(10, 9))), caracteristicaComponentes.get(i).getIdCaracteristicaComponente());
                        }
                    }
                }

            }

        }
    }

}
