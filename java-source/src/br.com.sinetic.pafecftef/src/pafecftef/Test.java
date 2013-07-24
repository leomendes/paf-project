/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pafecftef;

import bemajava.BemaString;
import br.com.sinetic.pafecftef.control.assinaturadigital.ADDLL;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.entities.AbreCaixa;
import br.com.sinetic.pafecftef.entities.DiaFiscal;
import br.com.sinetic.pafecftef.entities.TestDate;
import com.sun.jna.Native;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class Test {

    public static void main(String args[]) {
        testDate();
    }

    public static void testDate() {
        try {
            br.com.sinetic.pafecftef.db.DBControl.load();
            /*
            DiaFiscal df = new DiaFiscal();
            Calendar data = Calendar.getInstance();
            //data.add(Calendar.MONTH, -1);
            df.setDataHora(data.getTime());

            DBControl.gravaDiaFiscal(df);
            */
            /*
             TestDate t = new TestDate();
             Calendar data = Calendar.getInstance();
             data.add(Calendar.MONTH, 3);
        
             Calendar data2 = Calendar.getInstance();
             data2.add(Calendar.MONTH, 5);
        
             t.setDataHora(data.getTime());
             t.setnDataHora(data2.getTime());
        
             try {
             DBControl.gravaTestDate(t);
             } catch (Exception ex) {
             Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
             }
             */

            DiaFiscal dia = DBControl.getDiaFiscal(System.currentTimeMillis());
            System.out.println("");
            if (dia != null){
                System.out.println(dia.getId());
                System.out.println(dia.getDataHora());
            }

        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void testMD5() {
        System.out.println("sign_bema");
        try {
            System.loadLibrary("sign_bema");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load." + e);
        }
        try {
            ADDLL ad = (ADDLL) Native.loadLibrary("sign_bema", ADDLL.class);
            byte[] bMD5 = new byte[33];
            String cNomeArquivo = "C:\\arquivo.txt";
            byte[] bArquivo = new byte[cNomeArquivo.length() + 1];

            //bArquivo[bArquivo.length-1] = 0;

            for (int i = 0; i < cNomeArquivo.length(); i++) {
                bArquivo[i] = (byte) cNomeArquivo.charAt(i);
            }

            int retorno = ad.md5FromFile(bArquivo, bMD5);
            System.out.println("retorno: " + retorno);
            BemaString md5 = new BemaString();
            //retorno  = Bematech.md5FromFile("C:\\arquivo.txt", md5);

            System.out.println("retorno: " + retorno);
            System.out.println("md5: " + md5);
            System.out.println("bMD5: " + bMD5.toString());

            System.out.print(bMD5 + ": {");
            for (int i = 0; i < bMD5.length; i++) {
                System.out.print(String.valueOf(bMD5[i]));
                if (i < bMD5.length - 1) {
                    System.out.print(",");
                }
            }
            System.out.print("};");
            System.out.println();

        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load." + e);
        }
        try {
            System.load("C:\\Windows\\SysWOW64\\sign_bema.dll");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load." + e);
        }
        try {
            System.load("C:\\Windows\\System32\\sign_bema.dll");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load." + e);
        }
    }
}
