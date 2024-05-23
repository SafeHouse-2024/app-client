package org.app.client.herancas;

import com.github.britooo.looca.api.core.Looca;
import org.app.client.dao.controller.ComponenteController;
import org.app.client.dao.entity.Componente;

public class Memoria extends ComponenteController {
    private Integer idMemoria;
    private String fabricante;
    private Integer fkComponente;
    private Looca looca;

    public Memoria(Integer idMemoria, String fabricante, Integer fkComponente, Looca looca) {
        this.idMemoria = idMemoria;
        this.fabricante = fabricante;
        this.fkComponente = fkComponente;
        this.looca = looca;
    }

    public Memoria() {
    }

    @Override
    public Componente adicionarComponente(String nome, Integer fkComputador) {
        return super.adicionarComponente(nome, fkComputador);
    }

    public void atualizarValorCaracteristica(String valor, Integer idCaracteristica){

    }

    public Integer getIdMemoria() {
        return idMemoria;
    }

    public void setIdMemoria(Integer idMemoria) {
        this.idMemoria = idMemoria;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public Integer getFkComponente() {
        return fkComponente;
    }

    public void setFkComponente(Integer fkComponente) {
        this.fkComponente = fkComponente;
    }

    public Looca getLooca() {
        return looca;
    }

    public void setLooca(Looca looca) {
        this.looca = looca;
    }

    @Override
    public String toString() {
        return "Memoria{" +
                "idMemoria=" + idMemoria +
                ", fabricante='" + fabricante + '\'' +
                ", fkComponente=" + fkComponente +
                ", looca=" + looca +
                '}';
    }
}
