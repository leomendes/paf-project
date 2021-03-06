package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * SineticProdutoModelo generated by hbm2java
 */
public class SineticProdutoModelo  implements java.io.Serializable {


     private int codigo;
     private long codigoMarca;
     private String nome;
     private boolean ativado;
     private Set produtoses = new HashSet(0);

    public SineticProdutoModelo() {
    }

	
    public SineticProdutoModelo(int codigo, long codigoMarca, String nome, boolean ativado) {
        this.codigo = codigo;
        this.codigoMarca = codigoMarca;
        this.nome = nome;
        this.ativado = ativado;
    }
    public SineticProdutoModelo(int codigo, long codigoMarca, String nome, boolean ativado, Set produtoses) {
       this.codigo = codigo;
       this.codigoMarca = codigoMarca;
       this.nome = nome;
       this.ativado = ativado;
       this.produtoses = produtoses;
    }
   
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public long getCodigoMarca() {
        return this.codigoMarca;
    }
    
    public void setCodigoMarca(long codigoMarca) {
        this.codigoMarca = codigoMarca;
    }
    public String getNome() {
        return this.nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    public boolean isAtivado() {
        return this.ativado;
    }
    
    public void setAtivado(boolean ativado) {
        this.ativado = ativado;
    }
    public Set getProdutoses() {
        return this.produtoses;
    }
    
    public void setProdutoses(Set produtoses) {
        this.produtoses = produtoses;
    }




}


