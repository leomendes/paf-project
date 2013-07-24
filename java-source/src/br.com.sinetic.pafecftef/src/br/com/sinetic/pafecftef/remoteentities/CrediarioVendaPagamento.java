package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;

/**
 * CrediarioVendaPagamento generated by hbm2java
 */
public class CrediarioVendaPagamento  implements java.io.Serializable {


     private int codigo;
     private CrediarioFormaPagamentoDetalhado crediarioFormaPagamentoDetalhado;
     private PreVenda preVenda;
     private BigDecimal valor;
     private boolean status;

    public CrediarioVendaPagamento() {
    }

    public CrediarioVendaPagamento(int codigo, CrediarioFormaPagamentoDetalhado crediarioFormaPagamentoDetalhado, PreVenda preVenda, BigDecimal valor, boolean status) {
       this.codigo = codigo;
       this.crediarioFormaPagamentoDetalhado = crediarioFormaPagamentoDetalhado;
       this.preVenda = preVenda;
       this.valor = valor;
       this.status = status;
    }
   
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
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
    public BigDecimal getValor() {
        return this.valor;
    }
    
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
    public boolean isStatus() {
        return this.status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }




}

