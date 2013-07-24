/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.control.Registros;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.entities.EcfComprador;
import br.com.sinetic.pafecftef.entities.VendaPagamento;
import br.com.sinetic.pafecftef.remoteentities.CrediarioFormaPagamento;
import br.com.sinetic.pafecftef.remoteentities.CrediarioFormaPagamentoDetalhado;
import br.com.sinetic.pafecftef.remoteentities.CrediarioVendaPagamento;
import br.com.sinetic.pafecftef.remoteentities.CrediarioVendaParcelamento;
import br.com.sinetic.pafecftef.remoteentities.PreVenda;
import br.com.sinetic.pafecftef.ui.UICrediario;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.swing.JPanel;

/**
 *
 * @author Windows8
 */
public class ACrediario implements PanelAction {
    
    private UICrediario uiC;
    
    private List<CrediarioFormaPagamento> lCFP;
    private List<CrediarioFormaPagamentoDetalhado> lCFPD;
    
    private PreVenda actualPV;
    private CrediarioVendaParcelamento cvp;
    
    //Controle dos pagamentos
    private float totalPago;
    private float troco;
    
    //Controle das parcelas
    private float parcelaTotalPago;
    private int parcelaNumParPagas;
    private int parcelaNumTotalParcelas;
    
    public ACrediario() {
        uiC = new UICrediario();
        uiC.setAction(this);
    }
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * 
     * Se cupom for "ao consumidor", EcfComprador deve ser null
     * 
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela(PreVenda pv) {
        if (pv == null) {
            return false;
        }
        
        //Seta a pré-venda atual
        actualPV = pv;
        
        //carrega os meios de pagamento do banco
        lCFP = DBControl.getPagamentoMeios();
        lCFPD = null;
        
        if (lCFP == null) {
            Controller.messageBox("Erro ao carregar os meios de pagamento!");
            return false;
        }
        
        uiC.setPagamentoMeios(lCFP);
        
        
        //Carrega as parcelas pendentes para o cliente
        List<CrediarioVendaParcelamento> lCVP = DBControl.getParcelasCrediario(actualPV);
        
        parcelaTotalPago = 0;
        parcelaNumParPagas = 0;
        parcelaNumTotalParcelas = 0;
        this.cvp = null;
        
        //Atualiza informações
        for (CrediarioVendaParcelamento cvp: lCVP) {
            parcelaNumTotalParcelas++;
            if (cvp.getRecebimentoValor() != null) {
                parcelaTotalPago += cvp.getRecebimentoValor().floatValue();
                parcelaNumParPagas++;
            }
            else {
                if (this.cvp == null) {
                    this.cvp = cvp;
                }
            }
        }
        
        //Coloca as informações na UI
        uiC.setInfoParcela(cvp);
        
        
        try {
            uiC.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        
        if (!iniciaPagamento()) {
            return false;
        }
        return true;
    }
    
    
    private boolean iniciaPagamento() {
        //Abre Cupom
        if (actualPV == null) {
            return false;
        }
        
        if (!Control.abrePagamentoNaoFiscal(actualPV, cvp)) {
            return false;
        }
        
        if (!abreCupomNaoFiscal(cvp)) {
            Control.fechaPagamentoNaoFiscal();
            return false;
        }
        
        return true;
    }
    
    private boolean abreCupomNaoFiscal(CrediarioVendaParcelamento cvp) {
        List<String> linhas = new ArrayList<>();
        linhas.add("Comprovante de Pagamento");
        linhas.add("Venda nº: " + cvp.getPreVenda().getCodigo());
        linhas.add("Parcela: " + getNumParcelasAtual()+"/"+getNumParcelasTotal());
        linhas.add("Valor da Parcela: " + cvp.getParcelaValor());
        linhas.add("----------------------");
        for (String s : linhas) {
            if (!Control.usaPagamentoNaoFiscal(s)) {
                return false;
            }
        }
        return true;
    }
    
    private boolean imprimePagamentoNaoFiscal(String meio, float valor) {
        String linha = meio + ": R$ " + valor;
        if (!Control.usaPagamentoNaoFiscal(linha)) {
            return false;
        }
        return true;
    }
    
    private boolean fechaPagamentoNaoFiscal() {
        List<String> linhas = new ArrayList<>();
        linhas.add("----------------------");
        linhas.add("Total Pago: R$ "+ totalPago);
        linhas.add("Troco: R$ " + troco);
        for (String s : linhas) {
            if (!Control.usaPagamentoNaoFiscal(s)) {
                return false;
            }
        }
        
        if (!Control.fechaPagamentoNaoFiscal()) {
            return false;
        }
        return true;
    }
    
    public float getValorRestante() {
        if (actualPV == null) {
            return -1;
        }
        
        return actualPV.getValorFinal().floatValue()-parcelaTotalPago;
    }
    
    public int getNumParcelasTotal() {
        return parcelaNumTotalParcelas;
    }
    
    public int getNumParcelasAtual() {
        return parcelaNumParPagas+1;
    }
    
    public float getTotalPago() {
        return totalPago;
    }
    
    public float getTroco() {
        return troco;
    }
    
    public void meioPagamentoSelected(int index) {
        CrediarioFormaPagamento cfp = lCFP.get(index);
        
        //Verifica e seta os tipos de pagamento para esta forma
        Set<CrediarioFormaPagamentoDetalhado> sCFPD = cfp.getCrediarioFormaPagamentoDetalhados();
        
        //Copia para lista local
        lCFPD = new ArrayList<>();
        for (CrediarioFormaPagamentoDetalhado cfpd: sCFPD) {
            lCFPD.add(cfpd);
        }
        
        //Seta os tipos
        uiC.setPagamentoTipos(sCFPD);
    }
    
   
    public String getPagMeio(int index) {
        return lCFP.get(index).getNome().trim();
    }
    
    public CrediarioFormaPagamento getPagMeioDB(int index) {
        return lCFP.get(index);
    }
    
    public CrediarioFormaPagamentoDetalhado getPagTipoDB(int index) {
        return lCFPD.get(index);
    }
    
    public boolean estornaPagamento(int index) {
        return false;
    }
    
    public String getPagTipo(int iMeio, int iTipo) {
        //@todo Arumar
        Set<CrediarioFormaPagamentoDetalhado> sCFPD = lCFP.get(iMeio).getCrediarioFormaPagamentoDetalhados();
        int i = 0;
        for (CrediarioFormaPagamentoDetalhado cfpd: sCFPD) {
            if (i == iTipo) {
                return cfpd.getNome().trim();
            }
        }
        return null;
    }
    
   
    /**
     * Calcula o troco, utilizando os pagamentos já feitos.
     * 
     * @param valor Valor do pagamento atual
     * @param armazenar Se deve armazenar o valor do troco. (Usado após um pagamento real)
     * ou se não deve armazenar (Apenas atualizar um campo, para consulta)
     * @return 
     */
    private float calculaTroco(float valor, boolean armazenar) {
        float t = (totalPago+valor)-cvp.getParcelaValor().floatValue();
        if (t < 0) {
            t = 0;
        }
        if (armazenar) {
            troco = t;
        }
        return t;
    }
    
    
    public boolean checaValorTotalParaFecharCNF() {
        if (totalPago < cvp.getParcelaValor().floatValue()) {
            return false;
        }
        else {
            return true;
        }
    }
    
    public boolean checaNovoPagamento(float value) {
        if (totalPago > cvp.getParcelaValor().floatValue()) {
            return false;
        }
        else {
            return true;
        }
    }
    
    public boolean efetuarPagamento(float valor, int indexPagMeio, int indexPagTipo) {
        String sPagMeio = getPagMeio(indexPagMeio);
        
        
        //Pega os objetos de pagamento
        CrediarioFormaPagamentoDetalhado cfpd = getPagTipoDB(indexPagTipo);
        

        //Verifica o tipo de pagamento.
        //Se o pagamento é por crediário dá erro
        if (cfpd.getNumeroMaximoParcelas() != null && cfpd.getNumeroMaximoParcelas() > 1) {
            return false;
        }
        else {
            if (paga(sPagMeio, valor)) {
                return vendePagamento(cfpd, valor);
            }
            else {
                return false;
            }
        }

    }
    
    private boolean paga(String sPagMeio, float valor) {
        if (imprimePagamentoNaoFiscal(sPagMeio, valor)) {
            calculaTroco(valor, true);
            totalPago += valor;
            return true;
        }
        else {
            return false;
        }
    }
    
    
    
    private boolean vendePagamento(CrediarioFormaPagamentoDetalhado cfpd, float valor) {

        //@todo verificar se há a necessidade de gravar o pagamento do crediário.
        /*CrediarioVendaPagamento cvp = new CrediarioVendaPagamento();
        cvp.setCrediarioFormaPagamentoDetalhado(cfpd);
        cvp.setPreVenda(actualPV);
        cvp.setStatus(true);
        cvp.setValor(new BigDecimal(valor));

        try {
            DBControl.gravaPagamento(cvp);
        }
        catch (Exception e) {
            return false;
        }
        
        //Grava r07
        Registros.capturaR07(cfpd.getCodigo(), cvp, false);*/
        
        //Atualiza os meios de pagamento por dia.
        //@todo arrumar: 
        /*if (!DBControl.atualizaPagamentoDia(cfp, valor, Control.TIPO_DOC_CUPOM_FISCAL)) {
            return false;
        }*/
        return true;
    }
    
    /**
     * Utilizado para restaurar uma venda antiga
     * @param codigo
     * @return 
     */
    public boolean restauraVendaTela(long codigo) {
        return false;
    }
    
    public boolean descarregarTela() {

        return true;
    }
    

    
    /**
     * Realiza o pagamento dos itens comprados
     * 
     */
    public boolean fechaPagamento() {

        ////FINALIZA PAGAMENTO //////

                //Atualiza a parcela
        Date date = new Date();
        cvp.setRecebimentoData(date);
        cvp.setRecebimentoValor(new BigDecimal(totalPago));
        //@todo, colocar o status do pagamento, padrão é 1

        try {
            DBControl.atualizaParcelaCrediario(cvp);
        }
        catch (Exception e) {
            return false;
        }
        
        if (!fechaPagamentoNaoFiscal()) {
            return false;
        }
        
        //Grava r06
        //@todo Registros.capturaR06(Registros.R06_COMPROVANTE_NAO_FISCAL);
        
        return true;
    }
    
    
   
    @Override
    public JPanel getPanel() {
        return uiC;
    }
}
