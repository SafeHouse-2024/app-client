package org.app.client;



import java.net.URISyntaxException;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.util.List;
import org.app.client.dao.controller.*;
import org.app.client.dao.entity.*;
import org.app.client.login.Login;
import com.github.britooo.looca.api.core.Looca;
import org.app.client.util.ExecutarPrograma;
import org.app.client.util.captura.CapturaRede;
import org.app.client.util.captura.Inicializacao;

import org.app.client.util.websocket.Websocket;

import org.app.client.util.notificacoes.NotificacaoSlack;



public class Main {
    public static void main(String[] args) throws InterruptedException {

        String so = System.getProperty("os.name");
        String user = System.getProperty("user.name");

        Integer fkSistemaOperacional = 0;
        if (so.toUpperCase().contains("win".toUpperCase())) {
            fkSistemaOperacional = 1;
        } else if (so.contains("nux")) {
            fkSistemaOperacional = 2;
        }

        NomeProcessoController nomeProcessoController = new NomeProcessoController();
        List<NomeProcesso> processos = nomeProcessoController.listarProcessos(fkSistemaOperacional);
        Login login = new Login();
        String[] result = login.autenticar(fkSistemaOperacional).split("#");
        String codigoAcesso = result[0];
        String sudo = result[1];
        Looca looca = new Looca();
        Computador computador = Inicializacao.adicionarEstruturaMaquina(looca, codigoAcesso, fkSistemaOperacional);
        RegistroComponenteController registroComponenteController = new RegistroComponenteController();
        ComponenteController componenteController = new ComponenteController();
        UsoSistemaController usoSistemaController = new UsoSistemaController();
        AlertaController alertaController = new AlertaController();

        Socket socket;
        try {
            socket = Websocket.initializeWebsocketClient(EmpresaController.fetchEmpresa(computador.getIdComputador()), DarkStoreController.fetchDarkStore(computador.getIdComputador()), computador.getMacAddress());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }


        List<Componente> componentes = componenteController.listarComponentes(computador.getIdComputador());
        Componente componenteRede = componentes.stream().filter(componente -> componente.getNome().equals("Rede")).findFirst().get();

        CapturaRede capturaRede = new CapturaRede(registroComponenteController, componenteRede);
        Thread buscarRede = new Thread(capturaRede);
        ExecutarPrograma executarPrograma = new ExecutarPrograma(so, user, computador, processos, sudo);
        Thread executarInovacao = new Thread(executarPrograma);
        Inicializacao inicializacao = new Inicializacao(registroComponenteController, componentes, looca, usoSistemaController, fkSistemaOperacional, computador, alertaController);
        Thread iniciarMedicao = new Thread(inicializacao);
        iniciarMedicao.start();
        executarInovacao.start();
        buscarRede.start();

        socket.on("receive_message_%s".formatted(looca.getRede().getGrupoDeInterfaces().getInterfaces().get(looca.getRede().getGrupoDeInterfaces().getInterfaces().size() - 1).getEnderecoMac()), new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                    Integer contador = executarPrograma.getContador();
                    if(contador % 2 != 0){
                        System.out.println("Modo de segurança desativado");
                    }else{
                        System.out.println("Modo de segurança ativado");
                        executarPrograma.notifyThread();
                    }
                    executarPrograma.setContador(contador += 1);
            }
        });
    }
}
