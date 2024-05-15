package org.app.client.herancas;

import com.github.britooo.looca.api.core.Looca;
import org.app.client.dao.entity.Componente;

public class Memoria extends Componente{
    private Integer idMemoria;
    private String fabricante;
    private Integer fkComponente;
    private Looca looca;

    public Memoria(Integer idComponente, String nome, Integer fkComputador, Integer idMemoria, String fabricante, Integer fkComponente, Looca looca) {
        super(idComponente, nome, fkComputador);
        this.idMemoria = idMemoria;
        this.fabricante = fabricante;
        this.fkComponente = fkComponente;
        this.looca = looca;
    }

    public Memoria() {
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
