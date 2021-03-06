package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;

/**
 * FornecedoresTiposContato generated by hbm2java
 */
public class FornecedoresTiposContato  implements java.io.Serializable {


     private short codigo;
     private String tipoContato;
     private Boolean status;
     private Set crediarioClientesForContato3 = new HashSet(0);
     private Set crediarioClientesForContato2 = new HashSet(0);
     private Set crediarioClientesForContato1 = new HashSet(0);

    public FornecedoresTiposContato() {
    }

	
    public FornecedoresTiposContato(short codigo) {
        this.codigo = codigo;
    }
    public FornecedoresTiposContato(short codigo, String tipoContato, Boolean status, Set crediarioClientesForContato3, Set crediarioClientesForContato2, Set crediarioClientesForContato1) {
       this.codigo = codigo;
       this.tipoContato = tipoContato;
       this.status = status;
       this.crediarioClientesForContato3 = crediarioClientesForContato3;
       this.crediarioClientesForContato2 = crediarioClientesForContato2;
       this.crediarioClientesForContato1 = crediarioClientesForContato1;
    }
   
    public short getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(short codigo) {
        this.codigo = codigo;
    }
    public String getTipoContato() {
        return this.tipoContato;
    }
    
    public void setTipoContato(String tipoContato) {
        this.tipoContato = tipoContato;
    }
    public Boolean getStatus() {
        return this.status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
    public Set getCrediarioClientesForContato3() {
        return this.crediarioClientesForContato3;
    }
    
    public void setCrediarioClientesForContato3(Set crediarioClientesForContato3) {
        this.crediarioClientesForContato3 = crediarioClientesForContato3;
    }
    public Set getCrediarioClientesForContato2() {
        return this.crediarioClientesForContato2;
    }
    
    public void setCrediarioClientesForContato2(Set crediarioClientesForContato2) {
        this.crediarioClientesForContato2 = crediarioClientesForContato2;
    }
    public Set getCrediarioClientesForContato1() {
        return this.crediarioClientesForContato1;
    }
    
    public void setCrediarioClientesForContato1(Set crediarioClientesForContato1) {
        this.crediarioClientesForContato1 = crediarioClientesForContato1;
    }




}


