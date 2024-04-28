package org.app.client.util.captura;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.sistema.Sistema;
import org.app.client.dao.controller.*;
import org.app.client.dao.entity.CaracteristicaComponente;
import org.app.client.dao.entity.Componente;
import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.UsoSistema;
import org.springframework.dao.EmptyResultDataAccessException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Inicializacao {

    public static Computador adicionarEstruturaMaquina(Looca looca, String codigoAcesso, Integer fkSistemaOperacional) {

        ComponenteController componenteController = new ComponenteController();
        ComputadorController computadorController = new ComputadorController();
        CaracteristicaComponenteController caracteristicaComponenteController = new CaracteristicaComponenteController();

        List<RedeInterface> redes = looca.getRede().getGrupoDeInterfaces().getInterfaces();
        String macAddress = redes.get(redes.size() - 1).getEnderecoMac();
        System.out.println("O macAddress da máquina é " + macAddress);
        Computador computador = new Computador();
        try{
            computador = computadorController.buscarMaquina(macAddress);
        }catch (EmptyResultDataAccessException e){
            System.out.println("Máquina não cadastrada!");
        }

        if (computador.getAtivo().equals("Inativo") && codigoAcesso.equals(computador.getCodigoAcesso())) {

            List<Volume> volumes = looca.getGrupoDeDiscos().getVolumes();
            // // Adicionando componentes

            // Processador
            Componente processador = componenteController.adicionarComponente("Processador",
                    computador.getIdComputador());
            caracteristicaComponenteController.adicionarCaracteristica("Fabricante",
                    looca.getProcessador().getFabricante(), processador.getIdComponente());
                System.out.println("O fabricante do seu processador é " + looca.getProcessador().getFabricante());
            caracteristicaComponenteController.adicionarCaracteristica("Nome", looca.getProcessador().getNome(),
                    processador.getIdComponente());
                System.out.println("O nome do seu processador é " + looca.getProcessador().getNome());
            caracteristicaComponenteController.adicionarCaracteristica("Frequência",
                    looca.getProcessador().getFrequencia().toString(), processador.getIdComponente());
                System.out.println("A sua frequência é " + looca.getProcessador().getFrequencia());
            caracteristicaComponenteController.adicionarCaracteristica("Núcleos Físicos",
                    looca.getProcessador().getNumeroCpusFisicas().toString(), processador.getIdComponente());
                System.out.println("A quantidade de núcleos físicos são: " + looca.getProcessador().getNumeroCpusFisicas());
            caracteristicaComponenteController.adicionarCaracteristica("Núcleos Lógicos",
                    looca.getProcessador().getNumeroCpusLogicas().toString(), processador.getIdComponente());
                System.out.println("A quantidade de núcleos lógicos são: " + looca.getProcessador().getNumeroCpusLogicas());

            //Memoria RAM
            Componente memoria = componenteController.adicionarComponente("Memória", computador.getIdComputador());
            caracteristicaComponenteController.adicionarCaracteristica("Memória Total",
                    String.valueOf("%.2f GB".formatted(looca.getMemoria().getTotal() / Math.pow(10, 9))), memoria.getIdComponente());
            System.out.println("Sua máquina tem %.2fGB de memória RAM.".formatted(looca.getMemoria().getTotal() / Math.pow(10, 9)));

            //Disco Rígido
            Volume volumeArmazenado = volumes.get(0);
            for (int i = 0; i < volumes.size(); i++) {
                if(i == 0){
                    if(volumes.get(i).getTotal() > 0 && volumes.get(i).getTotal() > 128 * Math.pow(10, 9)){
                        Componente disco = componenteController.adicionarComponente("Disco", computador.getIdComputador());
                        if (volumes.get(i).getTotal() >= Math.pow(10, 9)) {
                            caracteristicaComponenteController.adicionarCaracteristica("Memória Total", String.valueOf("%.2f GB".formatted((volumes.get(i).getTotal()) / Math.pow(10, 9))), disco.getIdComponente());
                            caracteristicaComponenteController.adicionarCaracteristica("Memória Disponível", String.valueOf(volumes.get(i).getDisponivel() / Math.pow(10, 9)), disco.getIdComponente());
                            System.out.println("Seu disco tem %.2fGB de armazenamento.".formatted(looca.getMemoria().getTotal() / Math.pow(10, 9)));
                        }
                    }
                }
                if(i > 0) {
                    if (volumes.get(i).getTotal() > 0 && volumes.get(i).getTotal() > 128 * Math.pow(10, 9) && !volumes.get(i).getUUID().equals(volumeArmazenado.getUUID())) {
                        Componente disco = componenteController.adicionarComponente("Disco", computador.getIdComputador());
                        if (volumes.get(i).getTotal() >= Math.pow(10, 9)) {
                            caracteristicaComponenteController.adicionarCaracteristica("Memória Total", String.valueOf("%.2f GB".formatted((volumes.get(i).getTotal()) / Math.pow(10, 9))), disco.getIdComponente());
                            caracteristicaComponenteController.adicionarCaracteristica("Memória Disponível", String.valueOf(volumes.get(i).getDisponivel() / Math.pow(10, 9)), disco.getIdComponente());
                            System.out.println("Seu disco tem %.2fGB de armazenamento.".formatted(looca.getMemoria().getTotal() / Math.pow(10, 9)));
                        }
                        volumeArmazenado = volumes.get(i);
                    }
                }
            }

            Componente rede = componenteController.adicionarComponente("Rede", computador.getIdComputador());
            caracteristicaComponenteController.adicionarCaracteristica("IPV4", redes.get(redes.size()-1).getEnderecoIpv4().get(0), rede.getIdComponente());
            caracteristicaComponenteController.adicionarCaracteristica("IPV6", redes.get(redes.size()-1).getEnderecoIpv6().get(0), rede.getIdComponente());
            System.out.println("O endereço IPV4 da sua máquina é: " + redes.get(redes.size()-1).getEnderecoIpv4().get(0));
            System.out.println("O endereço IPV6 da sua máquina é: " + redes.get(redes.size()-1).getEnderecoIpv6().get(0));
            System.out.println("O sistema operacional da máquina é " + looca.getSistema().getSistemaOperacional());

            computadorController.inserirSistemaOperacional(fkSistemaOperacional, computador);
            computadorController.ativarMaquina(computador.getIdComputador());
            computador.setAtivo("Ativo");
        }

        if(!computador.getCodigoAcesso().equals(codigoAcesso)){
            System.out.println("Código de acesso inválido");
            System.exit(0);
        }

        return computador;
    }

    public static void capturarRegistros(RegistroComponenteController registroComponenteController, List<Componente> componentes, Looca looca) {
        Componente processador = componentes.stream().filter(componente -> componente.getNome().equalsIgnoreCase("Processador")).findFirst().get();
        Componente memoria = componentes.stream().filter(componente -> componente.getNome().equalsIgnoreCase("Memória")).findFirst().get();
        List<Componente> discos = componentes.stream().filter(componente -> componente.getNome().equalsIgnoreCase("Disco")).toList();
//        discos.forEach(disco -> pegarDisco(disco, looca));
        registroComponenteController.adicionarRegistro("Taxa de Uso", String.valueOf("%.2f".formatted(looca.getProcessador().getUso())), processador.getIdComponente());
        registroComponenteController.adicionarRegistro("Taxa de Uso", String.valueOf("%.2f".formatted(looca.getMemoria().getEmUso() / Math.pow(10, 9))), memoria.getIdComponente());
        System.out.println("Inserindo a medição de RAM no banco de dados");
        System.out.println("A taxa de uso da memória RAM é:  %.2f GB".formatted(looca.getMemoria().getEmUso() / Math.pow(10, 9)));
        System.out.println("Inserindo a medição de CPU no banco de dados");
        System.out.println("A taxa de uso do processador é: %.2f".formatted(looca.getProcessador().getUso()) + "%");
    }

    public static void pegarDisco(Componente componente, Looca looca) {
        CaracteristicaComponenteController caracteristicaComponenteController = new CaracteristicaComponenteController();
        List<CaracteristicaComponente> caracteristicaComponentes = caracteristicaComponenteController.listarCaracteristicas(componente.getIdComponente());
        List<Volume> volumes = looca.getGrupoDeDiscos().getVolumes();
        for (int i = 0; i < caracteristicaComponentes.size(); i++) {
            if(volumes.get(i).getTotal() > 0 && volumes.get(i).getTotal() > 128 * Math.pow(10, 9))
                if (caracteristicaComponentes.get(i).getNome().equals("Memória Disponível")) {
                    if (!caracteristicaComponentes.get(i).getValor().equals(String.valueOf("%.2f GB".formatted(volumes.get(i).getDisponivel() / Math.pow(10, 9))))) {
                        if (volumes.get(i).getDisponivel() > 0) {
                            if (volumes.get(i).getDisponivel() >= Math.pow(10, 9)) {
                                caracteristicaComponenteController.atualizarValorCaracteristica(String.valueOf("%.2f GB".formatted(volumes.get(i).getDisponivel() / Math.pow(10, 9))), caracteristicaComponentes.get(i).getIdCaracteristicaComponente());
                            }
                        }
                    }
                }

        }
        System.out.println("A memória disponível do disco é: " + volumes.get(0).getDisponivel());
    }

    public static void registrarUso(UsoSistemaController usoSistemaController, Sistema sistema, Integer fkSistemaOperacional, Computador computador){
        UsoSistema usoSistema = null;
        try{
            usoSistema = usoSistemaController.pegarUsoSistema(computador);
        }catch (EmptyResultDataAccessException e){
            System.out.println("Realizando a primeira medição de uso do sistema");
        }
        if(usoSistema == null){
            usoSistemaController.adicionarUsoSistema(sistema.getInicializado(), sistema.getTempoDeAtividade(), fkSistemaOperacional, computador);
            return;
        }
        if(!sistema.getInicializado().equals(ZonedDateTime.of(usoSistema.getDataInicializacao(), ZoneId.of("America/Sao_Paulo")).toInstant())){
            usoSistemaController.adicionarUsoSistema(sistema.getInicializado(), sistema.getTempoDeAtividade(), fkSistemaOperacional, computador);
            return;
        }
        usoSistemaController.atualizarUsoSistema(sistema.getTempoDeAtividade(), usoSistema);

        System.out.println("O tempo de atividade do sistema é: " + sistema.getTempoDeAtividade());

    }




}

