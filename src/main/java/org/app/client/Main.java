package org.app.client;

import java.util.List;
import org.app.client.dao.controller.*;
import org.app.client.dao.entity.Componente;
import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.NomeProcesso;
import org.app.client.login.Login;
import com.github.britooo.looca.api.core.Looca;
import org.app.client.util.ExecutarPrograma;
import org.app.client.util.captura.Inicializacao;
import org.app.client.util.notificacoes.NotificacaoSlack;
import org.app.client.conexao.Conexao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

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
        List<Componente> componentes = componenteController.listarComponentes(computador.getIdComputador());

        while (true) {
            System.out.println("Iniciando captura de dados");
            try {
                alertaController.getAllAlertasCPU(computador);
                ExecutarPrograma.executarPrograma(so, user, computador, processos, sudo);
                Inicializacao.capturarRegistros(registroComponenteController, componentes, looca);
                Inicializacao.registrarUso(usoSistemaController, looca.getSistema(), fkSistemaOperacional, computador);

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    NotificacaoSlack.EnviarNotificacaoSlack("Erro ao capturar dados para o computador " + computador.getIdComputador() + ": " + e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            Thread.sleep(5000);
        }
    }
}
