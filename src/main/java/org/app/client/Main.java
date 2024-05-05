package org.app.client;


import java.net.URISyntaxException;
import java.util.List;

import org.app.client.dao.controller.*;
import org.app.client.dao.entity.Componente;
import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.NomeProcesso;
import org.app.client.login.Login;

import com.github.britooo.looca.api.core.Looca;
import org.app.client.util.ExecutarPrograma;
import org.app.client.util.captura.Inicializacao;
import org.app.client.util.websocket.Websocket;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Looca teste = new Looca();

        String so = System.getProperty("os.name");
        String user = System.getProperty("user.name");

        Integer fkSistemaOperacional = 0;
        if(so.toUpperCase().contains("win".toUpperCase())){
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
        try {
            Websocket.initializeWebsocketClient();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        List<Componente> componentes = componenteController.listarComponentes(computador.getIdComputador());

         while(true){
            System.out.println("Iniciando captura de dados");

             ExecutarPrograma.executarPrograma(so, user, computador, processos, sudo);
             Inicializacao.capturarRegistros(registroComponenteController, componentes, looca);
             Inicializacao.registrarUso(usoSistemaController, looca.getSistema(), fkSistemaOperacional, computador);
             Thread.sleep(5000);
         }

    }
}