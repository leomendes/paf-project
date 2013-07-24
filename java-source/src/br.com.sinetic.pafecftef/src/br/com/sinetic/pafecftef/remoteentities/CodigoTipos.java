package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * CodigoTipos generated by hbm2java
 */
public class CodigoTipos  implements java.io.Serializable {


     private int codigo;
     private String tipo;
     private Set codigoUniversals = new HashSet(0);

    public CodigoTipos() {
    }

	
    public CodigoTipos(int codigo) {
        this.codigo = codigo;
    }
    public CodigoTipos(int codigo, String tipo, Set codigoUniversals) {
       this.codigo = codigo;
       this.tipo = tipo;
       this.codigoUniversals = codigoUniversals;
    }
   
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public String getTipo() {
        return this.tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    public Set getCodigoUniversals() {
        return this.codigoUniversals;
    }
    
    public void setCodigoUniversals(Set codigoUniversals) {
        this.codigoUniversals = codigoUniversals;
    }




}

