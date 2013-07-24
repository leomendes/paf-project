/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.control;

import br.com.sinetic.pafecftef.entities.AliquotasImpressora;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Windows8
 */
public class Aliquotas {

   
    private List<AliquotasImpressora> lAliquotasEntities;
    

    public Aliquotas () {
        lAliquotasEntities = new ArrayList<>();
    }
    
     
    public AliquotasImpressora addItem (float aliquota) {
        
        
        AliquotasImpressora ai = new AliquotasImpressora();
        ai.setAliquota((double)aliquota);
        
        lAliquotasEntities.add(ai);
        
        return ai;
    }
    
    public AliquotasImpressora addItem (int id, float aliquota) {
        
        
        AliquotasImpressora ai = new AliquotasImpressora();
        ai.setId(id);
        ai.setAliquota((double)aliquota);
        
        lAliquotasEntities.add(ai);
        
        return ai;
    }
    
    
    public double getAliquota(int n) {
        return lAliquotasEntities.get(n).getAliquota();
    }
    
    
    public boolean removeItem(float aliquota) {
        for (AliquotasImpressora i:lAliquotasEntities) {
            if (i.getAliquota() == aliquota) {
                lAliquotasEntities.remove(i);
                return true;
            }
        }
        return false;
    }
    
    public AliquotasImpressora getAliquotaEntity(int n) {
        if (n < lAliquotasEntities.size()) {
            try {
                return lAliquotasEntities.get(n);
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
        return lAliquotasEntities.size();
    }
    

}
