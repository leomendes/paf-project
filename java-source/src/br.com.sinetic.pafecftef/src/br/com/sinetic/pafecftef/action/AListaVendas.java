/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.ItensVendidos;
import br.com.sinetic.pafecftef.control.ListaDeVendas;
import br.com.sinetic.pafecftef.entities.Vendas;
import br.com.sinetic.pafecftef.printer.Printer;
import br.com.sinetic.pafecftef.ui.UIListaVendas;
import javax.swing.JPanel;
import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.remoteentities.PreVenda;

/**
 *
 * @author Administrador
 */
public class AListaVendas implements Runnable,PanelAction {
    
    private Thread thread;
    private boolean isRunning = false;
    private boolean paused = false;
    private UIListaVendas uiLV;
    
    //Teste
    private boolean criou = false;
    
    
    public AListaVendas() {
        
        uiLV = new UIListaVendas();
        uiLV.setAction(this);
        
        criaThread();
    }
    
    private void criaThread() {
        try {
            thread = new Thread(this);
            thread.start();
        }
        catch (Exception e) {
            System.out.println("Erro ao criar thread: AListaVendas");
        }
    }
    
    @Override
    public void run() {
        isRunning = true;
        paused = false;
        
        //@todo melhorar esta thread
        while (isRunning) {
            
            if (!paused) {
                atualizarVendas();
            }
            try {Thread.sleep(10000);} 
            catch (Exception e){}
        }
        
    }
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela() {
        
        //@todo
        //Aqui deve verificar se há algo no banco de venda pendente
        //Se houver, já deve chamar a função de restaurar
        //venda
        //Nem começa a carregar a venda
        
        
        //Reativa Thread
        if (!isRunning) {
            criaThread();
        }
        else if (paused) {
            //tira o pause da atualização
            paused = false;
        }
        
        try {
            //habilita os botões
            uiLV.habilitaComponentes();
            uiLV.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        
        return true;
    }
    
    
    /**
     * Toda vez que a tela deve ser escondida
     * @return 
     */
    public boolean descarregarTela() {
        
        //Pausa a atualização da lista
        paused = true;
        
        try {
            uiLV.setVisible(false);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    /**
     * Pega o codigo de venda do item selecionado
     * @return 
     * ERRO -1: Nenhum item selecionado
     * ERRO -2: Código inválido
     */
    public int getCodVenda() {
        return uiLV.getSelectedItem();
    }
    
    //Atualiza a listagem de vendas da lista
    private boolean atualizarVendas () {
        
        Control.atualizarVendas();
        uiLV.setList(Control.getListaVendas());
        
        return true;
    }
    
    
    public boolean cancelaPreVenda(int cod) {
        PreVenda pv = DBControl.getPreVenda(cod);
        if (Control.cancelaPreVenda(pv)) {
            atualizarVendas();
            return true;
        }
        else {
            return false;
        }
        
    }
    
    @Override
    public JPanel getPanel() {
        return uiLV;
    }
}
