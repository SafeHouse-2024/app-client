package org.app.client;


import java.util.List;

import org.app.client.dao.controller.*;
import org.app.client.dao.entity.Componente;
import org.app.client.dao.entity.Computador;
import org.app.client.login.Login;

import com.github.britooo.looca.api.core.Looca;
import org.app.client.util.captura.Inicializacao;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        String so = System.getProperty("os.name");

        Integer fkSistemaOperacional = 0;
        if(so.contains("win")){
            fkSistemaOperacional = 1;
        } else if (so.contains("nux")) {
            fkSistemaOperacional = 2;
        }

        Login login = new Login();
        String codigoAcesso = login.autenticar();
        Looca looca = new Looca();

        Computador computador = Inicializacao.adicionarEstruturaMaquina(looca, codigoAcesso, fkSistemaOperacional);
        RegistroComponenteController registroComponenteController = new RegistroComponenteController();
        ComponenteController componenteController = new ComponenteController();
        UsoSistemaController usoSistemaController = new UsoSistemaController();

        List<Componente> componentes = componenteController.listarComponentes(computador.getIdComputador());

         while(true){
             Inicializacao.capturarRegistros(registroComponenteController, componentes, looca);
             Inicializacao.registrarUso(usoSistemaController, looca.getSistema(), fkSistemaOperacional, computador);
             Thread.sleep(5000);
         }

    }
}