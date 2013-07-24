package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Fornecedores generated by hbm2java
 */
public class Fornecedores  implements java.io.Serializable {


     private int codigo;
     private FornecedoresRamosAtividades fornecedoresRamosAtividades;
     private Usuario usuarioByModificadoPor;
     private FornecedoresRelacoesComerciais fornecedoresRelacoesComerciais;
     private Usuario usuarioByAdicionadoPor;
     private String tipoFornecedor;
     private String endRua;
     private String endNumero;
     private String endComplemento;
     private String endCep;
     private String endBairro;
     private String endCidade;
     private String endEstado;
     private String observacoes;
     private Boolean status;
     private Short acessos;
     private Date adicionadoEm;
     private Date modificadoEm;
     private Set produtosesForCodFornecedor = new HashSet(0);
     private Set produtosesForCodFabricante = new HashSet(0);
     private Set produtoMarcas = new HashSet(0);

    public Fornecedores() {
    }

	
    public Fornecedores(int codigo) {
        this.codigo = codigo;
    }
    public Fornecedores(int codigo, FornecedoresRamosAtividades fornecedoresRamosAtividades, Usuario usuarioByModificadoPor, FornecedoresRelacoesComerciais fornecedoresRelacoesComerciais, Usuario usuarioByAdicionadoPor, String tipoFornecedor, String endRua, String endNumero, String endComplemento, String endCep, String endBairro, String endCidade, String endEstado, String observacoes, Boolean status, Short acessos, Date adicionadoEm, Date modificadoEm, Set produtosesForCodFornecedor, Set produtosesForCodFabricante, Set produtoMarcas) {
       this.codigo = codigo;
       this.fornecedoresRamosAtividades = fornecedoresRamosAtividades;
       this.usuarioByModificadoPor = usuarioByModificadoPor;
       this.fornecedoresRelacoesComerciais = fornecedoresRelacoesComerciais;
       this.usuarioByAdicionadoPor = usuarioByAdicionadoPor;
       this.tipoFornecedor = tipoFornecedor;
       this.endRua = endRua;
       this.endNumero = endNumero;
       this.endComplemento = endComplemento;
       this.endCep = endCep;
       this.endBairro = endBairro;
       this.endCidade = endCidade;
       this.endEstado = endEstado;
       this.observacoes = observacoes;
       this.status = status;
       this.acessos = acessos;
       this.adicionadoEm = adicionadoEm;
       this.modificadoEm = modificadoEm;
       this.produtosesForCodFornecedor = produtosesForCodFornecedor;
       this.produtosesForCodFabricante = produtosesForCodFabricante;
       this.produtoMarcas = produtoMarcas;
    }
   
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public FornecedoresRamosAtividades getFornecedoresRamosAtividades() {
        return this.fornecedoresRamosAtividades;
    }
    
    public void setFornecedoresRamosAtividades(FornecedoresRamosAtividades fornecedoresRamosAtividades) {
        this.fornecedoresRamosAtividades = fornecedoresRamosAtividades;
    }
    public Usuario getUsuarioByModificadoPor() {
        return this.usuarioByModificadoPor;
    }
    
    public void setUsuarioByModificadoPor(Usuario usuarioByModificadoPor) {
        this.usuarioByModificadoPor = usuarioByModificadoPor;
    }
    public FornecedoresRelacoesComerciais getFornecedoresRelacoesComerciais() {
        return this.fornecedoresRelacoesComerciais;
    }
    
    public void setFornecedoresRelacoesComerciais(FornecedoresRelacoesComerciais fornecedoresRelacoesComerciais) {
        this.fornecedoresRelacoesComerciais = fornecedoresRelacoesComerciais;
    }
    public Usuario getUsuarioByAdicionadoPor() {
        return this.usuarioByAdicionadoPor;
    }
    
    public void setUsuarioByAdicionadoPor(Usuario usuarioByAdicionadoPor) {
        this.usuarioByAdicionadoPor = usuarioByAdicionadoPor;
    }
    public String getTipoFornecedor() {
        return this.tipoFornecedor;
    }
    
    public void setTipoFornecedor(String tipoFornecedor) {
        this.tipoFornecedor = tipoFornecedor;
    }
    public String getEndRua() {
        return this.endRua;
    }
    
    public void setEndRua(String endRua) {
        this.endRua = endRua;
    }
    public String getEndNumero() {
        return this.endNumero;
    }
    
    public void setEndNumero(String endNumero) {
        this.endNumero = endNumero;
    }
    public String getEndComplemento() {
        return this.endComplemento;
    }
    
    public void setEndComplemento(String endComplemento) {
        this.endComplemento = endComplemento;
    }
    public String getEndCep() {
        return this.endCep;
    }
    
    public void setEndCep(String endCep) {
        this.endCep = endCep;
    }
    public String getEndBairro() {
        return this.endBairro;
    }
    
    public void setEndBairro(String endBairro) {
        this.endBairro = endBairro;
    }
    public String getEndCidade() {
        return this.endCidade;
    }
    
    public void setEndCidade(String endCidade) {
        this.endCidade = endCidade;
    }
    public String getEndEstado() {
        return this.endEstado;
    }
    
    public void setEndEstado(String endEstado) {
        this.endEstado = endEstado;
    }
    public String getObservacoes() {
        return this.observacoes;
    }
    
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
    public Boolean getStatus() {
        return this.status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public Short getAcessos() {
        return this.acessos;
    }
    
    public void setAcessos(Short acessos) {
        this.acessos = acessos;
    }
    public Date getAdicionadoEm() {
        return this.adicionadoEm;
    }
    
    public void setAdicionadoEm(Date adicionadoEm) {
        this.adicionadoEm = adicionadoEm;
    }
    public Date getModificadoEm() {
        return this.modificadoEm;
    }
    
    public void setModificadoEm(Date modificadoEm) {
        this.modificadoEm = modificadoEm;
    }
    public Set getProdutosesForCodFornecedor() {
        return this.produtosesForCodFornecedor;
    }
    
    public void setProdutosesForCodFornecedor(Set produtosesForCodFornecedor) {
        this.produtosesForCodFornecedor = produtosesForCodFornecedor;
    }
    public Set getProdutosesForCodFabricante() {
        return this.produtosesForCodFabricante;
    }
    
    public void setProdutosesForCodFabricante(Set produtosesForCodFabricante) {
        this.produtosesForCodFabricante = produtosesForCodFabricante;
    }
    public Set getProdutoMarcas() {
        return this.produtoMarcas;
    }
    
    public void setProdutoMarcas(Set produtoMarcas) {
        this.produtoMarcas = produtoMarcas;
    }




}

