package br.com.sinetic.pafecftef.printer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Administrador
 */
public interface Printer {
    
    public static final int TRIB_ISS = 1;
    public static final int TRIB_ICMS = 2;
    
    public static final String QNT_FRACAO = "F";
    public static final String QNT_INTEIRO = "UN";
    
    public static final String DESC_PERCENTUAL = "P";
    public static final String DESC_DINHEIRO = "D";

    public static final int LMF_COMPLETA = 0;
    public static final int LMF_SIMPLIFICADA = 1;
    
    
    public int analisaiRetorno();

    public int retornoImpressora();

    public Retorno leituraX();

    public Retorno reducaoZ();

    public Retorno abreCupom();
    
    public Retorno abreCupom(String cpfCnpj);
    
    public Retorno abreCupom(String cpfCnpj, String nome, String endereco);

    //Talvez pode ser retirado
    public Retorno fechaCupom(String acrescimoDesconto,
                    String tipoAcrescimoDesconto, String valorAcrescimoDesconto,
                    String mensagem, ArrayList<FormasPagamento> formasPagamento);


    public Retorno setarPortaSerial(String com);
    
    public Retorno abrirPortaSerial();

    public Retorno fecharPortaSerial();

    public Retorno horarioVerao();

    public Retorno memoriaFiscalPorData(String data1, String data2, int tipo);

    public Retorno memoriaFiscalPorReducao(int red1, int red2, int tipo);
    
    public Retorno memoriaFiscalPorDataEspelho(String data1, String data2, int tipo, String arquivo) throws IOException;

    public Retorno memoriaFiscalPorReducaoEspelho(int red1, int red2, int tipo, String arquivo) throws IOException;
    
    public Retorno memoriaFiscalPorDataArquivo(String data1, String data2, String arquivo) throws IOException;

    public Retorno memoriaFiscalPorReducaoArquivo(int red1, int red2, String arquivo) throws IOException;
    
    public Retorno espelhoMFDData(String arquivo, String dataInicial, String dataFinal) throws IOException;
    
    public Retorno espelhoMFDReducao(String arquivo, int redInicial, int redFinal) throws IOException;
    
    public Retorno arquivoMFDData( String arquivo, String dataInicial, String dataFinal, boolean unicoArquivo )throws IOException;
    
    public Retorno arquivoMFDReducao( String arquivo, int redInicial, int redFinal, boolean unicoArquivo )throws IOException;

    public Retorno cancelaCupom();

    public Retorno vendeItem(long codigo, String descricao, float aliquota, 
                        String tipoQnt, float quantidade, int casasDecimais, 
                        float preco, String tipoDesconto, float vlrDesconto);

    public Retorno cancelaItemAnterior();

    public Retorno cancelaItemGenerico(String item);

    /**
     * @param aliquota Aliquota a ser setada.
     * @param tipo Tipo do tributo (ICMS, ISS, ...)
     * @return 
     */
    public Retorno programaAliquota(float aliquota, int tipo);

    public Retorno programaFormaDePagamento(String descricao, boolean tef);
    
    public Retorno iniciaModoTEF();
    
    public Retorno finalizaModoTEF();
    
    public Retorno abreComprovanteNaoFiscalVinculado(String meioPag, float valor, String numCupom);
    
    public Retorno usaComprovanteNaoFiscalVinculadoTEF(String texto);
    
    public Retorno fechaComprovanteNaoFiscalVinculado();
    
    public Retorno iniciaPagamento(float desconto, String tipoDesc);
    
    public Retorno registraPagamento(String meio, float valor);
    
    public Retorno fechaPagamentoCupom(String msg);
    
    public Retorno suprimento(float valor);
    
    public Retorno sangria(float valor);
    
    public Retorno aberturaDoDia(float valor);
    
    public Retorno fechamentoDoDia();
    
    public Retorno EstornoFormasPagamento(String meioRetira, String meioColoca, float valor);
    
    public Retorno grandeTotal(PrinterString psGT);
    
    public Retorno NumeroSerie(PrinterString psNS);
    
    public Retorno modeloImpressora(PrinterString psMI);
    
    public Retorno dadosUltimaReducao(PrinterString ps);
    
    public Retorno vendaBruta(PrinterFloat pf);
    
    public Retorno flagsFiscais(PrinterInteger pi);
    
    public Retorno numeroSubstituicoesProprietario(PrinterString ps);
    
    public Retorno numeroCupom(PrinterString ps);
    
    public Retorno contadorCupomFiscal(PrinterString ps);
    
    public Retorno ultimoItemVendido(PrinterInteger pi);
    
    public Retorno verificaTruncamento(PrinterString ps);
    
    public Retorno numeroOperacoesNaoFiscais(PrinterString ps);
    
    public Retorno contadorRelatoriosGerenciais(PrinterString ps);
    
    public Retorno contadorComprovantesCredito(PrinterString ps);
    
    public Retorno contadoresTotalizadoresNaoFiscais(PrinterString ps);
    
    public Retorno mfAdicional(PrinterString psMA);
    
    public Retorno identificacaoPAFECF(String ig, String nl, String cnpj, String rs, String end, 
                                       String tel, String cont, String nc, String ver, String path, 
                                       String md5, String pathDA, String md5DA, String ns);
    
    public Retorno NomeiaRelatorioIdentificacaoPAFECF(PrinterInteger ret);
    
    public Retorno dataHoraUltimoDocumento(PrinterLong pl);
    
    public Retorno subTotal(PrinterFloat pf);
    
    public Retorno verificaImpressoraLigada();
    
    public Retorno abreRelatorioMeiosPagamento(int indice);
    
    public Retorno usaRelatorioMeiosPagamento(String meio, String tipoDoc, float valorAcumulado, Date data);
    
    public Retorno fechaRelatorioMeiosPagamento();
    
    public Retorno nomeiaRelatorioMeiosDePagamento(PrinterInteger ret);
    
    public Retorno nomeiaRelatorioPagamentoNaoFiscal(PrinterInteger ret);
    
    public Retorno abreRelatorioPagamentoNaoFiscal();
    
    public Retorno usaRelatorioPagamentoNaoFiscal(String texto);
    
    public Retorno fechaRelatorioPagamentoNaoFiscal();
    
    public Date getDataImpressora();
    
}
