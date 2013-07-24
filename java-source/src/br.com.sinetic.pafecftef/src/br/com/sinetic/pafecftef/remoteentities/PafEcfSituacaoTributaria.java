package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * PafEcfSituacaoTributaria generated by hbm2java
 */
public class PafEcfSituacaoTributaria  implements java.io.Serializable {


     private char sigla;
     private String nome;
     private Set sineticControleEstoques = new HashSet(0);
     private Set servicos = new HashSet(0);

    public PafEcfSituacaoTributaria() {
    }

	
    public PafEcfSituacaoTributaria(char sigla, String nome) {
        this.sigla = sigla;
        this.nome = nome;
    }
    public PafEcfSituacaoTributaria(char sigla, String nome, Set sineticControleEstoques, Set servicos) {
       this.sigla = sigla;
       this.nome = nome;
       this.sineticControleEstoques = sineticControleEstoques;
       this.servicos = servicos;
    }
   
    public char getSigla() {
        return this.sigla;
    }
    
    public void setSigla(char sigla) {
        this.sigla = sigla;
    }
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    public Set getSineticControleEstoques() {
        return this.sineticControleEstoques;
    }
    
    public void setSineticControleEstoques(Set sineticControleEstoques) {
        this.sineticControleEstoques = sineticControleEstoques;
    }
    public Set getServicos() {
        return this.servicos;
    }
    
    public void setServicos(Set servicos) {
        this.servicos = servicos;
    }




}


