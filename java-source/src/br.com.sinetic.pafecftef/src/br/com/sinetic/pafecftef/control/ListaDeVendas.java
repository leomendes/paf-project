/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.control;

import br.com.sinetic.pafecftef.entities.Vendas;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Administrador
 */
public class ListaDeVendas {
    
    public static final int STATUS_CRIADA = 0;
    public static final int STATUS_CARREGOU_LISTA = 1;
    public static final int STATUS_REG_ITENS = 2;
    public static final int STATUS_REG_PAGAMENTO = 3;
    public static final int STATUS_FINALIZA = 4;
    
    private List<Vendas> lVendasEntities;
    

    public ListaDeVendas () {
        lVendasEntities = new ArrayList<>();
    }
    
     
    public Vendas addItem (int codigo, String vendedor, String descricao) {
        
        
        Vendas vItem = new Vendas();
        vItem.setCodigo(codigo);
        vItem.setDescricao(descricao);
        vItem.setVendedor(vendedor);
        vItem.setStatus(STATUS_CRIADA);
        
        lVendasEntities.add(vItem);
        
        return vItem;
    }
    
    public boolean setItems(Set items, int vNum) {
        if (vNum < lVendasEntities.size()) {
            try {
                lVendasEntities.get(vNum).setItems(items);
                return true;
            }
            catch (Exception e) {
                return false;
            }
        }
        else {
            return false;
        }
    }
    
    public int getCodigo(int n) {
        return lVendasEntities.get(n).getCodigo();
    }
    
    public String getVendedor(int n) {
        return lVendasEntities.get(n).getVendedor();
    }
    
    public String getDescricao(int n) {
        return lVendasEntities.get(n).getDescricao();
    }
    
    public boolean removeItem(int codigo) {
        for (Vendas i:lVendasEntities) {
            if (i.getCodigo() == codigo) {
                lVendasEntities.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public Vendas getVenda(int n) {
        if (n < lVendasEntities.size()) {
            try {
                return lVendasEntities.get(n);
            }
            catch (Exception e) {
                return null;
            }
        }
        else {
            return null;
        }
    }
    
    public int getTotalItens() {
        return lVendasEntities.size();
    }
    
}

