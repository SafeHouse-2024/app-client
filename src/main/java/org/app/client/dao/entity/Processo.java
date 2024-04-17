package org.app.client.dao.entity;

import java.util.Objects;

public class Processo {

    private Integer idProcesso;
    private String nomeWindows;
    private String nomeLinux;
    private String permitido;

    public Processo() {
    }

    public Processo(Integer idProcesso, String nomeWindows, String nomeLinux, String permitido) {
        this.idProcesso = idProcesso;
        this.nomeWindows = nomeWindows;
        this.nomeLinux = nomeLinux;
        this.permitido = permitido;
    }

    public Integer getIdProcesso() {
        return idProcesso;
    }

    public void setIdProcesso(Integer idProcesso) {
        this.idProcesso = idProcesso;
    }

    public String getNomeWindows() {
        return nomeWindows;
    }

    public void setNomeWindows(String nomeWindows) {
        this.nomeWindows = nomeWindows;
    }

    public String getNomeLinux() {
        return nomeLinux;
    }

    public void setNomeLinux(String nomeLinux) {
        this.nomeLinux = nomeLinux;
    }

    public String getPermitido() {
        return permitido;
    }

    public void setPermitido(String permitido) {
        this.permitido = permitido;
    }

    @Override
    public String toString() {
        return "Processo{" +
                "idProcesso=" + idProcesso +
                ", nomeWindows='" + nomeWindows + '\'' +
                ", nomeLinux='" + nomeLinux + '\'' +
                ", permitido='" + permitido + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Processo processo = (Processo) o;
        return Objects.equals(idProcesso, processo.idProcesso);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idProcesso);
    }
}
