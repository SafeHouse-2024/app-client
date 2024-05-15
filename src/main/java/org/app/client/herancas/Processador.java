package org.app.client.herancas;

import com.github.britooo.looca.api.core.Looca;
import org.app.client.dao.entity.Componente;

public class Processador extends Componente{
    private Integer idProcessador;
    private String fabricante;
    private Integer fkComponente;
    private Looca looca;

    public Processador(Integer idComponente, String nome, Integer fkComputador, Integer idProcessador, String fabricante, Integer fkComponente, Looca looca) {
        super(idComponente, nome, fkComputador);
        this.idProcessador = idProcessador;
        this.fabricante = fabricante;
        this.fkComponente = fkComponente;
        this.looca = looca;
    }

    public Processador() {
    }

    public Integer getIdProcessador() {
        return idProcessador;
    }

    public void setIdProcessador(Integer idProcessador) {
        this.idProcessador = idProcessador;
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
        return "Processador{" +
                "idProcessador=" + idProcessador +
                ", fabricante='" + fabricante + '\'' +
                ", fkComponente=" + fkComponente +
                ", looca=" + looca +
                '}';
    }
}
