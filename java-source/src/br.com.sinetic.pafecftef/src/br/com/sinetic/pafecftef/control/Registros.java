/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.control;

import static br.com.sinetic.pafecftef.control.Control.ecfIdentificacao;
import br.com.sinetic.pafecftef.control.assinaturadigital.AssinaturaDigital;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.entities.EcfExecutaveis;
import br.com.sinetic.pafecftef.entities.PafRegR02;
import br.com.sinetic.pafecftef.entities.PafRegR03;
import br.com.sinetic.pafecftef.entities.PafRegR04;
import br.com.sinetic.pafecftef.entities.PafRegR05;
import br.com.sinetic.pafecftef.entities.PafRegR06;
import br.com.sinetic.pafecftef.entities.PafRegR07;
import br.com.sinetic.pafecftef.printer.Printer;
import br.com.sinetic.pafecftef.printer.PrinterFloat;
import br.com.sinetic.pafecftef.printer.PrinterInteger;
import br.com.sinetic.pafecftef.printer.PrinterLong;
import br.com.sinetic.pafecftef.printer.PrinterString;
import br.com.sinetic.pafecftef.printer.Retorno;
import br.com.sinetic.pafecftef.remoteentities.CrediarioCliente;
import br.com.sinetic.pafecftef.remoteentities.CrediarioFormaPagamentoDetalhado;
import br.com.sinetic.pafecftef.remoteentities.CrediarioVendaPagamento;
import br.com.sinetic.pafecftef.remoteentities.PreVenda;
import br.com.sinetic.pafecftef.remoteentities.PreVendaProduto;
import br.com.sinetic.pafecftef.remoteentities.PreVendaServico;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Windows8
 */
public class Registros {
    
    public static final String NOME_ARQ_MD5 = "ArqMD5.txt";
    public static final String NOME_ARQ_ESTOQUE = "ArqEstoque.txt";    
    public static final String NOME_ARQ_TAB_MERC_SERV = "ArqTabMercServ.txt";    
    public static final String NOME_ARQ_TROCO_CARTAO = "ArqTrocoCartao.txt";    
    
    public static final String R06_RELATORIO_GERENCIAL = "RG";
    public static final String R06_CONFERENCIA_MESA = "CM";
    public static final String R06_REGISTRO_VENDA = "RV";
    public static final String R06_COMPROVANTE_CRED_DEB = "CC";
    public static final String R06_COMPROVANTE_NAO_FISCAL = "CN";
    public static final String R06_COMPROVANTE_NAO_FISCAL_CANCEL = "NC";
    
    
    
    ////CAPTURA REGISTROS ////
    public static PafRegR02 capturaR02() {
        Printer pPrinter = Control.getPrinter();
        if (pPrinter == null) {
            return null;
        }
        
        Retorno ret;        
        PrinterString ps = new PrinterString();
        PrinterLong pl = new PrinterLong();
        PrinterFloat pf = new PrinterFloat();
        PrinterInteger pi = new PrinterInteger();
        
        //C02
        ret = pPrinter.NumeroSerie(ps);
        if (ret.isErro()) return null;
        String c02 = ps.buffer;
        
        //C03
        //@todo ret = pPrinter.mfAdicional(ps);
        //@todo if (ret.isErro()) return null;
        String c03 = "0";//ps.buffer;
        
        //C04
        ret = pPrinter.modeloImpressora(ps);
        if (ret.isErro()) return null;
        String c04 = ps.buffer;
        
        //C05
        ret = pPrinter.numeroSubstituicoesProprietario(ps);
        if (ret.isErro()) return null;
        String sC05 = ps.buffer;
        Integer c05;
        try {
             c05 = Integer.getInteger(sC05);
        }
        catch (Exception e) {
            return null;
        }
        
        //Prep
        ret = pPrinter.dadosUltimaReducao(ps);
        if (ret.isErro()) return null;
        String[] sDur = ps.buffer.split(",");
        
        //C06
        String sC06 = sDur[2];
        Integer c06;
        try {
             c06 = Integer.getInteger(sC06);
        }
        catch (Exception e) {
            return null;
        }        
        
        //C07
        String sC07 = sDur[3];
        Integer c07;
        try {
             c07 = Integer.getInteger(sC07);
        }
        catch (Exception e) {
            return null;
        }
        
        //C08
        String sC08 = sDur[1];
        Integer c08;
        try {
             c08 = Integer.getInteger(sC08);
        }
        catch (Exception e) {
            return null;
        }
        
        //C09
        String sC09 = sDur[sDur.length-1];
        SimpleDateFormat formatador = new SimpleDateFormat("ddMMyy");
        Date c09;
        try {
            c09 = formatador.parse(sC09);
        }
        catch (Exception e) {
            return null;
        }
        
        
        //C10 e C11
        ret = pPrinter.dataHoraUltimoDocumento(pl);
        if (ret.isErro()) return null;
        Long lC10c11 = pl.number;
        Date c10c11 = new Date(lC10c11);
        
        //C12
        ret = pPrinter.vendaBruta(pf);
        if (ret.isErro()) return null;
        Float c12 = pf.number;
        
        //C13
        ret = pPrinter.flagsFiscais(pi);
        if (ret.isErro()) return null;
        Integer iC13 = pi.number;
        String c13 = String.valueOf(iC13);
        
        
        PafRegR02 p = new PafRegR02();
        p.setNumFabricacao(c02);
        p.setMfAdicional(c03.charAt(0));
        p.setModeloEcf(c04);
        p.setNumeroUsuario(c05);
        p.setCrz(c06);
        p.setCoo(c07);
        p.setCro(c08);
        p.setDataMovimento(c09);
        p.setDataEmissao(c10c11);
        p.setVendaBrutaDiaria(new BigDecimal(c12));
        p.setParametroDesconto(c13.charAt(0));

        try {
            DBControl.gravaRegR02(p);
        }
        catch (Exception e) {
            return null;
        }
        
        return p;
    }
    
    public static List<PafRegR03> capturaR03() {
        Printer pPrinter = Control.getPrinter();
        if (pPrinter == null) {
            return null;
        }
        
        //Prep
        PrinterString ps = new PrinterString();
        Retorno ret;   
        ret = pPrinter.dadosUltimaReducao(ps);
        if (ret.isErro()) return null;
        String[] sDur = ps.buffer.split(",");
        String s = sDur[16];
        
        int TAM_ALIQ = 14;
        int TOTAL_ALIQ = 16;
        List<PafRegR03> list = new ArrayList<>();
        for (int i = 0; i < TOTAL_ALIQ; i++) {
            String sAliq = s.substring(i, i+TAM_ALIQ);
            float a = Float.parseFloat(sAliq)/100;
            PafRegR03 p = capturaR03Detalhe(String.valueOf(i), a);
            if (p == null) {
                return null;
            }
            list.add(p);
        }
        
        return list;
    }
    
    public static PafRegR03 capturaR03Detalhe(String totalizador, float valor) {
        Printer pPrinter = Control.getPrinter();
        if (pPrinter == null) {
            return null;
        }
        
        Retorno ret;        
        PrinterString ps = new PrinterString();
        PrinterLong pl = new PrinterLong();
        PrinterFloat pf = new PrinterFloat();
        PrinterInteger pi = new PrinterInteger();
        
        //C02
        ret = pPrinter.NumeroSerie(ps);
        if (ret.isErro()) return null;
        String c02 = ps.buffer;
        
        //C03
        //@todo ret = pPrinter.mfAdicional(ps);
        //@todo if (ret.isErro()) return null;
        String c03 = "0";//ps.buffer;
        
        //C04
        ret = pPrinter.modeloImpressora(ps);
        if (ret.isErro()) return null;
        String c04 = ps.buffer;
        
        //C05
        ret = pPrinter.numeroSubstituicoesProprietario(ps);
        if (ret.isErro()) return null;
        String sC05 = ps.buffer;
        Integer c05;
        try {
             c05 = Integer.getInteger(sC05);
        }
        catch (Exception e) {
            return null;
        }
        
        //Prep
        ret = pPrinter.dadosUltimaReducao(ps);
        if (ret.isErro()) return null;
        String[] sDur = ps.buffer.split(",");
        
        //C06
        String sC06 = sDur[2];
        Integer c06;
        try {
             c06 = Integer.getInteger(sC06);
        }
        catch (Exception e) {
            return null;
        }   
        
        //C07
        String c07 = totalizador;
        
        //C08
        Float c08 = valor;
        
        
        PafRegR03 p = new PafRegR03();
        p.setNumeroFabricacao(c02);
        p.setMfAdicional(c03.charAt(0));
        p.setModeloEcf(c04);
        p.setNumeroUsuario(c05);
        p.setCrz(c06);
        p.setTotalizadorParcial(c07);
        p.setValor(new BigDecimal(c08));

        try {
            DBControl.gravaRegR03(p);
        }
        catch (Exception e) {
            return null;
        }
        
        return p;
    }
    
    public static PafRegR04 capturaR04(PreVenda pv) {
        
        
        float descontosobSubTotal = pv.getDescontoValor().floatValue();
        String indicadorTipoDesconto = "V";
        float acrescimoSubtotal = 0;
        String tipoAcrescimoSubTotal = "V";
        float totalLiquido = pv.getValorFinal().floatValue();
        boolean cancelamento = pv.isCancelada();
        float cancelAcrescimo = 0;
        String ordemDesconto = "D";
        CrediarioCliente cliente = pv.getCrediarioCliente();
        
        
        Printer pPrinter = Control.getPrinter();
        if (pPrinter == null) {
            return null;
        }
        
        Retorno ret;        
        PrinterString ps = new PrinterString();
        PrinterLong pl = new PrinterLong();
        PrinterFloat pf = new PrinterFloat();
        PrinterInteger pi = new PrinterInteger();
        
        //C02
        ret = pPrinter.NumeroSerie(ps);
        if (ret.isErro()) return null;
        String c02 = ps.buffer;
        
        //C03
        //@todo ret = pPrinter.mfAdicional(ps);
        //@todo if (ret.isErro()) return null;
        String c03 = "0";//ps.buffer;
        
        //C04
        ret = pPrinter.modeloImpressora(ps);
        if (ret.isErro()) return null;
        String c04 = ps.buffer;
        
        //C05
        ret = pPrinter.numeroSubstituicoesProprietario(ps);
        if (ret.isErro()) return null;
        String c05 = ps.buffer;
        
        //C06
        ret = pPrinter.contadorCupomFiscal(ps);
        if (ret.isErro()) return null;
        String sC06 = ps.buffer;
        int c06;
        try {
             c06 = Integer.getInteger(sC06);
        }
        catch (Exception e) {
            return null;
        }  
        
        //Prep
        ret = pPrinter.dadosUltimaReducao(ps);
        if (ret.isErro()) return null;
        String[] sDur = ps.buffer.split(",");
        
        //C07
        String sC07 = sDur[3];
        Integer c07;
        try {
             c07 = Integer.getInteger(sC07);
        }
        catch (Exception e) {
            return null;
        }
        
        //C08 - Data da Emissão
        ret = pPrinter.dataHoraUltimoDocumento(pl);
        if (ret.isErro()) return null;
        long c08 = pl.number;
        
        //C09 - Subtotal
        ret = pPrinter.subTotal(pf);
        if (ret.isErro()) return null;
        float c09 = pf.number;
        
        //C10 - Desconto sobre o Subtotal
        float c10 = descontosobSubTotal;
        
        //c11 - Indicador de Tipo de Desconto
        char c11 = indicadorTipoDesconto.charAt(0);
        
        //C12 - Acrescimo Subtotal
        float c12 = acrescimoSubtotal;
        
        //C13 - Tipo de Acrescimo Subtotal
        char c13 = tipoAcrescimoSubTotal.charAt(0);
        
        //C14 - Valor total Liquido
        float c14 = totalLiquido;
        
        //C15 - Cancelamento
        boolean c15 = cancelamento;
        
        //C16 - Cancelamento sobre acréscimo
        float c16 = cancelAcrescimo;
        
        //C17 - Ordem de Desconto e acréscimo
        char c17 = ordemDesconto.charAt(0);
        
        //C18 - Nome do Cliente
        String c18 = cliente.getNome();
        
        //C19
        String c19 = cliente.getCpf();
        
        PafRegR04 p = new PafRegR04();
        p.setNumeroFabEcf(c02);
        p.setMfAdicional(c03.charAt(0));
        p.setModeloEcf(c04);
        p.setNumeroUsuario(c05);
        p.setCcfCvcCbp(c06);
        p.setCoo(c07);
        p.setDataInicio(new Date(c08));
        p.setSubtotal(c09);
        p.setDescontoSubtotal(c10);
        p.setTipoDescontoSubtotal(c11);
        p.setAcrescimoSubtotal(c12);
        p.setTipoAcrescimoSubtotal(c13);
        p.setTotalLiquido(c14);
        p.setIndicadorCancelamento(c15);
        p.setCancelamentoAcrescimoSubtotal(c16);
        p.setOrdemAplicacaoDescAcresc(c17);
        p.setNomeCliente(c18);
        p.setCpfCnpj(c19);
        
        

        try {
            DBControl.gravaRegR04(p);
        }
        catch (Exception e) {
            return null;
        }
        
        return p;
    }
    
    public static PafRegR05 capturaR05Produto(PreVendaProduto pvp) {
        Printer pPrinter = Control.getPrinter();
        if (pPrinter == null) {
            return null;
        }
        
        Retorno ret;        
        PrinterString ps = new PrinterString();
        PrinterLong pl = new PrinterLong();
        PrinterFloat pf = new PrinterFloat();
        PrinterInteger pi = new PrinterInteger();
        
        //C02
        ret = pPrinter.NumeroSerie(ps);
        if (ret.isErro()) return null;
        String c02 = ps.buffer;
        
        //C03
        //@todo ret = pPrinter.mfAdicional(ps);
        //@todo if (ret.isErro()) return null;
        String c03 = "0";//ps.buffer;
        
        //C04
        ret = pPrinter.modeloImpressora(ps);
        if (ret.isErro()) return null;
        String c04 = ps.buffer;
        
        //C05
        ret = pPrinter.numeroSubstituicoesProprietario(ps);
        if (ret.isErro()) return null;
        String c05 = ps.buffer;
        
        
        //Prep
        ret = pPrinter.dadosUltimaReducao(ps);
        if (ret.isErro()) return null;
        String[] sDur = ps.buffer.split(",");
        
        //C06 - COO
        String sC06 = sDur[3];
        Integer c06;
        try {
             c06 = Integer.getInteger(sC06);
        }
        catch (Exception e) {
            return null;
        }
        
        //C07 - CCF
        ret = pPrinter.contadorCupomFiscal(ps);
        if (ret.isErro()) return null;
        String sC07 = ps.buffer;
        int c07;
        try {
             c07 = Integer.getInteger(sC07);
        }
        catch (Exception e) {
            return null;
        }  
        
        //C08 - Último Item Vendido
        ret = pPrinter.ultimoItemVendido(pi);
        if (ret.isErro()) return null;
        int c08 = pi.number;
        
        //C09 - Código do Produto ou Serviço
        int c09 = pvp.getSineticControleEstoque().getCodigoProdutoReferencia();
        
        //C10 - Descrição
        long cod = pvp.getSineticControleEstoque().getCodigoProdutoReferencia();
        String c10 = Control.getDescricaoProduto(DBControl.getFirstProduto(cod));
        
        //C11 - Quantidade
        int c11 = pvp.getQuantidade();
        
        //C12 - Unidade de Medida
        String c12 = pvp.getSineticControleEstoque().getPafEcfUnidadeMedida().getSigla();
        
        //C13 - Valor Unitário
        float c13 = pvp.getValorInicial().floatValue()/pvp.getQuantidade();
        
        //C14 - Desconto sobre item
        float c14 = pvp.getDescontoValor().floatValue();
        
        //C15 - Acréscimo sobre item
        float c15 = 0.0f;
        
        //C16 - Valor total Líquido
        float c16 = pvp.getValorFinal().floatValue();
        
        //C17 - Código do Totalizador
        String c17 = ""; //@todo verificar
        
        //C18 - Indicador de cancelamento
        char c18 = !pvp.isStatus()?'S':'N';
        
        //C19 - Quantidade Cancelada
        int c19 = !pvp.isStatus()?pvp.getQuantidade():0;
        
        //C20 - Valor Cancelado
        float c20 = !pvp.isStatus()?pvp.getValorFinal().floatValue():0;
        
        //C21 - Indicador de cancelamento parcial
        float c21 = 0.0f;
        
        //C22 - arredondamento ou truncamento
        ret = pPrinter.verificaTruncamento(ps);
        if (ret.isErro()) return null;
        char c22 = ps.buffer.charAt(0);
        
        //C23 - Terceiro ou fabricacao própria
        char c23 = 'T';
        
        //C24 - Casas decimais da quantidade
        int c24 = 0;
        
        //C25 - Casas decimais do valor
        int c25 = 2;
        
        
        
        PafRegR05 p = new PafRegR05();
        p.setNumeroFabricacao(c02);
        p.setMfAdicional(c03.charAt(0));
        p.setModeloEcf(c04);
        p.setNumeroUsuario(c05);
        p.setCoo(c06);
        p.setCcfCvcCbp(c07);
        p.setNumeroItem(c08);
        p.setCodProduto(c09);
        p.setDescricao(c10);
        p.setQuantidade(c11);
        p.setUnidade(c12);
        p.setValorUnitario(new BigDecimal(c13));
        p.setDescontoItem(new BigDecimal(c14));
        p.setAcrescimoItem(new BigDecimal(c15));
        p.setValorTotalLiq(new BigDecimal(c16));
        p.setTotalizadorParcial(c17);
        p.setIndicadorCancelamento(c18);
        p.setQuantidadeCancelada(c19);
        p.setValorCancelado(new BigDecimal(c20));
        p.setCancelamentoAcrescimo(new BigDecimal(c21));
        p.setIat(c22);
        p.setIppt(c23);
        p.setCasasQnt(c24);
        p.setCasasValor(c25);
        

        try {
            DBControl.gravaRegR05(p);
        }
        catch (Exception e) {
            return null;
        }
        
        return p;
        
    }
    
    public static PafRegR05 capturaR05Servico(PreVendaServico pvs) {
        Printer pPrinter = Control.getPrinter();
        if (pPrinter == null) {
            return null;
        }
        
        Retorno ret;        
        PrinterString ps = new PrinterString();
        PrinterLong pl = new PrinterLong();
        PrinterFloat pf = new PrinterFloat();
        PrinterInteger pi = new PrinterInteger();
        
        //C02
        ret = pPrinter.NumeroSerie(ps);
        if (ret.isErro()) return null;
        String c02 = ps.buffer;
        
        //C03
        //@todo ret = pPrinter.mfAdicional(ps);
        //@todo if (ret.isErro()) return null;
        String c03 = "0";//ps.buffer;
        
        //C04
        ret = pPrinter.modeloImpressora(ps);
        if (ret.isErro()) return null;
        String c04 = ps.buffer;
        
        //C05
        ret = pPrinter.numeroSubstituicoesProprietario(ps);
        if (ret.isErro()) return null;
        String c05 = ps.buffer;
        
        
        //Prep
        ret = pPrinter.dadosUltimaReducao(ps);
        if (ret.isErro()) return null;
        String[] sDur = ps.buffer.split(",");
        
        //C06 - COO
        String sC06 = sDur[3];
        Integer c06;
        try {
             c06 = Integer.getInteger(sC06);
        }
        catch (Exception e) {
            return null;
        }
        
        //C07 - CCF
        ret = pPrinter.contadorCupomFiscal(ps);
        if (ret.isErro()) return null;
        String sC07 = ps.buffer;
        int c07;
        try {
             c07 = Integer.getInteger(sC07);
        }
        catch (Exception e) {
            return null;
        }  
        
        //C08 - Último Item Vendido
        ret = pPrinter.ultimoItemVendido(pi);
        if (ret.isErro()) return null;
        int c08 = pi.number;
        
        //C09 - Código do Produto ou Serviço
        int c09 = pvs.getCodigo();
        
        //C10 - Descrição
        String c10 = pvs.getServico().getDescricao();
        
        //C11 - Quantidade
        int c11 = 1;
        
        //C12 - Unidade de Medida
        String c12 = "U";
        
        //C13 - Valor Unitário
        float c13 = pvs.getValorInicial().floatValue();
        
        //C14 - Desconto sobre item
        float c14 = pvs.getDescontoValor().floatValue();
        
        //C15 - Acréscimo sobre item
        float c15 = 0.0f;
        
        //C16 - Valor total Líquido
        float c16 = pvs.getValorFinal().floatValue();
        
        //C17 - Código do Totalizador
        String c17 = ""; //@todo verificar
        
        //C18 - Indicador de cancelamento
        char c18 = !pvs.isStatus()?'S':'N';
        
        //C19 - Quantidade Cancelada
        int c19 = !pvs.isStatus()?1:0;
        
        //C20 - Valor Cancelado
        float c20 = !pvs.isStatus()?pvs.getValorFinal().floatValue():0;
        
        //C21 - Indicador de cancelamento parcial
        float c21 = 0.0f;
        
        //C22 - arredondamento ou truncamento
        ret = pPrinter.verificaTruncamento(ps);
        if (ret.isErro()) return null;
        char c22 = ps.buffer.charAt(0);
        
        //C23 - Terceiro ou fabricacao própria
        char c23 = 'T';
        
        //C24 - Casas decimais da quantidade
        int c24 = 0;
        
        //C25 - Casas decimais do valor
        int c25 = 2;
        
        
        
        PafRegR05 p = new PafRegR05();
        p.setNumeroFabricacao(c02);
        p.setMfAdicional(c03.charAt(0));
        p.setModeloEcf(c04);
        p.setNumeroUsuario(c05);
        p.setCoo(c06);
        p.setCcfCvcCbp(c07);
        p.setNumeroItem(c08);
        p.setCodProduto(c09);
        p.setDescricao(c10);
        p.setQuantidade(c11);
        p.setUnidade(c12);
        p.setValorUnitario(new BigDecimal(c13));
        p.setDescontoItem(new BigDecimal(c14));
        p.setAcrescimoItem(new BigDecimal(c15));
        p.setValorTotalLiq(new BigDecimal(c16));
        p.setTotalizadorParcial(c17);
        p.setIndicadorCancelamento(c18);
        p.setQuantidadeCancelada(c19);
        p.setValorCancelado(new BigDecimal(c20));
        p.setCancelamentoAcrescimo(new BigDecimal(c21));
        p.setIat(c22);
        p.setIppt(c23);
        p.setCasasQnt(c24);
        p.setCasasValor(c25);
        

        try {
            DBControl.gravaRegR05(p);
        }
        catch (Exception e) {
            return null;
        }
        
        return p;
        
    }
    
    public static PafRegR06 capturaR06(String tipoDocumento) {
        Printer pPrinter = Control.getPrinter();
        if (pPrinter == null) {
            return null;
        }
        
        Retorno ret;        
        PrinterString ps = new PrinterString();
        PrinterLong pl = new PrinterLong();
        PrinterFloat pf = new PrinterFloat();
        PrinterInteger pi = new PrinterInteger();
        
        //C02 Número de fabricação
        ret = pPrinter.NumeroSerie(ps);
        if (ret.isErro()) return null;
        String c02 = ps.buffer;
        
        //C03 MF Adicional
        //@todo ret = pPrinter.mfAdicional(ps);
        //@todo if (ret.isErro()) return null;
        String c03 = "0";//ps.buffer;
        
        //C04 Modelo do ECF
        ret = pPrinter.modeloImpressora(ps);
        if (ret.isErro()) return null;
        String c04 = ps.buffer;
        
        //C05 Número do usuário
        ret = pPrinter.numeroSubstituicoesProprietario(ps);
        if (ret.isErro()) return null;
        String sC05 = ps.buffer;
        Integer c05;
        try {
             c05 = Integer.getInteger(sC05);
        }
        catch (Exception e) {
            return null;
        }
        
        //C06 COO
        ret = pPrinter.numeroCupom(ps);
        if (ret.isErro()) return null;
        String sC06 = ps.buffer;
        Integer c06;
        try {
             c06 = Integer.getInteger(sC06);
        }
        catch (Exception e) {
            return null;
        }   
        
        //C07 GNF
        ret = pPrinter.numeroOperacoesNaoFiscais(ps);
        if (ret.isErro()) return null;
        String sC07 = ps.buffer;
        Integer c07;
        try {
             c07 = Integer.getInteger(sC07);
        }
        catch (Exception e) {
            return null;
        }   
        
        //C08 GRG 
        ret = pPrinter.contadorRelatoriosGerenciais(ps);
        if (ret.isErro()) return null;
        String sC08 = ps.buffer;
        Integer c08;
        try {
             c08 = Integer.getInteger(sC08);
        }
        catch (Exception e) {
            return null;
        }   
        
        //C09 CDC 
        ret = pPrinter.contadorComprovantesCredito(ps);
        if (ret.isErro()) return null;
        String sC09 = ps.buffer;
        Integer c09;
        try {
             c09 = Integer.getInteger(sC09);
        }
        catch (Exception e) {
            return null;
        }   
        
        //C10 Denominação
        String c10 = tipoDocumento;
        
        //C11 e C12
        ret = pPrinter.dataHoraUltimoDocumento(pl);
        if (ret.isErro()) return null;
        Long lC11c12 = pl.number;
        Date c11c12 = new Date(lC11c12);
        
        
        
        PafRegR06 p = new PafRegR06();
        p.setNumeroFabricacao(c02);
        p.setMfAdicional(c03.charAt(0));
        p.setModeloEcf(c04);
        p.setNumeroUsuario(c05);
        p.setCoo(c06);
        p.setGnf(c07);
        p.setGrg(c08);
        p.setCdc(c09);
        p.setDenominacao(c10);
        p.setDataHora(c11c12);

        try {
            DBControl.gravaRegR06(p);
        }
        catch (Exception e) {
            return null;
        }
        
        return p;
    }
    
    //@todo verificar melhor esta função quanto ao estorno
    public static PafRegR07 capturaR07(String meio, CrediarioFormaPagamentoDetalhado cfpd, float valor, boolean estorno) {
        Printer pPrinter = Control.getPrinter();
        if (pPrinter == null) {
            return null;
        }
        
        Retorno ret;        
        PrinterString ps = new PrinterString();
        
        //C02 Número de fabricação
        ret = pPrinter.NumeroSerie(ps);
        if (ret.isErro()) return null;
        String c02 = ps.buffer;
        
        //C03 MF adicional
        //@todo ret = pPrinter.mfAdicional(ps);
        //@todo if (ret.isErro()) return null;
        String c03 = "0";//ps.buffer;
        
        //C04 Modelo do ECF
        ret = pPrinter.modeloImpressora(ps);
        if (ret.isErro()) return null;
        String c04 = ps.buffer;
        
        //C05 Número do usuário
        ret = pPrinter.numeroSubstituicoesProprietario(ps);
        if (ret.isErro()) return null;
        String sC05 = ps.buffer;
        Integer c05;
        try {
             c05 = Integer.getInteger(sC05);
        }
        catch (Exception e) {
            return null;
        }
        
        //C06 COO
        ret = pPrinter.numeroCupom(ps);
        if (ret.isErro()) return null;
        String sC06 = ps.buffer;
        Integer c06;
        try {
             c06 = Integer.getInteger(sC06);
        }
        catch (Exception e) {
            return null;
        }   
        
        //C07 CCF
        ret = pPrinter.contadorCupomFiscal(ps);
        if (ret.isErro()) return null;
        String sC07 = ps.buffer;
        Integer c07;
        try {
             c07 = Integer.getInteger(sC07);
        }
        catch (Exception e) {
            return null;
        }   
        
        //C08 GNF
        ret = pPrinter.contadoresTotalizadoresNaoFiscais(ps);
        if (ret.isErro()) return null;
        String sC08 = "";
        String[] sTot = ps.buffer.split(",");
        
        
        if (meio.length() > 16) {
            meio = meio.substring(0, 16);
        }
        
        for (int i = 0; i < sTot.length; i++) {
            if (meio.equalsIgnoreCase(sTot[i]));
            sC08 = sTot[i];
        }
        
        Integer c08;
        try {
             c08 = Integer.getInteger(sC08);
        }
        catch (Exception e) {
            return null;
        }   
        
        //C09 Meio de Pagamento
        int c09 = cfpd==null?1:cfpd.getCodigo();
        
        //C10 Valor
        Float c10 = estorno?0:valor;
        
        //C11 Estorno
        char c11 = estorno?'S':'N';
        
        //C12 Valor Estorno
        Float c12 = estorno?valor:0;
        
        
        PafRegR07 p = new PafRegR07();
        p.setNumeroFabricacao(c02);
        p.setMfAdicional(c03.charAt(0));
        p.setModeloEcf(c04);
        p.setNumeroUsuario(c05);
        p.setCoo(c06);
        p.setCcf(c07);
        p.setGnf(c08);
        p.setFkCfpdBrMeioPagamento(c09);
        p.setValorPago(new BigDecimal(c10));
        p.setIndicadorEstorno(c11);
        p.setValorEstornado(new BigDecimal(c12));

        try {
            DBControl.gravaRegR07(p);
        }
        catch (Exception e) {
            return null;
        }
        
        return p;
    }
    
    ////GERA REGISTROS /////
    public static String geraEAD(String arquivo) {
        String reg = "EAD";
        
        AssinaturaDigital ad = Control.getAssinaturaDigital();
        
        byte[] b = ad.gerarEAD(arquivo);
        
        //-1 pois despreza o char de escape
        for (int i = 0; i < b.length-1; i++) {
            reg += (char)b[i];
        }
        
        return reg;
    }    
    
    private static String geraRegN1() {
        String reg = "N1";
        reg += formataRegPadraoN(Control.ecfIdentificacao.getShCnpj().trim(), 14);
        reg += formataRegPadraoX(Control.ecfIdentificacao.getShInscricaoEstadual().trim().toUpperCase(), 14);
        reg += formataRegPadraoX(Control.ecfIdentificacao.getShInscricaoMunicipal().trim().toUpperCase(), 14);
        reg += formataRegPadraoX(Control.ecfIdentificacao.getShRazaoSocial().trim().toUpperCase(), 50);
        
        return reg;
    }
    
    private static String geraRegN2() {
        String reg = "N2";
        reg += formataRegPadraoX(Control.ecfIdentificacao.getLaudo().trim().toUpperCase(), 10);
        reg += formataRegPadraoX(Control.ecfIdentificacao.getPafNomeComercial().trim().toUpperCase(), 50);
        reg += formataRegPadraoX(Control.ecfIdentificacao.getPafVersao().trim().toUpperCase(), 10);
        
        return reg;
    }
    
    private static String geraRegN3(EcfExecutaveis ee) {
        String reg = "N3";

        reg += formataRegPadraoX(ee.getNome().trim().toUpperCase(), 50);
        reg += formataRegPadraoX(ee.getMd5().trim().toUpperCase(), 32);
        
        return reg;
    }
    
    private static String geraRegN9(int qntReg) {
        //TODO ATIVIDADE - - Correção do MD5 informando local de gravação e Geração do Registro N9 
        String reg = "N9";
        
        reg += formataRegPadraoN(Control.ecfIdentificacao.getShCnpj().trim(), 14);
        reg += formataRegPadraoX(Control.ecfIdentificacao.getShInscricaoEstadual().trim().toUpperCase(), 14);
        reg += formataRegPadraoN(String.valueOf(qntReg), 6);
        
        return reg;
    }
    
    
    private static String geraRegE1() {
        List <Object[]> lE1 = DBControl.getRegistroE1(Control.configuracoes.getFkBrLojaId());
        String mfAd = Control.mfAdicional();
        if (mfAd == null) { 
            mfAd = " ";
        }
        if (lE1 == null) {
            return null;
        }
        
        Object[] rQuery = null;
        for (Object[] o: lE1) {
            rQuery = o;
            break;
        }
        
        if (rQuery == null) {
            return null;
        }

        String reg = "E1";
        reg += formataRegPadraoN((String)rQuery[0], 14); //02
        reg += formataRegPadraoX((String)rQuery[1], 14); //03
        reg += formataRegPadraoX((String)rQuery[2], 14); //04
        reg += formataRegPadraoX((String)rQuery[3], 50); //05
        reg += formataRegPadraoX((String)rQuery[4], 20); //06
        reg += formataRegPadraoX(mfAd, 1); //07
        reg += formataRegPadraoX((String)rQuery[5], 7); //08
        reg += formataRegPadraoX((String)rQuery[6], 20); //09
        reg += formataRegPadraoX((String)rQuery[7], 20); //10
        reg += formataRegPadraoX((String)rQuery[8], 8); //11
        reg += formataRegPadraoX((String)rQuery[9], 6); //12
        
        return reg;
    }
    
    private static String geraRegE2() {
        List <Object[]> lE2 = DBControl.getRegistroE2(Control.configuracoes.getFkBrLojaId());
        if (lE2 == null) {
            return null;
        }
        
        String reg = "";
        
        for (Object[] rQuery: lE2) {

            reg += "E2"; //01
            reg += formataRegPadraoN((String)rQuery[0], 14); //02
            reg += formataRegPadraoX((String)rQuery[1], 14); //03
            reg += formataRegPadraoX((String)rQuery[2], 50); //04
            reg += formataRegPadraoX((String)rQuery[3], 6); //05
            reg += formataRegPadraoX((String)rQuery[4], 1); //06
            reg += formataRegPadraoN((String)rQuery[5], 9); //07
        }
        return reg;
    }
    
    private static String geraRegE9() {
        List <Object[]> lE9 = DBControl.getRegistroE9(Control.configuracoes.getFkBrLojaId());
        if (lE9 == null) {
            return null;
        }
        
        Object[] rQuery = null;
        for (Object[] o: lE9) {
            rQuery = o;
            break;
        }
        
        if (rQuery == null) {
            return null;
        }

        
        String reg = "E9"; //01
        reg += formataRegPadraoN((String)rQuery[0], 14); //02
        reg += formataRegPadraoX((String)rQuery[1], 14); //03
        reg += formataRegPadraoN((String)rQuery[2], 6); //04
        
        return reg;
    }
    
    private static String geraRegP1() {
        List <Object[]> lP1 = DBControl.getRegistroP1(Control.configuracoes.getFkBrLojaId());
        
        if (lP1 == null) {
            return null;
        }
        
        Object[] rQuery = null;
        for (Object[] o: lP1) {
            rQuery = o;
            break;
        }
        
        if (rQuery == null) {
            return null;
        }


        String reg = "P1";
        reg += formataRegPadraoN((String)rQuery[0], 14); //02
        reg += formataRegPadraoX((String)rQuery[1], 14); //03
        reg += formataRegPadraoX((String)rQuery[2], 14); //04
        reg += formataRegPadraoX((String)rQuery[3], 50); //05
        
        return reg;
    }
    
    private static String geraRegP2() {
        List <Object[]> lP2 = DBControl.getRegistroP2(Control.configuracoes.getFkBrLojaId());
        if (lP2 == null) {
            return null;
        }
        
        String reg = "";

        
        for (Object[] rQuery: lP2) {

            reg += "P2"; //01
            reg += formataRegPadraoN((String)rQuery[1], 14); //02
            reg += formataRegPadraoX((String)rQuery[2], 14); //03
            reg += formataRegPadraoX((String)rQuery[3], 50); //04
            reg += formataRegPadraoX((String)rQuery[4], 6); //05
            reg += formataRegPadraoX((String)rQuery[5], 1); //06
            reg += formataRegPadraoX((String)rQuery[6], 1); //07
            reg += formataRegPadraoX((String)rQuery[7], 1); //08
            reg += formataRegPadraoN((String)rQuery[8], 4); //09
            reg += formataRegPadraoN((String)rQuery[9], 12); //10
        }
        return reg;
    }

    private static String geraRegP9() {
        List <Object[]> lP9 = DBControl.getRegistroP9(Control.configuracoes.getFkBrLojaId());
        if (lP9 == null) {
            return null;
        }
        
        Object[] rQuery = null;
        for (Object[] o: lP9) {
            rQuery = o;
            break;
        }
        
        if (rQuery == null) {
            return null;
        }

        
        String reg = "P9"; //01
        reg += formataRegPadraoN((String)rQuery[0], 14); //02
        reg += formataRegPadraoX((String)rQuery[1], 14); //03
        reg += formataRegPadraoN((String)rQuery[2], 6); //04
        
        return reg;
    }
    
    
    private static String geraRegH1() {
        List <Object[]> lH1 = DBControl.getRegistroH1(Control.configuracoes.getFkBrLojaId());
        String mfAd = Control.mfAdicional();
        if (mfAd == null) { 
            mfAd = " ";
        }
        if (lH1 == null) {
            return null;
        }
        
        Object[] rQuery = null;
        for (Object[] o: lH1) {
            rQuery = o;
            break;
        }
        
        if (rQuery == null) {
            return null;
        }


        String reg = "H1";
        reg += formataRegPadraoN((String)rQuery[0], 14); //02
        reg += formataRegPadraoX((String)rQuery[1], 14); //03
        reg += formataRegPadraoX((String)rQuery[2], 14); //04
        reg += formataRegPadraoX((String)rQuery[3], 50); //05
        reg += formataRegPadraoX((String)rQuery[4], 20); //06
        reg += formataRegPadraoX(mfAd, 1); //07
        reg += formataRegPadraoX((String)rQuery[6], 7); //08
        reg += formataRegPadraoX((String)rQuery[7], 20); //09
        reg += formataRegPadraoX((String)rQuery[8], 20); //10
        
        return reg;
    }
    
    private static String geraRegH2() {
        List <Object[]> lH2 = DBControl.getRegistroH2(Control.configuracoes.getFkBrLojaId());
        if (lH2 == null) {
            return null;
        }
        
        String reg = "";

        
        for (Object[] rQuery: lH2) {

            reg += "H2"; //01
            reg += formataRegPadraoN((String)rQuery[1], 14); //02
            reg += formataRegPadraoN((String)rQuery[2], 6); //03
            reg += formataRegPadraoN((String)rQuery[3], 6); //04
            reg += formataRegPadraoN((String)rQuery[4], 13); //05
            reg += formataRegPadraoX((String)rQuery[5], 8); //06
            reg += formataRegPadraoN((String)rQuery[6], 14); //07
            reg += formataRegPadraoN((String)rQuery[7], 7); //08

        }
        return reg;
    }

    private static String geraRegH9() {
        List <Object[]> lH9 = DBControl.getRegistroH9(Control.configuracoes.getFkBrLojaId());
        if (lH9 == null) {
            return null;
        }
        
        if (lH9.isEmpty()) {
            lH9 = DBControl.getRegistroH9Zerado(Control.configuracoes.getFkBrLojaId());
            if (lH9 == null) {
                return null;
            }
        }
        
        Object[] rQuery = null;
        for (Object[] o: lH9) {
            rQuery = o;
            break;
        }
        
        if (rQuery == null) {
            return null;
        }

        
        String reg = "H9"; //01
        reg += formataRegPadraoN((String)rQuery[1], 14); //02
        reg += formataRegPadraoX((String)rQuery[2], 14); //03
        reg += formataRegPadraoX((String)rQuery[3], 14); //04
        reg += formataRegPadraoN((String)rQuery[4], 6); //05
        
        return reg;
    }
    
    public static String geraArquivoMD5() {

        
        //Pega os executaveis do banco
        List<EcfExecutaveis> lEE = DBControl.getEcfExecutaveis();
        
        if (lEE == null) {
            return null;
        }
        
        AssinaturaDigital ad = Control.getAssinaturaDigital();
        if (ad == null) {
            return null;
        }
        
        //Gera MD5 para os executáveis
        for (EcfExecutaveis ee: lEE) {
            String fullPath = ee.getPath().trim()+ee.getNome().trim();
            ee.setMd5(ad.gerarMD5(fullPath));
            if (ee.getMd5() == null) {
                ee.setMd5(ad.gerarMD5("C:\\arquivo.txt"));
                if (ee.getMd5() == null)
                    return null;
            }
        }
        
        //Dá update nos executáveis
        DBControl.atualizarEcfExecutaveis(lEE);

        
        //Cria o arquivo MD5
        String arqPath = System.getProperty("user.dir") + "\\"+NOME_ARQ_MD5;
        File arquivo = criaArquivo(arqPath);
        
        
        //Gera os registros necessários
        String n1 = geraRegN1();
        String n2 = geraRegN2();
        String n3 = "";
        for (EcfExecutaveis ee: lEE) {
            n3 += geraRegN3(ee);
        }
        String n9 = geraRegN9(lEE.size());
        
        if (n1 == null || n2 == null ||
            n3 == null || n9 == null) {
            return null;
        }
        String registros = n1+n2+n3+n9;
        
        //Grava arquivo sem assinatura
        if (!gravaRegistros(arquivo, registros)) {
            return null;
        }

        //Gera o EAD do arquivo
        String ead = geraEAD(arqPath);
        
        //Grava assinatura
        if (!gravaRegistros(arquivo, ead)) {
            return null;
        }
        
        //Gera md5 do arquivo gerado
        String md5 = ad.gerarMD5(arqPath);
        if (md5 == null || md5.isEmpty()) {
            return null;
        }
        
        return md5;
    }
    
    public static boolean geraArquivoEletEstoque() {

        
        AssinaturaDigital ad = Control.getAssinaturaDigital();
        if (ad == null) {
            return false;
        }
        
                
        //Cria o arquivo MD5
        String arqPath = System.getProperty("user.dir") + "\\"+NOME_ARQ_ESTOQUE;
        File arquivo = criaArquivo(arqPath);
        
        
        //Gera os registros necessários
        String e1 = geraRegE1();
        String e2 = geraRegE2();
        String e9 = geraRegE9();
        
        if (e1 == null || e2 == null ||
            e9 == null) {
            return false;
        }
        String registros = e1+e2+e9;
        
        //Grava arquivo sem assinatura
        if (!gravaRegistros(arquivo, registros)) {
            return false;
        }

        //Gera o EAD do arquivo
        String ead = geraEAD(arqPath);
        
        //Grava assinatura
        if (!gravaRegistros(arquivo, ead)) {
            return false;
        }
        
        return true;
    }
    
    public static boolean geraArquivoTabMercServ() {

        
        AssinaturaDigital ad = Control.getAssinaturaDigital();
        if (ad == null) {
            return false;
        }
        
                
        //Cria o arquivo MD5
        String arqPath = System.getProperty("user.dir") + "\\"+NOME_ARQ_TAB_MERC_SERV;
        File arquivo = criaArquivo(arqPath);
        
        
        //Gera os registros necessários
        String p1 = geraRegP1();
        String p2 = geraRegP2();
        String p9 = geraRegP9();
        
        if (p1 == null || p2 == null ||
            p9 == null) {
            return false;
        }
        String registros = p1+p2+p9;
        
        //Grava arquivo sem assinatura
        if (!gravaRegistros(arquivo, registros)) {
            return false;
        }

        //Gera o EAD do arquivo
        String ead = geraEAD(arqPath);
        
        //Grava assinatura
        if (!gravaRegistros(arquivo, ead)) {
            return false;
        }
        
        return true;
    }
    
    public static boolean geraArquivoTrocoCartao() {
        AssinaturaDigital ad = Control.getAssinaturaDigital();
        if (ad == null) {
            return false;
        }
        
                
        //Cria o arquivo MD5
        String arqPath = System.getProperty("user.dir") + "\\"+NOME_ARQ_TROCO_CARTAO;
        File arquivo = criaArquivo(arqPath);
        
        
        //Gera os registros necessários
        String h1 = geraRegH1();
        String h2 = geraRegH2();
        String h9 = geraRegH9();
        
        if (h1 == null || h2 == null ||
            h9 == null) {
            return false;
        }
        String registros = h1+h2+h9;
        
        //Grava arquivo sem assinatura
        if (!gravaRegistros(arquivo, registros)) {
            return false;
        }

        //Gera o EAD do arquivo
        String ead = geraEAD(arqPath);
        
        //Grava assinatura
        if (!gravaRegistros(arquivo, ead)) {
            return false;
        }
        
        return true;
    }
    
    
    
    /// TRATAMENTO DE ARQUIVOS ////
    
    public static boolean gravaRegistros(File arquivo, String registros) {
        
        PrintWriter saida = null;
        FileWriter gravar = null;
        
        try {
            gravar = new FileWriter(arquivo, true);
            saida = new PrintWriter(gravar, true);
            saida.print(registros);
            saida.flush();
            gravar.flush();
        }
        catch (Exception e) {
            return false;
        }
        finally {
            if (saida != null) {
                saida.close();
            }
            try {
                gravar.close();
            }
            catch (IOException e) {
                return false;
            }
        }
        return true;
    }
    
    public static File criaArquivo(String arqPath) {
        File arquivo = null;
         try {
            //apaga arquivo anterior
            if (new File(arqPath).exists()) {
                new File(arqPath).delete();
            }
            //cria o arquivo TXT na mesma pasta do sistema
            arquivo = new File(arqPath);
            arquivo.setReadable(true, false);
            arquivo.setWritable(true, false);
        }
        catch (Exception e) {
            System.out.println("Erro ao criar arquivo "+NOME_ARQ_MD5);
            return null;
        }
        return arquivo;
    }
    
    ////FORMATADORES ////
    public static String formataRegPadraoN(String reg, int tamanho) {
        return formataRegGenerico(reg, tamanho, '0');
    }
    
    public static String formataRegPadraoX(String reg, int tamanho) {
        return formataRegGenerico(reg, tamanho, ' ');
    }
    
    public static String formataRegGenerico(String reg, int tamanho, char c) {
        String regRet = reg.toString();
        for (int i = tamanho; i > reg.length(); i--) {
            regRet = c + regRet;
        }
        return regRet;
    }
    
}
