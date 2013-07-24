/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.control.assinaturadigital;

import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.printer.bematech.*;


/**
 *
 * @author Administrador
 */
public class AssinaturaDigital {
    
    public static final int KEY_SIZE = 257;
    public static final int EAD_SIZE = 257;
    
    public static final byte[] PUBLIC_KEY = {67,53,68,67,66,67,70,56,56,65,65,55,50,55,
                                             54,50,51,48,56,51,56,69,50,55,54,69,53,65,
                                             68,50,57,48,57,53,57,65,50,54,51,55,65,55,
                                             70,70,50,51,50,51,65,54,65,57,48,53,52,57,
                                             70,56,66,49,51,55,70,67,51,53,57,65,50,69,
                                             54,67,50,57,70,69,50,54,55,67,56,50,53,68,
                                             68,70,70,48,53,54,57,54,65,54,66,68,52,52,
                                             66,54,54,70,49,69,70,67,66,55,53,70,65,68,
                                             55,51,51,69,51,56,65,52,49,66,52,53,48,68,
                                             51,65,55,51,55,53,68,49,57,51,52,55,67,65,
                                             54,70,49,57,55,53,56,57,68,55,69,56,51,55,
                                             54,55,54,50,65,48,69,56,57,51,66,53,69,49,
                                             54,54,48,70,66,54,52,57,55,50,65,51,67,53,
                                             65,69,67,50,65,51,51,52,53,49,48,55,69,70,
                                             57,65,51,68,68,49,57,55,55,49,67,50,70,53,
                                             70,51,57,68,48,65,70,54,53,56,54,66,57,69,
                                             67,54,68,50,56,65,57,65,67,48,67,66,57,70,
                                             53,57,49,51,67,53,57,69,67,57,65,67,56,66,
                                             54,65,55,51,0};
    
    public static final byte[] PRIVATE_KEY = {70,67,56,56,67,54,48,48,56,50,66,50,66,49,
                                              53,69,65,48,56,69,67,70,65,48,67,52,70,56,
                                              53,55,48,70,70,52,57,67,51,55,52,66,52,50,
                                              49,57,54,51,51,57,50,57,49,54,50,50,67,53,
                                              69,68,56,57,51,66,52,66,49,54,56,66,54,57,
                                              70,57,70,57,53,56,65,69,56,51,50,51,67,67,
                                              55,56,51,55,53,48,57,68,48,55,48,48,66,70,
                                              53,70,54,55,67,50,68,51,54,48,54,68,48,68,
                                              67,51,50,65,65,52,51,67,68,54,68,68,57,66,
                                              68,57,67,56,57,51,69,50,68,48,56,54,50,52,
                                              49,49,67,53,70,52,54,52,68,50,70,65,56,54,
                                              51,69,49,49,49,53,67,50,48,56,48,49,50,66,
                                              69,66,69,54,68,48,54,70,68,68,65,51,70,66,
                                              51,55,67,67,67,49,52,49,66,57,55,67,57,68,
                                              70,69,51,48,68,50,68,68,53,68,48,50,66,68,
                                              49,52,51,65,53,53,52,53,49,57,56,53,56,53,
                                              68,67,69,53,69,57,54,57,50,55,53,57,57,65,
                                              50,67,66,66,65,54,50,49,52,49,51,50,49,70,
                                              48,53,50,66,0};
    
    //Acesso à DLL de Assinatura Digital
    private ADDLL ad;
    
    private String sPublicKey = null;
    private String sPrivateKey = null;
    
    private int iRetorno;
    
    //VERBOSE
    private boolean VERBOSE = true;
    
    public AssinaturaDigital() throws Exception {
        try {
              ad = ADDLL.instance;
              
        }
        catch (Exception e) {
            throw new Exception("Erro ao abrir DLL de Assinatura Digital");
        }
        
    }
    
    ////// Métodos de Assinatura Digital /////
    
    
    
    public boolean gerarNovaChave() {

        /*byte[] cpublica = new byte[KEY_SIZE];
        byte[] cprivada = new byte[KEY_SIZE];
        
        //Gera a chave publica e privada
        if (ad == null) {
            return false;
        }
        
        iRetorno = ad.genkkey(cpublica, cprivada);
        
        //Valida chave publica e privada
        if (cpublica == null || cprivada == null) {
            return false;
        }
        
        if (iRetorno == 0) {
            return false;
        }

        //Inicializa chaves
        sPublicKey = "";
        sPrivateKey = "";
        
        //Copia array de bytes para chave
        //Salva em String
        //Não salva o último caractere pois e um scape.
        //Ficaria duplicado.
        for (int i = 0; i < KEY_SIZE-1; i++) {
            sPublicKey += (char)cpublica[i];
            sPrivateKey += (char)cprivada[i];
        }
        
        //Salva a nova chave
        bPrivateKey = cprivada;
        bPublicKey = cpublica;
        
        if (VERBOSE) {
            imprimeChave(bPrivateKey, "Chave Privada");
            imprimeChave(bPublicKey, "Chave Pública");
        }
        */
        return true;
    }
    
    public static byte[] getChavePublica() {
        return PUBLIC_KEY;
    }
    
    public static byte[] getChavePrivada() {
        return PRIVATE_KEY;
    }
    
    public static String getChavePublicaString() {
        String pubKey = "";
        for (int i = 0; i < AssinaturaDigital.PUBLIC_KEY.length-1; i++) {
            pubKey += (char)AssinaturaDigital.PUBLIC_KEY[i];
        }
        return pubKey;
    }
    
    public static String getChavePrivadaString() {
        String privKey = "";
        for (int i = 0; i < AssinaturaDigital.PRIVATE_KEY.length-1; i++) {
            privKey += (char)AssinaturaDigital.PRIVATE_KEY[i];
        }
        return privKey;
    }
    
    /**
     * Retorna uma assinatura em 257 bytes, sendo 256 de dados e um scape
     * @param sArquivo
     * @return 
     */
    public byte[] gerarEAD(String sArquivo) {
        
        byte[] ead = new byte[EAD_SIZE];
        
        if (ad == null) {
            return null;
        }
        
        if (PRIVATE_KEY == null || PUBLIC_KEY == null) {
            return null;
        }
        
        if (PRIVATE_KEY.length != KEY_SIZE ||
            PUBLIC_KEY.length != KEY_SIZE) {
            return null;
        }
        
        iRetorno = ad.generateEAD(sArquivo, PUBLIC_KEY, PRIVATE_KEY, ead, 1);
        
        if (iRetorno == 0) {
            return null;
        }
        
        //Imprime a chave
        if (VERBOSE) {
            imprimeChave(ead, "EAD");
        }
        
        
        return ead;
    }
    
    
    public boolean validaArquivo(String file) {
        if (file == null) {
            return false;
        }
        
        if (PRIVATE_KEY == null ||
            PUBLIC_KEY == null) {
            return false;
        }
        
        if (PUBLIC_KEY.length != KEY_SIZE ||
                PRIVATE_KEY.length != KEY_SIZE) {
            return false;
        }
        
        iRetorno = ad.validateFile(file, sPublicKey, sPrivateKey);
        
        if (iRetorno == 0) {
            return false;
        }
        else {
            return true;
        }
    }
    
    /**
     * @return 
     */
    public String gerarMD5(String sNomeArquivo) {
        
        byte[] bArquivo;
        
        //Lenght 32       
        byte[] bMD5 = new byte[33];

        bArquivo = new byte[sNomeArquivo.length()+1];
        //bArquivo[bArquivo.length-1] = 0;
        
        for (int i = 0; i < sNomeArquivo.length(); i++) {
            bArquivo[i] = (byte)sNomeArquivo.charAt(i);
        }
        
        iRetorno = ad.md5FromFile( bArquivo, bMD5 );

        //Imprime a chave
        if (VERBOSE) {
            imprimeChave(bMD5, "Chave MD5");
        }

        String sMD5 = "";
        // o lenght -1 é pq não copia o último caracter, pois é um escape
        for (int i = 0; i < bMD5.length-1; i++) {
            sMD5 += (char)bMD5[i];
        }
        
        if ( iRetorno == 0 ) {
            return null;
        }
        
        return sMD5;
    }
    
    private void imprimeChave(byte[] b, String nome) {
        System.out.print(nome+": {");
            for (int i = 0; i < b.length; i++ ){
                System.out.print(String.valueOf(b[i]));
                if (i < b.length-1) {
                    System.out.print(",");
                }
            }
            System.out.print("};");
            System.out.println();
    }
    
}
