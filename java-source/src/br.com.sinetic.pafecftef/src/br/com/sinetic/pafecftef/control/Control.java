package br.com.sinetic.pafecftef.control;

import bemajava.BemaString;
import bemajava.Bematech;
import br.com.sinetic.pafecftef.action.Controller;
import br.com.sinetic.pafecftef.control.assinaturadigital.AssinaturaDigital;
import br.com.sinetic.pafecftef.control.assinaturadigital.Criptografia;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.entities.AbreCaixa;
import br.com.sinetic.pafecftef.entities.AliquotasImpressora;
import br.com.sinetic.pafecftef.entities.ConfiguracoesSistema;
import br.com.sinetic.pafecftef.entities.DiaFiscal;
import br.com.sinetic.pafecftef.entities.EcfComprador;
import br.com.sinetic.pafecftef.entities.EcfExecutaveis;
import br.com.sinetic.pafecftef.entities.EcfIdentificacao;
import br.com.sinetic.pafecftef.entities.EcfReducoesz;
import br.com.sinetic.pafecftef.entities.EcfRelatoriosGerenciais;
import br.com.sinetic.pafecftef.entities.EcfSangrias;
import br.com.sinetic.pafecftef.entities.EcfSuprimentos;
import br.com.sinetic.pafecftef.entities.FechaCaixa;
import br.com.sinetic.pafecftef.entities.PafMeiosPagamentoDia;
import br.com.sinetic.pafecftef.entities.PafProduct;
import br.com.sinetic.pafecftef.entities.PafRegR02;
import br.com.sinetic.pafecftef.entities.PrevendaAberta;
import br.com.sinetic.pafecftef.printer.Printer;
import br.com.sinetic.pafecftef.printer.PrinterInteger;
import br.com.sinetic.pafecftef.printer.PrinterLong;
import br.com.sinetic.pafecftef.printer.PrinterString;
import br.com.sinetic.pafecftef.printer.Retorno;
import br.com.sinetic.pafecftef.remoteentities.CrediarioFormaPagamento;
import br.com.sinetic.pafecftef.remoteentities.CrediarioFormaPagamentoDetalhado;
import br.com.sinetic.pafecftef.remoteentities.CrediarioVendaParcelamento;
import br.com.sinetic.pafecftef.remoteentities.PreVenda;
import br.com.sinetic.pafecftef.remoteentities.PreVendaProduto;
import br.com.sinetic.pafecftef.remoteentities.PreVendaServico;
import br.com.sinetic.pafecftef.remoteentities.Produtos;
import br.com.sinetic.pafecftef.remoteentities.Servico;
import br.com.sinetic.pafecftef.remoteentities.SineticControleEstoque;
import br.com.sinetic.pafecftef.util.HashUtil;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Set;
import org.ini4j.Config;
import org.ini4j.Wini;

/**
 *
 * @author Administrador
 */
public class Control {
    
    //@TODO
    //TESTE!!!
    public static final float ALIQUOTA_TESTE = 5.00f;
    
    
    //Tipos de Tributo para aliquota
    public static final int TRIB_ISS = 1;
    public static final int TRIB_ICMS = 2;
    
    //Realatórios Gerenciais
    public static final int RG_IDENT_PAFECF = 1;
    
    
    //Status de Pré-Vendas Abertas
    
    //Foi capturada, neste estado, pode ser continuada sem ser cancelada.
    // A ação a ser tomada é colocar o "em_progresso = false"
    public static final int PV_STATUS_CAPTURADA = 0;
    
    //Venda foi iniciada - Pulou da tela de pré-venda para a tela de Formas
    // de pagamento, porém o CF não começou a ser impresso.
    public static final int PV_STATUS_INICIADA = 1; //Foi capturada
    
    // Cupom já começou a ser impresso. Ou seja, venda só pode ser cancelada.
    public static final int PV_STATUS_CF = 2;
    
    //Cupom terminou de ser impresso, porém não foi gravada como venda no banco.
    //Pode ser finalizada no banco.
    public static final int PV_STATUS_FINAL_CF = 3;
    
    //Status usado para finalizar esta pré-venda pendente
    public static final int PV_STATUS_FINALIZAR = 4;
    
    
    ///Status de dia fiscal
    public static final int DIA_FISCAL_NAO_CRIADO = 0;
    public static final int DIA_FISCAL_NAO_ABERTO = 1;
    public static final int DIA_FISCAL_ABERTO = 2;
    public static final int DIA_FISCAL_FECHADO = 3;
    private static int diaFiscal = DIA_FISCAL_NAO_CRIADO;
    
    ///Tipo de documento para pagamento
    public static final int TIPO_DOC_CUPOM_FISCAL = 1;
    public static final int TIPO_DOC_CNF = 2;
    public static final int TIPO_DOC_NOTA_FISCAL = 3;
    
    //Para ARQUIVO MD-5
    private static List<EcfExecutaveis> lEE = null;
    public static final String NOME_ARQ_AAC = "aac.ini";

   
    //Impressora a ser utilizada no sistema
    private static Printer pPrinter;
    private static Retorno rRet;
    
    public static String new_line = System.getProperty("line.separator");
    
    
    
    //Assinatura Digital
    private static AssinaturaDigital ad;
    
    //Criptografia
    private static Criptografia cripto;
    
    //Listas de Vendas e Itens Carregados
    public static List<ItensVendidos> iv;
    public static ListaDeVendas ldv;
    
    public static ConfiguracoesSistema configuracoes;
    public static EcfIdentificacao ecfIdentificacao;
    
    //Controle de bloqueio do PAF
    private static boolean bBloqueado = false;
    private static boolean bBlockECFNaoResp = false;
    
    
    
    public static boolean load() {
        //função que carrega todos os itens utilizados no Control
        
        //seta PAF como desbloqueado.
        bBloqueado = false;
        bBlockECFNaoResp = false;
        
        //Inicia Assinatura Digital
        try {
            ad = new AssinaturaDigital();
        }
        catch (Exception e) {
            Controller.messageBox("Erro ao criar instância de assinatura digital");
            return false;
        }
        
        //Inicia Criptografia
        cripto = new Criptografia(AssinaturaDigital.PUBLIC_KEY);
        
        //Carrega a identificação da SH - Sinetic
        ecfIdentificacao = DBControl.getEcfIdentificacao();
        
       
        String md5 = Registros.geraArquivoMD5();
        
        if (md5 == null || md5.isEmpty()) {
            Controller.messageBox("Não foi possível gerar arquivo MD-5 com os executáveis\n "
                                + "Aplicação será encerrada!");
            //@todo tirar este comentário
            System.exit(-1);
        }
        
        configuracoes.setMd5(md5);
        /*
        if (!atualizaAAC(md5)) {
            Controller.messageBox("Não foi possível atualizar Arquivo Auxiliar Criptografado\n "
                                + "Aplicação será encerrada!");
            System.exit(-1);
        }
        */
        //Inicia listas de vendas
        ldv = new ListaDeVendas();
        iv = new ArrayList<ItensVendidos>();
        
        //Carrega os principais executáveis
        lEE = DBControl.getEcfExecutaveis();
        
        
        
        return true;
    }
    
    public static boolean loadConfiguracoes() {
        //Dá load no arquivo de configurações
        configuracoes = DBControl.getConfiguracoes();
        
        if (configuracoes == null) {
            return false;
        }
        else {
            return true;
        }
    }
    
    public static boolean updateConfiguracoes() {
        return DBControl.atualizarConfiguracoes(configuracoes);
    }
    
    /**
     * Método deve ser chamado no Main para dar baixa em dia fiscal pendente e
     * Carregar o status do dia fiscal atual.
     */
    public static void loadDiaFiscal() {
        //Tenta fechar dia fiscal anterior, caso aberto.
        if (fechaDiaFiscalAbertoAnterior()) {
            Controller.messageBox("Foi fechado um Dia Fiscal anterior em aberto!\n"
                                 + "Redução Z emitida.");
        }
        
        //Verifica dia fiscal 
        diaFiscal = verificarStatusDiaFiscalNoBD();
    }
    
    public static AssinaturaDigital getAssinaturaDigital() {
        return ad;
    }
    
    public static Printer getPrinter() {
        return pPrinter;
    }
    
    /**
     * Caso o PAF esteja bloqueado, retornará TRUE.
     */
    public static boolean isBloqueado() {
        return bBloqueado|bBlockECFNaoResp;
    }
    
    public static void setBloqueado(boolean b) {
        bBloqueado = b;
    }
    
    /**
     * Deve ser setado true quando o ECF não responder.
     * @param b 
     */
    public static void setBlockECFNaoResponde(boolean b) {
        bBlockECFNaoResp = b;
    }
    
    public static void atualizaGrandeTotal() {
        //Pega o último Grande Total da impressora
        PrinterString ps = new PrinterString();
        
        rRet = pPrinter.grandeTotal(ps);
        if (ps.buffer == null || rRet.isErro()) {
            Controller.messageBox("Não foi possível ler o Grande Total da Impressora conectada!\n "
                                + "Aplicação será encerrada!");
            System.exit(-1);
            //@todo não encerrar a aplicação, mas pedir para interferir na impressora e tentar novamente
        }
        
        //Atualiza nas configurações locais
        configuracoes.setGrandeTotal(ps.buffer);
        
        //Abre arquivo auxiliar criptografado
        Wini ini = abreAAC();

        //Atualiza Grande Total no AAC
        String s = cripto.encrypt(ps.buffer);
        ini.put("TOTAL_GTC", "GTC", s);
        try {
            ini.store();
        }
        catch (IOException e) {
            Controller.messageBox("Não foi possível atualizar o Grande Total no AAC!\n "
                                + "Aplicação será encerrada!");
            System.exit(-1);
        }
        
    }
    
    public static int getStatusDiaFiscal() {
        return diaFiscal;
    }
    
    private static void setStatusDiaFiscal(int status) {
        diaFiscal = status;
    }
    
    public static void atualizaInfoECFConectado() {
        //Pega o último Grande Total da impressora
        PrinterString ps = new PrinterString();
        
        rRet = pPrinter.grandeTotal(ps);
        if (ps.buffer == null || rRet.isErro()) {
            Controller.messageBox("Não foi possível ler o Grande Total da Impressora conectada!\n "
                                + "Aplicação será encerrada!");
            System.exit(-1);
        }
        configuracoes.setGrandeTotal(ps.buffer);
        
        //Pega o numero do ECF conectado
        ps = new PrinterString();
        rRet = pPrinter.NumeroSerie(ps);
        if (rRet.isErro()) {
            Controller.messageBox("Não foi possível ler o Número de Série da Impressora conectada!\n "
                                + "Aplicação será encerrada!");
            System.exit(-1);
        }
        
        configuracoes.setNumeroSerie(ps.buffer);
    }
    
    public static boolean verificaImpressoraLigada() {
        rRet = pPrinter.verificaImpressoraLigada();
        if (rRet.isErro()) {
            return false;
        }
        else {
            return true;
        }
    }
    
    public static boolean checaECFConectado() {
        ArrayList<String> lEcfs = ecfsFromINI();

        if (lEcfs.size() <= 0) {
            return false;
        }
        
        String ecfAtual = configuracoes.getNumeroSerie();
        //Confere se o ECF conectado é autorizado
        for (String sE: lEcfs) {
            if (ecfAtual.matches(sE)) {
                return true;
            }
        }
            
        return false;
    }
    
    public static boolean checaCRZCRO() {
        //Pega os dados da última Reducao Z
        PafRegR02 p = DBControl.getUltimoRegR02();
        if (p == null) return false;
        
        //Prep
        PrinterString ps = new PrinterString();
        Retorno ret = pPrinter.dadosUltimaReducao(ps);
        if (ret.isErro()) return false;
        String[] sDur = ps.buffer.split(",");
        
        //C06
        String sC06 = sDur[2];
        Integer crz;
        try {
             crz = Integer.getInteger(sC06);
        }
        catch (Exception e) {
            return false;
        }        
        
        //C08
        String sC08 = sDur[1];
        Integer cro;
        try {
             cro = Integer.getInteger(sC08);
        }
        catch (Exception e) {
            return false;
        }
        
        if (cro == p.getCro()) {
            if (crz == p.getCrz()) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean recomporDadosAAC() {
        //Pega o último Grande Total da impressora
        PrinterString ps = new PrinterString();
        
        rRet = pPrinter.grandeTotal(ps);
        if (ps.buffer == null || rRet.isErro()) {
            return false;
        }
        
        //Atualiza nas configurações locais
        configuracoes.setGrandeTotal(ps.buffer);
        
        //Abre arquivo auxiliar criptografado
        Wini ini = abreAAC();

        //Atualiza Grande Total no AAC
        String s = cripto.encrypt(ps.buffer);
        ini.put("TOTAL_GTC", "GTC", s);
        try {
            ini.store();
        }
        catch (IOException e) {
            return false;
        }
        
        
        //Pega os ECFs do arquivo
        int j = 0;
        for (int i = 1; i < 256; i++) {
            j = i;
            s = ini.get("ECFs", "ECF"+i);
            if (s== null) {
                break;
            }
        }
        
        //Adiciona o ECF Conectado
        ps = new PrinterString();
        Retorno ret = pPrinter.NumeroSerie(ps);
        if (ret.isErro()) return false;
        String sECFC = cripto.encrypt(ps.buffer);
        ini.put("ECFs", "ECF"+j, sECFC); 
        
        return true;
    }
    
    private static ArrayList<String> ecfsFromINI() {
        ArrayList<String> lEcfs = new ArrayList<>();
        
        //Abre arquivo auxiliar criptografado
        Wini ini = abreAAC();

        //Lê os ECFs e descriptografa cada um
        String s = null;
        for (int i = 1; i < 256; i++) {
            s = ini.get("ECFs", "ECF"+i);
            if (s != null) {
                lEcfs.add(cripto.decrypt(s));
                s = null;
            }
            else {
                break;
            }
        }
        return lEcfs;
    }
    
    
    public static boolean checaGrandeTotal() {
        
        //Abre arquivo auxiliar criptografado
        Wini ini = abreAAC();

        //Lê o Grande Total
        String s = ini.get("TOTAL_GTC", "GTC");
        if (s != null) {
            s = cripto.decrypt(s);
        }
        else {
            return false;
        }

        //Pega o Grande Total do ECF Conectado
        if (!s.matches(configuracoes.getGrandeTotal())) {
            return false;
        }
            
        return true;
    }
    
    
    private static Wini abreAAC() {
        //Cria o arquivo
        File file = new File(NOME_ARQ_AAC);
        if (!file.exists()) {
            return null;
        }

        //Abre o arquivo no verificador de INI
        Wini ini;
        try {
             ini = new Wini(file);
        }
        catch (IOException e ){
            return null;
        }
        
        Config config = new Config();
        config.setStrictOperator(true);
        config.setEscape(false);
        ini.setConfig(config);
        
        return ini;
    }
    
    public static boolean nomeiaRelatorios() {
        boolean ok = true;
        
        PrinterInteger pi = new PrinterInteger();
        try {
            pPrinter.nomeiaRelatorioPagamentoNaoFiscal(pi);
        }
        catch (Exception e)
        {
            ok = false;
        }
        
        return ok;
    }
    
    /**
     * Cancela TODAS as Pré-Vendas pendentes.
     * Deve ser usado no fechamento de caixa.
     * @return 
     */
    private static boolean cancelaPreVendasAbertas() throws Exception {
       List<PreVenda> lPV = DBControl.getPreVendasAbertasProgresso();
       if (lPV == null) {
           return false;
       }
       
       for (PreVenda p: lPV) {
           if (!cancelaPreVenda(p)) {
               int cod = -1;
               if (p!= null) {
                   cod = p.getCodigo();
               }
               throw new Exception("Erro ao cancelar pré-venda: "+cod);
           }
       }
       return true;
    }
    
    public static void checaVendaPendente() {
        PrevendaAberta pva = DBControl.getPreVendaPendente();
        
        if (pva == null) {
            return;
        }
        
        int status = pva.getStatus();
        PreVenda pv = DBControl.getPreVenda(pva.getFkBrPrevendaId());
        
        if (pv == null) {
            Controller.messageBox("Inconsistência na pré-venda aberta. \n Pré-venda não encontrada!\n"
                    + " Nenhuma ação foi tomada!");
            return;
        }
        
        switch (status) {
            case PV_STATUS_CAPTURADA:
            case PV_STATUS_INICIADA:
                pv.setEmProgresso(false);
                DBControl.atualizaPreVenda(pv);
                DBControl.removePreVendaPendente(pva.getId());
                Controller.messageBox("Pré-venda em aberto foi reintegrada à lista de pré-vendas.");
                break;
            
            case PV_STATUS_CF:
                if (!cancelaPreVenda(pv)) {
                    Controller.messageBox("Erro ao cancelar Pré-venda pendente!");
                    return;
                }
                DBControl.removePreVendaPendente(pva.getId());
                Controller.messageBox("Pré-venda não pode ser recuperada. \n Pré-venda cancelada!");
                break;
                
            case PV_STATUS_FINAL_CF:
                //@todo fazer procedimento de baixa de estoque
                break;
                
            case PV_STATUS_FINALIZAR:
                DBControl.removePreVendaPendente(pva.getId());
                Controller.messageBox("Flag de pré-venda pendente foi removida!");
                break;
                
            default:
                Controller.messageBox("Status da Pré-venda pendente não encontrado! \n "
                        + "Nenhuma ação foi tomada!");
                break;
                
        }
    }
    
    public static boolean setaVendaPendente(int idPreVenda, int status) {
        
        //Procura algum registro de abertura desta pré-venda
        PrevendaAberta pva = DBControl.getPreVendaPendenteByFK(idPreVenda);
        
        //Se não há nenhum registro, cria um
        if (pva == null) {
            pva = new PrevendaAberta();
            pva.setFkBrPrevendaId(idPreVenda);
            pva.setStatus(status);
            try {
                DBControl.gravaPrevendaPendente(pva);
            }
            catch (Exception e) {
                return false;
            }
            return true;
        }
        //Se há algum, atualiza o registro
        else {
            //Finalizar a pré-venda pendente
            if (status == PV_STATUS_FINALIZAR) {
                DBControl.removePreVendaPendente(pva.getId());
                return true;
            }
            //Atualiza a pré-venda pendente
            else {
                pva.setStatus(status);
                DBControl.atualizaPreVendaPendente(pva);
                return true;
            }
        }
    }
    
        
    private static int verificarStatusDiaFiscalNoBD() {
        DiaFiscal df = DBControl.getDiaFiscal(System.currentTimeMillis());
        
        if (df == null) {
            return DIA_FISCAL_NAO_CRIADO;
        }
        else {
            if (df.getFechaCaixa()!= null) {
                return DIA_FISCAL_FECHADO;
            }
            else if (df.getAbreCaixa() != null) {
                return DIA_FISCAL_ABERTO;
            }
            else {
                return DIA_FISCAL_NAO_ABERTO;
            }
        }
    }
    
    public static boolean abrirDiaFiscal(float suprimento) {
        int status = getStatusDiaFiscal();
        DiaFiscal df = null;
        if (status == Control.DIA_FISCAL_NAO_CRIADO) {
            df = new DiaFiscal();
            df.setDataHora(new Date(System.currentTimeMillis()));
        }
        else if (status == Control.DIA_FISCAL_NAO_ABERTO) {
            df = DBControl.getDiaFiscal(System.currentTimeMillis());
        }
        else if (status == Control.DIA_FISCAL_FECHADO ||
                 status == Control.DIA_FISCAL_ABERTO) {
            return false;
        }
        else {
            return false;
        }
        

        if (suprimento < 0) {
            suprimento = 0;
        }
        
        //Emite automaticamente uma Leitura X e faz o suprimento
        rRet = pPrinter.aberturaDoDia(suprimento);
        if (rRet.isErro()) {
            return false;
        }
        else {
            //Se tudo OK, grava a Leitura X
            PrinterLong pl = new PrinterLong();
            rRet = pPrinter.dataHoraUltimoDocumento(pl);
            if (rRet.isErro() || pl.number == null) {
                //Tratar Erros pertinentes
                return false;
            }

            try {
                DBControl.gravaLeituraX(pl.number);
            }
            catch (Exception e) {
                return false;
            }
        }
        
        
        
        AbreCaixa ac = new AbreCaixa();
        ac.setDataHora(new Date(System.currentTimeMillis()));
        try {
            DBControl.gravaAbreCaixa(ac);
        }
        catch (Exception e) {
            return false;
        }
        
        df.setAbreCaixa(ac);
        
        if (status == Control.DIA_FISCAL_NAO_CRIADO) {
            try {
                DBControl.gravaDiaFiscal(df);
            }
            catch (Exception e) {
                return false;
            }
        }
        else if (status == Control.DIA_FISCAL_NAO_ABERTO) {
            if (!DBControl.atualizaDiaFiscal(df)) {
                return false;
            }
        }
        
        setStatusDiaFiscal(Control.DIA_FISCAL_ABERTO);
        
        if (suprimento > 0) {
            df = DBControl.getDiaFiscal(System.currentTimeMillis());
            EcfSuprimentos es = new EcfSuprimentos();
            es.setDataHora(getDataImpressora());
            es.setValor(suprimento);
            es.setDiaFiscal(df);
            try {
                DBControl.gravaSuprimento(suprimento, getDataImpressora().getTime());
            }
            catch (Exception e) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Fecha um dia fiscal anterior em aberto.
     * 
     * @return Retorna true se realmente havia um dia fiscal em aberto.
     * Se não havia nenhum dia fiscal em aberto, retorna false.
     */
    public static boolean fechaDiaFiscalAbertoAnterior() {
        DiaFiscal df = DBControl.getUltimoDiaFiscalAberto();
        
        if (df == null) {
            return false;
        }
        if (df.getAbreCaixa() == null) {
            //Não faz nenhuma ação.
        }
        else if (df.getFechaCaixa() == null) {
            //Chama fechamento de caixa do dia anterior.
            if (!fecharDiaFiscal(0, df, false)) {
                Controller.messageBox("Erro ao fechar Dia Fiscal pendente");
                return false;
            }
        }
        return true;
    }
    
    /**
     * Fecha um dia fiscal.
     * @param sangria - Se há sangria
     * @param df - A instância do dia fiscal
     * @param atualizaStatus Se vai atualizar o status do dia fiscal corrente na aplicação
     * @return 
     */
    private static boolean fecharDiaFiscal(float sangria, DiaFiscal df, boolean atualizaStatus) {
        if (df == null) {
            return false;
        }
        
        if (sangria > 0) {
            rRet = pPrinter.sangria(sangria);
            if (rRet.isErro()) {
                return false;
            }
            EcfSangrias es = new EcfSangrias();
            es.setDataHora(getDataImpressora());
            es.setValor(sangria);
            es.setDiaFiscal(df);
            try {
                DBControl.gravaSangria(es);
            }
            catch (Exception e) {
                return false;
            }
        }
        
        //Se está fechando o dia, então deve fechar as pré-vendas ANTES da Redução Z
        if (atualizaStatus) {
            try {
                if (cancelaPreVendasAbertas()) {
                    Controller.messageBox("Pré-vendas pendentes canceladas!");
                }
            }
            catch (Exception e) {
                Controller.messageBox("Erro ao cancelar pré-vendas pendentes!");
                return false;
            }
        }
        
        //Emite automaticamente a redução Z
        rRet = pPrinter.fechamentoDoDia();
        if (rRet.isErro()) {
            Controller.messageBox("Erro ao emitir fechamento do dia");
            return false;
        }
        else {
            //Se tudo OK, grava a redução Z emitida pelo fechamento do dia no banco
            PrinterLong pl = new PrinterLong();
            rRet = pPrinter.dataHoraUltimoDocumento(pl);
            if (rRet.isErro() || pl.number == null) {
                //Tratar Erros pertinentes
                return false;
            }

            try {
                DBControl.gravaReducaoZ(df, pl.number);
            }
            catch (Exception e) {
                return false;
            }
        }
        
       
        //Verifica se a redução Z é a primeira do mês.
        if (reducaoZPrimeiraDoMes()) {
            if (leituraMesAnteriorMemoriaFiscal()) {
                Controller.messageBox("Primeira Redução Z do mês.\n"
                                    + "Emitindo Leitura da Memória Fiscal do mês anterior.");
            }
            else {
                Controller.messageBox("Primeira Redução Z do mês.\n"
                                    + "ERRO ao emitir Leitura da Memória Fiscal do mês anterior.");
            }
        }
        
        
        FechaCaixa fc = new FechaCaixa();
        fc.setDataHora(new Date(System.currentTimeMillis()));
        try {
            DBControl.gravaFechaCaixa(fc);
        }
        catch (Exception e) {
            return false;
        }
        
        df.setFechaCaixa(fc);
        
        if (!DBControl.atualizaDiaFiscal(df)) {
            return false;
        }
        
        //Se NÃO está fechando o dia, então deve fechar as pré-vendas APÓS a Redução Z
        if (!atualizaStatus) {
            try {
                if (cancelaPreVendasAbertas()) {
                    Controller.messageBox("Pré-vendas pendentes canceladas!");
                }
            }
            catch (Exception e) {
                Controller.messageBox("Erro ao cancelar pré-vendas pendentes!");
                return false;
            }
        }
        
        //Neste caso, está fechando o dia fiscal no dia correto, ou seja, não está atrasado.
        if (atualizaStatus) {
            setStatusDiaFiscal(Control.DIA_FISCAL_FECHADO);
        }
        
        return true;
    }
    

    
    /**
     * Uma redução Z é automaticamente chamada.
     * 
     * @param suprimento
     * @return 
     */
    public static boolean fecharDiaFiscal(float sangria) {
        int status = getStatusDiaFiscal();
        DiaFiscal df = null;
        if (status == Control.DIA_FISCAL_NAO_CRIADO ||
            status == Control.DIA_FISCAL_NAO_ABERTO ||
            status == Control.DIA_FISCAL_FECHADO) {
            return false;
        }
        else if (status == Control.DIA_FISCAL_ABERTO) {
            df = DBControl.getDiaFiscal(System.currentTimeMillis());
        }
        else {
            return false;
        }
        
        return fecharDiaFiscal(sangria, df, true);
    }
    
    public static String numeroSerieECF() {
        PrinterString ps = new PrinterString();
        rRet = pPrinter.NumeroSerie(ps);
        if (rRet.isErro()) {
            return null;
        }
        return ps.buffer;
    }
    
    public static String mfAdicional() {
        PrinterString ps = new PrinterString();
        rRet = pPrinter.mfAdicional(ps);
        if (rRet.isErro()) {
            return null;
        }
        return ps.buffer;
    }
    
    public static boolean reducaoZPrimeiraDoMes() {
        EcfReducoesz erzUltima = DBControl.getUltimaReducaoZ();
        EcfReducoesz erzPenultima = DBControl.getPenultimaReducaoZ();
        
        if (erzUltima == null || erzPenultima == null) {
            return false;
        }
        
        Calendar calUltima = new GregorianCalendar();
        calUltima.setTimeInMillis(erzUltima.getDataHora().getTime());
        Calendar calPenultima = new GregorianCalendar();
        calUltima.setTimeInMillis(erzPenultima.getDataHora().getTime());
        int monthUltima = calUltima.get(Calendar.MONTH);
        int monthPenultima = calPenultima.get(Calendar.MONTH);
        
        //Se os meses forem diferentes quer dizer que a última mudou o mês
        if (monthUltima != monthPenultima) {
            return true;
        }
        return false;
    }
    

    
    /////////////// TRATAMENTO DE ARQUIVOS ////////////
    
    
    
    private static boolean atualizaAAC(String md5) {
        
        //Abre o arquivo
        File arquivo = null;
        try {
            if (new File(NOME_ARQ_AAC).exists()) {
                arquivo = new File(NOME_ARQ_AAC);
            }
            else {
                return false;
            }
        }
        catch (Exception e) {
            System.out.println("Erro ao abrir arquivo "+NOME_ARQ_AAC);
            return false;
        }
        
        
        //INI
        try {
            Wini ini = new Wini(arquivo);

            Config config = new Config();
            config.setStrictOperator(true);
            config.setEscape(false);
            ini.setConfig(config);
            
            
            //Grava espaço para MD5 do arquivo de executáveis
            ini.put("ArqMD5", "MD5", md5); 
            
            ini.store();
        }
        catch (IOException e) {
            return false;
        }
        
        return true;
    }
    

    
    //////////////// FIM DO TRATAMENTO DE ARQUIVOS ////////////
    
    
    public static void start(Printer p) throws Exception {
        pPrinter = p;
        
        if (pPrinter == null) {
            throw new Exception("Variável 'Control.printer' não pode ser nula");
        }
        else {
            //pPrinter.setarPortaSerial(configuracoes.getPortaSerial());
            //pPrinter.abrirPortaSerial();
        }
    }
    
    public static void stop() {
        pPrinter.fecharPortaSerial();
    }
    
    public static boolean iniciaVenda(PreVenda pv, EcfComprador comprador) {
        
        //Verifica novas aliquotas
        if (!verificaAliquotas(pv)) {
            return false;
        }
        
        //abre cupom
        if (comprador == null || comprador.getCpfCnpj() == null ||
            comprador.getCpfCnpj().isEmpty()) {
            rRet = pPrinter.abreCupom();
        }
        else {
            if ((comprador.getNome() == null || comprador.getNome().isEmpty()) ) {
                rRet = pPrinter.abreCupom(comprador.getCpfCnpj());
            }
            else {
                rRet = pPrinter.abreCupom(comprador.getCpfCnpj(), comprador.getNome(), comprador.getEndereco());
            }
        }
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        else {
            //Grava comprador no banco
            if (comprador != null && comprador.getCpfCnpj() != null &&
                !comprador.getCpfCnpj().isEmpty()) {
                int sID;
                try {
                    sID = DBControl.gravaComprador(comprador);
                }
                catch (Exception e) {
                    return false;
                }

                if (rRet.isErro()) {
                    DBControl.removeComprador(sID);
                    //@todo Fecha cupom e anula cupom anterior
                    return false;
                }
            }
        }
                
        //Inicia os itens vendidos
        
        //Vende os Produtos
        Set sPVP = pv.getPreVendaProdutos();
        Iterator it = sPVP.iterator();
        PreVendaProduto pvp;
        while(it.hasNext()) {
            pvp = (PreVendaProduto) it.next();
            SineticControleEstoque sce = pvp.getSineticControleEstoque();
            long cod = sce.getCodigoProdutoReferencia(); //@todo Mudar para código do produto e não código de referência
            String desc = Control.getDescricaoProduto(DBControl.getFirstProduto(cod));
            
            //@TODO aliquota
            float aliquota = 5.00f;
            
            rRet = pPrinter.vendeItem(cod, desc, 
                    aliquota, sce.getPafEcfUnidadeMedida().getSigla(), 
                    (float)pvp.getQuantidade(), 2, 
                    pvp.getValorInicial().floatValue()/pvp.getQuantidade(), 
                    String.valueOf(pvp.getPafEcfDescontoTipo().getSigla()), 
                    pvp.getDescontoValor().floatValue());
            if (rRet.isErro()) {
                //Tratar Erros pertinentes
                return false;
            }    
            
            //@todo #2222 Req 018
            
            //Se o item foi cancelado na pré-venda, cancela no cupom
            if (!pvp.isStatus()) {
                if (!cancelaItemAnterior()) {
                    return false;
                }
            }
            
            //Grava registro R05
            Registros.capturaR05Produto(pvp);
        }
        
        //Adiciona os Serviços
        Set sPVS = pv.getPreVendaServicos();
        it = sPVS.iterator();
        PreVendaServico pvs;
        while(it.hasNext()) {
            pvs = (PreVendaServico) it.next();
            Servico s = pvs.getServico();
            
            //@TODO aliquota
            float aliquota = ALIQUOTA_TESTE;
            
            rRet = pPrinter.vendeItem(s.getCodigo(), s.getDescricao(), 
                    aliquota, s.getPafEcfUnidadeMedida().getSigla(), 
                    1.0f, 2, 
                    pvs.getValorInicial().floatValue(), String.valueOf(pvs.getPafEcfDescontoTipo().getSigla()), 
                    pvs.getDescontoValor().floatValue());
            if (rRet.isErro()) {
                //Tratar Erros pertinentes
                return false;
            }  
            
            //@todo #2222 Req 018
            
            //Se o item foi cancelado na pré-venda, cancela no cupom
            if (!pvs.isStatus()) {
                if (!cancelaItemAnterior()) {
                    return false;
                }
            }
            
            //Grava registro R05
            Registros.capturaR05Servico(pvs);
        }
        
        
        
        return true;
    }
    
    private static boolean verificaAliquotas(PreVenda pv) {
        Set<PreVendaProduto> sPVP = pv.getPreVendaProdutos();
        Set<PreVendaServico> sPVS = pv.getPreVendaServicos();
        
        for (PreVendaProduto pvp: sPVP) {
            float aliquota = ALIQUOTA_TESTE;//@todo pvp.getAliquota;
            if (!verificarAliquotaCadastrada(aliquota, TRIB_ICMS)) {
                return false;
            }
        }
        
        
        for (PreVendaServico pvs: sPVS) {
            float aliquota = ALIQUOTA_TESTE;//@todo pvs.getAliquota;
            if (!verificarAliquotaCadastrada(aliquota, TRIB_ISS)) {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean verificarAliquotaCadastrada(float aliquota, int tributacao) {
        AliquotasImpressora ai = DBControl.getAliquota(aliquota, tributacao);
        
        if (ai == null) {
            if (!programaAliquota(aliquota, tributacao)) {
                return false;
            }
            
            try {
                DBControl.gravaAliquota(aliquota, tributacao);
            }
            catch (Exception e) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean cancelaItemAnterior() {
        rRet = pPrinter.cancelaItemAnterior();
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        return true;
    }
    
    
    public static boolean iniciaPagamento(float desconto, String tipo) {
        rRet = pPrinter.iniciaPagamento(desconto, tipo);
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        return true;
    }
    
    public static boolean efetuaPagamento(String meio, float valor, CrediarioFormaPagamentoDetalhado cfpd) {
        //Verifica se o pagamento é modo TEF
        //Se é, deve buscar autorização e imprimir o modo TEF
        if (cfpd.getTefApi() != null && !cfpd.getTefApi().isEmpty()) {
            rRet = pPrinter.iniciaModoTEF();
            if (rRet.isErro()) {
                //Tratar Erros pertinentes
                return false;
            }
            if (chamaAPITef(cfpd.getTefApi(), valor)) {
                rRet = pPrinter.finalizaModoTEF();
                    if (rRet.isErro()) {
                    //Tratar Erros pertinentes
                    return false;
                }
                    
                PrinterString ps = new PrinterString();
                rRet = pPrinter.numeroCupom(ps);
                if (rRet.isErro()) {
                    //Tratar Erros pertinentes
                    return false;
                }
                
                    
                rRet = pPrinter.abreComprovanteNaoFiscalVinculado(meio, valor, ps.buffer);
                if (rRet.isErro()) {
                    //Tratar Erros pertinentes
                    return false;
                }
                    
                rRet = pPrinter.usaComprovanteNaoFiscalVinculadoTEF("Pagamento utilizando TEF");
                if (rRet.isErro()) {
                    //Tratar Erros pertinentes
                    return false;
                }
                    
                rRet = pPrinter.fechaComprovanteNaoFiscalVinculado();
                if (rRet.isErro()) {
                    //Tratar Erros pertinentes
                    return false;
                }
                
                //Captura dados para R06
                Registros.capturaR06(Registros.R06_COMPROVANTE_NAO_FISCAL);
                
                //Captura dados para R07
                Registros.capturaR07(meio, cfpd, valor, false);
 
           }
        }
        
        
        rRet = pPrinter.registraPagamento(meio, valor);
        
        //Grava registro R07
        //Registros.capturaR07(meio, cfpd, valor, false);
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        return true;
    }
    
    public static boolean abrePagamentoNaoFiscal(PreVenda pv, CrediarioVendaParcelamento cvp) {
        rRet = pPrinter.abreRelatorioPagamentoNaoFiscal();
        if (rRet.isErro()) {
            return false;
        }
        return true;
    }
    
    public static boolean usaPagamentoNaoFiscal(String texto) {
        rRet = pPrinter.usaRelatorioPagamentoNaoFiscal(texto + "\n");
        if (rRet.isErro()) {
            return false;
        }
        return true;
    }
    
    public static boolean fechaPagamentoNaoFiscal() {
        rRet = pPrinter.fechaRelatorioPagamentoNaoFiscal();
        if (rRet.isErro()) {
            return false;
        }
        return true;
    }
    
    
    public static boolean relatorioMeiosDePagamento(List<PafMeiosPagamentoDia> lP) {
        PrinterInteger pi = new PrinterInteger();
        rRet = pPrinter.nomeiaRelatorioMeiosDePagamento(pi);
        if (rRet.isErro() || pi == null) {
            return false;
        }
        
        rRet = pPrinter.abreRelatorioMeiosPagamento(pi.number);
        
        if (rRet.isErro()) {
            return false;
        }

        for (PafMeiosPagamentoDia p: lP) {
            int idCodPagDet = p.getFkBrCodigoPagDetalhado();
            CrediarioFormaPagamento cfp = DBControl.getPagamentoMeio(idCodPagDet);
            rRet = pPrinter.usaRelatorioMeiosPagamento(cfp.getNome(), p.getTipoDocumento().getNome(), p.getValor().floatValue(), p.getDatahora());
        
            if (rRet.isErro()) {
                return false;
            }
        }
        
        rRet = pPrinter.fechaRelatorioMeiosPagamento();
        
        if (rRet.isErro()) {
            return false;
        }
        
        return true;
    }
    
    
    public boolean EstornoFormasPagamento(int tipoRetira, int tipoColoca, float valor) {
        /*rRet = pPrinter.EstornoFormasPagamento(tipoRetira, tipoColoca, valor);
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }*/
        return true;
    }
    
    /**
     * Fecha a venda e o cupom
     * 
     * @param msg
     * @return 
     */
    public static boolean fechaPagamentoCupom(PreVenda pv) {
        
        //@todo #2222 Req 018
        
        
        //Pega MD5
        String sMD5 = "MD5: " + Control.configuracoes.getMd5();
        //Pega Mensagem Promocional
        String sMP = Control.configuracoes.getMensagemPromocional();
        //Pega Numero da Pré-Venda
        String sPV = "PV: " + formataNumPreVenda(pv.getCodigo());
        
        
        
        rRet = pPrinter.fechaPagamentoCupom(sMD5 + "\n" + sPV + "\n\n" + sMP);
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        //Captura dados para registro R04
        Registros.capturaR04(pv);
        
        return true;
    }
    
    
    public static boolean cancelaCupom() {
        rRet = pPrinter.cancelaCupom();
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        return true;
    }
    
    /**
     * Emite uma Redução Z.
     * 
     * A Redução "Z" é, também, um documento fiscal emitido pelo ECF com 
     * informações idênticas às da Leitura "X", mas que importa, exclusivamente,
     * em zerar os totalizadores parciais. A redução Z deve ser emitida no 
     * encerramento diário das atividades do estabelecimento.
     */
    public static boolean reducaoZ() {
        rRet = pPrinter.reducaoZ();
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        PrinterLong pl = new PrinterLong();
        rRet = pPrinter.dataHoraUltimoDocumento(pl);
        if (rRet.isErro() || pl.number == null) {
            //Tratar Erros pertinentes
            return false;
        }
        
        try {
            DBControl.gravaReducaoZ(pl.number);
        }
        catch (Exception e) {
            return false;
        }
        
        //captura registro R02
        if (Registros.capturaR02() == null) {
            return false;
        }
        
        //captura registro R03
        //@todo pegar da tabela de aliquotas #2235
        /*if (Registros.capturaR03() == null) {
            return false;
        }*/
        
        
        return true;
    }
    
    /**
     * Emite uma Leitura X na Impressora fiscal
     * 
     * A Leitura "X" é o documento fiscal emitido pelo ECF que indica os valores
     * acumulados nos contadores e totalizadores, sem que sejam zerados ou 
     * diminuídos esses valores. 
     */
    public static boolean leituraX() {
        rRet = pPrinter.leituraX();
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        PrinterLong pl = new PrinterLong();
        rRet = pPrinter.dataHoraUltimoDocumento(pl);
        if (rRet.isErro() || pl.number == null) {
            //Tratar Erros pertinentes
            return false;
        }
        
        try {
            DBControl.gravaLeituraX(pl.number);
        }
        catch (Exception e) {
            return false;
        }
        
        return true;
    }
    
    
    
    
    /**
     * Emite um relatório gerencia de Identificação 
     * do PAF ECF (Req. XLIII, item 1 do Ato Cotepe)
     * 
     */
    public static boolean identificacaoPAFECF() {
        //Verifica se relatório gerencial já está programado
        EcfRelatoriosGerenciais eRG = DBControl.getRelatorioGerencial(RG_IDENT_PAFECF);
        String ig = "00"; //Indice Gerencial
        
        if (eRG == null) {
            //Não há relatório gerencial já programado
            //Programa relatório gerencial na impressora.
            //Comanda a impressão do relatório
            PrinterInteger pi = new PrinterInteger();
            rRet = pPrinter.NomeiaRelatorioIdentificacaoPAFECF(pi);
            if (rRet.isErro() || pi.number == null) {
                //Tratar Erros pertinentes
                return false;
            }
            
            ig = String.format("%02d", pi.number);
            eRG = new EcfRelatoriosGerenciais();
            eRG.setTipo(RG_IDENT_PAFECF);
            eRG.setIndiceGerencial(ig);
            try {
                DBControl.gravaRelatorioGerencial(eRG);
            }
            catch (Exception e) {
                return false;
            }
        }
        else {
            ig = eRG.getIndiceGerencial();
        }
        
        //Busca os dados de identificação
        EcfIdentificacao ecfID = DBControl.getEcfIdentificacao();
        if (ecfID == null) {
            return false;
        }
        
        String nl = ecfID.getLaudo().trim();
        String cnpj = ecfID.getShCnpj().trim();
        String rs = ecfID.getShRazaoSocial().trim();
        String end = ecfID.getShEndereco().trim();
        String tel = ecfID.getShTelefone().trim();
        String cont = ecfID.getShContato().trim();
        String nc = ecfID.getPafNomeComercial().trim();
        String ver = ecfID.getPafVersao().trim();
        String path = ecfID.getPafPrincipalExecutavel().trim();
        String md5 = "";
        String pathDA = "";
        String md5DA = "";
        String ns = "";

        //Md5 do principal executável
        List<EcfExecutaveis> ecfEx = DBControl.getEcfExecutaveis();
        EcfExecutaveis eePrincipal = null;
        for (EcfExecutaveis ee: ecfEx) {
            if (ee.getNome().trim().matches(path.trim())) {
                md5 = ee.getMd5().trim();
                eePrincipal = ee;
            }
        }
        if (eePrincipal != null) {
            ecfEx.remove(eePrincipal);
        }
        else {
            return false;
        }
                
        //Path e Md5 dos outros executáveis
        int i = 1;
        for (EcfExecutaveis ee: ecfEx) {
            pathDA += ee.getPath().trim()+ee.getNome().trim();
            md5DA += ee.getMd5().trim();
            if (i < ecfEx.size()) {
                pathDA +=",";
                md5DA +=",";
            }
            i++;
        }

        // Impressoras autorizadas
        ArrayList<String> lEcfs = ecfsFromINI();
        i = 1;
        for (String s: lEcfs) {
            ns += s.trim();
            if (i < lEcfs.size()) {
                ns +=",";
            }
            i++;
        }
                
        //Comanda a impressão do relatório
        rRet = pPrinter.identificacaoPAFECF(ig, nl, cnpj, rs, end, tel, cont, nc, 
                                            ver, path, md5, pathDA, md5DA, ns);
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        return true;
    }
    
    /**
     * Realiza uma operação de suprimento no caixa em dinheiro
     */
    public static boolean suprimento(float valor) {
        int sID;
        try {
            sID = DBControl.gravaSuprimento(valor, getDataImpressora().getTime());
        }
        catch (Exception e) {
            return false;
        }
        
        rRet = pPrinter.suprimento(valor);
        if (rRet.isErro()) {
            DBControl.removeSuprimento(sID);
            return false;
        }
        
        //@todo
        //Verificar retorno da impressora
        //Se não funcionar, remover do banco
        
        return true;
    }
    
    /**
     * Registra a abertura do caixa no banco.
     * Deve ser usado só em caso de sucesso de abertura e
     * independentemente de leitura x ou suprimento.
     * @param tm currentTimeMillis
     * @return 
     */
    /*public static boolean registraAberturaCaixa(long tm) {
        try {
            DBControl.gravaAberturaDeCaixa(tm);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }*/
    
    /**
     * Realiza uma operação de sangria no caixa em dinheiro
     */
    public static boolean sangria(float valor) {
        int sID;
        try {
            sID = DBControl.gravaSangria(valor, getDataImpressora().getTime());
        }
        catch (Exception e) {
            return false;
        }
        
        rRet = pPrinter.sangria(valor);
        if (rRet.isErro()) {
            DBControl.removeSangria(sID);
            return false;
        }
        
        //@todo
        //Verificar retorno da impressora
        //Se não funcionar, remover do banco
        
        return true;
    }
    
    /*public static boolean registraFechamentoCaixa(long tm) {
        try {
            DBControl.gravaFechamentoDeCaixa(tm);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }*/
    
    
    public static Date getDataHoraUltimoCupom() {
        //Se tudo OK, grava a Leitura X
        PrinterLong pl = new PrinterLong();
        rRet = pPrinter.dataHoraUltimoDocumento(pl);
        if (rRet.isErro() || pl.number == null) {
            //Tratar Erros pertinentes
            return null;
        }
        
        Date d = new Date(pl.number);
        return d;
    }
    
    /**
     * Cancela um item lançado na impressora fiscal.
     * Deve ser fornecido o número do item a ser cancelado.
     * @param itemNum Número do item a ser cancelado.
     */
    public static boolean cancelaItem(int itemNum) {
        try {
            rRet = pPrinter.cancelaItemGenerico(String.valueOf(itemNum));
        }
        catch (Exception e) {
            return false;
        }
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        return true;
    }
    
   
    /**
     * Faz uma leitura do mês atual da memória fiscal da impressora.
     * 
     * Quando e como deve ser feita a leitura da Memória Fiscal?
     * Deve ser feita no final de cada período de apuração, que é mensal.
     * 
     * 
     * @return 
     */
    public static boolean leituraMesAtualMemoriaFiscal() {
        
        //Cria um padrão para ser usado na impressora fiscal.
        //dd/MM/yyyy é um dos padrões aceitos
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = GregorianCalendar.getInstance();
        
        //Data de hoje
        Date date = new Date();
        cal.setTime(date);
        //Vai para o primeiro dia do mês
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        //Salva a data do primeiro dia do mês
        String dataAtual = dateFormat.format(cal.getTime());
        
        //Vai para o próximo mês
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
        //Volta um dia e vai para o último dia do mês anterior, ou seja, este mes.
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-1);
        
        //Salva a data do último dia do mês
        String fimDoMes = dateFormat.format(cal.getTime());
        
        rRet = pPrinter.memoriaFiscalPorData(dataAtual, fimDoMes, Printer.LMF_COMPLETA);
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        return true;
    }
    
    /**
     * Faz uma leitura do mês anterior da memória fiscal da impressora.
     * 
     * Quando e como deve ser feita a leitura da Memória Fiscal?
     * Deve ser feita no final de cada período de apuração, que é mensal.
     * 
     * 
     * @return 
     */
    public static boolean leituraMesAnteriorMemoriaFiscal() {
        
        //Cria um padrão para ser usado na impressora fiscal.
        //dd/MM/yyyy é um dos padrões aceitos
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = GregorianCalendar.getInstance();
        
        //Data de hoje
        Date date = new Date();
        cal.setTime(date);
        //Vai para o mês anterior
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)-1);
        //Vai para o primeiro dia do mês
        cal.set(Calendar.DAY_OF_MONTH, 1);
        
        //Salva a data do primeiro dia do mês
        String dataAtual = dateFormat.format(cal.getTime());
        
        //Vai para o próximo mês
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH)+1);
        //Volta um dia e vai para o último dia do mês anterior, ou seja, este mes.
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH)-1);
        
        //Salva a data do último dia do mês
        String fimDoMes = dateFormat.format(cal.getTime());
        
        rRet = pPrinter.memoriaFiscalPorData(dataAtual, fimDoMes, Printer.LMF_COMPLETA);
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        return true;
    }
    
    /**
     * Realiza a leitura da memória fiscal por data
     * @param dataInicio
     * @param dataFinal
     * @param tipo LMF_COMPLETA ou SIMPLIFICADA
     * @param geraEspelho
     * @return 
     */
    public static boolean lmfData(String dataInicio, String dataFinal, int tipo) {
        rRet = pPrinter.memoriaFiscalPorData(dataInicio, dataFinal, tipo);
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        return true;    
    }
    
    public static boolean lmfDataEspelho(String dataInicio, String dataFinal, int tipo) {
        //@todo carregar do banco se possível
        String sTipo = tipo==Printer.LMF_COMPLETA?"C":"S";
        String arquivo = System.getProperty("user.dir")+"\\MF_Espelho_Data_"+sTipo+".txt";
        try {
            rRet = pPrinter.memoriaFiscalPorDataEspelho(dataInicio, dataFinal, tipo, arquivo);
        }
        catch (IOException e) {
            return false;
        }
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        Controller.messageBox("Arquivo gravado com sucesso!\n"+arquivo);
        return true;  
    }
    
    public static boolean lmfDataArquivo(String dataInicio, String dataFinal) {
        //@todo carregar do banco se possível
        String arquivo = System.getProperty("user.dir")+"\\MF_ArquivoCotepe_Data.txt";
        try {
            rRet = pPrinter.memoriaFiscalPorDataArquivo(dataInicio, dataFinal, arquivo);
        }
        catch (IOException e) {
            return false;
        }
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        Controller.messageBox("Arquivo gravado com sucesso!\n"+arquivo);
        return true;  
    }
    
           
    public static boolean lmfReducao(int redInicio, int redFinal, int tipo) {
        rRet = pPrinter.memoriaFiscalPorReducao(redInicio, redFinal, tipo);
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        return true;    
    }
    
    public static boolean lmfReducaoEspelho(int redInicial, int redFinal, int tipo) {
        //@todo carregar do banco se possível
        String sTipo = tipo==Printer.LMF_COMPLETA?"C":"S";
        String arquivo = System.getProperty("user.dir")+"\\MF_Espelho_Reducao_"+sTipo+".txt";
        try {
            rRet = pPrinter.memoriaFiscalPorReducaoEspelho(redInicial, redFinal, tipo, arquivo);
        }
        catch (IOException e) {
            return false;
        }
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        Controller.messageBox("Arquivo gravado com sucesso!\n"+arquivo);
        return true;  
    }
    
    public static boolean lmfReducaoArquivo(int redInicial, int redFinal) {
        //@todo carregar do banco se possível
        String arquivo = System.getProperty("user.dir")+"\\MF_ArquivoCotepe_Reducao.txt";
        try {
            rRet = pPrinter.memoriaFiscalPorReducaoArquivo(redInicial, redFinal, arquivo);
        }
        catch (IOException e) {
            return false;
        }
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        Controller.messageBox("Arquivo gravado com sucesso!\n"+arquivo);
        return true;  
    }
    
    
    public static boolean mfdDataEspelho(String dataInicio, String dataFinal) {
        //@todo carregar do banco se possível
        String arquivo = System.getProperty("user.dir")+"\\MFD_Espelho_Data.txt";
        try {
            rRet = pPrinter.espelhoMFDData(arquivo, dataInicio, dataFinal);
        }
        catch (IOException e) {
            return false;
        }
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        Controller.messageBox("Arquivo gravado com sucesso!\n"+arquivo);
        return true;  
    }
    
    public static boolean mfdReducaoEspelho(int redInicial, int redFinal) {
        //@todo carregar do banco se possível
        String arquivo = System.getProperty("user.dir")+"\\MFD_Espelho_Reducao.txt";
        try {
            rRet = pPrinter.espelhoMFDReducao(arquivo, redInicial, redFinal);
        }
        catch (IOException e) {
            return false;
        }
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        Controller.messageBox("Arquivo gravado com sucesso!\n"+arquivo);
        return true;  
    }
    
    public static boolean mfdDataArquivo(String dataInicio, String dataFinal) {
        //@todo carregar do banco se possível
        String arquivo = System.getProperty("user.dir")+"\\MFD_ArquivoCotepe_Data.txt";
        boolean unicoArquivo = true;
        try {
            rRet = pPrinter.arquivoMFDData(arquivo, dataInicio, dataFinal, unicoArquivo);
        }
        catch (IOException e) {
            return false;
        }
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        Controller.messageBox("Arquivo gravado com sucesso!\n"+arquivo);
        return true;  
    }
    
    public static boolean mfdReducaoArquivo(int redInicial, int redFinal) {
        //@todo carregar do banco se possível
        String arquivo = System.getProperty("user.dir")+"\\MFD_ArquivoCotepe_Reducao.txt";
        boolean unicoArquivo = true;
        try {
            rRet = pPrinter.arquivoMFDReducao(arquivo, redInicial, redFinal, unicoArquivo);
        }
        catch (IOException e) {
            return false;
        }
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        
        Controller.messageBox("Arquivo gravado com sucesso!\n"+arquivo);
        return true;  
    }
    
    public static boolean arquivoConfiguracao(String arquivo) {
         ArrayList<String> arqConf = new ArrayList<>();
         
         arqConf.add("-------------------------------------------------------");
         arqConf.add("               PARAMETROS DE CONFIGURACAO");
         arqConf.add("-------------------------------------------------------");
         arqConf.add("            IDENTIFICACAO E CARACTERISTICAS");
         arqConf.add("             DO PROGRAMA APLICATIVO FISCAL");
         arqConf.add("");
         arqConf.add("TODAS AS PARAMETRIZACOES RELACIONADAS NESTE RELATORIO");
         arqConf.add("SAO DE CONFIGURACAO INACESSIVEL AO USUARIO DO PAF-ECF");
         arqConf.add("NÃO É DOCUMENTO FISCAL");
         arqConf.add("");
         arqConf.add("A ATIVACAO OU NAO DESTES PARAMETROS E DETERMINADA PELA");
         arqConf.add("UNIDADE FEDERADA E SOMENTE PODE SER FEITA PELA INTERV-");
         arqConf.add("ENCAO DA EMPRESA DESENVOLVEDORA DO PAF ECF");
         arqConf.add("");
         arqConf.add("-------------------------------------------------------");
         arqConf.add("");
         arqConf.add("Nome Comercial.....................: "+ecfIdentificacao.getPafNomeComercial());
         arqConf.add("Versao.............................: "+ecfIdentificacao.getPafVersao());
         arqConf.add("Linguagem de Programação...........: "+ecfIdentificacao.getLinguagemProgramacao());
         arqConf.add("Gerenciador de Banco de Dados......:" +ecfIdentificacao.getBancoDeDados() );
         arqConf.add("-------------------------------------------------------");
         arqConf.add("                    FUNCIONALIDADES");
         arqConf.add("-------------------------------------------------------");
         arqConf.add("Tipo de Funcionamento..............: Em rede");
         arqConf.add("Tipo de Desenvolvimento............: Exclusivo Terceirizado");
         arqConf.add("Integracao do PAF-ECF..............: Retaguarda");
         arqConf.add("-------------------------------------------------------");
         arqConf.add("          PARAMETROS PARA NAO CONCOMITANCIA");
         arqConf.add("-------------------------------------------------------");
         arqConf.add("Pre-Venda - {Parametrizavel}......................:SIM");
         arqConf.add("DAV Por ECF - {Parametrizavel}....................:NAO");
         arqConf.add("DAV Imp.Nao Fiscal - {Parametrizavel}.............:NAO");
         arqConf.add("DAV-OS - {Parametrizavel}.........................:NAO");
         arqConf.add("");
         arqConf.add("-------------------------------------------------------");
         arqConf.add("            A CRITERIO DA UNIDADE FEDERADA");
         arqConf.add("-------------------------------------------------------");
         arqConf.add("");
         arqConf.add("REQUISITO IV Item 2");
         arqConf.add("REALIZAR REGISTRO DE PRE VENDA....................:SIM");
         arqConf.add("");
         arqConf.add("REQUISITO IV Item 3");
         arqConf.add("EMITIR DAV IMPRESSO EM EQUIPAMENTO NAO FISCAL ....:NAO");
         arqConf.add("");
         arqConf.add("REQUISITO IV Item 4");
         arqConf.add("EMITIR DAV IMPRESSO NO ECF");
         arqConf.add("COMO RELATORIO GERENCIAL..........................:NAO");
         arqConf.add("");
         arqConf.add("REQUISITO VIII-A Item 2");
         arqConf.add("MINAS LEGAL.........................................:NAO");
         arqConf.add("");
         arqConf.add("REQUISITO VIII-A Item 2A");
         arqConf.add("CUPOM MANIA.......................................:NAO");
         arqConf.add("");
         arqConf.add("REQUISITO VIII-A Item 2B");
         arqConf.add("NOTA LEGAL..........................................:NAO");
         arqConf.add("");
         arqConf.add("REQUISITO XVIII Item 1 Alinea a");
         arqConf.add("CONSULTA DE PRECOS - TOTALIZACAO DOS");
         arqConf.add("VALORES DA LISTA DE ITENS.........................:NAO");
         arqConf.add("");
         arqConf.add("REQUISITO XVIII Item 1 Alinea b");
         arqConf.add("CONSULTA DE PRECOS - TRANSFORMACAO DAS INFORMACOES");
         arqConf.add("DIGITADAS EM REGISTROS DE PRE VENDA...............:NAO");
         arqConf.add("");
         arqConf.add("REQUISITO XVIII Item 1 Alinea c");
         arqConf.add("CONSULTA DE PRECOS - TRANSFORMACAO DAS INFORMACOES");
         arqConf.add("DIGITADAS EM REGISTROS DE DAV.....................:NAO");
         arqConf.add("");
         arqConf.add("REQUISITO XXII Item 7 Alinea b");
         arqConf.add("NAO COINCIDENCIA DO GT - RECOMPOSICAO");
         arqConf.add("DO GT NO ARQUIVO AUXILIAR CRIPTOGRAFADO");
         arqConf.add("NO CASO DE INCREMENTO NO CRO......................:SIM");
         arqConf.add("");
         arqConf.add("REQUISITO XXXVI - A Item 1");
         arqConf.add("POSTO DE COMBUSTIVEL - IMPEDE REGISTRO DE VENDA OU");
         arqConf.add("EMISSAO DE CUPOM FISCAL QUANDO DETECTAR");
         arqConf.add("ESTOQUE ZERO OU NEGATIVO..........................:NAO");
         arqConf.add("");
         arqConf.add("REQUISITO XXXIX - Item 1");
         arqConf.add("BAR, RESTAURANTE E SIMILAR - IMPRESSORA NAO FISCAL");
         arqConf.add("INSTALADA NOS AMBIENTES DE PRODUCAO...............:NAO");
         arqConf.add("");
        
         //Grava arquivo
         File file = new File(arquivo);
         if (file.exists()) {
             file.delete();
         }
         file = new File(arquivo);
         file.setWritable(true);
         
         FileWriter fw = null;
         PrintWriter pw = null;
         try {
            fw = new FileWriter(file);
            pw = new PrintWriter(fw);
            
            for (String s: arqConf) {
                pw.print(s);
            }
         }
         catch (Exception e) {
             return false;
         }
         finally {
             if (pw != null) {
                pw.close();
             }
         }
         
         return true;
    }
    
    /**
     * Programa a alíquota vigente na impressora.
     * @param aliq Valor da alíquota
     * @param tipo Tipo da Alíquota: TRIB_ISS, TRIB_ICMS, etc..
     * @return 
     */
    public static boolean programaAliquota(float aliq, int tipo) {
        rRet = pPrinter.programaAliquota(aliq, tipo);
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        return true;
        
    }
    
    
    
    public static boolean programaFormaDePagamento(String descricao, boolean tef) {
        rRet = pPrinter.programaFormaDePagamento(descricao, tef);
        
        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return false;
        }
        return true;
    }
    
    
    
    ///LISTA DE VENDAS
    
    //Atualiza a listagem de vendas da lista
    public static boolean atualizarVendas () {
        
        //PADRAO
        //Pega as vendas do banco
            
        //Formata as vendas no formato de Objeto
        
        //Compara com as vendas ja existentes na lista
        
        //Atualiza as vendas
        //TERMINA PADRAO
        
        List<PreVenda> lPV = DBControl.getPreVendasAbertas();
        
        ldv = new ListaDeVendas();
        
        for (PreVenda pv: lPV) {
            ldv.addItem(pv.getCodigo(), pv.getUsuario().getNome(), String.valueOf(pv.getValorFinal()));
        }
        
        /*
        //PARA FINS DE TESTE
        //Cria lista de venda de TESTE
        //@todo, deve buscar do banco da Gouveia a lista de pré-vendas
        if (!criou) {
            Vendas v1 = ldv.addItem(153234, "Vendedor 1", "Lente x, Óculos x");
            Vendas v2 = ldv.addItem(516854, "Vendedora 2", "Relógio Technos, Óculos Mormaii, Len....");


            //Teste cria lista de TESTE para venda de Itens
            ItensVendidos ivL = new ItensVendidos(v1);
            ivL.addItem(124, "Lente x", 5f, Printer.QNT_INTEIRO, 2f, 2, 10.50f, Printer.DESC_DINHEIRO, 1f);
            ivL.addItem(123, "Óculos", 5f, Printer.QNT_INTEIRO, 1f, 2, 50f, Printer.DESC_DINHEIRO, 0f);
            iv.add(ivL);
            
            //Teste cria lista de TESTE para venda de Itens
            ivL = new ItensVendidos(v2);
            ivL.addItem(124, "Lente 2", 5f, Printer.QNT_INTEIRO, 2f, 2, 10.50f, Printer.DESC_DINHEIRO, 1f);
            ivL.addItem(123, "Óculos Da Xuxa", 5f, Printer.QNT_INTEIRO, 1f, 2, 50f, Printer.DESC_DINHEIRO, 0f);
            iv.add(ivL);
            
            criou = true;
        }*/
        return true;
    }
    
        /**
     * Busca no banco de dados a venda
     * @param codVenda Codigo da Venda a ser buscada no banco intermediário
     * @return 
     */
    public static ItensVendidos loadVenda(long codVenda) {
        
        //PARA FINS DE TESTE
        //@todo deve buscar na lista de vendas os itens com o codVenda
        for (ItensVendidos i: iv) {
            if (i.getVendaCodigo() == codVenda) {
                return i;
            }
        }
        return null;
    }
    
    public static ListaDeVendas getListaVendas() {
        //TESTE
        //@todo deve buscar do banco e pegar a lista mais atualizada
        return ldv;
    }
    
    public static boolean cancelaPreVenda(PreVenda pv) {
        if (pv == null) {
            return false;
        }
        
        //Rotina de cancelamento de Pré-venda
        //O cupom deve ser aberto e em seguida cancelado.
        
        boolean progressoOriginal = pv.isEmProgresso();
        //Captura a pré-venda
        if (!setPVProgresso(true, pv)) {
            return false;
        }
        
        //Verifica se a Pré-venda foi gerada vazia
        if (pv.getPreVendaProdutos().size() <= 0 &&
            pv.getPreVendaServicos().size() <= 0) {
            return auxCancelaPreVenda(pv);
        }

        
        //Pega o primeiro pagamento disponível
        String sMeio = DBControl.getPagamentoMeios().get(0).getNome().trim();
        System.out.println("sMeio: " + sMeio);
        //Verifica se e valido
        if (sMeio == null || sMeio.isEmpty()) {
            Controller.messageBox("Não há meios de pagamentos cadastrados!");
            setPVProgresso(progressoOriginal, pv);
            return false;
        }
        
        
        //Emite cupom com a pré-venda
        System.out.println("Inicia a venda!");
        if (!iniciaVenda(pv, null)) {
            setPVProgresso(progressoOriginal, pv);
            return false;
        }
        
        System.out.println("Inicia o pagamento: " + pv.getDescontoValor().floatValue() + ":" + String.valueOf(pv.getPafEcfDescontoTipo().getSigla()));
        if (!Control.iniciaPagamento(pv.getDescontoValor().floatValue(), String.valueOf(pv.getPafEcfDescontoTipo().getSigla()))) {
            setPVProgresso(progressoOriginal, pv);
            return false;
        }
        
        System.out.println("Efetua o pagamento: " + pv.getValorFinal().floatValue());
        if (!efetuaPagamento(sMeio, pv.getValorFinal().floatValue(), null)) {
            setPVProgresso(progressoOriginal, pv);
            return false;
        }
        
        System.out.println("Fecha cupom");
        if (!fechaPagamentoCupom(pv)) {
            setPVProgresso(progressoOriginal, pv);
            return false;
        }
        
        System.out.println("Cancela o último cupom");
        //Cancela o último cupom
        if (!cancelaCupom()) {
            setPVProgresso(progressoOriginal, pv);
            return false;
        }
        
        
        //Atualiza o GrandeTotal da impressora
        atualizaGrandeTotal();
        
        pv = DBControl.getPreVenda(pv.getCodigo());
        if (pv == null) {
    
            Controller.messageBox("Erro grave ao capturar pré-venda do banco de dados central. \n"
                                + "A pré-venda não foi cancelada no banco de dados.");
            return false;
            
        }
        
        if (!auxCancelaPreVenda(pv)) return false;
        
        return true;
    }
    
    private static boolean auxCancelaPreVenda(PreVenda pv) {
        pv.setEmProgresso(false);
        pv.setCancelada(true);
        pv.setFinalizada(true);
        //Grava no banco a pré-venda cancelada
        return DBControl.atualizaPreVenda(pv);
    }
    
    private static boolean setPVProgresso(boolean p, PreVenda pv) {
        pv.setEmProgresso(p);
        return DBControl.atualizaPreVenda(pv);
    }
    
    
    /////////////// Método que chama o TEF ///////////////////
    private static boolean chamaAPITef(String tefAPI, float valor) {
        //@todo fazer
        return true;
    }
    
    
    /////Métodos de formatação
    private static String formataNumPreVenda(int cod) {
        return String.format("%010d", cod);
    }
    
    public static String getDescricaoProduto(Produtos p) {
        String et1 = p.getEtiqueta1();
        String et2 = p.getEtiqueta2();
        return (et1.isEmpty()?"":(et1 + " / ")) + (et2.isEmpty()?"":et2);
    }
    
   public static Date getDataImpressora(){
        return pPrinter.getDataImpressora();
    }
   
   public static boolean gravarProduto(PafProduct pafProduct) {
       try{
            String hash = HashUtil.generateHashDigestMD5(pafProduct.toString());
            pafProduct.setHash(hash);
            DBControl.gravaGenerico(pafProduct);
       }catch(Exception e){
           return Boolean.FALSE;
       }
       return Boolean.TRUE;
   }
   
   public static PafProduct getProductByGtin(String gtin){
       try{
           return DBControl.getPafProduct(gtin);
       }catch(Exception e){
           return null;
       }
   }
   
    public static boolean vendeItem(PafProduct pafProduct, int qtd) {
        float aliquota = ALIQUOTA_TESTE;

        rRet = pPrinter.vendeItem(Integer.valueOf(pafProduct.getGtin()), pafProduct.getDescricao(),
                aliquota, pafProduct.getUnidadeMedida(),
                (float) qtd, 2,
                pafProduct.getValor().floatValue(),
                "D",
                (float) 0);

        if (rRet.isErro()) {
            //Tratar Erros pertinentes
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
   
}