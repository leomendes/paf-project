/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.entities;

import java.io.Serializable;

/**
 *
 * @author user
 */
public class PafProduct implements Serializable{
 
    private String gtin;
    private String descricao;
    private String status;
    private String unidadeMedida;
    private Double valor;
    private String hash;

    /**
     * @return the gtin
     */
    public String getGtin() {
        return gtin;
    }

    /**
     * @param gtin the gtin to set
     */
    public void setGtin(String gtin) {
        this.gtin = gtin;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the unidadeMedida
     */
    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    /**
     * @param unidadeMedida the unidadeMedida to set
     */
    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    /**
     * @return the valor
     */
    public Double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }
  
    /**
     * @return the hash
     */
    public String getHash() {
        return hash;
    }

    /**
     * @param hash the hash to set
     */
    public void setHash(String hash) {
        this.hash = hash;
    }
    
    public String getStatusDesc(){
        if(status == null)
            return "";
        
        switch (status) {
            case "I":
                return "Isento";
            case "N":
                return "Não Tributado";
            case "F":
                return "Substituição Tributária";
            case "T":
                return "Tributado pelo ICMS";
            case "S":
                return "Tributado pelo ISSQN";
        }
        
        return "";
    }
    
    @Override
    public String toString(){
        return gtin+descricao+valor+unidadeMedida+status;
    }
}
