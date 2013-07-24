package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA


import java.util.Date;

/**
 * LojaFormaPagamento generated by hbm2java
 */
public class LojaFormaPagamento  implements java.io.Serializable {


     private int codigo;
     private CrediarioFormaPagamentoDetalhado crediarioFormaPagamentoDetalhado;
     private Loja loja;
     private Usuario usuario;
     private Date criadoEm;
     private boolean status;

    public LojaFormaPagamento() {
    }

	
    public LojaFormaPagamento(int codigo, CrediarioFormaPagamentoDetalhado crediarioFormaPagamentoDetalhado, Loja loja, boolean status) {
        this.codigo = codigo;
        this.crediarioFormaPagamentoDetalhado = crediarioFormaPagamentoDetalhado;
        this.loja = loja;
        this.status = status;
    }
    public LojaFormaPagamento(int codigo, CrediarioFormaPagamentoDetalhado crediarioFormaPagamentoDetalhado, Loja loja, Usuario usuario, Date criadoEm, boolean status) {
       this.codigo = codigo;
       this.crediarioFormaPagamentoDetalhado = crediarioFormaPagamentoDetalhado;
       this.loja = loja;
       this.usuario = usuario;
       this.criadoEm = criadoEm;
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
    public Loja getLoja() {
        return this.loja;
    }
    
    public void setLoja(Loja loja) {
        this.loja = loja;
    }
    public Usuario getUsuario() {
        return this.usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    public Date getCriadoEm() {
        return this.criadoEm;
    }
    
    public void setCriadoEm(Date criadoEm) {
        this.criadoEm = criadoEm;
    }
    public boolean isStatus() {
        return this.status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }




}

