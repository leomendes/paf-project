/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.entities.AliquotasImpressora;
import br.com.sinetic.pafecftef.ui.UIAbreCaixa;
import br.com.sinetic.pafecftef.ui.UIAliquotas;
import java.awt.Frame;
import java.util.List;
import javax.swing.JDialog;

/**
 *
 * @author Windows8
 */
public class AAliquotas implements DialogAction {
    
    private UIAliquotas uiAliquotas;
    
    
    public AAliquotas(Frame f) {
      
        uiAliquotas = new UIAliquotas(f, true);
        uiAliquotas.setAction(this);
        
    }
    
   
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela() {
        List<AliquotasImpressora> aL = DBControl.getAliquotas();
        
        if (aL == null) {
            return false;
        }
        
        if (!uiAliquotas.setAliquotas(aL)) {
            return false;
        }
        
        try {
            uiAliquotas.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean fecharTela() {
        try {
            uiAliquotas.setVisible(false);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    
    @Override
    public JDialog getDialog() {
        return uiAliquotas;
    }
}
