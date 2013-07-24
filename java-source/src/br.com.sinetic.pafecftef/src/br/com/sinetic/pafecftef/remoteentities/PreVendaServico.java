package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.math.BigDecimal;

/**
 * PreVendaServico generated by hbm2java
 */
public class PreVendaServico  implements java.io.Serializable {


     private int codigo;
     private PafEcfDescontoTipo pafEcfDescontoTipo;
     private Servico servico;
     private PreVenda preVenda;
     private BigDecimal valorInicial;
     private BigDecimal descontoValor;
     private BigDecimal valorFinal;
     private boolean status;

    public PreVendaServico() {
    }

	
    public PreVendaServico(int codigo, BigDecimal valorInicial, BigDecimal descontoValor, BigDecimal valorFinal, boolean status) {
        this.codigo = codigo;
        this.valorInicial = valorInicial;
        this.descontoValor = descontoValor;
        this.valorFinal = valorFinal;
        this.status = status;
    }
    public PreVendaServico(int codigo, PafEcfDescontoTipo pafEcfDescontoTipo, Servico servico, PreVenda preVenda, BigDecimal valorInicial, BigDecimal descontoValor, BigDecimal valorFinal, boolean status) {
       this.codigo = codigo;
       this.pafEcfDescontoTipo = pafEcfDescontoTipo;
       this.servico = servico;
       this.preVenda = preVenda;
       this.valorInicial = valorInicial;
       this.descontoValor = descontoValor;
       this.valorFinal = valorFinal;
       this.status = status;
    }
   
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public PafEcfDescontoTipo getPafEcfDescontoTipo() {
        return this.pafEcfDescontoTipo;
    }
    
    public void setPafEcfDescontoTipo(PafEcfDescontoTipo pafEcfDescontoTipo) {
        this.pafEcfDescontoTipo = pafEcfDescontoTipo;
    }
    public Servico getServico() {
        return this.servico;
    }
    
    public void setServico(Servico servico) {
        this.servico = servico;
    }
    public PreVenda getPreVenda() {
        return this.preVenda;
    }
    
    public void setPreVenda(PreVenda preVenda) {
        this.preVenda = preVenda;
    }
    public BigDecimal getValorInicial() {
        return this.valorInicial;
    }
    
    public void setValorInicial(BigDecimal valorInicial) {
        this.valorInicial = valorInicial;
    }
    public BigDecimal getDescontoValor() {
        return this.descontoValor;
    }
    
    public void setDescontoValor(BigDecimal descontoValor) {
        this.descontoValor = descontoValor;
    }
    public BigDecimal getValorFinal() {
        return this.valorFinal;
    }
    
    public void setValorFinal(BigDecimal valorFinal) {
        this.valorFinal = valorFinal;
    }
    public boolean isStatus() {
        return this.status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }




}

