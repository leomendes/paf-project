/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;


import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.entities.PafMeiosPagamentoDia;
import br.com.sinetic.pafecftef.ui.UIMeiosDePagamento;
import java.awt.Frame;
import java.util.List;
import javax.swing.JDialog;

/**
 *
 * @author Windows8
 */
public class AMeiosDePagamento implements DialogAction {
    private UIMeiosDePagamento uiMdP;
    
    public AMeiosDePagamento(Frame f) {
      
        uiMdP = new UIMeiosDePagamento(f, true);
        uiMdP.setAction(this);
    }
    
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela() {
        try {
            uiMdP.clearScreen();
            uiMdP.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean fecharTela() {
        try {
            uiMdP.setVisible(false);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean EmitirRelatorioMdP(String dataInicio, String dataFim) {
        List<PafMeiosPagamentoDia> lP = DBControl.getMeiosDePagamento(dataInicio, dataFim);
        
        if (lP == null || lP.isEmpty()) {
            return false;
        }
        
        if (Control.relatorioMeiosDePagamento(lP)) {
            return true;
        }
        else {
            return false;
        }
    
    }
    
    @Override
    public JDialog getDialog() {
        return uiMdP;
    }
            
}
