/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.ui.UIArqMFD;
import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.printer.Printer;
import br.com.sinetic.pafecftef.ui.UIEspelhoMFD;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JPanel;


/**
 *
 * @author Sinetic
 */
public class AEspelhoMFD implements DialogAction {
    
    private UIEspelhoMFD uiEspelhoMFD;
    
    public AEspelhoMFD(Frame f) {
      
        uiEspelhoMFD = new UIEspelhoMFD(f, true);
        uiEspelhoMFD.setAction(this);
        
    }
    
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela() {
        try {
            uiEspelhoMFD.clearScreen();
            uiEspelhoMFD.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean fecharTela() {
        try {
            uiEspelhoMFD.setVisible(false);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean EmitirEspelhoMFDData(String dataInicio, String dataFinal) {
        if (Control.mfdDataEspelho(dataInicio, dataFinal)) {
            return true;
        }
        return false;
    }
    
    public boolean EmitirEspelhoMFDReducao(int iRI, int iRF) {
        
        if (iRI < 0) {
            System.out.println("Espelho MFD não pode ser emitido, Redução Inicial menor que 0");
            return false;
        }
        
        if (iRI < 0 || iRF < iRI) {
            System.out.println("Espelho MFD não pode ser emitido, "
                               + "Redução Final menor que 0 ou menor que Redução Inicial");
            return false;
        }
        
        if (Control.mfdReducaoEspelho(iRI, iRF)) {
            return true;
        }
        return false;
    }
    
    
    @Override
    public JDialog getDialog() {
        return uiEspelhoMFD;
    }
}
