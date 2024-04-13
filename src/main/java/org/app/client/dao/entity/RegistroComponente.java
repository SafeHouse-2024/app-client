package org.app.client.dao.entity;

import java.time.LocalDateTime;

public class RegistroComponente {
  private Integer idRegistroComponente;
  private String nome;
  private String valor;
  private LocalDateTime dataRegistro;
  private Integer fkComponente;

  public RegistroComponente() {
  }

  public RegistroComponente(Integer idRegistroComponente, String nome, String valor, LocalDateTime dataRegistro, Integer fkComponente) {
    this.idRegistroComponente = idRegistroComponente;
    this.nome = nome;
    this.valor = valor;
    this.dataRegistro = dataRegistro;
    this.fkComponente = fkComponente;
  }

  public Integer getIdRegistroComponente() {
    return idRegistroComponente;
  }

  public void setIdRegistroComponente(Integer idRegistroComponente) {
    this.idRegistroComponente = idRegistroComponente;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getValor() {
    return valor;
  }

  public void setValor(String valor) {
    this.valor = valor;
  }

  public LocalDateTime getDataRegistro() {
    return dataRegistro;
  }

  public void setDataRegistro(LocalDateTime dataRegistro) {
    this.dataRegistro = dataRegistro;
  }

  public Integer getFkComponente() {
    return fkComponente;
  }

  public void setFkComponente(Integer fkComponente) {
    this.fkComponente = fkComponente;
  }

  @Override
  public String toString() {
    return "RegistroComponente{" +
            "idRegistroComponente=" + idRegistroComponente +
            ", nome='" + nome + '\'' +
            ", valor='" + valor + '\'' +
            ", dataRegistro=" + dataRegistro +
            ", fkComponente=" + fkComponente +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    RegistroComponente that = (RegistroComponente) o;

    if (!idRegistroComponente.equals(that.idRegistroComponente)) return false;
    return true;
  }

  @Override
  public int hashCode() {
    Integer result = idRegistroComponente.hashCode();
    return result;
  }

}
