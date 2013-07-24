/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.ui.UIArqMFD;
import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.printer.Printer;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JPanel;


/**
 *
 * @author Sinetic
 */
public class AArqMFD implements DialogAction {
    
    private UIArqMFD uiArqMFD;
    
    public AArqMFD(Frame f) {
      
        uiArqMFD = new UIArqMFD(f, true);
        uiArqMFD.setAction(this);
        
    }
    
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela() {
        try {
            uiArqMFD.clearScreen();
            uiArqMFD.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean fecharTela() {
        try {
            uiArqMFD.setVisible(false);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean EmitirArquivoMFDData(String dataInicio, String dataFinal) {
        if (Control.mfdDataArquivo(dataInicio, dataFinal)) {
            return true;
        }
        return false;
    }
    
    public boolean EmitirArquivoMFDReducao(int iRI, int iRF) {
        
        if (iRI < 0) {
            System.out.println("Arquivo MFD não pode ser emitido, Redução Inicial menor que 0");
            return false;
        }
        
        if (iRI < 0 || iRF < iRI) {
            System.out.println("Arquivo MFD não pode ser emitido, "
                               + "Redução Final menor que 0 ou menor que Redução Inicial");
            return false;
        }
        
        if (Control.mfdReducaoArquivo(iRI, iRF)) {
            return true;
        }
        return false;
    }
    
    
    @Override
    public JDialog getDialog() {
        return uiArqMFD;
    }
}
