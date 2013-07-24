/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.ui.UIFechaCaixa;
import java.awt.Frame;
import javax.swing.JDialog;


/**
 *
 * @author Sinetic
 */
public class AFechaCaixa implements DialogAction {
    
    private UIFechaCaixa uiFechaCaixa;
    
    private boolean saSimples = false;
    
    public AFechaCaixa(Frame f) {
      
        uiFechaCaixa = new UIFechaCaixa(f, true);
        uiFechaCaixa.setAction(this);
        
    }
    
    /**
     * Próxima ação será suprimento simples, 
     * sem registrar abertura de caixa.
     * @return 
     */
    public void sangriaSimples() {
        saSimples = true;
    }

    
    
    public boolean fechaCaixa(float lValor) {

        //Verifica se é uma sangria simples ou um fechamento de caixa
        if (saSimples) {
            if (lValor > 0) {
                if (!Control.sangria(lValor)) {
                    Controller.messageBox("Erro ao realizar sangria!");
                    return false;
                }
            }
        }
        else {
            Control.fecharDiaFiscal(lValor);
        }
        
        return true;
    }
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela() {
        try {
            if (saSimples) {
                uiFechaCaixa.clearScreen("Sangria", "Sangria");
            }
            else {
                uiFechaCaixa.clearScreen("Fecha Caixa", "Fecha Caixa");
            }
            uiFechaCaixa.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean fecharTela() {
        try {
            saSimples = false;
            uiFechaCaixa.setVisible(false);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }

    
    
    @Override
    public JDialog getDialog() {
        return uiFechaCaixa;
    }
}

