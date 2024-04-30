package org.app.client.dao.entity;

import java.util.Objects;

public class NomeProcesso {

    private Integer idNome;
    private String nome;

    public NomeProcesso() {
    }

    public NomeProcesso(Integer idNome, String nome) {
        this.idNome = idNome;
        this.nome = nome;
    }

    public Integer getIdNome() {
        return idNome;
    }

    public void setIdNome(Integer idNome) {
        this.idNome = idNome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NomeProcesso that = (NomeProcesso) o;
        return Objects.equals(idNome, that.idNome);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idNome);
    }

}
