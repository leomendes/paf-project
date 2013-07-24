/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.printer;

import java.util.ArrayList;
import java.util.List;
import br.com.sinetic.pafecftef.control.Control;

/**
 *
 * @author Administrador
 */
public class Retorno {
    private List<CErro>lErros;
    private int numErros;
    private boolean erro;
    
    public Retorno () {
        erro = false;
        numErros = 0;
        lErros = new ArrayList<>();
    }
    
    /**
     * Esta função também comunica a conexão entre ECF e aplicação.
     * Deve ser utilizada em TODAS as vezes que a impressora for utilizada!
     * @return 
     */
    public boolean isErro() {
        return erro;
    }
    
    public void setErroDeComunicacao(boolean b) {
        Control.setBlockECFNaoResponde(b);
    }
    
    public int getNumErros() {
        return numErros;
    }
    
    public String getDescriptionErrorOf(int n) {
        try {
            String ret = lErros.get(n).sDesc;
            return ret;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public Integer getErrorNumOf(int n) {
        try {
            Integer ret = lErros.get(n).iErro;
            return ret;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public Integer getTipoRetorno(int n) {
        try {
            Integer ret = lErros.get(n).iTipoRet;
            return ret;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Esta função também trava a aplicação caso a impressora não responder.
     * 
     * @param iTipoRetorno
     * @param iNumber
     * @param sDescription
     * @return 
     */
    public boolean addError(int iTipoRetorno, int iNumber, String sDescription) {
        try {
            CErro ce = new CErro();
            ce.iTipoRet = iTipoRetorno;
            ce.iErro = iNumber;
            ce.sDesc = sDescription;
            lErros.add(ce);
            erro = true;
            numErros++;
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    
}

class CErro {
    public int iErro;
    public String sDesc;
    public int iTipoRet;
}