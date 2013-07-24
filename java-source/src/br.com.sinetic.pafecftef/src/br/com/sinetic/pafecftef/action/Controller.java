/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.entities.EcfComprador;
import br.com.sinetic.pafecftef.remoteentities.PreVenda;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrador
 */
public class Controller {
    
    public static final int ESTADO_LOAD = -2;
    public static final int ESTADO_ANTERIOR = -1;
    public static final int ESTADO_LVENDAS = 0;
    public static final int ESTADO_FPAGAMENTO = 1;
    public static final int ESTADO_LMFC = 4;
    public static final int ESTADO_LMFS = 5;
    public static final int ESTADO_ARQMFD = 6;
    public static final int ESTADO_RESTAURAVENDA = 8;
    public static final int ESTADO_ABRECAIXA = 9;
    public static final int ESTADO_SUPRIMENTO = 10;
    public static final int ESTADO_SANGRIA = 11;
    public static final int ESTADO_FECHACAIXA = 12;
    public static final int ESTADO_CONFIGURACOES = 13;
    public static final int ESTADO_CONFIRMA_PREVENDA = 14;
    public static final int ESTADO_ESPELHOMFD = 15;
    public static final int ESTADO_ARQESTOQUE = 16;
    public static final int ESTADO_MEIOS_PAGAMENTO = 17;
    public static final int ESTADO_CREDIARIO = 18;
    public static final int ESTADO_ALIQUOTAS = 19;
    public static final int ESTADO_PRODUTO_SERVICO = 20;
    public static final int ESTADO_CAD_PRODUTO_SERVICO = 21;
    
    public static final int SEM_ERRO = 0;
    public static final int ERRO_ESTADO_N_EXISTE = 1;
    public static final int ERRO_NAO_PERMITIDO = 2;
    public static final int ERRO_ESTADO_JA_ATUAL = 3;
    public static final int ERRO_TRANSICAO_N_EXISTE = 4;
    
    private static int estado;
    private static int estadoAnterior;
    
    
    //Controladores das Interfaces Gráficas
    private static ATelaPrincipal aTP;
    private static AListaVendas aLV;
    private static AFormasDePagamento aFP;
    private static ACrediario aC;
    private static ALMFC aLMFC;
    private static ALMFS aLMFS;
    private static AArqMFD aArqMFD;
    private static AAbreCaixa aAC;
    private static AFechaCaixa aFC;
    private static AConfiguracoes aPS;
    private static AConfirmarPreVenda aCPV;
    private static AEspelhoMFD aEMFD;
    private static AArqEstoque aArqE;
    private static AMeiosDePagamento aMdP;
    private static AAliquotas aA;
    private static AProdutosServicos aPROD;
    private static ACadProdutosServicos aCadProd;
    
    public static void iniciarController() {
        
        JDialog.setDefaultLookAndFeelDecorated(true);
        
        aTP = new ATelaPrincipal();
        
        //@todo [workaround]
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
        
        if (aTP.carregarTela()) {
            
        }
        
        aLV = new AListaVendas();
        aFP = new AFormasDePagamento();
        aCPV = new AConfirmarPreVenda();
        aC = new ACrediario();
        
        aAC = new AAbreCaixa(aTP.getMainFrame());
        aFC = new AFechaCaixa(aTP.getMainFrame());
        aPS = new AConfiguracoes(aTP.getMainFrame());
        aEMFD = new AEspelhoMFD(aTP.getMainFrame());
        aLMFC = new ALMFC(aTP.getMainFrame());
        aLMFS = new ALMFS(aTP.getMainFrame());
        aArqMFD = new AArqMFD(aTP.getMainFrame());
        aArqE = new AArqEstoque(aTP.getMainFrame());
        aMdP = new AMeiosDePagamento(aTP.getMainFrame());
        aA = new AAliquotas(aTP.getMainFrame());
        aPROD = new AProdutosServicos(aTP.getMainFrame());
        aCadProd = new ACadProdutosServicos(aTP.getMainFrame());
        //Estado Inicial
        estado = ESTADO_LOAD;
        
        mudarEstado(ESTADO_LVENDAS);
    }
    
    /**
     * Requisita uma mudança de estado ao controller.
     * Ou seja, por esta função, um componente visual, pode
     * enviar um sinal para a troca de tela.
     * 
     * @param novoEstado
     * @return 
     */
    public static int mudarEstado (int novoEstado) {
        
        if (novoEstado == estado) {
            return ERRO_ESTADO_JA_ATUAL;
        }
        
       
        switch (novoEstado) {
            
            case ESTADO_LVENDAS:
                //Estado veio das Formas de Pagamento (Ou cancelou o cupom, ou vendeu)
                if (estado == ESTADO_FPAGAMENTO) {
                    //Descarrega tela de pagamentos
                    aFP.descarregarTela();
                    
                    //Mostra tela de vendas
                    if (aLV.carregarTela()) {
                        setarEstado(ESTADO_LVENDAS);
                        aTP.mostrarTela(aLV);
                    }
                }
                //Veio da tela de pré-venda: Usuário desistiu da pré-venda.
                else if (estado == ESTADO_CONFIRMA_PREVENDA) {
                    aCPV.descarregarTela();
                    
                    //Mostra tela de vendas
                    if (aLV.carregarTela()) {
                        setarEstado(ESTADO_LVENDAS);
                        aTP.mostrarTela(aLV);
                    }
                }
                else if (estado == ESTADO_CREDIARIO) {
                    aC.descarregarTela();
                    
                    //Mostra tela de vendas
                    if (aLV.carregarTela()) {
                        setarEstado(ESTADO_LVENDAS);
                        aTP.mostrarTela(aLV);
                    }
                }
                else if (estado == ESTADO_LOAD) {
                    //Mostra tela de vendas
                    if (aLV.carregarTela()) {
                        setarEstado(ESTADO_LVENDAS);
                        aTP.mostrarTela(aLV);
                    }
                }
                else if (estado == ESTADO_ABRECAIXA ||
                         estado == ESTADO_SUPRIMENTO) {
                    aAC.fecharTela();
                    
                    //Mostra tela de vendas
                    if (aLV.carregarTela()) {
                        setarEstado(ESTADO_LVENDAS);
                        aTP.mostrarTela(aLV);
                    }
                }
                else if (estado == ESTADO_FECHACAIXA ||
                         estado == ESTADO_SANGRIA) {
                    aFC.fecharTela();
                    
                    //Mostra tela de vendas
                    if (aLV.carregarTela()) {
                        setarEstado(ESTADO_LVENDAS);
                        aTP.mostrarTela(aLV);
                    }
                }
                else if (estado == ESTADO_ESPELHOMFD) {
                    aEMFD.fecharTela();
                    
                    //Mostra tela de vendas
                    if (aLV.carregarTela()) {
                        setarEstado(ESTADO_LVENDAS);
                        aTP.mostrarTela(aLV);
                    }
                }
                else if (estado == ESTADO_LMFC) {
                    aLMFC.fecharTela();
                    
                    //Mostra tela de vendas
                    if (aLV.carregarTela()) {
                        setarEstado(ESTADO_LVENDAS);
                        aTP.mostrarTela(aLV);
                    }
                }
                else if (estado == ESTADO_LMFS) {
                    aLMFS.fecharTela();
                    
                    //Mostra tela de vendas
                    if (aLV.carregarTela()) {
                        setarEstado(ESTADO_LVENDAS);
                        aTP.mostrarTela(aLV);
                    }
                }
                else if (estado == ESTADO_ARQMFD) {
                    aArqMFD.fecharTela();
                    
                    //Mostra tela de vendas
                    if (aLV.carregarTela()) {
                        setarEstado(ESTADO_LVENDAS);
                        aTP.mostrarTela(aLV);
                    }
                }
                else if (estado == ESTADO_CONFIGURACOES) {
                    aPS.fecharTela();
                    
                    //Mostra tela de vendas
                    if (aLV.carregarTela()) {
                        setarEstado(ESTADO_LVENDAS);
                        aTP.mostrarTela(aLV);
                    }
                }
                else if (estado == ESTADO_ANTERIOR) {
                    if (aLV.carregarTela()) {
                        setarEstado(ESTADO_LVENDAS);
                        aTP.mostrarTela(aLV);
                    }
                }
                else {
                    return ERRO_TRANSICAO_N_EXISTE;
                }
                break;
                
            case ESTADO_CONFIRMA_PREVENDA:
                if (Control.isBloqueado()) {
                    Controller.messageBox("Ação não pode ser executada pois o PAF está bloqueado!");
                    return ERRO_NAO_PERMITIDO;
                }
                else if (Control.getStatusDiaFiscal() != Control.DIA_FISCAL_ABERTO) {
                    Controller.messageBox("Ação não pode ser executada pois o caixa não está aberto!");
                    return ERRO_NAO_PERMITIDO;
                }
                
                //Estado veio de Lista Vendas (Caminho normal)
                if (estado == ESTADO_LVENDAS) {
                    //Recebe codigo selecionado
                    int cod = aLV.getCodVenda();
                    
                    //Se nenhum item foi selecionado
                    if (cod == -1) {
                        //Dar código de erro
                        //@todo
                    }
                    else {
                        //Descarrega a tela de Lista de Vendas
                        aLV.descarregarTela();
                        
                        //Mostra a tela de Confirmar a Pré-Venda
                        if (aCPV.carregarTela(cod)) {
                            setarEstado(ESTADO_CONFIRMA_PREVENDA);
                            aTP.mostrarTela(aCPV);
                        }
                    }
                }
                else if (estado == ESTADO_ANTERIOR) {
                    setarEstado(ESTADO_CONFIRMA_PREVENDA);
                }
                else {
                    return ERRO_TRANSICAO_N_EXISTE;
                }
                break;
                
                
            case ESTADO_FPAGAMENTO:
                if (Control.isBloqueado()) {
                    Controller.messageBox("Ação não pode ser executada pois o PAF está bloqueado!");
                    return ERRO_NAO_PERMITIDO;
                }
                else if (Control.getStatusDiaFiscal() != Control.DIA_FISCAL_ABERTO) {
                    Controller.messageBox("Ação não pode ser executada pois o caixa não está aberto!");
                    return ERRO_NAO_PERMITIDO;
                }
                
                //Estado veio de Confirmação de Pré-Venda
                if (estado == ESTADO_CONFIRMA_PREVENDA) {
                    PreVenda pv = aCPV.getPreVenda();
                    EcfComprador c = aCPV.getComprador();
                    
                    //Se nenhum item foi selecionado
                    if (pv == null) {
                        //Dar código de erro
                        //@todo
                    }
                    else {
                        //Descarrega a tela de Confirmação de Pré-Venda
                        aCPV.descarregarTela();
                        
                        //Mostra a tela de Confirmar a Pré-Venda
                        if (aFP.carregarTela(pv, c)) {
                            setarEstado(ESTADO_FPAGAMENTO);
                            aTP.mostrarTela(aFP);
                        }
                    }
                }
                else if (estado == ESTADO_ANTERIOR) {
                    setarEstado(ESTADO_FPAGAMENTO);
                }
                else {
                    return ERRO_TRANSICAO_N_EXISTE;
                }
                break;
                
            case ESTADO_CREDIARIO:
                if (Control.isBloqueado()) {
                    Controller.messageBox("Ação não pode ser executada pois o PAF está bloqueado!");
                    return ERRO_NAO_PERMITIDO;
                }
                else if (Control.getStatusDiaFiscal() != Control.DIA_FISCAL_ABERTO) {
                    Controller.messageBox("Ação não pode ser executada pois o caixa não está aberto!");
                    return ERRO_NAO_PERMITIDO;
                }
                
                //Estado veio de Confirmação de Pré-Venda
                if (estado == ESTADO_LVENDAS) {
                    PreVenda pv = DBControl.getPreVenda(aTP.getCrediarioCodigo());
                   
                    //Se nenhum item foi selecionado
                    if (pv == null) {
                        Controller.messageBox("Erro ao capturar Pré-Venda do Banco de Dados Central.");
                    }
                    else {
                        //Descarrega a tela de Lista de Vendas
                        aLV.descarregarTela();
                        
                        //Mostra a tela de Confirmar a Pré-Venda
                        if (aC.carregarTela(pv)) {
                            setarEstado(ESTADO_CREDIARIO);
                            aTP.mostrarTela(aC);
                        }
                    }
                }
                else if (estado == ESTADO_ANTERIOR) {
                    setarEstado(ESTADO_CREDIARIO);
                }
                else {
                    return ERRO_TRANSICAO_N_EXISTE;
                }
                break;
                
            case ESTADO_RESTAURAVENDA:
                if (Control.isBloqueado()) {
                    Controller.messageBox("Ação não pode ser executada pois o PAF está bloqueado!");
                    return ERRO_NAO_PERMITIDO;
                }
                else if (Control.getStatusDiaFiscal() != Control.DIA_FISCAL_ABERTO) {
                    Controller.messageBox("Ação não pode ser executada pois o caixa não está aberto!");
                    return ERRO_NAO_PERMITIDO;
                }
                
                //Estado veio de Lista Vendas
                if (estado == ESTADO_LVENDAS) {
                    aLV.descarregarTela();
                        
                    long cod = DBControl.isVendaPendente();
                    
                    //Mostra a tela de Formas de Pagamento
                    if (aFP.restauraVendaTela(cod)) {
                        setarEstado(ESTADO_RESTAURAVENDA);
                        aTP.mostrarTela(aFP);
                    }
                }
                else if (estado == ESTADO_LOAD) {
                    int cod = -1;
                    //@todo
                    //Deve pegar o codigo do banco
                    //Da tabela pendente
                    
                    //Mostra a tela de Formas de Pagamento
                    if (aFP.restauraVendaTela(cod)) {
                        setarEstado(ESTADO_RESTAURAVENDA);
                        aTP.mostrarTela(aFP);
                    }
                }
                else {
                    return ERRO_TRANSICAO_N_EXISTE;
                }
                break;
                
            case ESTADO_ABRECAIXA:
                if (Control.isBloqueado()) {
                    Controller.messageBox("Ação não pode ser executada pois o PAF está bloqueado!");
                    return ERRO_NAO_PERMITIDO;
                }
                else if (Control.getStatusDiaFiscal() == Control.DIA_FISCAL_ABERTO) {
                    Controller.messageBox("Ação não pode ser executada pois o caixa já está aberto!");
                    return ERRO_NAO_PERMITIDO;
                }
                else if (Control.getStatusDiaFiscal() == Control.DIA_FISCAL_FECHADO) {
                    Controller.messageBox("Ação não pode ser executada pois o caixa já está fechado!");
                    return ERRO_NAO_PERMITIDO;
                }
                
                aAC.carregarTela();
                break;
                
            case ESTADO_SUPRIMENTO:
                if (Control.isBloqueado()) {
                    Controller.messageBox("Ação não pode ser executada pois o PAF está bloqueado!");
                    return ESTADO_ANTERIOR;
                }
                else if (Control.getStatusDiaFiscal() != Control.DIA_FISCAL_ABERTO) {
                    Controller.messageBox("Ação não pode ser executada pois o caixa não está aberto!");
                    return ERRO_NAO_PERMITIDO;
                }
                
                aAC.suprimentoSimples();
                aAC.carregarTela();
                break;
                
            case ESTADO_FECHACAIXA:
                if (Control.isBloqueado()) {
                    Controller.messageBox("Ação não pode ser executada pois o PAF está bloqueado!");
                    return ESTADO_ANTERIOR;
                }
                else if (Control.getStatusDiaFiscal() != Control.DIA_FISCAL_ABERTO) {
                    Controller.messageBox("Ação não pode ser executada pois o caixa não está aberto!");
                    return ERRO_NAO_PERMITIDO;
                }
                
                aFC.carregarTela();
                break;
                
            case ESTADO_SANGRIA:
                if (Control.isBloqueado()) {
                    Controller.messageBox("Ação não pode ser executada pois o PAF está bloqueado!");
                    return ESTADO_ANTERIOR;
                }
                else if (Control.getStatusDiaFiscal() != Control.DIA_FISCAL_ABERTO) {
                    Controller.messageBox("Ação não pode ser executada pois o caixa não está aberto!");
                    return ERRO_NAO_PERMITIDO;
                }
                
                aFC.sangriaSimples();
                aFC.carregarTela();

                break;
                
            case ESTADO_CONFIGURACOES:
                aPS.carregarTela();
                break;
                
            case ESTADO_ARQESTOQUE:
                aArqE.carregarTela();
                break;
                
            case ESTADO_ALIQUOTAS:
                aA.carregarTela();
                break;
                
            case ESTADO_PRODUTO_SERVICO:
                aPROD.carregarTela();
                break;
                
            case ESTADO_CAD_PRODUTO_SERVICO:
                aCadProd.carregarTela();
                break;
                
            case ESTADO_ESPELHOMFD:
                if (estado == ESTADO_LVENDAS) {
                    setarEstado(ESTADO_ESPELHOMFD);
                    aEMFD.carregarTela();
                }
                else {
                    return ERRO_TRANSICAO_N_EXISTE;
                }
                break;
                
                
            case ESTADO_LMFC:
                //Estado veio de Lista Vendas
                if (estado == ESTADO_LVENDAS) {
                    setarEstado(ESTADO_LMFC);
                    aLMFC.carregarTela();
                }
                else {
                    return ERRO_TRANSICAO_N_EXISTE;
                }
                break;
                
            case ESTADO_LMFS:
                //Estado veio de Lista Vendas
                if (estado == ESTADO_LVENDAS) {
                    setarEstado(ESTADO_LMFS);
                    aLMFS.carregarTela();
                }
                else {
                    return ERRO_TRANSICAO_N_EXISTE;
                }
                break;
                
            case ESTADO_ARQMFD:
                //Estado veio de Lista Vendas
                if (estado == ESTADO_LVENDAS) {
                    setarEstado(ESTADO_ARQMFD);
                    aArqMFD.carregarTela();
                }
                else {
                    return ERRO_TRANSICAO_N_EXISTE;
                }
                break;
                
            case ESTADO_MEIOS_PAGAMENTO:
                aMdP.carregarTela();
                break;
                
            case ESTADO_ANTERIOR:
                if (estado == ESTADO_ABRECAIXA ||
                    estado == ESTADO_SUPRIMENTO) {
                    //@todo tratar abrecaixa
                    aAC.fecharTela();
                    
                    mudarEstado(estadoAnterior);
                }
                else if (estado == ESTADO_FECHACAIXA ||
                    estado == ESTADO_SANGRIA) {
                    //@todo tratar abrecaixa
                    aFC.fecharTela();
                    
                    mudarEstado(estadoAnterior);
                }
                else if (estado == ESTADO_CONFIGURACOES) {
                    aPS.fecharTela();
                    
                    mudarEstado(estadoAnterior);
                }
                else if (estado == ESTADO_ESPELHOMFD) {
                    aEMFD.fecharTela();
                    
                    mudarEstado(estadoAnterior);
                }
                else if (estado == ESTADO_LMFC) {
                    aLMFC.fecharTela();
                    
                    mudarEstado(estadoAnterior);
                }
                else if (estado == ESTADO_LMFS) {
                    aLMFS.fecharTela();
                    
                    mudarEstado(estadoAnterior);
                }
                else if (estado == ESTADO_ARQMFD) {
                    aArqMFD.fecharTela();
                    
                    mudarEstado(estadoAnterior);
                }
                else {
                    return ERRO_TRANSICAO_N_EXISTE;
                }
                break;
                
            default:
                return ERRO_ESTADO_N_EXISTE;
                
        }
        
        return SEM_ERRO;
    }
    
    private static void setarEstado(int novoEstado) {
        estadoAnterior = estado;
        estado = novoEstado;
    }
    
    public static boolean isAbleToQuit() {
        if (estado == ESTADO_FPAGAMENTO ||
            estado == ESTADO_RESTAURAVENDA) {
                return false;
        }
        
        return true;
    }
    
  
    
    ////// Controle de Mensagens
    
    public static void setInfoTP(String s) {
        aTP.setInfo(s);
    }
    
    
    public static void messageBox(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
    
    public static boolean confirmBox(String msg) {
        if (JOptionPane.showConfirmDialog(
            null,
            msg,
            "Pergunta do Sistema", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            return true;
        }
        else {
            return false;
        
        }
    }
    
    public static String inputBox(String msg) {
        return JOptionPane.showInputDialog(msg);
    }
    
    //Posicionamento
    
    public static int getPosX (int boxWidth) {
        int w = aTP.getUIWidth();
        int r = (w/2)-(boxWidth/2);
        return r;
    }
    
    public static int getPosY (int boxHeight) {
        int h = aTP.getUIHeigth();
        int b = aTP.getStatusBarHeight();
        int i = aTP.getStatusBarHeight();
        int r = (h/2)+b-i;
        return r;
    }
}
