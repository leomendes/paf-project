package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * CrediarioFormaPagamento generated by hbm2java
 */
public class CrediarioFormaPagamento  implements java.io.Serializable {


     private int codigo;
     private String nome;
     private String descricao;
     private boolean status;
     private Set crediarioFormaPagamentoDetalhados = new HashSet(0);

    public CrediarioFormaPagamento() {
    }

	
    public CrediarioFormaPagamento(int codigo, String nome, String descricao, boolean status) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
    }
    public CrediarioFormaPagamento(int codigo, String nome, String descricao, boolean status, Set crediarioFormaPagamentoDetalhados) {
       this.codigo = codigo;
       this.nome = nome;
       this.descricao = descricao;
       this.status = status;
       this.crediarioFormaPagamentoDetalhados = crediarioFormaPagamentoDetalhados;
    }
   
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    public boolean isStatus() {
        return this.status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }
    public Set getCrediarioFormaPagamentoDetalhados() {
        return this.crediarioFormaPagamentoDetalhados;
    }
    
    public void setCrediarioFormaPagamentoDetalhados(Set crediarioFormaPagamentoDetalhados) {
        this.crediarioFormaPagamentoDetalhados = crediarioFormaPagamentoDetalhados;
    }




}


