/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.entities;

import java.util.Date;

/**
 *
 * @author user
 */
public class TestDate {
    
    private Integer id;
    private Date dataHora;
    private Date nDataHora;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the dataHora
     */
    public Date getDataHora() {
        return dataHora;
    }

    /**
     * @param dataHora the dataHora to set
     */
    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    /**
     * @return the nDataHora
     */
    public Date getnDataHora() {
        return nDataHora;
    }

    /**
     * @param nDataHora the nDataHora to set
     */
    public void setnDataHora(Date nDataHora) {
        this.nDataHora = nDataHora;
    }
    
}
