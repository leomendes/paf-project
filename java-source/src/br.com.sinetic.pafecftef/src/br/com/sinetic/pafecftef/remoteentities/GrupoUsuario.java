package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * GrupoUsuario generated by hbm2java
 */
public class GrupoUsuario  implements java.io.Serializable {


     private int id;
     private String nome;
     private String descricao;
     private String pagina;
     private Boolean status;
     private Set usuarios = new HashSet(0);

    public GrupoUsuario() {
    }

	
    public GrupoUsuario(int id) {
        this.id = id;
    }
    public GrupoUsuario(int id, String nome, String descricao, String pagina, Boolean status, Set usuarios) {
       this.id = id;
       this.nome = nome;
       this.descricao = descricao;
       this.pagina = pagina;
       this.status = status;
       this.usuarios = usuarios;
    }
   
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
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
    public String getPagina() {
        return this.pagina;
    }
    
    public void setPagina(String pagina) {
        this.pagina = pagina;
    }
    public Boolean getStatus() {
        return this.status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public Set getUsuarios() {
        return this.usuarios;
    }
    
    public void setUsuarios(Set usuarios) {
        this.usuarios = usuarios;
    }




}

