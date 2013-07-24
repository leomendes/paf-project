/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.printer.bematech;

import br.com.sinetic.pafecftef.printer.Printer;
import br.com.sinetic.pafecftef.printer.FormasPagamento;
import br.com.sinetic.pafecftef.printer.Retorno;
import bemajava.*;
import br.com.sinetic.pafecftef.action.Controller;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import br.com.sinetic.pafecftef.control.assinaturadigital.AssinaturaDigital;
import br.com.sinetic.pafecftef.printer.PrinterFloat;
import br.com.sinetic.pafecftef.printer.PrinterInteger;
import br.com.sinetic.pafecftef.printer.PrinterLong;
import br.com.sinetic.pafecftef.printer.PrinterString;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.ini4j.Config;
import org.ini4j.Wini;
import java.util.Date;


public class BemaPrinter implements Printer {


    private int iRetorno;
    private Retorno rRetorno; 
    
    public static final int MAX_DESC_SIZE = 29;
    
    public static final int RET_IRET = 1;
    public static final int RET_ST1 = 3;
    public static final int RET_ST2 = 4;
    
    public static final String PATH_PRINTER_INI = "c:/windows/system32/BemaFI32.ini";

    public static final String RELATORIO_PAG_NAO_FISCAL = "9";
    
    public BemaPrinter () {
    }
    
    @Override
    public int analisaiRetorno() {
        String sErro = "";
        switch (iRetorno) {
        case 0:
            sErro = "Erro de Comunicação!";
            JOptionPane.showMessageDialog(null, sErro);
            rRetorno.addError(RET_IRET, 0, sErro);
            rRetorno.setErroDeComunicacao(true);
            break;
        case -1:
            sErro = "Erro de Execução na Função. Verifique!";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -1, sErro);
            break;
        case -2:
            sErro = "Parâmetro Inválido !";
            JOptionPane.showMessageDialog(null, sErro);
            rRetorno.addError(RET_IRET, -2, sErro);
            break;
        case -3:
            sErro = "Alíquota não programada!";
            JOptionPane.showMessageDialog(null, sErro);
            rRetorno.addError(RET_IRET, -3, sErro);
                break;
        case -4:
            sErro = "Arquivo BemaFI32.INI não encontrado. Verifique!";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -4, sErro);
                break;
        case -5:
            sErro = "Erro ao Abrir a Porta de Comunicação!";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -5, sErro);
                break;
        case -6:
            sErro = "Impressora Desligada ou Desconectada!";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -6, sErro);
                break;
        case -7:
            sErro = "Banco Não Cadastrado no Arquivo BemaFI32.ini!";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -7, sErro);
                break;
        case -8:
            sErro = "Erro ao Criar ou Gravar no Arquivo Retorno.txt ou Status.txt";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -8, sErro);
                break;
        case -18:
            sErro = "Não foi possível abrir arquivo INTPOS.001 !";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -18, sErro);
                break;
        case -19:
            sErro = "Parâmetro Diferentes !";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -19, sErro);
                break;
        case -20:
            sErro = "Transação Cancelada pelo Operador !";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -20, sErro);
                break;
        case -21:
            sErro = "A Transação não foi Aprovada !";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -21, sErro);
                break;
        case -22:
            sErro = "Não foi Possível Terminar a Impressão !";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -22, sErro);
                break;
        case -23:
            sErro = "Não foi Possível Terminar a Operação !";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -23, sErro);
                break;
        case -24:
            sErro = "Forma de Pagamento não Programada.";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, 24, sErro);
                break;
        case -25:
            sErro = "Totalizador não Fiscal não Programado.";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -25, sErro);
                break;
        case -26:
            sErro = "Transação já Efetuada !";
            JOptionPane.showMessageDialog(null, sErro);
            rRetorno.addError(RET_IRET, -26, sErro);
            break;
        case -28:
            sErro = "Não há Informações para Serem Impressas !";
            JOptionPane.showMessageDialog(null,sErro);
            rRetorno.addError(RET_IRET, -28, sErro);
            break;

        default:
            rRetorno.setErroDeComunicacao(false);
                break;
        }

        return iRetorno;
    }

    @Override
    public int retornoImpressora() {
        BemaInteger iACK = new BemaInteger();
        BemaInteger iST1 = new BemaInteger();
        BemaInteger iST2 = new BemaInteger();

        iRetorno =  Bematech.RetornoImpressora(iACK, iST1, iST2);
        String sErro = "";

        if (iACK.number == 6) {
            // Verifica ST1
            if (iST1.number >= 128) {
                sErro = "Fim de Papel";
                iST1.number = iST1.number - 128;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST1, 128, sErro);
            }
            if (iST1.number >= 64) {
                sErro = "Pouco Papel";
                iST1.number = iST1.number - 64;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST1, 64, sErro);
            }
            if (iST1.number >= 32) {
                sErro = "Erro no Relógio";
                iST1.number = iST1.number - 32;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST1, 32, sErro);
            }
            if (iST1.number >= 16) {
                sErro = "Impressora em ERRO";
                iST1.number = iST1.number - 16;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST1, 16, sErro);
            }
            if (iST1.number >= 8) {
                sErro = "CMD não iniciado com ESC";
                iST1.number = iST1.number - 8;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST1, 8, sErro);
            }
            if (iST1.number >= 4) {
                sErro = "Comando Inexistente";
                iST1.number = iST1.number - 4;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST1, 4, sErro);
            }
            if (iST1.number >= 2) {
                sErro = "Cupom Aberto";
                iST1.number = iST1.number - 2;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST1, 2, sErro);
            }
            if (iST1.number >= 1) {
                sErro = "Nº de Parâmetros Inválidos";
                iST1.number = iST1.number - 1;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST1, 1, sErro);
            }

            // Verifica ST2
            if (iST2.number >= 128) {
                sErro = "Tipo de Parâmetro Inválido";
                iST2.number = iST2.number - 128;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST2, 128, sErro);
            }
            if (iST2.number >= 64) {
                sErro = "Memória Fiscal Lotada";
                iST2.number = iST2.number - 64;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST2, 64, sErro);
            }
            if (iST2.number >= 32) {
                sErro = "CMOS não Volátil";
                iST2.number = iST2.number - 32;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST2, 32, sErro);
            }
            if (iST2.number >= 16) {
                sErro = "Alíquota Não Programada";
                iST2.number = iST2.number - 16;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST2, 16, sErro);
            }
            if (iST2.number >= 8) {
                sErro = "Alíquotas lotadas";
                iST2.number = iST2.number - 8;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST2, 8, sErro);
            }
            if (iST2.number >= 4) {
                sErro = "Cancelamento não Permitido";
                iST2.number = iST2.number - 4;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST2, 4, sErro);
            }
            if (iST2.number >= 2) {
                sErro = "CGC/IE não Programados";
                iST2.number = iST2.number - 2;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST2, 2, sErro);
            }
            if (iST2.number >= 1) {
                sErro = "Comando não Executado";
                iST2.number = iST2.number - 1;
                JOptionPane.showMessageDialog(null, sErro);
                rRetorno.addError(RET_ST2, 1, sErro);
            }
        }
        return iRetorno;
    }

    @Override
    public Retorno leituraX() {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.LeituraX();
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }
                
            }
            else {
                ok = true;
            }
        }
        
        return rRetorno;
    }

    @Override
    public Retorno reducaoZ() {
        rRetorno = new Retorno();
        if (JOptionPane.showConfirmDialog(
                        null,
                        "Deseja Emitir a Redução Z? - ATENÇÃO: Caixa será encerrado!",
                        "Pergunta do Sistema", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            
            boolean ok = false;
            while (!ok) {
                iRetorno =  Bematech.ReducaoZ("", "");
                analisaiRetorno();
                retornoImpressora();
                if (rRetorno.isErro()) {
                    if (!Controller.confirmBox("Deseja tentar novamente?")) {
                        ok = true;
                    }

                }
                else {
                    ok = true;
                }
            }
        }
        return rRetorno;
    }
    
    @Override
    /**
     * IndiceGerencial: STRING com o índice do Relatório Gerencial, com até 2 caracteres.
        NumeroLaudo: STRING com o número do laudo, com até 15 caracteres.
        CNPJ: STRING com o CNPJ do desenvolvedor, com até 18 caracteres.
        RazaoSocial: STRING com a razão social, com  até 80 caracteres.
        Endereco: STRING com o endereço, com até 85 caracteres.
        Telefone: STRING com o telefone, com até 38 caracteres.
        Contato: STRING com o contato, com até 38 caracteres.
        NomeComercial: STRING com o nome comercial, com até 80 caracteres.
        Versao: STRING com a versão, com até 40 caracteres.
        Path: STRING com o caminho do principal executável da aplicação, com até 65 caracteres.
        MD5: STRING com o MD5 do principal executável da aplicação, com 32 caracteres.
        DemaisArquivos: STRING com o caminho dos demais arquivos, com até 670 caracteres. Cada PATH é limitado a 65 caracteres e separado por vígula do PATH seguinte. Total de 10 Paths.
        MD5DemaisArquivos: STRING com o MD5 dos demais arquivos, com até 338 caracteres. Cada MD5 deve possuir 32 caracteres e separado por vírgula do MD5 seguinte. Total de 10 MD5.
        NumeroSerie: STRING com os números de fabricação dos ECFs autorizados a utilizar o PAF-ECF, com até 420 caracteres. Cada número de fabricação deve possui até 20 caracteres e separado por vírgula do número seguinte. Total de 20 números de fabricação.
     */
    public Retorno identificacaoPAFECF(String ig, String nl, String cnpj, String rs, String end, 
                                       String tel, String cont, String nc, String ver, String path, 
                                       String md5, String pathDA, String md5DA, String ns) {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.IdentificacaoPAFECF(ig, nl, cnpj, rs, end, tel, cont, nc, ver, path, 
                                                     md5, pathDA, md5DA, ns);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno NomeiaRelatorioIdentificacaoPAFECF(PrinterInteger ret) {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.NomeiaRelatorioIdentificacaoPAFECF();
            ret.number = new Integer(iRetorno);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }

    @Override
    public Retorno nomeiaRelatorioPagamentoNaoFiscal(PrinterInteger ret) {
        //Nomeia o relatório
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno = Bematech.NomeiaRelatorioGerencialMFD(RELATORIO_PAG_NAO_FISCAL, "Receb. Não Fiscal");
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno abreRelatorioPagamentoNaoFiscal() {
        //Nomeia o relatório
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno = Bematech.AbreRelatorioGerencialMFD(RELATORIO_PAG_NAO_FISCAL);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno usaRelatorioPagamentoNaoFiscal(String texto) {
        //Abre o relatório
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno = Bematech.UsaRelatorioGerencialMFD(texto);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno fechaRelatorioPagamentoNaoFiscal() {
        //Abre o relatório
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno = Bematech.FechaRelatorioGerencial();
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    /**
     * CGC_CPF: STRING até 29 caracteres com o CGC ou CPF do cliente.
        Nome: STRING até 30 caracteres com o nome do cliente.
        Endereco: STRING até 80 caracteres com o endereço do cliente.
     * @param cpfCnpj
     * @param nome
     * @param endereco
     * @return 
     */
    @Override
    public Retorno abreCupom(String cpfCnpj, String nome, String endereco) {
        rRetorno = new Retorno();
        //Checa tamanho dos campos
        String sErro = null;
        if (cpfCnpj.length() > 29) {
            sErro = "O CPF/CNPJ não pode ter mais que 29 caracteres!";
        }
        else if (nome.length() > 30) {
            sErro = "O NOME do comprador não pode ter mais que 30 caracteres!";
        }
        else if (endereco.length() > 80) {
            sErro = "O ENDEREÇO do comprador não pode ter mais que 80 caracteres!";
        }
        
        if (sErro != null) {
            JOptionPane.showMessageDialog(null, sErro);
            rRetorno.addError(-1, -1, sErro);
            return rRetorno;
        }
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.AbreCupomMFD(cpfCnpj, nome, endereco);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    /**
     * Cupom ao consumidor
     * @return 
     */
    @Override
    public Retorno abreCupom() {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.AbreCupom("");
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    /**
     * Cupom ao consumidor
     * @return 
     */
    @Override
    public Retorno abreCupom(String cpfCnpj) {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.AbreCupom(cpfCnpj);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }

    @Override
    public Retorno fechaCupom(String acrescimoDesconto,
                    String tipoAcrescimoDesconto, String valorAcrescimoDesconto,
                    String mensagem, ArrayList<FormasPagamento> formasPagamento) {
        rRetorno = new Retorno();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.IniciaFechamentoCupom(acrescimoDesconto,
                            tipoAcrescimoDesconto, valorAcrescimoDesconto);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        
        for (int i = 0; i < formasPagamento.size(); i++) {
            ok = false;
        
            while (!ok) {
                iRetorno =  Bematech.EfetuaFormaPagamento(formasPagamento.get(i)
                                .getDescricao(), formasPagamento.get(i).getValor());
                analisaiRetorno();
                retornoImpressora();
                if (rRetorno.isErro()) {
                    if (!Controller.confirmBox("Deseja tentar novamente?")) {
                        ok = true;
                    }

                }
                else {
                    ok = true;
                }
            }
        }

        ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.TerminaFechamentoCupom(mensagem);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }

    @Override
    public Retorno setarPortaSerial(String com) {
        rRetorno = new Retorno();
        
        try {
            Wini ini = new Wini(new File(PATH_PRINTER_INI));

            Config config = new Config();
            config.setStrictOperator(true);
            config.setEscape(false);
            ini.setConfig(config);
            ini.put("Sistema", "Porta", com);            
            ini.store();
        }
        catch (IOException e) {
            Controller.messageBox("Erro ao setar porta serial!");
            rRetorno.addError(-1, -1, "Erro ao setar porta serial!");
            return rRetorno;
        }
        
        
        return new Retorno();
    }
    
    @Override
    public Retorno abrirPortaSerial() {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.AbrePortaSerial();
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }

    @Override
    public Retorno fecharPortaSerial() {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.FechaPortaSerial();
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }

    @Override
    public Retorno horarioVerao() {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.ProgramaHorarioVerao();
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }

    //////// Leituras da memória fiscal e MFD para o menu fiscal///////////
    
    @Override
    public Retorno memoriaFiscalPorData(String data1, String data2, int tipo) {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            if (tipo == Printer.LMF_COMPLETA) {
                iRetorno =  Bematech.LeituraMemoriaFiscalDataMFD(data1, data2, "C");
            }
            else if (tipo == Printer.LMF_SIMPLIFICADA) {
                iRetorno =  Bematech.LeituraMemoriaFiscalDataMFD(data1, data2, "S");
            }
            else {
                rRetorno.addError(0, 0, "Tipo de Leitura de Memória Fiscal não existe");
                return rRetorno;
            }
        
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }

    @Override
    public Retorno memoriaFiscalPorReducao(int red1, int red2, int tipo) {
        rRetorno = new Retorno();
        
        String sRI = String.valueOf(red1);
        String sRF = String.valueOf(red2);
        
        boolean ok = false;
        
        while (!ok) {
            if (tipo == Printer.LMF_COMPLETA) {
                iRetorno =  Bematech.LeituraMemoriaFiscalReducaoMFD(sRI, sRF, "C");
            }
            else if (tipo == Printer.LMF_SIMPLIFICADA) {
                iRetorno =  Bematech.LeituraMemoriaFiscalReducaoMFD(sRI, sRF, "S");
            }
            else {
                rRetorno.addError(0, 0, "Tipo de Leitura de Memória Fiscal não existe");
                return rRetorno;
            }

            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno memoriaFiscalPorDataEspelho(String data1, String data2, int tipo, String arquivo) throws IOException{
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            if (tipo == Printer.LMF_COMPLETA) {
                iRetorno =  Bematech.LeituraMemoriaFiscalSerialDataPAFECF(data1, data2, "C", 
                                                                          AssinaturaDigital.getChavePublicaString(), 
                                                                          AssinaturaDigital.getChavePrivadaString());
            }
            else if (tipo == Printer.LMF_SIMPLIFICADA) {
                iRetorno =  Bematech.LeituraMemoriaFiscalSerialDataPAFECF(data1, data2, "S", 
                                                                          AssinaturaDigital.getChavePublicaString(), 
                                                                          AssinaturaDigital.getChavePrivadaString());
            }
            else {
                rRetorno.addError(0, 0, "Tipo de Leitura de Memória Fiscal não existe");
                return rRetorno;
            }

            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        //Grava no arquivo
        BemaString arqContent = new BemaString();
        int retArq = Bematech.LeArquivoRetorno(arqContent);
        
        if (retArq == 1) {
            gravaStringEmArquivo(arqContent.buffer, arquivo);
        }
        
        return rRetorno;
    }
    
    @Override
    public Retorno memoriaFiscalPorReducaoEspelho(int red1, int red2, int tipo, String arquivo) throws IOException{
        rRetorno = new Retorno();
        
        String sRI = String.valueOf(red1);
        String sRF = String.valueOf(red2);
        
        boolean ok = false;
        
        while (!ok) {
            if (tipo == Printer.LMF_COMPLETA) {
                iRetorno =  Bematech.LeituraMemoriaFiscalSerialReducaoPAFECF(sRI, sRF, "C", 
                                                                          AssinaturaDigital.getChavePublicaString(), 
                                                                          AssinaturaDigital.getChavePrivadaString());
            }
            else if (tipo == Printer.LMF_SIMPLIFICADA) {
                iRetorno =  Bematech.LeituraMemoriaFiscalSerialReducaoPAFECF(sRI, sRF, "S", 
                                                                          AssinaturaDigital.getChavePublicaString(), 
                                                                          AssinaturaDigital.getChavePrivadaString());
            }
            else {
                rRetorno.addError(0, 0, "Tipo de Leitura de Memória Fiscal não existe");
                return rRetorno;
            }

            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        
        //Grava no arquivo
        BemaString arqContent = new BemaString();
        int retArq = Bematech.LeArquivoRetorno(arqContent);
        
        if (retArq == 1) {
            gravaStringEmArquivo(arqContent.buffer, arquivo);
        }
        
        return rRetorno;
    }
    
    @Override
    public Retorno memoriaFiscalPorDataArquivo(String data1, String data2, String arquivo) throws IOException {
        rRetorno = new Retorno();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.ArquivoMFD(null, data1, data2, "D", "01" , 0,
                                            AssinaturaDigital.getChavePublicaString(), 
                                            AssinaturaDigital.getChavePrivadaString(), 1);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
            
        //Grava no arquivo
        BemaString arqContent = new BemaString();
        int retArq = Bematech.LeArquivoRetorno(arqContent);
        
        if (retArq == 1) {
            gravaStringEmArquivo(arqContent.buffer, arquivo);
        }
        
        return rRetorno;
    }

    @Override
    public Retorno memoriaFiscalPorReducaoArquivo(int red1, int red2, String arquivo) throws IOException {
        rRetorno = new Retorno();
        
        String sRI = String.valueOf(red1);
        String sRF = String.valueOf(red2);
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.ArquivoMFD(null, sRI, sRF, "C", "01" , 0,
                                            AssinaturaDigital.getChavePublicaString(), 
                                            AssinaturaDigital.getChavePrivadaString(), 1);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        
        //Grava no arquivo
        BemaString arqContent = new BemaString();
        int retArq = Bematech.LeArquivoRetorno(arqContent);
        
        if (retArq == 1) {
            gravaStringEmArquivo(arqContent.buffer, arquivo);
        }
        
        return rRetorno;
    }
    
    @Override
    public Retorno espelhoMFDData(String arquivo, String dataInicial, String dataFinal) throws IOException {
        rRetorno = new Retorno();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.EspelhoMFD(arquivo, dataInicial, dataFinal, "D", "01" ,
                                            AssinaturaDigital.getChavePublicaString(), 
                                            AssinaturaDigital.getChavePrivadaString());
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        
        return rRetorno;
    }
    
    @Override
    public Retorno espelhoMFDReducao(String arquivo, int redInicial, int redFinal) throws IOException {
        rRetorno = new Retorno();
        
        String sRI = String.valueOf(redInicial);
        String sRF = String.valueOf(redFinal);
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.EspelhoMFD(arquivo, sRI, sRF, "C", "01" ,
                                            AssinaturaDigital.getChavePublicaString(), 
                                            AssinaturaDigital.getChavePrivadaString());
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        
        return rRetorno;
    }
    
    @Override
    public Retorno arquivoMFDData( String arquivo, String dataInicial, String dataFinal, boolean unicoArquivo )throws IOException {
        rRetorno = new Retorno();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.ArquivoMFD(null, dataInicial, dataFinal, "D", "01" , 1,
                                            AssinaturaDigital.getChavePublicaString(), 
                                            AssinaturaDigital.getChavePrivadaString(), 1);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        
        //Grava no arquivo
        BemaString arqContent = new BemaString();
        int retArq = Bematech.LeArquivoRetorno(arqContent);
        
        if (retArq == 1) {
            gravaStringEmArquivo(arqContent.buffer, arquivo);
        }
        
        return rRetorno;
    }
    
    @Override
    public Retorno arquivoMFDReducao( String arquivo, int redInicial, int redFinal, boolean unicoArquivo )throws IOException {
        rRetorno = new Retorno();
        
        String sRI = String.valueOf(redInicial);
        String sRF = String.valueOf(redFinal);
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.ArquivoMFD(null, sRI, sRF, "C", "01" , 1,
                                            AssinaturaDigital.getChavePublicaString(), 
                                            AssinaturaDigital.getChavePrivadaString(), 1);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        
        //Grava no arquivo
        BemaString arqContent = new BemaString();
        int retArq = Bematech.LeArquivoRetorno(arqContent);
        
        if (retArq == 1) {
            gravaStringEmArquivo(arqContent.buffer, arquivo);
        }
        
        return rRetorno;
    }
    
    @Override
    public Retorno cancelaCupom() {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.CancelaCupom();
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
            
        return rRetorno;
    }

    @Override
    public Retorno vendeItem(long codigo, String descricao, float aliquota, 
                        String tipoQnt, float quantidade, int casasDecimais, 
                        float preco, String tipoDesconto, float vlrDesconto) {

        
        rRetorno = new Retorno();
        
        //Porta os valores para a impressora Bematech:
        String cod = String.valueOf(codigo);
        String aliq = converteAliquota(aliquota);
        String tQ = "I";
        String vU = "";
        String qnt = "";
        
        //Porta a Quantidade
        if (tipoQnt.matches(Printer.QNT_FRACAO)) {
            tQ = "F";
            if (quantidade > 999.9999f) {
                rRetorno.addError(0, 0, "Quantidade não permitida");
                qnt = "0";
            }
            else {
                qnt = converteQntFrac(quantidade);
            }
        }
        else if (tipoQnt.matches(Printer.QNT_INTEIRO)) {
            tQ = "I";
            if (quantidade > 9999) {
                rRetorno.addError(0, 0, "Quantidade não permitida");
                qnt = "0";
            }
            else {
                int q = (int)quantidade;
                qnt = String.valueOf(q);
            }
        }
        else {
            rRetorno.addError(0, 0, "Tipo de quantidade inválida");
            qnt = "0";
        }
        
        
        //Porta Valores
        vU = converteValor(preco, casasDecimais);
        
        
        //Porta Desconto
        String tD = "$";
        String vD = "";
        if (tipoDesconto.matches(Printer.DESC_DINHEIRO)) {
            tD = "$";
            if (vlrDesconto > 999999.99f) {
                rRetorno.addError(0, 0, "Valor do desconto maior que o permitido");
                vD = "000";
            }
            else if (vlrDesconto < 0) {
                rRetorno.addError(0, 0, "Valor do desconto não pode ser negativo");
                vD = "000";
            }
            else {
                vD = converteValor(vlrDesconto, 2);
            }
        }
        else if (tipoDesconto.matches(Printer.DESC_PERCENTUAL)) {
            tD = "%";
            if (vlrDesconto > 99.99f) {
                rRetorno.addError(0, 0, "Valor do desconto maior que o permitido");
                vD = "000";
            }
            else if (vlrDesconto < 0) {
                rRetorno.addError(0, 0, "Valor do desconto não pode ser negativo");
                vD = "000";
            }
            else {
                vD = converteValor(vlrDesconto, 2);
            }
        }
        
        String fDesc = formatDescricao(descricao);
             
        if (!rRetorno.isErro()) {
            boolean ok = false;
        
            while (!ok) {
                iRetorno =  Bematech.VendeItem(cod, fDesc, aliq, tQ,
                                qnt, casasDecimais, vU, tD, vD);
                analisaiRetorno();
                retornoImpressora();
                if (rRetorno.isErro()) {
                    if (!Controller.confirmBox("Deseja tentar novamente?")) {
                        ok = true;
                    }

                }
                else {
                    ok = true;
                }
            }
            
        }
        return rRetorno;
    }

    @Override
    public Retorno cancelaItemAnterior() {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.CancelaItemAnterior();
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
            
        return rRetorno;
    }

    @Override
    public Retorno cancelaItemGenerico(String item) {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.CancelaItemGenerico(item);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
            
        return rRetorno;
    }

    
    /**
     * Aliquota a ser setada.
     * Tipo: 0 para ICMS e 1 para ISS
     */
    @Override
    public Retorno programaAliquota(float aliquota, int tipo) {
        rRetorno = new Retorno();
        
        //Formata a aliquota para Bematech
        String aliq = converteAliquota(aliquota);
        
        int trib = 0;
        if (tipo == Printer.TRIB_ICMS) {
            trib = 0;
        }
        else if (tipo == Printer.TRIB_ISS) {
            trib = 1;
        }
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.ProgramaAliquota(aliq, trib);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
            
         return rRetorno;
    }
    
    @Override
    public Retorno verificaImpressoraLigada() {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.VerificaImpressoraLigada();
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }

    
    //////// MEIOS DE PAGAMENTO /////
    
    /**
     * Cadastra uma forma de pagamento na impressora térmica/fiscal
     * da Bematech.
     * 
     * @param descricao Descricao da forma de pagamento
     * @param tef Se esta forma de pagamento ira utilizar o TEF
     * @return 
     */
    @Override
    public Retorno programaFormaDePagamento(String descricao, boolean tef) {
        rRetorno = new Retorno();
        String sTef = "0";
        
        if (tef) {
            sTef = "1";
        }
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.ProgramaFormaPagamentoMFD(descricao, sTef);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        
        return rRetorno;
    }
    
    @Override
    public Retorno iniciaModoTEF() {
        rRetorno = new Retorno();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.IniciaModoTEF();
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno iniciaPagamento(float desconto, String tipoDesc) {
        rRetorno = new Retorno();
        
        
        String sTipoDesc = "$";
        if (tipoDesc.matches(Printer.DESC_PERCENTUAL)) {
            sTipoDesc = "%";
        }
        
        String sDesconto = converteValor(desconto, 2);
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.IniciaFechamentoCupom("D", sTipoDesc, sDesconto);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    
    @Override
    public Retorno registraPagamento(String meio, float valor) {
        rRetorno = new Retorno();
        
        if (meio == null || meio.isEmpty()) {
            rRetorno.addError(-1, -1, "Meio de pagamento inválido");
            return rRetorno;
        }
        if (meio.length() > 16) {
            meio = meio.substring(0, 16);
        }
        
        String sValor = converteValor(valor, 2);
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.EfetuaFormaPagamento(meio, sValor);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno fechaPagamentoCupom(String msg) {
        rRetorno = new Retorno();
        
        if (msg == null || msg.isEmpty()) {
            rRetorno.addError(-1, -1, "Mensagem inválida");
            return rRetorno;
        }
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.TerminaFechamentoCupom(msg);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    
    @Override
    public Retorno finalizaModoTEF() {
        rRetorno = new Retorno();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.FinalizaModoTEF();
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno abreComprovanteNaoFiscalVinculado(String meioPag, float valor, String numCupom) {
        rRetorno = new Retorno();
        
        String sValor = String.valueOf(valor);
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.AbreComprovanteNaoFiscalVinculado(meioPag, sValor, numCupom);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno usaComprovanteNaoFiscalVinculadoTEF(String texto) {
        rRetorno = new Retorno();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.UsaComprovanteNaoFiscalVinculado(texto);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno fechaComprovanteNaoFiscalVinculado() {
        rRetorno = new Retorno();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.FechaComprovanteNaoFiscalVinculado();
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno EstornoFormasPagamento(String meioRetira, String meioColoca, float valor) {
        rRetorno = new Retorno();
        
        
        String sValor = converteValor(valor, 2);
       
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.EstornoFormasPagamento(meioRetira, meioColoca, sValor);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno suprimento (float valor) {
        rRetorno = new Retorno();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.Suprimento(converteValor(valor, 2), "Dinheiro");
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno sangria(float valor) {
        rRetorno = new Retorno();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.Sangria(converteValor(valor, 2));
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    @Override
    public Retorno aberturaDoDia(float valor) {
        //@workaround
        rRetorno = leituraX();
        
        if (rRetorno.isErro()) {
            return rRetorno;
        }
        
        if (valor > 0) {
            return suprimento(valor);
        }
        else {
            return new Retorno();
        }
        
        /*
        //@todo fazer com a impressora real
        rRetorno = new Retorno();
        iRetorno =  Bematech.AberturaDoDia(converteValor(valor, 2), null);
        analisaiRetorno();
        retornoImpressora();
        return rRetorno;*/
    }
    
    @Override
    public Retorno fechamentoDoDia() {
        //workaround
        
        return reducaoZ();
        
        /*
        //@todo fazer com a impressora real
        rRetorno = new Retorno();
        iRetorno =  Bematech.FechamentoDoDia();
        analisaiRetorno();
        retornoImpressora();
        return rRetorno;*/
    }
    
    @Override
    public Retorno grandeTotal(PrinterString gt) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.GrandeTotal(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        gt.buffer = bs.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno NumeroSerie(PrinterString psNS) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.NumeroSerieMFD(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        psNS.buffer = bs.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno modeloImpressora(PrinterString psMI) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.ModeloImpressora(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        psMI.buffer = bs.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno numeroSubstituicoesProprietario(PrinterString ps){
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.NumeroSubstituicoesProprietario(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        ps.buffer = bs.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno numeroCupom(PrinterString ps) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.NumeroCupom(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        ps.buffer = bs.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno contadorCupomFiscal(PrinterString ps) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.ContadorCupomFiscalMFD(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        ps.buffer = bs.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno ultimoItemVendido(PrinterInteger pi) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.UltimoItemVendido(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }

        try {
            pi.number = Integer.getInteger(bs.buffer);
        }
        catch (Exception e) {
            rRetorno.addError(-1, -1, "Erro ao obter último item vendido");
        }
        
        return rRetorno;
    }
    
    @Override
    public Retorno verificaTruncamento(PrinterString ps) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.VerificaTruncamento(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }

        ps.buffer = bs.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno contadorRelatoriosGerenciais(PrinterString ps) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.ContadorRelatoriosGerenciaisMFD(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }

        ps.buffer = bs.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno contadorComprovantesCredito(PrinterString ps) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.ContadorComprovantesCreditoMFD(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }

        ps.buffer = bs.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno numeroOperacoesNaoFiscais(PrinterString ps) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.NumeroOperacoesNaoFiscais(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }

        ps.buffer = bs.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno contadoresTotalizadoresNaoFiscais(PrinterString ps) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.ContadoresTotalizadoresNaoFiscais(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        ps.buffer = bs.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno dadosUltimaReducao(PrinterString ps) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.DadosUltimaReducaoMFD(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        ps.buffer = bs.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno vendaBruta(PrinterFloat pf) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.VendaBruta(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        String number = bs.buffer.substring(0, bs.buffer.length()-2) + "."+ bs.buffer.substring(bs.buffer.length()-2, bs.buffer.length());
        
        try {
            pf.number = Float.valueOf(number);
        }
        catch (Exception e) {
            rRetorno.addError(-1, -1, "Erro de conversão");
            return rRetorno;
        }
        
        return rRetorno;
    }
    
    
    @Override
    public Retorno flagsFiscais(PrinterInteger pi) {
        rRetorno = new Retorno();
        BemaInteger bi = new BemaInteger();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.FlagsFiscais3MFD(bi);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        pi.number = bi.number;
        
        return rRetorno;
    }
    
    @Override
    public Retorno mfAdicional(PrinterString psMA) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        BemaString bs1 = new BemaString();
        BemaString bs2 = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.DataHoraGravacaoUsuarioSWBasicoMFAdicional(bs, bs1, bs2);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        psMA.buffer = bs2.buffer;
        
        return rRetorno;
    }
    
    @Override
    public Retorno dataHoraUltimoDocumento(PrinterLong pl) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.DataHoraUltimoDocumentoMFD(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        if (rRetorno.isErro()) {
            return rRetorno;
        }
        
        SimpleDateFormat formatador = new SimpleDateFormat("ddMMyyHHmmss");  
        Date d = null;
        try {
            d = formatador.parse(bs.buffer);
        }
        catch (ParseException e) {
            rRetorno = new Retorno();
            rRetorno.addError(-1, -1, "Erro");
            return rRetorno;
        }
        
        pl.number = d.getTime();
        
        return rRetorno;
    }
    
    @Override
    public Retorno subTotal(PrinterFloat pf) {
        rRetorno = new Retorno();
        BemaString bs = new BemaString();
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.SubTotal(bs);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        pf.number = Float.parseFloat(bs.buffer)/100;
        
        return rRetorno;
    }
    
    @Override
    public Retorno abreRelatorioMeiosPagamento(int indice) {
        rRetorno = new Retorno();
        
        String sI = String.format("%02d", indice);
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.AbreRelatorioMeiosPagamento(sI);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }

    @Override
    public Retorno usaRelatorioMeiosPagamento(String meio, String tipoDoc, float valorAcumulado, Date data) {
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        String sD = formatador.format(data);
        
        String sVA = "";
        
        try {
            sVA = Float.toString(valorAcumulado);
        }
        catch (Exception e) {
            Retorno r = new Retorno();
            r.addError(-1, -1, "Erro de conversão");
            return r; 
        }
        
        rRetorno = new Retorno();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.UsaRelatorioMeiosPagamento(meio, tipoDoc, sVA, sD);
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }

    @Override
    public Retorno fechaRelatorioMeiosPagamento() {
        rRetorno = new Retorno();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.FechaRelatorioMeiosPagamento();
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }

    @Override
    public Retorno nomeiaRelatorioMeiosDePagamento(PrinterInteger pi) {
        rRetorno = new Retorno();
        
        boolean ok = false;
        
        while (!ok) {
            iRetorno =  Bematech.NomeiaRelatorioMeiosDePagamento();
            pi.number = iRetorno;
            analisaiRetorno();
            retornoImpressora();
            if (rRetorno.isErro()) {
                if (!Controller.confirmBox("Deseja tentar novamente?")) {
                    ok = true;
                }

            }
            else {
                ok = true;
            }
        }
        return rRetorno;
    }
    
    
    
    //Auxiliar - Mexe com arquivo
    private void gravaStringEmArquivo(String s, String arquivo) throws IOException {
        File f = new File(arquivo);
        if (f.exists()) {
            f.delete();
        }

        f = new File(arquivo);
        f.setWritable(true);
        FileWriter fw = new FileWriter(f);
        fw.write(s);
        fw.close();
    }
    
    
    /////// Métodos de Conversão /////
    
    
    /**
     * Converte aliquota em float para o padrao Bematech
     * @param aliquota
     * @return 
     */
    private String converteAliquota(float aliquota) {
        String aliq = "";
        aliq = String.format("%.2f", aliquota) ;
        aliq = aliq.replace(",", "");
        aliq = aliq.replace(".", "");
        if (aliquota < 10) {
            aliq = "0"+aliq;
        }
        return aliq;
    }
    
    private String converteQntFrac (float qnt) {
        String sQ = "";
        sQ = String.format("%.3f", qnt);
        sQ = sQ.replace(",", "");
        sQ = sQ.replace(".", "");
        if (qnt < 10) {
            sQ = "0"+sQ;
        }
        else if (qnt < 100) {
            sQ = "00"+sQ;
        }
        else if (qnt < 1000) {
            sQ = "000"+sQ;
        }
        return sQ;
    }
    
    private String converteValor(float valor, int casasDecimais) {
        String vU = "";
        vU = String.format("%."+casasDecimais+"f", valor);
        vU = vU.replace(",", "");
        vU = vU.replace(".", "");
        return vU;
    }
    
    private void printByteArray(byte[] b) {
        for (int i = 0; i < b.length; i++) {
            System.out.print((char)b[i]);
        }
        System.out.println();
    }
    
    private byte[] stringToByteArray(String s) {
        if (s == null) return null;
        
        byte[] b = new byte[s.length()+1];
        for (int i = 0; i < s.length(); i++) {
            b[i] = (byte)s.charAt(i);
        }
        return b;
    }
    
    private byte[] intToByteArray(int iNum, int size) {
        if (iNum < 0) return null;
        if (size < 1) return null;
        
        byte[] b = new byte[size+1];

        String sNum = String.valueOf(iNum);
        int len = size-sNum.length();
        for (int i = 0; i < len; i++) {
            sNum = "0"+sNum;
        }

        return stringToByteArray(sNum);
    }
    
    private String formatDescricao(String desc) {
        if (desc == null ) {
            return "";
        }
        else if (desc.isEmpty()) {
            return "";
        }
        else if (desc.length() < MAX_DESC_SIZE) {
            return desc;
        }
        else {
            return desc.substring(0, MAX_DESC_SIZE);
        }
    }
    
    public Date getDataImpressora(){
        BemaString bs = new BemaString();
        BemaString bs1 = new BemaString();
        rRetorno = new Retorno();
        
        iRetorno = Bematech.DataHoraImpressora(bs,bs1);
        
        analisaiRetorno();
        retornoImpressora();
        
        SimpleDateFormat formatador = new SimpleDateFormat("ddMMyyHHmmss");  
        Date date = null;
        try {
            date = formatador.parse(bs.buffer+bs1.getBuffer());
        }catch (ParseException e) {
            date = new Date(System.currentTimeMillis());
            rRetorno = new Retorno();
            rRetorno.addError(-1, -1, "Erro");
        }        
        
        return date;
    }
}
