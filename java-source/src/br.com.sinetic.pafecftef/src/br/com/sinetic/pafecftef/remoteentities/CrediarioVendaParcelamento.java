package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;
import java.util.Date;

/**
 * CrediarioVendaParcelamento generated by hbm2java
 */
public class CrediarioVendaParcelamento  implements java.io.Serializable {


     private int codigo;
     private CrediarioTiposStatusPrestacao crediarioTiposStatusPrestacao;
     private CrediarioFormaPagamentoDetalhado crediarioFormaPagamentoDetalhado;
     private PreVenda preVenda;
     private BigDecimal parcelaValor;
     private Date parcelaData;
     private BigDecimal recebimentoValor;
     private Date recebimentoData;
     private BigDecimal jurosAoDia;
     private BigDecimal descontoAoDia;

    public CrediarioVendaParcelamento() {
    }

	
    public CrediarioVendaParcelamento(int codigo, CrediarioTiposStatusPrestacao crediarioTiposStatusPrestacao, PreVenda preVenda, BigDecimal parcelaValor, Date parcelaData, BigDecimal jurosAoDia, BigDecimal descontoAoDia) {
        this.codigo = codigo;
        this.crediarioTiposStatusPrestacao = crediarioTiposStatusPrestacao;
        this.preVenda = preVenda;
        this.parcelaValor = parcelaValor;
        this.parcelaData = parcelaData;
        this.jurosAoDia = jurosAoDia;
        this.descontoAoDia = descontoAoDia;
    }
    public CrediarioVendaParcelamento(int codigo, CrediarioTiposStatusPrestacao crediarioTiposStatusPrestacao, CrediarioFormaPagamentoDetalhado crediarioFormaPagamentoDetalhado, PreVenda preVenda, BigDecimal parcelaValor, Date parcelaData, BigDecimal recebimentoValor, Date recebimentoData, BigDecimal jurosAoDia, BigDecimal descontoAoDia) {
       this.codigo = codigo;
       this.crediarioTiposStatusPrestacao = crediarioTiposStatusPrestacao;
       this.crediarioFormaPagamentoDetalhado = crediarioFormaPagamentoDetalhado;
       this.preVenda = preVenda;
       this.parcelaValor = parcelaValor;
       this.parcelaData = parcelaData;
       this.recebimentoValor = recebimentoValor;
       this.recebimentoData = recebimentoData;
       this.jurosAoDia = jurosAoDia;
       this.descontoAoDia = descontoAoDia;
    }
   
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public CrediarioTiposStatusPrestacao getCrediarioTiposStatusPrestacao() {
        return this.crediarioTiposStatusPrestacao;
    }
    
    public void setCrediarioTiposStatusPrestacao(CrediarioTiposStatusPrestacao crediarioTiposStatusPrestacao) {
        this.crediarioTiposStatusPrestacao = crediarioTiposStatusPrestacao;
    }
    public CrediarioFormaPagamentoDetalhado getCrediarioFormaPagamentoDetalhado() {
        return this.crediarioFormaPagamentoDetalhado;
    }
    
    public void setCrediarioFormaPagamentoDetalhado(CrediarioFormaPagamentoDetalhado crediarioFormaPagamentoDetalhado) {
        this.crediarioFormaPagamentoDetalhado = crediarioFormaPagamentoDetalhado;
    }
    public PreVenda getPreVenda() {
        return this.preVenda;
    }
    
    public void setPreVenda(PreVenda preVenda) {
        this.preVenda = preVenda;
    }
    public BigDecimal getParcelaValor() {
        return this.parcelaValor;
    }
    
    public void setParcelaValor(BigDecimal parcelaValor) {
        this.parcelaValor = parcelaValor;
    }
    public Date getParcelaData() {
        return this.parcelaData;
    }
    
    public void setParcelaData(Date parcelaData) {
        this.parcelaData = parcelaData;
    }
    public BigDecimal getRecebimentoValor() {
        return this.recebimentoValor;
    }
    
    public void setRecebimentoValor(BigDecimal recebimentoValor) {
        this.recebimentoValor = recebimentoValor;
    }
    public Date getRecebimentoData() {
        return this.recebimentoData;
    }
    
    public void setRecebimentoData(Date recebimentoData) {
        this.recebimentoData = recebimentoData;
    }
    public BigDecimal getJurosAoDia() {
        return this.jurosAoDia;
    }
    
    public void setJurosAoDia(BigDecimal jurosAoDia) {
        this.jurosAoDia = jurosAoDia;
    }
    public BigDecimal getDescontoAoDia() {
        return this.descontoAoDia;
    }
    
    public void setDescontoAoDia(BigDecimal descontoAoDia) {
        this.descontoAoDia = descontoAoDia;
    }




}


