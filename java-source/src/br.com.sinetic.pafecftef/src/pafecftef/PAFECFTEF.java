/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pafecftef;

import bemajava.BemaString;
import bemajava.Bematech;
import br.com.sinetic.pafecftef.action.Controller;
import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.control.Registros;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.printer.bematech.BemaPrinter;

/**
 *
 * @author Administrador
 */
public class PAFECFTEF {

    public static PAFECFTEF instance;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        instance = new PAFECFTEF();
        //testeData();
    }
    
    public void testePAFECFTEF() {
        ////// INICIA OS BANCOS DE DADOS /////
        if (!DBControl.load()) {
            Controller.messageBox("Erro ao iniciar bancos de dados! \n A aplicação será finalizada!");
            System.exit(-1);
        }

        ///// CARREGA CONFIGURAÇÕES /////
        if (!Control.loadConfiguracoes()) {
            Controller.messageBox("Erro ao carregar configurações! \n A aplicação será finalizada!");
            System.exit(-1);
        }

        ////// INICIA A PARTE DE CONTROLE ////
        if (!Control.load()) {
            Controller.messageBox("Erro ao carregar 'Control'!");
            System.exit(-1);
        }

        ////// FUNCOES DA IMPRESSORA //////
        BemaPrinter printer = new BemaPrinter();

        //Seta a impressora no controle
        try {
            Control.start(printer);
        } catch (Exception e) {
            Controller.messageBox("Erro ao criar impressora");
            System.exit(-1);
        }

        printer.leituraX();

        bemajava.BemaString bs = new BemaString();
        int ret = Bematech.UFProprietarioMFD(bs);
        Controller.messageBox("BI: " + bs.buffer);



        //if (Registros.geraArquivoTabMercServ()) {
        //if (Registros.geraArquivoEletEstoque()) {
        if (Registros.geraArquivoTrocoCartao()) {
            //List<PafRegR03> p = Registros.capturaR03();
            //if (p != null) {
            System.out.println("OK");
        } else {
            System.out.println("Erro!");
        }
    }

    public PAFECFTEF() {

        ////// INICIA OS BANCOS DE DADOS /////
        if (!DBControl.load()) {
            Controller.messageBox("Erro ao iniciar bancos de dados! \n A aplicação será finalizada!");
            System.exit(-1);
        }

        ///// CARREGA CONFIGURAÇÕES /////
        if (!Control.loadConfiguracoes()) {
            Controller.messageBox("Erro ao carregar configurações! \n A aplicação será finalizada!");
            System.exit(-1);
        }

        ////// INICIA A PARTE DE CONTROLE ////
        if (!Control.load()) {
            Controller.messageBox("Erro ao carregar 'Control'!");
            System.exit(-1);
        }



        ////// FUNCOES DA IMPRESSORA //////
        BemaPrinter printer = new BemaPrinter();
        //Seta a impressora no controle
        try {
            Control.start(printer);
        } catch (Exception e) {
            Controller.messageBox("Erro ao criar impressora");
            System.exit(-1);
        }
        ///// TERMINA IMPRESSORA //////////

        //// Requerimentos do PAF quanto à impressora///
        Control.atualizaInfoECFConectado();

        //Chega ECF Conectado
        //@todo Testar estas funções com todas as p5ossibilidades
        if (!Control.checaECFConectado()) {
            if (!Control.checaCRZCRO()) {
                Controller.messageBox("A impressora conectada não está autorizada! \n"
                        + "A aplicação será bloqueada!");
                Control.setBloqueado(true);
            } else {
                if (Control.recomporDadosAAC()) {
                    Controller.messageBox("Corrigido problema no arquivo AAC!");
                } else {
                    Controller.messageBox("Encontrado problema no arquivo AAC! \n"
                            + "Arquivo não pode ser corrigido! PAF bloquado!");
                    Control.setBloqueado(true);
                }
            }
        }

        //Checa Grande Total
        if (!Control.checaGrandeTotal()) {
            if (!Control.checaCRZCRO()) {
                Controller.messageBox("O Grande Total da impressora conectada é diferente do arquivo auxiliar! \n"
                        + "A aplicação será bloqueada!");
                Control.setBloqueado(true);
            } else {
                if (Control.recomporDadosAAC()) {
                    Controller.messageBox("Corrigido problema no arquivo AAC!");
                } else {
                    Controller.messageBox("Encontrado problema no arquivo AAC! \n"
                            + "Arquivo não pode ser corrigido! PAF bloquado!");
                    Control.setBloqueado(true);
                }
            }
        }


        /////Checagens do dia fiscal////
        Control.loadDiaFiscal();


        ////// CHECA VENDA PENDENTE ///////
        Control.checaVendaPendente();

        ////// NOMEIA RELATÓRIOS GERENCIAIS //////
        //if (!Control.nomeiaRelatorios()) {
        //Controller.messageBox("Erro ao nomear relatórios gerenciais");
        //}


        //////// INICIA O CONTROLLER ///////
        Controller.iniciarController();

        Controller.setInfoTP("Aplicação Iniciada...");


    }
}
