package org.app.client.util;

import org.app.client.conexao.Conexao;
import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.Processo;
import org.app.client.util.driver.DriverManagerLinux;
import org.app.client.util.driver.DriverManagerWindows;
import org.app.client.util.tasks.TaskManager;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ExecutarPrograma {

    public static Conexao conexao = new Conexao();

    public static void executarPrograma(String so, String user, Computador computador, List<Processo> processos, String sudo){

        TaskManager.taskKill(so, computador, processos);
        if(so.toUpperCase().contains("win".toUpperCase())){
            DriverManagerWindows.removerDriversInvalidos(computador);
        }else if(so.contains("nux")){
            DriverManagerLinux.removerDriversInvalidos(user, computador, sudo);
        }
    }
}
