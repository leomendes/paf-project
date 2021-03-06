package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * CodigoUniversal generated by hbm2java
 */
public class CodigoUniversal  implements java.io.Serializable {


     private long codigo;
     private CodigoTipos codigoTipos;
     private Set produtoses = new HashSet(0);

    public CodigoUniversal() {
    }

	
    public CodigoUniversal(long codigo) {
        this.codigo = codigo;
    }
    public CodigoUniversal(long codigo, CodigoTipos codigoTipos, Set produtoses) {
       this.codigo = codigo;
       this.codigoTipos = codigoTipos;
       this.produtoses = produtoses;
    }
   
    public long getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }
    public CodigoTipos getCodigoTipos() {
        return this.codigoTipos;
    }
    
    public void setCodigoTipos(CodigoTipos codigoTipos) {
        this.codigoTipos = codigoTipos;
    }
    public Set getProdutoses() {
        return this.produtoses;
    }
    
    public void setProdutoses(Set produtoses) {
        this.produtoses = produtoses;
    }




}


