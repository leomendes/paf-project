/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.ui.UIAbreCaixa;
import java.awt.Frame;
import javax.swing.JDialog;


/**
 *
 * @author Sinetic
 */
public class AAbreCaixa implements DialogAction {
    
    private UIAbreCaixa uiAbreCaixa;
    
    private boolean suSimples = false;
    
    public AAbreCaixa(Frame f) {
      
        uiAbreCaixa = new UIAbreCaixa(f, true);
        uiAbreCaixa.setAction(this);
        
    }
    
    /**
     * Próxima ação será suprimento simples, 
     * sem registrar abertura de caixa.
     * @return 
     */
    public void suprimentoSimples() {
        suSimples = true;
    }

    
    public boolean abreCaixa(float lValor) {
        //Verifica se é um suprimento simples ou uma abertura de caixa
        if (suSimples) {
            if (lValor > 0) {
                if (!Control.suprimento(lValor)) {
                    Controller.messageBox("Erro ao realizar suprimento!");
                    return false;
                }
            }
        }
        else {
            if (Control.abrirDiaFiscal(lValor)) {
                Controller.messageBox("Caixa aberto!");
            }
            else {
                Controller.messageBox("Erro ao abrir caixa!");
            }
            
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
            if (suSimples) {
                uiAbreCaixa.clearScreen(false, "Suprimento", "Suprimento");
            }
            else {
                uiAbreCaixa.clearScreen(true, "Abre Caixa", "Abre Caixa");
            }
            uiAbreCaixa.setVisible(true);
        }
        catch (Exception e) {
            suSimples = false;
            return false;
        }
        return true;
    }
    
    public boolean fecharTela() {
        try {
            suSimples = false;
            uiAbreCaixa.setVisible(false);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean efetuarSuprimento(float valor) {
        
        return false;
    }
    
    @Override
    public JDialog getDialog() {
        return uiAbreCaixa;
    }
}

