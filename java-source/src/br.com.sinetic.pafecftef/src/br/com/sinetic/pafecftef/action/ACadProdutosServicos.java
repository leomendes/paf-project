/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.entities.PafProduct;
import br.com.sinetic.pafecftef.ui.UICadProdutosServicos;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author user
 */
public class ACadProdutosServicos implements DialogAction {

    private UICadProdutosServicos uICadProdutosServicos;

    public ACadProdutosServicos(Frame frame) {
        uICadProdutosServicos = new UICadProdutosServicos(frame, true);
        uICadProdutosServicos.setAction(this);
    }
    
    public ACadProdutosServicos(JFrame frame) {
        uICadProdutosServicos = new UICadProdutosServicos(frame, true);
        uICadProdutosServicos.setAction(this);
    }

    public boolean carregarTela() {
        try {
            uICadProdutosServicos.setVisible(Boolean.TRUE);
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    public boolean carregarTela(String gtin) {
        try {
            PafProduct product = Control.getProductByGtin(gtin);
            uICadProdutosServicos.loadPafProduct(product.getGtin(), 
                    product.getDescricao(), product.getUnidadeMedida(), 
                    product.getValor(), product.getStatus());
            
            uICadProdutosServicos.setVisible(Boolean.TRUE);
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    public boolean fecharTela() {
        try {
            uICadProdutosServicos.setVisible(Boolean.FALSE);
        } catch (Exception e) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
    public boolean gravar(String gtin, String desc, String value, String unid, String sit){
        PafProduct pafProduct = new PafProduct();
        pafProduct.setGtin(gtin);
        pafProduct.setDescricao(desc);
        pafProduct.setUnidadeMedida(unid);
        pafProduct.setValor(Double.parseDouble(value));
        pafProduct.setStatus(sit);
        
        if(Control.gravarProduto(pafProduct)){
            return Boolean.TRUE;
        }
        
        return Boolean.FALSE;
    }

    @Override
    public JDialog getDialog() {
        return uICadProdutosServicos;
    }
}
