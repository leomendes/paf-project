/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.ui.UITelaPrincipal;
import java.awt.Frame;
import javax.swing.JPanel;

/**
 *
 * @author Administrador
 */
public class ATelaPrincipal {
    
    private UITelaPrincipal uiTP;
    
    private int crediarioCod = -1;
    
    public ATelaPrincipal() {
        uiTP = new UITelaPrincipal();
        uiTP.setAction(this);
    }
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela() {
        uiTP.setVisible(true);
        
        return true;
    }
    
    
    /**
     * Toda vez que a tela deve ser escondida
     * @return 
     */
    public boolean fecharAplicação() {
        try {
            uiTP.setVisible(false);
            uiTP.dispose();
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean mostrarTela (PanelAction pa) {
        try {
            uiTP.setPanel(pa.getPanel());
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public boolean mostrarDialog (DialogAction da) {
        try {
            uiTP.setDialog(da.getDialog());
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public void setInfo(String s) {
        uiTP.setInfo(s);
    }
    
    public void addInfo(String s) {
        uiTP.setInfo(uiTP.getInfo() + " " + s);
    }
    
    public Frame getMainFrame() {
        return uiTP.getMainFrame();
    }
    
    
    //Posicionamento
    public int getUIWidth() {
        return uiTP.getUIWidth();
    }
    
    public int getUIHeigth() {
        return uiTP.getUIHeigth();
    }
    
    public int getMenuBarHeight() {
        return uiTP.getMenuBarHeight();
    }
    
    public int getStatusBarHeight() {
        return uiTP.getStatusBarHeight();
    }
    
    //Menu
    
    public void menuLeituraX() {
        Controller.setInfoTP("Emitindo Leitura X");
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (!Control.leituraX()) {
                    Controller.messageBox("Erro ao emitir Leitura X!");
                }
                else {
                    Controller.setInfoTP("Comando Enviado!");
                }
            }
        }).start();
    }
    
    public void menuReducaoZ() {
        Controller.setInfoTP("Emitindo Redução Z");
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (!Control.reducaoZ()) {
                    Controller.messageBox("Erro ao emitir Redução Z!");
                }
                else {
                    Controller.setInfoTP("Comando Enviado!");
                }
            }
        }).start();
    }
    
    public void menuIdentificacaoPAFECF() {
        Controller.setInfoTP("Emitindo Identificação do PAF ECF");
        
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (!Control.identificacaoPAFECF()) {
                    Controller.messageBox("Erro ao emitir Identificação do PAF ECF!");
                }
                else {
                    Controller.setInfoTP("Comando Enviado!");
                }
            }
        }).start();
    }
    
    public boolean checaCrediario(int codigo) {
        boolean ret = false;
        try {
            ret = DBControl.vendaHasParcelas(codigo);
        }
        catch (Exception e) {
            return false;
        }
        crediarioCod = codigo;
        return ret;
    }
    
    public int getCrediarioCodigo() {
        return crediarioCod;
    }
    
    public void checarECF() {
        if (Control.verificaImpressoraLigada()) {
            Controller.messageBox("ECF está conectado!");
        }
        else {
            Controller.messageBox("ECF não está conectado!");
        }
    }
    
    public void funcaoNaoSuportada() {
        Controller.messageBox("Função não suportada pelo modelo de ECF utilizado");
    }
    
    public void mensagemTITP() {
        Controller.messageBox("Este PAFECF não executa funções de baixa de estoque "
                + "com base em índices técnicos de produção, não podendo ser utilizando "
                + "por estabelecimento que necessite deste recurso");
    }
}
