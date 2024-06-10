package org.app.client.util.captura;

import com.github.britooo.looca.api.core.Looca;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.rede.RedeInterface;
import com.github.britooo.looca.api.group.sistema.Sistema;
import org.app.client.Log;
import org.app.client.LogType;
import org.app.client.conexao.Conexao;
import org.app.client.dao.controller.*;
import org.app.client.dao.entity.CaracteristicaComponente;
import org.app.client.dao.entity.Componente;
import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.UsoSistema;
import org.app.client.herancas.Ping;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inicializacao implements Runnable{

    private final RegistroComponenteController registroComponenteController;
    private final List<Componente> componentes;
    private final Looca looca;
    private final UsoSistemaController usoSistemaController;
    private final Integer fkSistemaOperacional;
    private final Computador computador;
    private final AlertaController alertaController;


    public Inicializacao(RegistroComponenteController registroComponenteController, List<Componente> componentes, Looca looca, UsoSistemaController usoSistemaController, Integer fkSistemaOperacional, Computador computador, AlertaController alertaController) {
        this.registroComponenteController = registroComponenteController;
        this.componentes = componentes;
        this.looca = looca;
        this.usoSistemaController = usoSistemaController;
        this.fkSistemaOperacional = fkSistemaOperacional;
        this.computador = computador;
        this.alertaController = alertaController;
    }

    public static Computador adicionarEstruturaMaquina(Looca looca, String codigoAcesso, Integer fkSistemaOperacional) throws IOException {

        ComponenteController componenteController = new ComponenteController();
        ComputadorController computadorController = new ComputadorController();
        CaracteristicaComponenteController caracteristicaComponenteController = new CaracteristicaComponenteController();
        String so = System.getProperty("os.name");

        RedeInterface interfaceRede = looca.getRede().getGrupoDeInterfaces().getInterfaces().stream().filter(r -> r.getNome().equals("enX0")).findFirst().orElse(looca.getRede().getGrupoDeInterfaces().getInterfaces().get(looca.getRede().getGrupoDeInterfaces().getInterfaces().size()-1));
        String macAddress = interfaceRede.getEnderecoMac();
        Computador computador = new Computador();
        try{
            computador = computadorController.buscarMaquina(macAddress);
            System.out.println(computador);
        }catch (EmptyResultDataAccessException e){
            Log.generateLog("Máquina não cadastrada", LogType.INICIALIZACAO);
        }

        if (computador.getAtivo().equals("Inativo") && codigoAcesso.equalsIgnoreCase(computador.getCodigoAcesso())) {

            List<Volume> volumes = looca.getGrupoDeDiscos().getVolumes();
            Map<Componente, Map<String, Object>> valoresCaracteristicas = new HashMap<>();
            Map<String, Object> valores = new HashMap<>();

            Componente rede = componenteController.adicionarComponente("Rede", computador.getIdComputador());
            System.out.println("O id do computador é: " + computador.getIdComputador());

            Componente processador = componenteController.adicionarComponente("Processador", computador.getIdComputador());

            Componente memoria = componenteController.adicionarComponente("Memória", computador.getIdComputador());
            Computador computadorLocal = salvarMaquinaLocalCasoNaoExista(computador);
            //Disco Rígido
            Volume volumeArmazenado = volumes.get(0);
            for (int i = 0; i < volumes.size(); i++) {
                if(i == 0){
                    if(volumes.get(i).getTotal() > 0 && volumes.get(i).getTotal() > 128 * Math.pow(10, 9)){
                        Componente disco = componenteController.adicionarComponente("Disco", computador.getIdComputador());
                        if (volumes.get(i).getTotal() >= Math.pow(10, 9)) {
                            valores.put("Memória Total", "%.2f GB".formatted((volumes.get(i).getTotal() / Math.pow(10, 9))));
                            valores.put("idComponente", disco.getIdComponente());
                            valores.put("Memória Disponível", "%.2f GB".formatted((volumes.get(i).getDisponivel() / Math.pow(10, 9))));
                            System.out.println("Seu disco tem %.2fGB de armazenamento.".formatted(looca.getMemoria().getTotal() / Math.pow(10, 9)));
                        }
                        valoresCaracteristicas.put(disco, valores);
                    }
                }
                if(i > 0) {
                    if (volumes.get(i).getTotal() > 0 && volumes.get(i).getTotal() > 128 * Math.pow(10, 9) && !volumes.get(i).getUUID().equals(volumeArmazenado.getUUID())) {
                        Componente disco = componenteController.adicionarComponente("Disco", computador.getIdComputador());
                        if (volumes.get(i).getTotal() >= Math.pow(10, 9)) {
                            valores.put("Memória Total", "%.2f GB".formatted((volumes.get(i).getTotal() / Math.pow(10, 9))));
                            valores.put("idComponente", disco.getIdComponente());
                            valores.put("Memória Disponível", "%.2f GB".formatted((volumes.get(i).getDisponivel() / Math.pow(10, 9))));
                            System.out.println("Seu disco tem %.2fGB de armazenamento.".formatted(looca.getMemoria().getTotal() / Math.pow(10, 9)));
                        }
                        volumeArmazenado = volumes.get(i);
                    }
                }
            }

            caracteristicaComponenteController.adicionarCaracteristica("Fabricante", looca.getProcessador().getFabricante(), processador.getIdComponente());
            Log.generateLog("O fabricante do seu processador é %s".formatted(looca.getProcessador().getFabricante()), LogType.INICIALIZACAO);

            caracteristicaComponenteController.adicionarCaracteristica("Nome", looca.getProcessador().getNome(), processador.getIdComponente());
            Log.generateLog("O nome do seu processador é %s".formatted(looca.getProcessador().getNome()), LogType.INICIALIZACAO);

            caracteristicaComponenteController.adicionarCaracteristica("Frequência", looca.getProcessador().getFrequencia().toString(), processador.getIdComponente());
            Log.generateLog("A sua frequência é %d".formatted(looca.getProcessador().getFrequencia()), LogType.INICIALIZACAO);

            caracteristicaComponenteController.adicionarCaracteristica("Núcleos Físicos", looca.getProcessador().getNumeroCpusFisicas().toString(), processador.getIdComponente());
            Log.generateLog("Quantidade de núcleos físicos: %d".formatted(looca.getProcessador().getNumeroCpusFisicas()), LogType.INICIALIZACAO);

            caracteristicaComponenteController.adicionarCaracteristica("Núcleos Lógicos", looca.getProcessador().getNumeroCpusLogicas().toString(), processador.getIdComponente());
            Log.generateLog("Quantidade de núcleos lógicos: %d".formatted(looca.getProcessador().getNumeroCpusLogicas()), LogType.INICIALIZACAO);

            caracteristicaComponenteController.adicionarCaracteristica("Memória Total", "%.2f GB".formatted(looca.getMemoria().getTotal() / Math.pow(10, 9)), memoria.getIdComponente());
            Log.generateLog("Sua máquina tem %.2fGB de memória RAM.".formatted(looca.getMemoria().getTotal() / Math.pow(10, 9)), LogType.INICIALIZACAO);

            for(Map.Entry<Componente, Map<String, Object>> caracteristicas: valoresCaracteristicas.entrySet()){
                Componente componente = caracteristicas.getKey();
                for(Map.Entry<String, Object> caracteristica: caracteristicas.getValue().entrySet()){
                    if(!caracteristica.getKey().equals("idComponente")){
                        caracteristicaComponenteController.adicionarCaracteristica(caracteristica.getKey(), caracteristica.getValue().toString(), componente.getIdComponente());
                    }
                }
            }

            if(valoresCaracteristicas.isEmpty()){
                Componente disco = componenteController.adicionarComponente("Disco", computador.getIdComputador());
                ComponenteController.adicionarComponenteLocalmente("Disco", computadorLocal.getIdComputador(), disco.getIdComponente());
                caracteristicaComponenteController.adicionarCaracteristica("Memória Total", "%.2f GB".formatted((volumes.get(0).getTotal() / Math.pow(10, 9))), disco.getIdComponente());
                caracteristicaComponenteController.adicionarCaracteristica("Memória Disponível", "%.2f GB".formatted((volumes.get(0).getTotal() / Math.pow(10,9))), disco.getIdComponente());
                Log.generateLog("Seu disco tem %.2fGB de armazenamento.".formatted(looca.getMemoria().getTotal() / Math.pow(10, 9)), LogType.INICIALIZACAO);
                System.out.println("Seu disco tem %.2fGB de armazenamento.".formatted(looca.getGrupoDeDiscos().getVolumes().get(0) .getTotal()/ Math.pow(10, 9)));
            }

            try{
                caracteristicaComponenteController.adicionarCaracteristica("IPV6", interfaceRede.getEnderecoIpv6().get(0), rede.getIdComponente());
            }catch(IndexOutOfBoundsException e){
                System.out.println("Houve um problema no armazenamento do IPV6");
            }

            try{
                caracteristicaComponenteController.adicionarCaracteristica("IPV4", interfaceRede.getEnderecoIpv4().get(0), rede.getIdComponente());
            }catch (IndexOutOfBoundsException e){
                System.out.println("Houve um problema no armazenamento do IPV4");
            }

            Log.generateLog("O endereço IPV4 da sua máquina é: " + interfaceRede.getEnderecoIpv4().get(0), LogType.INICIALIZACAO);
            Log.generateLog("O endereço IPV6 da sua máquina é: " + interfaceRede.getEnderecoIpv6().get(0), LogType.INICIALIZACAO);
            Log.generateLog("O sistema operacional da máquina é " + looca.getSistema().getSistemaOperacional(), LogType.INICIALIZACAO);

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
        Ping ping = new Ping();
        Componente processador = componentes.stream().filter(componente -> componente.getNome().equalsIgnoreCase("Processador")).findFirst().get();
        Componente memoria = componentes.stream().filter(componente -> componente.getNome().equalsIgnoreCase("Memória")).findFirst().get();
        Componente rede = componentes.stream().filter(componente -> componente.getNome().equalsIgnoreCase("Rede")).findFirst().get();
        List<Componente> discos = componentes.stream().filter(componente -> componente.getNome().equalsIgnoreCase("Disco")).toList();
//        discos.forEach(disco -> pegarDisco(disco, looca));

        registroComponenteController.adicionarRegistro("Taxa de Uso", String.valueOf("%.2f".formatted(looca.getProcessador().getUso())), processador.getIdComponente());
        registroComponenteController.adicionarRegistro("Taxa de Uso", String.valueOf("%.2f".formatted(looca.getMemoria().getEmUso() / Math.pow(10, 9))), memoria.getIdComponente());
//        registroComponenteController.adicionarRegistro("Ping", String.valueOf("%s".formatted(ping.getPing())), rede.getIdComponente());
//        registroComponenteController.adicionarRegistro("Download", String.valueOf("%s".formatted(ping.getDownload())), rede.getIdComponente());
//        registroComponenteController.adicionarRegistro("Upload", String.valueOf("%s".formatted(ping.getUpload())), rede.getIdComponente());
        System.out.println("Inserindo a medição de RAM no banco de dados");
        System.out.println("A taxa de uso da memória RAM é: %.2f GB".formatted(looca.getMemoria().getEmUso() / Math.pow(10, 9)));
        System.out.println("Inserindo a medição de CPU no banco de dados");
        System.out.println("A taxa de uso do processador é: %.2f".formatted(looca.getProcessador().getUso()) + "%");
    }

    private static void pegarDisco(Componente componente, Looca looca) {
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

    private static void registrarUso(UsoSistemaController usoSistemaController, Sistema sistema, Integer fkSistemaOperacional, Computador computador){
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
        if(!sistema.getInicializado().equals(ZonedDateTime.of(usoSistema.getDataInicializacao(), ZoneId.of("UTC")).toInstant())){
            usoSistemaController.adicionarUsoSistema(sistema.getInicializado(), sistema.getTempoDeAtividade(), fkSistemaOperacional, computador);
            return;
        }
        usoSistemaController.atualizarUsoSistema(sistema.getTempoDeAtividade(), usoSistema);

        System.out.println("O tempo de atividade do sistema é: " + sistema.getTempoDeAtividade());
    }

    private void realizarMedicao(){
        capturarRegistros(registroComponenteController, componentes, looca);
        registrarUso(usoSistemaController, looca.getSistema(), fkSistemaOperacional, computador);
    }

    public void buscarAlertas(){
        alertaController.getAllAlertasCPU(computador);
        alertaController.getAllAlertasRAM(computador);
        alertaController.getAllAlertasDisco(computador);
    }

    public static Computador salvarMaquinaLocalCasoNaoExista(Computador computador){
        Conexao conexao = new Conexao();

        JdbcTemplate getConexao = conexao.getJdbcTemplate();
        Computador computadorLocal = null;
        try {
            computadorLocal = getConexao.queryForObject("SELECT * FROM Computador WHERE macAddress = ?", new BeanPropertyRowMapper<>(Computador.class), computador.getMacAddress());
        }catch (EmptyResultDataAccessException e){
        }
        if(computadorLocal == null){
            List<Componente> componentes = ComponenteController.listarComponentes(computador.getIdComputador());
            if(!componentes.isEmpty()){
                Computador computadorInserido = ComputadorController.inserirMaquinaLocal(computador);
                computadorLocal = computadorInserido;
                componentes.forEach(componente -> ComponenteController.adicionarComponenteLocalmente(componente.getNome(), computadorInserido.getIdComputador(), componente.getIdComponente()));
                System.out.println("Máquina cadastrada com sucesso");
            }else{
                computadorLocal = new Computador();
            }
        }

        return computadorLocal;
    }

    @Override
    public void run() {
        while(true){
            realizarMedicao();
            buscarAlertas();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
