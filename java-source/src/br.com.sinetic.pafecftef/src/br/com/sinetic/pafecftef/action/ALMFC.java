/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.printer.Printer;
import br.com.sinetic.pafecftef.printer.bematech.BemaPrinter;
import br.com.sinetic.pafecftef.ui.UILMFC;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 *
 * @author Sinetic
 */
public class ALMFC implements DialogAction {
    
    private UILMFC uiLMFC;
    
    public ALMFC(Frame f) {
      
        uiLMFC = new UILMFC(f, true);
        uiLMFC.setAction(this);
        
    }
    
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela() {
        try {
            uiLMFC.clearScreen();
            uiLMFC.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean fecharTela() {
        try {
            uiLMFC.setVisible(false);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean EmitirLeituraMFCompletaData(String dataInicio, String dataFinal, boolean geraEspelho, boolean geraArquivo) {
        if (!Control.lmfData(dataInicio, dataFinal, Printer.LMF_COMPLETA)) {
            return false;
        }
        
        if (geraEspelho) {
            if (!Control.lmfDataEspelho(dataInicio, dataFinal, Printer.LMF_COMPLETA)) {
                return false;
            }
        }
        
        if (geraArquivo) {
            if (!Control.lmfDataArquivo(dataInicio, dataFinal)) {
                return false;
            }
        }
        
        return true;
    }
    
    public boolean EmitirLeituraMFCompletaReducao(int iRI, int iRF, boolean geraEspelho, boolean geraArquivo) {
        
        if (iRI < 0) {
            System.out.println("Leitura não pode ser emitida, Redução Inicial menor que 0");
            return false;
        }
        
        if (iRI < 0 || iRF < iRI) {
            System.out.println("Leitura não pode ser emitida, "
                               + "Redução Final menor que 0 ou menor que Redução Inicial");
            return false;
        }
        
        if (!Control.lmfReducao(iRI, iRF, Printer.LMF_COMPLETA)) {
            return false;
        }
        
        if (geraEspelho) {
            if (!Control.lmfReducaoEspelho(iRI, iRF, Printer.LMF_COMPLETA)) {
                return false;
            }
        }
        
        if (geraArquivo) {
            if (!Control.lmfReducaoArquivo(iRI, iRF)) {
                return false;
            }
        }
        return false;
    }
    
    
    @Override
    public JDialog getDialog() {
        return uiLMFC;
    }
}
