package org.app.client.dao.entity;

import java.util.Objects;

public class Alerta {

    private Integer idComputador;
    private Integer quantidade;

    public Alerta() {
    }

    public Alerta(Integer idComputador, Integer quantidade) {
        this.idComputador = idComputador;
        this.quantidade = quantidade;
    }

    public Integer getIdComputador() {
        return idComputador;
    }

    public void setIdComputador(Integer idComputador) {
        this.idComputador = idComputador;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "Alerta{" +
                "idComputador=" + idComputador +
                ", quantidade=" + quantidade +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alerta alerta = (Alerta) o;
        return Objects.equals(idComputador, alerta.idComputador) && Objects.equals(quantidade, alerta.quantidade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idComputador, quantidade);
    }
}
