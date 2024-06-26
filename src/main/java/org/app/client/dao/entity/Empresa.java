package org.app.client.dao.entity;

public class Empresa {
  private Integer idEmpresa;
  private String nome;
  private String email;
  private String razaoSocial;
  private String telefone;
  private String cnpj;
  private String codigoCriacao;

  public Empresa() {
  }

  public Empresa(Integer idEmpresa, String nome, String email, String razaoSocial, String telefone, String cnpj) {
    this.idEmpresa = idEmpresa;
    this.nome = nome;
    this.email = email;
    this.razaoSocial = razaoSocial;
    this.telefone = telefone;
    this.cnpj = cnpj;
  }

  public Integer getIdEmpresa() {
    return idEmpresa;
  }

  public void setIdEmpresa(Integer idEmpresa) {
    this.idEmpresa = idEmpresa;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getRazaoSocial() {
    return razaoSocial;
  }

  public void setRazaoSocial(String razaoSocial) {
    this.razaoSocial = razaoSocial;
  }

  public String getTelefone() {
    return telefone;
  }

  public void setTelefone(String telefone) {
    this.telefone = telefone;
  }

  public String getCnpj() {
    return cnpj;
  }

  public void setCnpj(String cnpj) {
    this.cnpj = cnpj;
  }

  public String getCodigoCriacao() {
    return codigoCriacao;
  }

  public void setCodigoCriacao(String codigoCriacao) {
    this.codigoCriacao = codigoCriacao;
  }

  @Override
  public String toString() {
    return "Empresa [idEmpresa=" + idEmpresa + ", nome=" + nome + ", email=" + email + ", razaoSocial=" + razaoSocial
        + ", telefone=" + telefone + ", cnpj=" + cnpj + "]";
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;

    if (obj == null)
      return false;

    if (getClass() != obj.getClass())
      return false;

    Empresa other = (Empresa) obj;

    if (idEmpresa == null) {
      if (other.idEmpresa != null)
        return false;
    } else if (!idEmpresa.equals(other.idEmpresa))
      return false;

    return true;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((idEmpresa == null) ? 0 : idEmpresa.hashCode());
    return result;
  }
}
