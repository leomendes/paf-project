/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.entities;

import br.com.sinetic.pafecftef.control.Control;
import java.util.Date;

/**
 *
 * @author user
 */
public class Entity {
    
    protected Date dataHora;
    
    public void setDatePrinter(){
        setDataHora(Control.getDataImpressora());
    }
    
    public Date getDataHora() {
        return this.dataHora;
    }
    
    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }
    
}
