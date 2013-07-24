/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.ui.UIConfiguracoes;
import java.awt.Frame;
import javax.swing.JDialog;

/**
 *
 * @author Windows8
 */
public class AConfiguracoes implements DialogAction {
    
    private UIConfiguracoes uiPortaSerial;
    
    private boolean suSimples = false;
    
    public AConfiguracoes(Frame f) {
      
        uiPortaSerial = new UIConfiguracoes(f, true);
        uiPortaSerial.setAction(this);
        
    }
    
    /**
     * @return 
     */
    public boolean setPorta(String ps) {
        Control.configuracoes.setPortaSerial(ps);
        return Control.updateConfiguracoes();
    }

    
    /**
     */
    public boolean carregarTela() {
        uiPortaSerial.clearScreen(Control.configuracoes.getPortaSerial());
        uiPortaSerial.setVisible(true);
        return true;
    }
    
    public boolean fecharTela() {
        try {
            uiPortaSerial.setVisible(false);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
   
    
    @Override
    public JDialog getDialog() {
        return uiPortaSerial;
    }
}
