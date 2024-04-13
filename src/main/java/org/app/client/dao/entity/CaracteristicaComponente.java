package org.app.client.dao.entity;

public class CaracteristicaComponente {
  private Integer idCaracteristicaComponente;
  private String nome;
  private String valor;
  private Integer fkComponente;

  public CaracteristicaComponente() {
  }

  public CaracteristicaComponente(Integer idCaracteristicaComponente, String nome, String valor, Integer fkComponente) {
    this.idCaracteristicaComponente = idCaracteristicaComponente;
    this.nome = nome;
    this.valor = valor;
    this.fkComponente = fkComponente;
  }

  public Integer getIdCaracteristicaComponente() {
    return idCaracteristicaComponente;
  }

  public void setIdCaracteristicaComponente(Integer idCaracteristicaComponente) {
    this.idCaracteristicaComponente = idCaracteristicaComponente;
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

  public Integer getFkComponente() {
    return fkComponente;
  }

  public void setFkComponente(Integer fkComponente) {
    this.fkComponente = fkComponente;
  }

  @Override
  public String toString() {
    return "CaracteristicaComponente{" +
            "idCaracteristicaComponente=" + idCaracteristicaComponente +
            ", nome='" + nome + '\'' +
            ", valor='" + valor + '\'' +
            ", fkComponente=" + fkComponente +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CaracteristicaComponente)) return false;

    CaracteristicaComponente that = (CaracteristicaComponente) o;

    if (!getIdCaracteristicaComponente().equals(that.getIdCaracteristicaComponente())) return false;
    return getIdCaracteristicaComponente().equals(that.getIdCaracteristicaComponente());
  }

  @Override
  public int hashCode() {
    Integer result = getIdCaracteristicaComponente().hashCode();
    return result;
  }

}
