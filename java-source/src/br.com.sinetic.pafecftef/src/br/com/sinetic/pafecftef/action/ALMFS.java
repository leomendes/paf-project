/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.printer.Printer;
import br.com.sinetic.pafecftef.ui.UILMFS;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author Sinetic
 */
public class ALMFS implements DialogAction {
    private UILMFS uiLMFS;
    
    public ALMFS(Frame f) {
      
        uiLMFS = new UILMFS(f, true);
        uiLMFS.setAction(this);
        
    }
    
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela() {
        try {
            uiLMFS.clearScreen();
            uiLMFS.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean fecharTela() {
        try {
            uiLMFS.setVisible(false);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean EmitirLeituraMFSimplificadaData(String dataInicio, String dataFinal, boolean geraEspelho) {
        if (!Control.lmfData(dataInicio, dataFinal, Printer.LMF_SIMPLIFICADA)) {
            return false;
        }
        
        if (geraEspelho) {
            if (!Control.lmfDataEspelho(dataInicio, dataFinal, Printer.LMF_SIMPLIFICADA)) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean EmitirLeituraMFSimplificadaReducao(int iRI, int iRF, boolean geraEspelho) {
        
        if (iRI < 0) {
            System.out.println("Leitura não pode ser emitida, Redução Inicial menor que 0");
            return false;
        }
        
        if (iRI < 0 || iRF < iRI) {
            System.out.println("Leitura não pode ser emitida, "
                               + "Redução Final menor que 0 ou menor que Redução Inicial");
            return false;
        }
        
        if (!Control.lmfReducao(iRI, iRF, Printer.LMF_SIMPLIFICADA)) {
            return false;
        }
        
        if (geraEspelho) {
            if (!Control.lmfReducaoEspelho(iRI, iRF, Printer.LMF_SIMPLIFICADA)) {
                return false;
            }
        }
        

        return false;
    }
    
    
    @Override
    public JDialog getDialog() {
        return uiLMFS;
    }
}
