package org.app.client;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.github.britooo.looca.api.group.discos.Volume;
import org.app.client.dao.controller.*;
import org.app.client.dao.entity.Componente;
import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.Processo;
import org.app.client.dao.entity.UsoSistema;
import org.app.client.login.Login;

import com.github.britooo.looca.api.core.Looca;
import org.app.client.util.ExecutarPrograma;
import org.app.client.util.captura.Inicializacao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        Looca teste = new Looca();
        System.out.println(teste.getRede().getGrupoDeInterfaces().getInterfaces().get(0).getEnderecoMac());

        String so = System.getProperty("os.name");
        String user = System.getProperty("user.name");

        Integer fkSistemaOperacional = 0;
        if(so.toUpperCase().contains("win".toUpperCase())){
            fkSistemaOperacional = 1;
        } else if (so.contains("nux")) {
            fkSistemaOperacional = 2;
        }

        ProcessoController processoController = new ProcessoController();
        List<Processo> processos = processoController.listarProcessos(fkSistemaOperacional);
        Login login = new Login();
        String[] result = login.autenticar(fkSistemaOperacional).split("#");
        String codigoAcesso = result[0];
        String sudo = result[1];
        Looca looca = new Looca();
        Computador computador = Inicializacao.adicionarEstruturaMaquina(looca, codigoAcesso, fkSistemaOperacional);
        RegistroComponenteController registroComponenteController = new RegistroComponenteController();
        ComponenteController componenteController = new ComponenteController();
        UsoSistemaController usoSistemaController = new UsoSistemaController();

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