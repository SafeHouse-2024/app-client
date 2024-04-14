package org.app.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.github.britooo.looca.api.group.discos.Disco;
import com.github.britooo.looca.api.group.discos.Volume;
import com.github.britooo.looca.api.group.processador.Processador;
import org.app.client.dao.controller.*;
import org.app.client.dao.entity.Componente;
import org.app.client.dao.entity.Computador;
import org.app.client.login.Login;
import org.app.client.util.ExecutarPrograma;

import com.github.britooo.looca.api.core.Looca;
import org.app.client.dao.entity.CaracteristicaComponente;
import org.app.client.util.captura.Inicializacao;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Login login = new Login();
        String codigoAcesso = login.autenticar();
        Looca looca = new Looca();
        Computador computador = Inicializacao.adicionarEstruturaMaquina(looca, codigoAcesso);
        RegistroComponenteController registroComponenteController = new RegistroComponenteController();
        ComponenteController componenteController = new ComponenteController();
        List<Componente> componentes = componenteController.listarComponentes(computador.getIdComputador());


         while(true){
             Inicializacao.capturarRegistros(registroComponenteController, componentes, looca);
             Thread.sleep(5000);
         }

    }
}