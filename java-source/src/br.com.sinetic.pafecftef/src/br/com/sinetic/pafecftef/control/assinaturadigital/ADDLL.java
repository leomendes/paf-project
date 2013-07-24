package br.com.sinetic.pafecftef.control.assinaturadigital;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import com.sun.jna.Native;


/**
 *
 * @author Administrador
 */
public interface ADDLL extends com.sun.jna.win32.StdCallLibrary {

    public ADDLL instance = (ADDLL) Native.loadLibrary("sign_bema", ADDLL.class);

    public int genkkey(byte[] cpublica, byte[] cprivada);

    public int setLibType(int tipo);

    public int generateEAD(String cNomeArquivo, byte[] cChavePublica, byte[] cChavePrivada, byte[] cEAD, int iSign);
    
    public int validateFile(String cNomeArquivo,  String eChavePublica, String eChavePrivada);
    
    public int md5FromFile( byte[] cNomeArquivo, byte[] bMD5 );
    
}

