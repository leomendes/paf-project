/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.ui.UIArqEstoque;
import java.awt.Frame;
import java.io.File;
import javax.swing.JDialog;
import br.com.sinetic.pafecftef.control.Control;
import static br.com.sinetic.pafecftef.control.Control.ecfIdentificacao;
import br.com.sinetic.pafecftef.control.Registros;
import br.com.sinetic.pafecftef.entities.EcfExecutaveis;

/**
 *
 * @author Windows8
 */
public class AArqEstoque implements DialogAction {
    
    public static final String NOME_ARQ_ESTOQUE = "ArqEstoque.txt";
    
    private UIArqEstoque uiArqE;
    
    public AArqEstoque(Frame f) {
      
        uiArqE = new UIArqEstoque(f, true);
        uiArqE.setAction(this);
        
    }
    
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela() {
        try {
            uiArqE.clearScreen();
            uiArqE.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean fecharTela() {
        try {
            uiArqE.setVisible(false);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    
    public boolean gerarArquivoEstoque(boolean completo) {
        if (completo) {
            if (Registros.geraArquivoEletEstoque()) {
                return true;
            }
            else {
                return true;
            }
        }
        else {
            
        }
        return false;
    }
    
    /*private static boolean estoqueSimples() {

        //Cria o arquivo MD5
        String arqPath = System.getProperty("user.dir") + "\\"+NOME_ARQ_ESTOQUE;
        File arquivo = Control.criaArquivo(arqPath);
        
        
        //Gera os registros necessários
        String e1 = geraRegE1();
        String e2 = geraRegE2();
        String e9 = "";
        for (EcfExecutaveis ee: lEE) {
            e9 += geraRegE9(ee);
        }
        
        if (e1 == null || e2 == null ||
            e9 == null) {
            return false;
        }
        String registros = e1+e2+e9;
        
        //Grava arquivo sem assinatura
        if (!Control.gravaRegistros(arquivo, registros)) {
            return false;
        }

        //Gera o EAD do arquivo
        String ead = Control.geraEAD(arqPath);
        
        //Grava assinatura
        if (!Control.gravaRegistros(arquivo, ead)) {
            return false;
        }
        
        return true;
    }*/
    
    
    
    @Override
    public JDialog getDialog() {
        return uiArqE;
    }
}
