package org.app.client.dao.entity;

public class Computador {
  private Integer idComputador;
  private String macAddress;
  private String nome;
  private String ativo;
  private String codigoAcesso;
  private Integer fkDarkStore;
  private Integer fkFuncionario;
  private Integer fkToken;

  public Computador() {
  }

  public Computador(Integer idComputador, String macAddress, String nome, String ativo, String codigoAcesso, Integer fkDarkStore, Integer fkFuncionario, Integer fkToken) {
    this.idComputador = idComputador;
    this.macAddress = macAddress;
    this.nome = nome;
    this.ativo = ativo;
    this.codigoAcesso = codigoAcesso;
    this.fkDarkStore = fkDarkStore;
    this.fkFuncionario = fkFuncionario;
    this.fkToken = fkToken;
  }

  //Getters and Setters

  public Integer getIdComputador() {
    return idComputador;
  }

  public void setIdComputador(Integer idComputador) {
    this.idComputador = idComputador;
  }

  public String getMacAddress() {
    return macAddress;
  }

  public void setMacAddress(String macAddress) {
    this.macAddress = macAddress;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getAtivo() {
    return ativo;
  }

  public void setAtivo(String ativo) {
    this.ativo = ativo;
  }

  public String getCodigoAcesso() {
    return codigoAcesso;
  }


  public void setCodigoAcesso(String codigoAcesso) {
    this.codigoAcesso = codigoAcesso;
  }

  public Integer getFkDarkStore() {
    return fkDarkStore;
  }

  public void setFkDarkStore(Integer fkDarkStore) {
    this.fkDarkStore = fkDarkStore;
  }

  public Integer getFkFuncionario() {
    return fkFuncionario;
  }

  public void setFkFuncionario(Integer fkFuncionario) {
    this.fkFuncionario = fkFuncionario;
  }

  public Integer getFkToken() {
    return fkToken;
  }

  public void setFkToken(Integer fkToken) {
    this.fkToken = fkToken;
  }

  @Override
  public String toString() {
    return "Computador{" +
            "idComputador=" + idComputador +
            ", macAddress='" + macAddress + '\'' +
            ", nome='" + nome + '\'' +
            ", ativo='" + ativo + '\'' +
            ", codigoAcesso='" + codigoAcesso + '\'' +
            ", fkDarkStore=" + fkDarkStore +
            ", fkFuncionario=" + fkFuncionario +
            ", fkToken=" + fkToken +
            '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Computador that = (Computador) o;

    if (idComputador != null ? !idComputador.equals(that.idComputador) : that.idComputador != null) return false;
    return idComputador != null ? idComputador.equals(that.idComputador) : that.idComputador == null;
  }

  @Override
  public int hashCode() {
    Integer result = idComputador != null ? idComputador.hashCode() : 0;
    return result;
  }
}
