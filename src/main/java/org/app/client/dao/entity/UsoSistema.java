package org.app.client.dao.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;

public class UsoSistema {

    private Integer idUsoSistema;
    private LocalDateTime dataInicializacao;
    private Integer tempoAtividadeMinutos;
    private Integer fkSistemaOperacional;
    private Integer fkComputador;

    public UsoSistema() {
    }

    public UsoSistema(Integer idUso, LocalDateTime dataInicializacao, Integer tempoAtividadeMinutos, Integer fkSistemaOperacional, Integer fkComputador) {
        this.idUsoSistema = idUso;
        this.dataInicializacao = dataInicializacao;
        this.tempoAtividadeMinutos = tempoAtividadeMinutos;
        this.fkSistemaOperacional = fkSistemaOperacional;
        this.fkComputador = fkComputador;
    }

    public Integer getIdUsoSistema() {
        return idUsoSistema;
    }

    public void setIdUsoSistema(Integer idUso) {
        this.idUsoSistema = idUso;
    }

    public LocalDateTime getDataInicializacao() {
        return dataInicializacao;
    }

    public void setDataInicializacao(LocalDateTime dataInicializacao) {
        this.dataInicializacao = dataInicializacao;
    }

    public Integer getTempoAtividadeMinutos() {
        return tempoAtividadeMinutos;
    }

    public void setTempoAtividadeMinutos(Integer tempoAtividadeMinutos) {
        this.tempoAtividadeMinutos = tempoAtividadeMinutos;
    }

    public Integer getFkSistemaOperacional() {
        return fkSistemaOperacional;
    }

    public void setFkSistemaOperacional(Integer fkSistemaOperacional) {
        this.fkSistemaOperacional = fkSistemaOperacional;
    }

    public Integer getFkComputador() {
        return fkComputador;
    }

    public void setFkComputador(Integer fkComputador) {
        this.fkComputador = fkComputador;
    }

    @Override
    public String toString() {
        return "UsoSistema{" +
                "idUso=" + idUsoSistema +
                ", tempoInicializacao=" + dataInicializacao +
                ", tempoAtividadeMinutos=" + tempoAtividadeMinutos +
                ", fkSistemaOperacional=" + fkSistemaOperacional +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsoSistema that = (UsoSistema) o;
        return Objects.equals(idUsoSistema, that.idUsoSistema);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idUsoSistema);
    }
}
