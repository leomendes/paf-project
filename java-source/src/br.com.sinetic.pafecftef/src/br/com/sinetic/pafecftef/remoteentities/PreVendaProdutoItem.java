package br.com.sinetic.pafecftef.remoteentities;
// Generated 05-Mar-2013 18:04:02 by Hibernate Tools 3.2.1.GA



/**
 * PreVendaProdutoItem generated by hbm2java
 */
public class PreVendaProdutoItem  implements java.io.Serializable {


     private int codigo;
     private PreVendaProduto preVendaProduto;
     private PreVenda preVenda;
     private Produtos produtos;
     private boolean status;

    public PreVendaProdutoItem() {
    }

	
    public PreVendaProdutoItem(int codigo, boolean status) {
        this.codigo = codigo;
        this.status = status;
    }
    public PreVendaProdutoItem(int codigo, PreVendaProduto preVendaProduto, PreVenda preVenda, Produtos produtos, boolean status) {
       this.codigo = codigo;
       this.preVendaProduto = preVendaProduto;
       this.preVenda = preVenda;
       this.produtos = produtos;
       this.status = status;
    }
   
    public int getCodigo() {
        return this.codigo;
    }
    
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public PreVendaProduto getPreVendaProduto() {
        return this.preVendaProduto;
    }
    
    public void setPreVendaProduto(PreVendaProduto preVendaProduto) {
        this.preVendaProduto = preVendaProduto;
    }
    public PreVenda getPreVenda() {
        return this.preVenda;
    }
    
    public void setPreVenda(PreVenda preVenda) {
        this.preVenda = preVenda;
    }
    public Produtos getProdutos() {
        return this.produtos;
    }
    
    public void setProdutos(Produtos produtos) {
        this.produtos = produtos;
    }
    public boolean isStatus() {
        return this.status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }




}

