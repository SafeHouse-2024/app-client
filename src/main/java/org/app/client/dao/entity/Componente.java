package org.app.client.dao.entity;

public class Componente {
  private Integer idComponente;
  private String nome;
  private Integer fkComputador;

  public Componente() {
  }

  public Componente(Integer idComponente, String nome, Integer fkComputador) {
    this.idComponente = idComponente;
    this.nome = nome;
    this.fkComputador = fkComputador;
  }

  public Integer getIdComponente() {
    return idComponente;
  }

  public void setIdComponente(Integer idComponente) {
    this.idComponente = idComponente;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public Integer getFkComputador() {
    return fkComputador;
  }

  public void setFkComputador(Integer fkComputador) {
    this.fkComputador = fkComputador;
  }

  @Override
  public String toString() {
    return "Componente{" +
            "idComponente=" + idComponente +
            ", nome='" + nome + '\'' +
            ", fkComputador=" + fkComputador +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Componente that = (Componente) o;

    if (idComponente != null ? !idComponente.equals(that.idComponente) : that.idComponente != null) return false;
    return idComponente != null ? idComponente.equals(that.idComponente) : that.idComponente == null;
  }

  @Override
  public int hashCode() {
    int result = idComponente != null ? idComponente.hashCode() : 0;
    return result;
  }
}
