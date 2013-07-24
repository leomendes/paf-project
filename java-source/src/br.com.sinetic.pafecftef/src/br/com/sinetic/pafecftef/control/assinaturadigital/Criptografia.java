/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.control.assinaturadigital;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 *
 * @author Windows8
 */
public class Criptografia {
    

    private BasicTextEncryptor textEncryptor;
    
    
    public Criptografia(byte[] key) {
        textEncryptor = new BasicTextEncryptor();
        String pw = "";
        for (int i = 0; i < key.length; i++) {
            pw += key[i];
        }
        
        textEncryptor.setPassword(pw);
    }
    
    public String encrypt(String s) {
        return textEncryptor.encrypt(s);
    }
    
    public String decrypt(String s) {
        return textEncryptor.decrypt(s);
    }
    
}
