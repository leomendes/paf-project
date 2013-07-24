/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.control;

import java.util.ArrayList;
import java.util.List;
import br.com.sinetic.pafecftef.entities.Item;
import br.com.sinetic.pafecftef.entities.Vendas;

/**
 * Esta classe fornece os itens vendidos da interface de venda para o controle da impressora.
 * Servirá como um importador de pacotes de outra parte do sistema.
  */
public class ItensVendidos {
    
    public static final int STATUS_CRIADO = 0;
    public static final int STATUS_REGISTRADO = 1;
    
    private Vendas venda;
    private List<Item> lItemEntities;
    
    
    public ItensVendidos (Vendas v) throws NullPointerException{
        if (v == null) {
            throw new NullPointerException("Venda nula.");
        }
        
        venda = v;
        lItemEntities = new ArrayList<>();
    }
    
    /**
     * 
     * @return Retorna o Item como entidade do BD
     */
    public Item addItem (long codigo, String descricao, float aliquota, 
                        int tipoQnt, float quantidade, int casasDecimais, 
                        float preco, int tipoDesconto, float vlrDesconto) {
        
        //IE
        Item ie = new Item();
        ie.setAliquota((double)aliquota);
        ie.setCasasDecimais(casasDecimais);
        ie.setCodigo(codigo);
        ie.setDescricao(descricao);
        ie.setPreco((double)preco);
        ie.setQuantidade((double)quantidade);
        ie.setTipoDesconto(tipoDesconto);
        ie.setTipoQuantidade(tipoQnt);
        ie.setValorDesconto((double)vlrDesconto);
        ie.setVendas(venda);
        ie.setStatus(STATUS_CRIADO);
        
        lItemEntities.add(ie);
        
        return ie;
    }
    
    public long getCodigo(int n) {
        return lItemEntities.get(n).getCodigo();
    }
    
    public String getDescricao(int n) {
        return lItemEntities.get(n).getDescricao();
    }
    
    public float getAliquota(int n) {
        return Float.parseFloat(String.valueOf(lItemEntities.get(n).getAliquota()));
    }
    
    public int getTipoQnt(int n) {
        return lItemEntities.get(n).getTipoQuantidade();
    }
    
    public float getQuantidade(int n) {
        return Float.parseFloat(String.valueOf(lItemEntities.get(n).getQuantidade()));
    }
    
    public int getCasasDecimais(int n) {
        return lItemEntities.get(n).getCasasDecimais();
    }
    
    public float getPreco(int n) {
        return Float.parseFloat(String.valueOf(lItemEntities.get(n).getPreco()));
    }
    
    public int getTipoDesconto(int n) {
        return lItemEntities.get(n).getTipoDesconto();
    }
   
    public float getValorDesconto(int n) {
        return Float.parseFloat(String.valueOf(lItemEntities.get(n).getValorDesconto()));
    }
    
    public int getStatus(int n) {
        return lItemEntities.get(n).getStatus();
    }
    
    public boolean removeItem(long codigo) {
        for (Item i:lItemEntities) {
            if (i.getCodigo() == codigo) {
                lItemEntities.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public Item getItem(int n) {
        if (n < lItemEntities.size()) {
            return lItemEntities.get(n);
        }
        else {
            return null;
        }
    }
    
    public int getTotalItens() {
        return lItemEntities.size();
    }
    
    public long getVendaCodigo() {
        return venda.getCodigo();
    }
    
    public int getVendaStatus() {
        return venda.getStatus();
    }
    
    public Vendas getVendas() {
        return venda;
    }
    
    public void setVendaStatus(int status) {
        venda.setStatus(status);
    }
}


