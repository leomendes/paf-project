/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.entities.PafProduct;
import br.com.sinetic.pafecftef.ui.UICadProdutosServicos;
import br.com.sinetic.pafecftef.ui.UIProdutosServicos;
import java.awt.Frame;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author user
 */
public class AProdutosServicos implements DialogAction {

    private UIProdutosServicos uiProdutosServicos;
    
    public AProdutosServicos(JFrame frame){
        uiProdutosServicos = new UIProdutosServicos(frame, true);
        uiProdutosServicos.setAction(this);
        uiProdutosServicos.setaCadProdutosServicos(new ACadProdutosServicos(frame));
    }

    public AProdutosServicos(Frame mainFrame) {
        uiProdutosServicos = new UIProdutosServicos(mainFrame, true);
        uiProdutosServicos.setAction(this);
        uiProdutosServicos.setaCadProdutosServicos(new ACadProdutosServicos(mainFrame));
    }
    
     /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela() {
        List<PafProduct> list;
        list = DBControl.getPafProduct();
        
        if (list == null) {
            return false;
        }
        
        if (!uiProdutosServicos.setRegistros(list)) {
            return false;
        }
        
        try {
            uiProdutosServicos.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        return Boolean.TRUE;
    }
    
    public boolean fecharTela() {
        uiProdutosServicos.setVisible(false);
        return Boolean.TRUE;
    }
    
    @Override
    public JDialog getDialog() {
        return uiProdutosServicos;
    }
    
}
