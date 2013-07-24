package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * ServicoTipo generated by hbm2java
 */
public class ServicoTipo  implements java.io.Serializable {


     private int codigo;
     private String nome;
     private String descricao;
     private boolean status;
     private Set servicos = new HashSet(0);

    public ServicoTipo() {
    }

	
    public ServicoTipo(int codigo, String nome, String descricao, boolean status) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.status = status;
    }
    public ServicoTipo(int codigo, String nome, String descricao, boolean status, Set servicos) {
       this.codigo = codigo;
       this.nome = nome;
       this.descricao = descricao;
       this.status = status;
       this.servicos = servicos;
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
    public Set getServicos() {
        return this.servicos;
    }
    
    public void setServicos(Set servicos) {
        this.servicos = servicos;
    }




}


