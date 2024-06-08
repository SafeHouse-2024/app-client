package org.app.client.dao.entity;

import java.util.Objects;

public class DarkStore {

    private String nome;
    private Integer idDarkStore;
    private String rua;
    private String numero;
    private String complemento;
    private String cep;
    private String uf;
    private Integer fkEmpresa;

    public DarkStore() {
    }

    public DarkStore(String nome, Integer idDarkStore, String rua, String numero, String complemento, String cep, String uf, Integer fkEmpresa) {
        this.idDarkStore = idDarkStore;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.cep = cep;
        this.uf = uf;
        this.fkEmpresa = fkEmpresa;
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getIdDarkStore() {
        return idDarkStore;
    }

    public void setIdDarkStore(Integer idDarkStore) {
        this.idDarkStore = idDarkStore;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Integer getFkEmpresa() {
        return fkEmpresa;
    }

    public void setFkEmpresa(Integer fkEmpresa) {
        this.fkEmpresa = fkEmpresa;
    }

    @Override
    public String toString() {
        return "DarkStore{" +
                "nome='" + nome + '\'' +
                ", idDarkStore=" + idDarkStore +
                ", rua='" + rua + '\'' +
                ", numero='" + numero + '\'' +
                ", complemento='" + complemento + '\'' +
                ", cep='" + cep + '\'' +
                ", uf='" + uf + '\'' +
                ", fkEmpresa=" + fkEmpresa +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DarkStore darkStore = (DarkStore) o;
        return Objects.equals(idDarkStore, darkStore.idDarkStore);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idDarkStore);
    }
}
