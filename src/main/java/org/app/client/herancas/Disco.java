package org.app.client.herancas;

import org.app.client.dao.entity.Componente;

public class Disco extends Componente{
    private Integer idDisco;
    private String fabricante;
    private Integer fkComponente;

    public Disco(Integer idComponente, String nome, Integer fkComputador, Integer idDisco, String fabricante, Integer fkComponente) {
        super(idComponente, nome, fkComputador);
        this.idDisco = idDisco;
        this.fabricante = fabricante;
        this.fkComponente = fkComponente;
    }

    public Disco() {
    }

    public Integer getIdDisco() {
        return idDisco;
    }

    public void setIdDisco(Integer idDisco) {
        this.idDisco = idDisco;
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

    @Override
    public String toString() {
        return "Disco{" +
                "idDisco=" + idDisco +
                ", fabricante='" + fabricante + '\'' +
                ", fkComponente=" + fkComponente +
                '}';
    }
}
