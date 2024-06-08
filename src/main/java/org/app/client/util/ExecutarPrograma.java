package org.app.client.util;


import org.app.client.conexao.Conexao;
import org.app.client.conexao.ConexaoSql;
import org.app.client.dao.controller.AlertaController;
import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.NomeProcesso;
import org.app.client.util.driver.DriverManagerLinux;
import org.app.client.util.driver.DriverManagerWindows;
import org.app.client.util.tasks.TaskManager;

import java.util.ArrayList;
import java.util.List;

public class ExecutarPrograma implements Runnable{

    public static Conexao conexao = new Conexao();
    public static ConexaoSql conexaoSql = new ConexaoSql();
    private String so;
    private String user;
    private Computador computador;
    private List<NomeProcesso> processos = new ArrayList<>();
    private String sudo;
    private Integer contador = 1;


    public ExecutarPrograma(String so, String user, Computador computador, List<NomeProcesso> processos, String sudo) {
        this.so = so;
        this.user = user;
        this.computador = computador;
        this.processos = processos;
        this.sudo = sudo;
    }

    @Override
    public void run() {
            while(true){
                executarCaptura();
                synchronized (this){
                    try {
                        if(contador % 2 == 0){
                            this.wait();
                        }else{
                            Thread.sleep(2000);
                        }
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
    }

    public void notifyThread(){
        synchronized (this){
            this.notify();
        }
    }

    public void executarCaptura(){
        TaskManager.taskKill(so, computador, processos);
        if(so.toUpperCase().contains("win".toUpperCase())){
            DriverManagerWindows.removerDriversInvalidos(computador);
        }else if(so.contains("nux")){
            DriverManagerLinux.removerDriversInvalidos(user, computador, sudo);
        }
    }

    public Integer getContador() {
        return contador;
    }

    public void setContador(Integer contador) {
        this.contador = contador;
    }
}
