/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.ItensVendidos;
import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.control.Registros;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.entities.EcfComprador;
import br.com.sinetic.pafecftef.entities.VendaPagamento;
import br.com.sinetic.pafecftef.printer.Printer;
import br.com.sinetic.pafecftef.remoteentities.CrediarioFormaPagamento;
import br.com.sinetic.pafecftef.remoteentities.CrediarioFormaPagamentoDetalhado;
import br.com.sinetic.pafecftef.remoteentities.CrediarioTiposStatusPrestacao;
import br.com.sinetic.pafecftef.remoteentities.CrediarioVendaPagamento;
import br.com.sinetic.pafecftef.remoteentities.CrediarioVendaParcelamento;
import br.com.sinetic.pafecftef.remoteentities.PreVenda;
import br.com.sinetic.pafecftef.remoteentities.Usuario;
import br.com.sinetic.pafecftef.ui.UIFormasDePagamento;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.JPanel;
import java.util.Set;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.GregorianCalendar;
/**
 *
 * @author Administrador
 */
public class AFormasDePagamento implements PanelAction {
    
    private UIFormasDePagamento uiFP;
    
    private PreVenda actualPV;
    
    private EcfComprador comprador = null;
    
    private List<CrediarioFormaPagamento> lCFP;
    private List<CrediarioFormaPagamentoDetalhado> lCFPD;
    
    //Controle dos pagamentos
    private float totalPago;
    private float troco;
    
    public AFormasDePagamento() {
        uiFP = new UIFormasDePagamento();
        uiFP.setAction(this);
    }
    
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * 
     * Se cupom for "ao consumidor", EcfComprador deve ser null
     * 
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela(PreVenda pv, EcfComprador c) {
        if (pv == null) {
            return false;
        }
        
        //Seta a pré-venda atual
        actualPV = pv;
        
        //identificação do comprador
        comprador = c;
        
        //carrega os meios de pagamento do banco
        lCFP = DBControl.getPagamentoMeios();
        lCFPD = null;
        
        if (lCFP == null) {
            Controller.messageBox("Erro ao carregar os meios de pagamento!");
            return false;
        }
        
        uiFP.setPagamentoMeios(lCFP);
        
        try {
            uiFP.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        
        iniciaVenda();
        return true;
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
        uiFP.setPagamentoTipos(sCFPD);
    }
    
    public void tipoPagamentoSelected(int index) {
        CrediarioFormaPagamentoDetalhado cfpd = lCFPD.get(index);
        
        //Verifica se é crediário
        Short sMaxParc = cfpd.getNumeroMaximoParcelas();
        if (sMaxParc != null && sMaxParc > 1) {
            uiFP.setParcelas(sMaxParc);
        }
        else {
            uiFP.removeParcelas();
        }
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
    
    public float getTotalPago() {
        return totalPago;
    }
    
    public float getTroco() {
        return troco;
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
        float t = (totalPago+valor)-actualPV.getValorFinal().floatValue();
        if (t < 0) {
            t = 0;
        }
        if (armazenar) {
            troco = t;
        }
        return t;
    }
    
    
    public boolean checaValorTotalParaFecharCF() {
        if (totalPago < actualPV.getValorFinal().floatValue()) {
            return false;
        }
        else {
            return true;
        }
    }
    
    public boolean checaNovoPagamento(float value) {
        if (totalPago > actualPV.getValorFinal().floatValue()) {
            return false;
        }
        else {
            return true;
        }
    }
    
    public boolean efetuarPagamento(float valor, int indexPagMeio, int indexPagTipo, int numParcelas) {
        String sPagMeio = getPagMeio(indexPagMeio);
        
        
        //Pega os objetos de pagamento
        CrediarioFormaPagamento cfp = getPagMeioDB(indexPagMeio);
        CrediarioFormaPagamentoDetalhado cfpd = getPagTipoDB(indexPagTipo);
        

        //Verifica o tipo de pagamento.
        //Se o pagamento é por crediário
        if (cfpd.getNumeroMaximoParcelas() != null && cfpd.getNumeroMaximoParcelas() > 1) {
            //Cria as formas de pagamento para banco remoto
            if (paga(sPagMeio, valor, cfpd)) {
                return vendeCrediario(cfpd, valor, numParcelas);
            }
            else {
                return false;
            }
        }
        else {
            if (paga(sPagMeio, valor, cfpd)) {
                return vendePagamento(cfpd, valor);
            }
            else {
                return false;
            }
        }

    }
    
    private boolean paga(String sPagMeio, float valor, CrediarioFormaPagamentoDetalhado cfpd) {
        if (Control.efetuaPagamento(sPagMeio, valor, cfpd)) {
            calculaTroco(valor, true);
            totalPago += valor;
            return true;
        }
        else {
            return false;
        }
    }
    
    private boolean vendeCrediario(CrediarioFormaPagamentoDetalhado cfpd, float valor, int numParc) {
        //cria o crediario
        Calendar c = new GregorianCalendar();
        
        //Calcula as parcelas
        DecimalFormat df = new DecimalFormat("###.##");
        df.setRoundingMode(RoundingMode.DOWN);
        
        float valorPorParcela = valor/numParc;
        float[] parcelas = new float[numParc];
        float round = Float.valueOf(df.format(valorPorParcela));
        float resto = valor - round*numParc;
        for (int i = 0; i < parcelas.length; i++) {
            parcelas[i] = round;
            if (resto > 0.0001f) {
                parcelas[i] += 0.01f;
                resto -= 0.01f;
            }
        }
        
        
        for (int i = 0; i < numParc; i++) {
            
            //Cria parcela no banco
            CrediarioVendaParcelamento cvp = new CrediarioVendaParcelamento();
            cvp.setCrediarioFormaPagamentoDetalhado(cfpd);
            cvp.setPreVenda(actualPV);
            cvp.setJurosAoDia(new BigDecimal(0));
            cvp.setDescontoAoDia(new BigDecimal(0));
            cvp.setParcelaValor(new BigDecimal(parcelas[i]));
            c.add(Calendar.MONTH, i+1);
            cvp.setParcelaData(c.getTime());
            CrediarioTiposStatusPrestacao ctsp = DBControl.getTipoPrestacao(1);
            cvp.setCrediarioTiposStatusPrestacao(ctsp);

            try {
                DBControl.gravaParcelaCrediario(cvp);
            }
            catch (Exception e) {
                return false;
            }
        }

        //Atualiza os meios de pagamento por dia.
        //@todo arrumar: 
        /*if (!DBControl.atualizaPagamentoDia(cfp, valor, Control.TIPO_DOC_CUPOM_FISCAL)) {
            return false;
        }*/
        
        return true;
    }
    
    private boolean vendePagamento(CrediarioFormaPagamentoDetalhado cfpd, float valor) {
            
        CrediarioVendaPagamento cvp = new CrediarioVendaPagamento();
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
    

    private void iniciaVenda() {
        
        //@todo implementar um "tentar novamente"
        //Inicia a abertura do cupom fiscal na impressora
        if (iniciaVendaECF()) {
            //Carrega Cabeçalho e Itens na UI
            uiFP.addListaVendaToCF(actualPV);
        }
        
    }
    
    
    private boolean iniciaVendaECF() {
        //Abre Cupom
        if (actualPV == null) {
            return false;
        }
        

        Control.setaVendaPendente(actualPV.getCodigo(), Control.PV_STATUS_CF);
        if (!Control.iniciaVenda(actualPV, comprador)) {
            return false;
        }
        
        if (!Control.iniciaPagamento(actualPV.getDescontoValor().floatValue(), String.valueOf(actualPV.getPafEcfDescontoTipo().getSigla()))) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Realiza o pagamento dos itens comprados
     * 
     */
    public boolean fechaCupom() {

        ////FINALIZA PAGAMENTO //////

        //Fecha Cupom
        Control.fechaPagamentoCupom(actualPV);
        
        //Atualiza valor do Grande Total no AAC
        Control.atualizaGrandeTotal();
        
        
        Control.setaVendaPendente(actualPV.getCodigo(), Control.PV_STATUS_FINAL_CF);
        
        //Seta a data da finalização
        Date d = Control.getDataHoraUltimoCupom();
        //Finaliza a pré-venda
        actualPV.setFinalizadoEm(d);
        actualPV.setEmProgresso(false);
        if (!DBControl.atualizaPreVenda(actualPV)) {
            Controller.messageBox("Erro ao finalizar Pré-Venda no Banco de Dados Remoto");
            return false;
        }
        
        actualPV.setFinalizada(true);
        if (!DBControl.atualizaPreVenda(actualPV)) {
            Controller.messageBox("Erro ao finalizar Pré-Venda no Banco de Dados Remoto");
            return false;
        }
       
        Control.setaVendaPendente(actualPV.getCodigo(), Control.PV_STATUS_FINALIZAR);
        return true;
    }
    
    
    @Override
    public JPanel getPanel() {
        return uiFP;
    }
    
}
