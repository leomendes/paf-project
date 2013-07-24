/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.db;

import br.com.sinetic.pafecftef.action.Controller;
import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.control.ItensVendidos;
import br.com.sinetic.pafecftef.control.ListaDeVendas;
import br.com.sinetic.pafecftef.entities.AbreCaixa;
import br.com.sinetic.pafecftef.entities.AliquotasImpressora;
import br.com.sinetic.pafecftef.entities.ConfiguracoesSistema;
import br.com.sinetic.pafecftef.entities.DiaFiscal;
import br.com.sinetic.pafecftef.entities.EcfComprador;
import br.com.sinetic.pafecftef.entities.EcfExecutaveis;
import br.com.sinetic.pafecftef.entities.EcfIdentificacao;
import br.com.sinetic.pafecftef.entities.EcfLeiturasx;
import br.com.sinetic.pafecftef.entities.EcfReducoesz;
import br.com.sinetic.pafecftef.entities.EcfRelatoriosGerenciais;
import br.com.sinetic.pafecftef.entities.EcfSangrias;
import br.com.sinetic.pafecftef.entities.EcfSuprimentos;
import br.com.sinetic.pafecftef.entities.FechaCaixa;
import br.com.sinetic.pafecftef.entities.Vendas;
import br.com.sinetic.pafecftef.entities.Item;
import br.com.sinetic.pafecftef.entities.PafMeiosPagamentoDia;
import br.com.sinetic.pafecftef.entities.PafProduct;
import br.com.sinetic.pafecftef.entities.PafRegR02;
import br.com.sinetic.pafecftef.entities.PafRegR03;
import br.com.sinetic.pafecftef.entities.PafRegR04;
import br.com.sinetic.pafecftef.entities.PafRegR05;
import br.com.sinetic.pafecftef.entities.PafRegR06;
import br.com.sinetic.pafecftef.entities.PafRegR07;
import br.com.sinetic.pafecftef.entities.PrevendaAberta;
import br.com.sinetic.pafecftef.entities.TipoDocumento;
import br.com.sinetic.pafecftef.entities.VendaPagamento;
import br.com.sinetic.pafecftef.hibernate.HibernateQueries;
import br.com.sinetic.pafecftef.remoteentities.CrediarioFormaPagamento;
import br.com.sinetic.pafecftef.remoteentities.CrediarioFormaPagamentoBandeira;
import br.com.sinetic.pafecftef.remoteentities.CrediarioFormaPagamentoDetalhado;
import br.com.sinetic.pafecftef.remoteentities.CrediarioTiposStatusPrestacao;
import br.com.sinetic.pafecftef.remoteentities.CrediarioVendaPagamento;
import br.com.sinetic.pafecftef.remoteentities.CrediarioVendaParcelamento;
import br.com.sinetic.pafecftef.remoteentities.PreVenda;
import br.com.sinetic.pafecftef.remoteentities.PreVendaProduto;
import br.com.sinetic.pafecftef.remoteentities.PreVendaServico;
import br.com.sinetic.pafecftef.remoteentities.Produtos;
import br.com.sinetic.pafecftef.remoteentities.Usuario;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import javax.persistence.TemporalType;
import org.eclipse.persistence.jpa.internal.jpql.parser.DateTime;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Expression;
import org.hibernate.exception.JDBCConnectionException;

/**
 *
 * @author Windows8
 */
public class DBControl {
    
    
    public static final int GRUPO_GERENTE = 3;
    //Hibernate
    //Banco Local    
    private static HibernateQueries hqBL;
    private static Session sessionBL;
    
    //Banco Remoto
    private static HibernateQueries hqBR;
    private static Session sessionBR;
    
    public static boolean load() {
        //Dá load no banco local
        hqBL = new HibernateQueries("hibernate.cfg.xml");
        if (!abreSessaoLocal()) {
            Controller.messageBox("Erro ao iniciar Sessão de banco de dados local!");
            return false;
        }
        
        //Dá load no banco Remoto
        hqBR = new HibernateQueries("hibernate_remote.cfg.xml");
        if (!abreSessaoRemota()) {
            Controller.messageBox("Erro ao iniciar Sessão de banco de dados remoto!");
            return false;
        }
        
        
        
        return true;
    }
    
    private static boolean abreSessaoLocal() {
        try {
            sessionBL = hqBL.newSession();
        }
        catch (Exception e) {
            return false;
        }
        if (sessionBL == null) {
            return false;
        }
        return true;
    }

    
    private static void fechaSessaoLocal() {
        if (sessionBL.isOpen()) {
            sessionBL.close();
        }
    }
    
    private static boolean abreSessaoRemota() {
        try {
            sessionBR = hqBR.newSession();
        }
        catch (Exception e) {
            return false;
        }
        if (sessionBR == null) {
            return false;
        }
        return true;
    }
    
    
    private static void fechaSessaoRemota() {
        if (sessionBR.isOpen()) {
            sessionBR.close();
        }
    }
    
    
    /////////BANCO REMOTO /////////////////
    /**
     * Pega lista das pré-vendas abertas que não foram capturadas.
     * @return 
     */
    public static List<PreVenda> getPreVendasAbertas() {
        Query query = sessionBR.createQuery("SELECT a FROM PreVenda a WHERE " +
                                            "finalizada = FALSE AND " +
                                            "em_progresso = FALSE AND " +
                                            "cancelada = FALSE " +
                                            "ORDER BY a.codigo ASC");
        
        boolean ok = false;
        List<PreVenda> listPV = null;
        while (!ok) {
            try {
                listPV = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    listPV = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        
        return listPV;
    }
    
    /**
     * Pega lista das pré-vendas abertas e em progresso ou não
     * @return 
     */
    public static List<PreVenda> getPreVendasAbertasProgresso() {
        Query query = sessionBR.createQuery("SELECT a FROM PreVenda a WHERE " +
                                            "finalizada = FALSE AND " +
                                            "cancelada = FALSE " +
                                            "ORDER BY a.codigo ASC");
        boolean ok = false;
        List<PreVenda> listPV = null;
        while (!ok) {
            try {
                listPV = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    listPV = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return listPV;
    }
    
    public static PreVenda getPreVenda(int codigo) {
        PreVenda pv = null;
        Query query = sessionBR.createQuery("SELECT a FROM PreVenda a WHERE codigo = "+codigo);
        boolean ok = false;
        List<PreVenda> listPV = null;
        while (!ok) {
            try {
                listPV = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    listPV = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        for (PreVenda a:listPV) {
            pv = a;
        }
        
        return pv;
    }
    
    
    
    
    /**
     * Ou grava uma nova venda, ou atualiza uma em execução
     * @param v
     * @return 
     */
    public static boolean atualizaPreVenda(PreVenda pv) {
        Transaction t = sessionBR.beginTransaction();
        
        boolean ok = false;
        
        while (!ok) {
            try {
                sessionBR.update(pv);
                t.commit();
                ok = true;
            }
            catch (Exception e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    t.rollback();
                    return false;
                }
            }
        }
        return true;
    }
    
    
    public static boolean atualizaPreVendaProduto(PreVendaProduto pvp) {
        Transaction t = sessionBR.beginTransaction();
        
        boolean ok = false;
        
        while (!ok) {
            try {
                sessionBR.update(pvp);
                t.commit();
                ok = true;
            }
            catch (Exception e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    t.rollback();
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean atualizaPreVendaServico(PreVendaServico pvs) {
        Transaction t = sessionBR.beginTransaction();
        
        boolean ok = false;
        
        while (!ok) {
            try {
                sessionBR.update(pvs);
                t.commit();
                ok = true;
            }
            catch (Exception e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    t.rollback();
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean vendaHasParcelas(int codPV) throws Exception{
        PreVenda pv = getPreVenda(codPV);
        if (pv == null) {
            throw new Exception ("Erro ao carregar pré-venda");
        }
        
        Set<CrediarioVendaParcelamento> sCVP = pv.getCrediarioVendaParcelamentos();
        if (sCVP == null) {
            return false;
        }
        else if (sCVP.isEmpty()) {
            return false;
        }
        
        return true;
    }
    
    
    public static boolean validaGerente(String user, String senha) {
        //@todo arrumar a criptografia do usuario e senha
        Query query = sessionBR.createQuery("SELECT a FROM Usuario a WHERE a.codigo = '"+user+"' AND a.senha = '" + senha + "'");
        boolean ok = false;
        List<Usuario> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    return false;
                }
            }
        }
        
        if (list.size() >0) {
            if (list.get(0).getGrupoUsuario().getId() == GRUPO_GERENTE) {
                return true;
            }
        }
        return false;
    }
            
            
    /**
     * Pega o primeiro produto com o codigo de referência
     * @return 
     */
    public static Produtos getFirstProduto(long codigo_referencia) {

        //@todo Tentar fazer uma query para pegar apenas o primeiro produto
        Query query = sessionBR.createQuery("SELECT a FROM Produtos a WHERE a.codReferencia = "+codigo_referencia);
        boolean ok = false;
        List<Produtos> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        Produtos p = null;
        
        for (Produtos a:list) {
            p = a;
        }

        return p;
    }
    
    
    public static List<CrediarioFormaPagamento> getPagamentoMeios() {
        Query query = sessionBR.createQuery("SELECT a FROM CrediarioFormaPagamento a ORDER BY a.codigo ASC");
        boolean ok = false;
        List<CrediarioFormaPagamento> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
            
    public static CrediarioFormaPagamento getPagamentoMeio(int codigo) {
        CrediarioFormaPagamento cfp = null;
        
        Query query = sessionBR.createQuery("SELECT a FROM CrediarioFormaPagamento a WHERE a.codigo = " + codigo);
        boolean ok = false;
        List<CrediarioFormaPagamento> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        for (CrediarioFormaPagamento a:list) {
            cfp = a;
            break;
        }
        
        return cfp;
    }
    
    public static List<CrediarioFormaPagamentoDetalhado> getPagamentoTipos(int codFormaPag) {
        Query query = sessionBR.createQuery("SELECT a FROM CrediarioFormaPagamentoDetalhado a WHERE a.codFormaPagamento = "+codFormaPag+" ORDER BY a.codigo ASC");
        boolean ok = false;
        List<CrediarioFormaPagamentoDetalhado> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    public static List<PafMeiosPagamentoDia> getMeiosDePagamento(String sI, String sF) {
        SimpleDateFormat fEntrada = new SimpleDateFormat("dd/MM/yyyy");
        Date dI = null;
        Date dF = null;
        Calendar dtIni = Calendar.getInstance();
        Calendar dtEnd = Calendar.getInstance();
        try {
            dI = fEntrada.parse(sI);
            dF = fEntrada.parse(sF);
            
            dtIni.setTime(dI);
            dtIni.set(Calendar.HOUR_OF_DAY, 0);  
            dtIni.set(Calendar.MINUTE, 0);  
            dtIni.set(Calendar.SECOND, 1);  
            dtIni.set(Calendar.MILLISECOND, 0);  

            dtEnd.setTime(dF);
            dtEnd.set(Calendar.HOUR_OF_DAY, 23);  
            dtEnd.set(Calendar.MINUTE, 59);  
            dtEnd.set(Calendar.SECOND, 59);  
            dtEnd.set(Calendar.MILLISECOND, 999);
        }catch(Exception e){
            return null;
        }
        
        Query query = sessionBR.createQuery("SELECT a FROM "
                + "PafMeiosPagamentoDia a WHERE a.datahora >= :dtIni"
                + " AND a.datahora <= :dtEnd");
        query.setParameter("dtIni", dtIni.getTime());
        query.setParameter("dtEnd", dtEnd.getTime());
        
        boolean ok = false;
        List<PafMeiosPagamentoDia> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    public static int gravaPagamento(CrediarioVendaPagamento cvp) throws Exception{
        Transaction t = sessionBR.beginTransaction();
        
        if (cvp == null) {
            throw new Exception ("Erro ao gravar Pagamento no banco");
        }
        
        boolean ok = false;
        
        while (!ok) {
            try {
                sessionBR.save(cvp);
                t.commit();

                if (t.wasCommitted()) {
                    hqBR.refresh(cvp);
                    return cvp.getCodigo();
                }
                else {
                    throw new Exception ("Erro ao gravar Pagamento no banco");
                }
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    t.rollback();
                    throw new Exception ("Erro ao gravar Pagamento no banco");
                }
            }
            catch (Exception e) {
                if (!Controller.confirmBox("Erro ao gravar pagamento no Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    t.rollback();
                    throw new Exception ("Erro ao gravar Pagamento no banco");
                }
            }
        }
        throw new Exception ("Erro ao gravar Pagamento no banco");
    }
    
    public static CrediarioTiposStatusPrestacao getTipoPrestacao(int codigo) {
        CrediarioTiposStatusPrestacao c = null;
        
        Query query = sessionBR.createQuery("SELECT a FROM CrediarioTiposStatusPrestacao a WHERE a.codigo = " + codigo);
        boolean ok = false;
        List<CrediarioTiposStatusPrestacao> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        for (CrediarioTiposStatusPrestacao a:list) {
            c = a;
            break;
        }
        
        return c;
    }
    
    public static int gravaParcelaCrediario(CrediarioVendaParcelamento cvp) throws Exception{
        Transaction t = sessionBR.beginTransaction();
        
        if (cvp == null) {
            throw new Exception ("Erro ao gravar Parcela de Crediário no banco");
        }
        
        boolean ok = false;
        
        while (!ok) {
            try {
                sessionBR.save(cvp);
                t.commit();

                if (t.wasCommitted()) {
                    hqBR.refresh(cvp);
                    return cvp.getCodigo();
                }
                else {
                    throw new Exception ("Erro ao gravar Parcela de Crediário no banco");
                }

            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    t.rollback();
                    throw new Exception ("Erro ao gravar Pagamento no banco");
                }
            }
            catch (Exception e) {
                if (!Controller.confirmBox("Erro ao gravar pagamento no Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    t.rollback();
                    throw new Exception ("Erro ao gravar Pagamento no banco");
                }
            }
        }
        throw new Exception ("Erro ao gravar Pagamento no banco");
    }
    
    public static boolean atualizaParcelaCrediario(CrediarioVendaParcelamento cvp) {
        Transaction t = sessionBR.beginTransaction();
        
        boolean ok = false;
        
        while (!ok) {
            try {
                sessionBR.update(cvp);
                t.commit();
                ok = true;
            }
            catch (Exception e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    t.rollback();
                    return false;
                }
            }
        }
        return true;
    }
    
    public static List<CrediarioVendaParcelamento> getParcelasCrediario(PreVenda pv) {
        Query query = sessionBR.createQuery("SELECT a FROM CrediarioVendaParcelamento a ORDER BY a.codigo ASC");
        boolean ok = false;
        List<CrediarioVendaParcelamento> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    //////BANCO REMOTO SEM HIBERNATE////////
    /**
     * Tabelas mapeadas nesta funçao
     * 
     * [0]: cnpj
     * [1]: inscricao_estadual
     * [2]: inscricao_municipal
     * [3]: razao_social
     * [4]: numero_fabricacao
     * [5]: tipo_ecf
     * [6]: marca_ecf
     * [7]: modelo_ecf
     * [8]: data_estoque
     * [9]: hora_estoque
     * 
     * Da tabela 'loja'
     * 
     * @param loja
     * @return 
     */
    public static List<Object[]> getRegistroE1(int loja) {
        String sQuery = "SELECT /*02*/LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS cnpj,\n" +
"       /*03*/RPAD(REGEXP_REPLACE(loja.ie, '[^0-9]', '', 'g'), 14, ' ') AS inscricao_estadual,\n" +
"       /*04*/RPAD(' ', 14, ' ') AS inscricao_municipal,\n" +
"       /*05*/RPAD(UPPER(loja.nome), 50, ' ') AS razao_social,\n" +
"       /*06*/RPAD(' ', 20, ' ') AS numero_fabricacao,\n" +
"       /*08*/RPAD(' ', 7, ' ') AS tipo_ecf,\n" +
"       /*09*/RPAD(' ', 20, ' ') AS marca_ecf,\n" +
"       /*10*/RPAD(' ', 20, ' ') AS modelo_ecf,\n" +
"       /*11*/TO_CHAR(NOW(), 'YYYYMMDD') AS data_estoque,\n" +
"       /*12*/TO_CHAR(NOW(), 'HHMISS') AS hora_estoque\n" +
"FROM loja\n" +
                        "WHERE loja.numero = "+ loja+ ";";
        Query query = sessionBR.createSQLQuery(sQuery).
                                addScalar("cnpj", Hibernate.STRING).
                                addScalar("inscricao_estadual", Hibernate.STRING).
                                addScalar("inscricao_municipal", Hibernate.STRING).
                                addScalar("razao_social", Hibernate.STRING).
                                addScalar("numero_fabricacao", Hibernate.STRING).
                                addScalar("tipo_ecf", Hibernate.STRING).
                                addScalar("marca_ecf", Hibernate.STRING).
                                addScalar("modelo_ecf", Hibernate.STRING).
                                addScalar("data_estoque", Hibernate.STRING).
                                addScalar("hora_estoque",Hibernate.STRING);
        boolean ok = false;
        List<Object[]> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    /**
     * Tabelas mapeadas nesta funçao
     * 
     * [0]: cnpj
     * [1]: codigo_produto
     * [2]: descricao_produto
     * [3]: unidade
     * [4]: mensuracao_estoque
     * [5]: quantidade_estoque
     * 
     * Da tabela 'loja'
     * 
     * @param loja
     * @return 
     */
    public static List<Object[]> getRegistroE2(int loja) {
        String sQuery = 
"SELECT LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS cnpj,\n" +
"       /*03*/RPAD(CAST(controle_estoque.codigo as TEXT), 14, ' ') AS codigo_produto,\n" +
"       /*04*/RPAD(UPPER((produto_grupo.grupo || ' ' || produto_subgrupo.subgrupo || ' ' || produto_marca.nome_exibicao || ' ' || produto_modelo.nome)), 50, ' ') AS descricao_produto,\n" +
"       /*05*/RPAD(controle_estoque.unidade_medida, 6, ' ') AS unidade,\n" +
"       /*06*/'+' AS mensuracao_estoque,\n" +
"       /*07*/LPAD(CAST(controle_estoque.quantidade as TEXT), 9, '0') AS quantidade_estoque\n" +
"FROM sinetic_controle_estoque controle_estoque, loja, produtos, produto_grupo, produto_subgrupo, produto_marca, sinetic_produto_modelo produto_modelo\n" +
"WHERE loja.numero = "+loja+" AND -- nÃºmero da loja cujos dados serÃ£o usados.\n" +
"      -- inÃ­cio descriÃ§Ã£o\n" +
"      controle_estoque.codigo_produto_referencia = produtos.cod_referencia AND\n" +
"      produtos.grupo = produto_grupo.codigo AND\n" +
"      produtos.subgrupo = produto_subgrupo.codigo AND\n" +
"      produtos.cod_marca = produto_marca.codigo AND\n" +
"      produtos.cod_modelo = produto_modelo.codigo AND\n" +
"      -- fim descriÃ§Ã£o\n" +
"      -- inÃ­cio loja na qual estÃ£o os produtos do estoque\n" +
"      controle_estoque.codigo IN (SELECT DISTINCT codigo_controle_estoque\n" +
"				  FROM sinetic_controle_estoque_rastreio\n" +
"				  WHERE codigo_produto IN (SELECT cod_produto\n" +
"							   FROM mov_estoque\n" +
"							   WHERE ultima_loja = "+loja+"/* AND ultimo_status BETWEEN 61 AND 160*/)) -- loja onde os produtos se encontram/produtos que ainda nÃ£o foram vendidos.\n" +
"      -- fim loja na qual estÃ£o os produtos do estoque\n" +
"GROUP BY cnpj, controle_estoque.codigo, descricao_produto, unidade, quantidade_estoque\n" +
"ORDER BY CAST(controle_estoque.codigo AS INTEGER);";
        Query query = sessionBR.createSQLQuery(sQuery).
                                addScalar("cnpj", Hibernate.STRING).
                                addScalar("codigo_produto", Hibernate.STRING).
                                addScalar("descricao_produto", Hibernate.STRING).
                                addScalar("unidade", Hibernate.STRING).
                                addScalar("mensuracao_estoque", Hibernate.STRING).
                                addScalar("quantidade_estoque", Hibernate.STRING);
        boolean ok = false;
        List<Object[]> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    /**
     * Tabelas mapeadas nesta funçao
     * 
     * [0]: cnpj
     * [1]: inscricao_estadual
     * [2]: total_registros_e2
     * 
     * Da tabela 'loja'
     * 
     * @param loja
     * @return 
     */
    public static List<Object[]> getRegistroE9(int loja) {
        String sQuery = "SELECT /*02*/LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS cnpj,\n" +
"       /*03*/RPAD(REGEXP_REPLACE(loja.ie, '[^0-9]', '', 'g'), 14, ' ') AS inscricao_estadual,\n" +
"       /*04*/LPAD(CAST(re2.nregistros as TEXT), 6, '0') AS total_registros_e2\n" +
"FROM loja,\n" +
"    ( SELECT count(*) AS nregistros\n" +
"      FROM sinetic_controle_estoque controle_estoque\n" +
"      WHERE controle_estoque.codigo IN ( SELECT DISTINCT codigo_controle_estoque\n" +
"					 FROM sinetic_controle_estoque_rastreio controle_estoque_rastreio\n" +
"					 WHERE controle_estoque_rastreio.codigo_produto IN ( SELECT cod_produto\n" +
"											     FROM mov_estoque\n" +
"											     WHERE ultima_loja = "+loja+"/* AND ultimo_status BETWEEN 61 AND 160*/)) ) re2 -- loja onde os produtos se encontram/produtos que ainda nÃ£o foram vendidos.\n" +
                        "WHERE loja.numero = "+ loja+ ";";
        Query query = sessionBR.createSQLQuery(sQuery).
                                addScalar("cnpj", Hibernate.STRING).
                                addScalar("inscricao_estadual", Hibernate.STRING).
                                addScalar("total_registros_e2", Hibernate.STRING);

        boolean ok = false;
        List<Object[]> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    
    /**
     * Tabelas mapeadas nesta funçao
     * 
     * [0]: cnpj
     * [1]: inscricao_estadual
     * [2]: inscricao_municipal
     * [3]: razao_social
     * 
     * Da tabela 'loja'
     * 
     * @param loja
     * @return 
     */
    public static List<Object[]> getRegistroP1(int loja) {
        String sQuery = "SELECT /*02*/LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS cnpj,\n" +
"       /*03*/RPAD(REGEXP_REPLACE(loja.ie, '[^0-9]', '', 'g'), 14, ' ') AS inscricao_estadual,\n" +
"       /*04*/RPAD(' ', 14, ' ') AS inscricao_municipal,\n" +
"       /*05*/RPAD(UPPER(loja.nome), 50, ' ') AS razao_social\n" +
"FROM loja\n" +
"WHERE loja.numero = "+ loja+ ";";
        Query query = sessionBR.createSQLQuery(sQuery).
                                addScalar("cnpj", Hibernate.STRING).
                                addScalar("inscricao_estadual", Hibernate.STRING).
                                addScalar("inscricao_municipal", Hibernate.STRING).
                                addScalar("razao_social", Hibernate.STRING);
        boolean ok = false;
        List<Object[]> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    
    /**
     * Tabelas mapeadas nesta funçao
     * 
     * [0]: tipo_registro
     * [1]: cnpj
     * [2]: codigo_produto_servico
     * [3]: descricao_produto_servico
     * [4]: unidade
     * [5]: iat
     * [6]: ippt
     * [7]: situacao_tributaria
     * [8]: aliquota
     * [9]: valor_unitario
     * 
     * 
     * @param loja
     * @return 
     */
    public static List<Object[]> getRegistroP2(int loja) {
        String sQuery = "SELECT * FROM (\n" +
"\n" +
"(SELECT /*01*/'P2' AS tipo_registro," +
"/*02*/LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS cnpj,\n" +
"       /*03*/RPAD(CAST(controle_estoque.codigo as TEXT), 14, ' ') AS codigo_produto_servico,\n" +
"       /*04*/RPAD(UPPER((produto_grupo.grupo || ' ' || produto_subgrupo.subgrupo || ' ' || produto_marca.nome_exibicao || ' ' || produto_modelo.nome)), 50, ' ') AS descricao_produto_servico,\n" +
"       /*05*/RPAD(controle_estoque.unidade_medida, 6, ' ') AS unidade,\n" +
"       /*06*/RPAD(controle_estoque.arredondamento, 1, ' ') AS iat,\n" +
"       /*07*/RPAD(controle_estoque.producao, 1, ' ') AS ippt,\n" +
"       /*08*/RPAD(controle_estoque.situacao_tributaria, 1, ' ') AS situacao_tributaria,\n" +
"       /*09*/LPAD('0', 4, '0') AS aliquota,\n" +
"       /*10*/LPAD(CAST(CAST(rp2.preco_produto as NUMERIC) as TEXT), 12, '0') AS valor_unitario\n" +
"FROM sinetic_controle_estoque controle_estoque, loja, produtos, produto_grupo, produto_subgrupo, produto_marca, sinetic_produto_modelo produto_modelo,\n" +
"     (SELECT controle_estoque.codigo AS codigo_produto, MAX(produtos_precificacao.preco_sugerido) AS preco_produto\n" +
"      FROM sinetic_controle_estoque controle_estoque, sinetic_controle_estoque_rastreio controle_estoque_rastreio, produtos_precificacao\n" +
"      WHERE controle_estoque.codigo = controle_estoque_rastreio.codigo_controle_estoque\n" +
"            AND controle_estoque_rastreio.codigo_produto = produtos_precificacao.cod_produto\n" +
"      GROUP BY controle_estoque.codigo) rp2\n" +
"WHERE loja.numero = "+loja+ " AND -- nÃºmero da loja cujos dados serÃ£o usados.\n" +
"      -- inÃ­cio descriÃ§Ã£o\n" +
"      controle_estoque.codigo_produto_referencia = produtos.cod_referencia AND\n" +
"      produtos.grupo = produto_grupo.codigo AND\n" +
"      produtos.subgrupo = produto_subgrupo.codigo AND\n" +
"      produtos.cod_marca = produto_marca.codigo AND\n" +
"      produtos.cod_modelo = produto_modelo.codigo AND\n" +
"      -- fim descriÃ§Ã£o\n" +
"      -- inÃ­cio loja na qual estÃ£o os produtos do estoque\n" +
"      controle_estoque.codigo IN (SELECT DISTINCT codigo_controle_estoque\n" +
"				  FROM sinetic_controle_estoque_rastreio\n" +
"				  WHERE codigo_produto IN (SELECT cod_produto\n" +
"							   FROM mov_estoque\n" +
"							   WHERE ultima_loja = "+ loja+ "/* AND ultimo_status BETWEEN 61 AND 160*/)) -- loja onde os produtos se encontram/produtos que ainda nÃ£o foram vendidos.\n" +
"      -- fim loja na qual estÃ£o os produtos do estoque\n" +
"      AND controle_estoque.codigo = rp2.codigo_produto\n" +
"GROUP BY cnpj, controle_estoque.codigo, descricao_produto_servico, unidade, iat, ippt, situacao_tributaria, aliquota, valor_unitario\n" +
"ORDER BY CAST(controle_estoque.codigo AS INTEGER))\n" +
"\n" +
"UNION\n" +
"\n" +
"(SELECT /*01*/'P2' AS tipo_registro,\n" +
"       /*02*/LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS cnpj,\n" +
"       /*03*/RPAD(CAST(servico.codigo as TEXT), 14, ' ') AS codigo_produto_servico,\n" +
"       /*04*/RPAD(UPPER(servico.descricao), 50, ' ') AS descricao_produto_servico,\n" +
"       /*05*/RPAD(servico.unidade_medida, 6, ' ') AS unidade,\n" +
"       /*06*/RPAD(servico.arredondamento, 1, ' ') AS iat,\n" +
"       /*07*/RPAD(servico.producao, 1, ' ') AS ippt,\n" +
"       /*08*/RPAD(servico.situacao_tributaria, 1, ' ') AS situacao_tributaria,\n" +
"       /*09*/LPAD('0', 4, '0') AS aliquota,\n" +
"       /*10*/LPAD(CAST(servico.valor as TEXT), 12, '0') AS valor_unitario\n" +
"FROM servico, loja\n" +
"WHERE loja.numero = "+loja+"\n" +
"ORDER BY CAST(servico.codigo AS INTEGER))\n" +
"\n" +
") produto_servico\n" +
"\n" +
"ORDER BY CAST(produto_servico.codigo_produto_servico AS INTEGER);";

        Query query = sessionBR.createSQLQuery(sQuery).
                                addScalar("tipo_registro", Hibernate.STRING).
                                addScalar("cnpj", Hibernate.STRING).
                                addScalar("codigo_produto_servico", Hibernate.STRING).
                                addScalar("descricao_produto_servico", Hibernate.STRING).
                                addScalar("unidade", Hibernate.STRING).
                                addScalar("iat", Hibernate.STRING).
                                addScalar("ippt", Hibernate.STRING).
                                addScalar("situacao_tributaria", Hibernate.STRING).
                                addScalar("aliquota", Hibernate.STRING).
                                addScalar("valor_unitario", Hibernate.STRING);
        boolean ok = false;
        List<Object[]> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    /**
     * Tabelas mapeadas nesta funçao
     * 
     * [0]: cnpj
     * [1]: inscricao_estadual
     * [2]: total_registros_p2
     * 
     * @param loja
     * @return 
     */
    public static List<Object[]> getRegistroP9(int loja) {
        String sQuery = "SELECT /*02*/LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS cnpj,\n" +
"       /*03*/RPAD(REGEXP_REPLACE(loja.ie, '[^0-9]', '', 'g'), 14, ' ') AS inscricao_estadual,\n" +
"       /*04*/LPAD(CAST((SUM(rp9_1.nprodutos + rp9_2.nservicos)) as TEXT), 6, '0') AS total_registros_p2\n" +
"       \n" +
"FROM loja,\n" +
"     ( SELECT COUNT(controle_estoque.codigo) AS nprodutos\n" +
"       FROM sinetic_controle_estoque controle_estoque\n" +
"       WHERE controle_estoque.codigo IN ( SELECT DISTINCT codigo_controle_estoque\n" +
"				          FROM sinetic_controle_estoque_rastreio controle_estoque_rastreio\n" +
"				          WHERE controle_estoque_rastreio.codigo_produto IN ( SELECT cod_produto\n" +
"											      FROM mov_estoque\n" +
"											      WHERE ultima_loja = "+loja+"/* AND ultimo_status BETWEEN 61 AND 160*/)) ) rp9_1,\n" +
"     ( SELECT COUNT(servico.codigo) AS nservicos\n" +
"       FROM servico ) rp9_2\n" +
"       \n" +
"WHERE loja.numero = "+loja+"  -- nÃºmero da loja cujos dados serÃ£o usados.\n" +
"GROUP BY cnpj, inscricao_estadual;";
        Query query = sessionBR.createSQLQuery(sQuery).
                                addScalar("cnpj", Hibernate.STRING).
                                addScalar("inscricao_estadual", Hibernate.STRING).
                                addScalar("total_registros_p2", Hibernate.STRING);
        boolean ok = false;
        List<Object[]> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    /**
     * Tabelas mapeadas nesta funçao
     * 
     * 
     * @param loja
     * @return 
     */
    public static List<Object[]> getRegistroH1(int loja) {
        String sQuery = "SELECT\n" +
"	LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS \"02\",\n" +
"	RPAD(REGEXP_REPLACE(loja.ie, '[^0-9]', '', 'g'), 14, ' ') AS \"03\",\n" +
"	RPAD(REGEXP_REPLACE('', '[^0-9]', '', 'g'), 14, ' ') AS \"04\",\n" +
"	RPAD(UPPER(loja.nome), 50, ' ') AS \"05\",\n" +
"	RPAD(''/*lojas_ecfs.numero_fabricacao*/, 20, ' ') AS \"06\",\n" +
"	RPAD('', 01, ' ') AS \"07\",\n" +
"	RPAD('', 07, ' ') AS \"08\",\n" +
"	RPAD('', 20, ' ') AS \"09\",\n" +
"	RPAD('', 20, ' ') AS \"10\"\n" +
"FROM loja\n" +
"WHERE loja.numero = "+ loja+ ";";
        Query query = sessionBR.createSQLQuery(sQuery).
                                addScalar("02", Hibernate.STRING).
                                addScalar("03", Hibernate.STRING).
                                addScalar("04", Hibernate.STRING).
                                addScalar("05", Hibernate.STRING).
                                addScalar("06", Hibernate.STRING).
                                addScalar("07", Hibernate.STRING).
                                addScalar("08", Hibernate.STRING).
                                addScalar("09", Hibernate.STRING).
                                addScalar("10", Hibernate.STRING);
        boolean ok = false;
        List<Object[]> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    /**
     * Tabelas mapeadas nesta funçao
     * 
     * 
     * @param loja
     * @return 
     */
    public static List<Object[]> getRegistroH2(int loja) {
        String sQuery = "SELECT\n" +
"	CAST('H2' as TEXT) AS \"01\",\n" +
"	LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS \"02\",\n" +
"	LPAD(REGEXP_REPLACE(crediario_venda_pagamento.coo, '[^0-9]', '', 'g'), 06, '0') AS \"03\",\n" +
"	LPAD(REGEXP_REPLACE(crediario_venda_pagamento.ccf, '[^0-9]', '', 'g'), 06, '0') AS \"04\",\n" +
"	LPAD(CAST(CAST((crediario_venda_pagamento.valor - pre_venda.valor_final) as NUMERIC(8,2)) as TEXT), 13, '0') AS \"05\",\n" +
"	TO_CHAR(pre_venda.finalizado_em, 'YYYYMMDD') AS \"06\",\n" +
"	LPAD(REGEXP_REPLACE(crediario_cliente.cpf, '[^0-9]', '', 'g'), 14, '0') AS \"07\",\n" +
"	LPAD(REGEXP_REPLACE('', '[^0-9]', '', 'g'), 07, '0') AS \"08\"	\n" +
"	\n" +
"FROM\n" +
"	loja, pre_venda, crediario_venda_pagamento, crediario_forma_pagamento_detalhado, crediario_cliente\n" +
"\n" +
"WHERE\n" +
"	loja.numero = "+loja+"\n" +
"	AND loja.numero = pre_venda.codigo_loja\n" +
"	AND pre_venda.codigo_cliente = crediario_cliente.codigo\n" +
"	AND crediario_forma_pagamento_detalhado.cod_bandeira IS NOT NULL AND crediario_forma_pagamento_detalhado.cod_operadora IS NOT NULL\n" +
"	AND crediario_venda_pagamento.cod_forma_pagamento_detalhado = crediario_forma_pagamento_detalhado.codigo\n" +
"	AND (crediario_venda_pagamento.valor - pre_venda.valor_final) > 0\n" +
"\n" +
"UNION\n" +
"\n" +
"SELECT\n" +
"	CAST('H2' as TEXT) AS \"01\",\n" +
"	LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS \"02\",\n" +
"	LPAD(REGEXP_REPLACE(crediario_venda_parcelamento.coo, '[^0-9]', '', 'g'), 06, '0') AS \"03\",\n" +
"	LPAD(REGEXP_REPLACE(crediario_venda_parcelamento.ccf, '[^0-9]', '', 'g'), 06, '0') AS \"04\",\n" +
"	LPAD(CAST(CAST((crediario_venda_parcelamento.parcela_valor - crediario_venda_parcelamento.recebimento_valor) as NUMERIC(8,2)) as TEXT), 13, '0') AS \"05\",\n" +
"	TO_CHAR(crediario_venda_parcelamento.recebimento_data, 'YYYYMMDD') AS \"06\",\n" +
"	LPAD(REGEXP_REPLACE(crediario_cliente.cpf, '[^0-9]', '', 'g'), 14, '0') AS \"07\",\n" +
"	LPAD(REGEXP_REPLACE('', '[^0-9]', '', 'g'), 07, '0') AS \"08\"	\n" +
"	\n" +
"FROM\n" +
"	loja, pre_venda, crediario_venda_parcelamento, crediario_forma_pagamento_detalhado, crediario_cliente\n" +
"\n" +
"WHERE\n" +
"	loja.numero = "+loja+"\n" +
"	AND loja.numero = pre_venda.codigo_loja\n" +
"	AND pre_venda.codigo = crediario_venda_parcelamento.codigo_venda\n" +
"	AND pre_venda.codigo_cliente = crediario_cliente.codigo\n" +
"	AND crediario_forma_pagamento_detalhado.cod_bandeira IS NOT NULL AND crediario_forma_pagamento_detalhado.cod_operadora IS NOT NULL\n" +
"	AND crediario_venda_parcelamento.cod_forma_pagamento_detalhado = crediario_forma_pagamento_detalhado.codigo\n" +
"	AND (crediario_venda_parcelamento.parcela_valor - crediario_venda_parcelamento.recebimento_valor) > 0";

        Query query = sessionBR.createSQLQuery(sQuery).
                                addScalar("01", Hibernate.STRING).
                                addScalar("02", Hibernate.STRING).
                                addScalar("03", Hibernate.STRING).
                                addScalar("04", Hibernate.STRING).
                                addScalar("05", Hibernate.STRING).
                                addScalar("06", Hibernate.STRING).
                                addScalar("07", Hibernate.STRING).
                                addScalar("08", Hibernate.STRING);
        boolean ok = false;
        List<Object[]> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    
    /**
     * Tabelas mapeadas nesta funçao
     * 
     * 
     * @param loja
     * @return 
     */
    public static List<Object[]> getRegistroH9(int loja) {
        String sQuery = "SELECT \n" +
"	CAST('H9' as TEXT) AS \"01\",\n" +
"	LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS \"02\",\n" +
"	RPAD(REGEXP_REPLACE(loja.ie, '[^0-9]', '', 'g'), 14, ' ') AS \"03\",\n" +
"	RPAD(REGEXP_REPLACE('', '[^0-9]', '', 'g'), 14, ' ') AS \"04\",\n" +
"	COUNT(inner_table.*) AS \"05\"\n" +
"\n" +
"FROM loja, (\n" +
"\n" +
"SELECT\n" +
"	LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS \"02\",\n" +
"	LPAD(REGEXP_REPLACE(''/*lojas_ecfs.coo*/, '[^0-9]', '', 'g'), 06, '0') AS \"03\",\n" +
"	LPAD(REGEXP_REPLACE(''/*lojas_ecfs.ccf*/, '[^0-9]', '', 'g'), 06, '0') AS \"04\",\n" +
"	LPAD(CAST(CAST((crediario_venda_pagamento.valor - pre_venda.valor_final) as NUMERIC(8,2)) as TEXT), 13, '0') AS \"05\",\n" +
"	TO_CHAR(pre_venda.finalizado_em, 'YYYYMMDD') AS \"06\",\n" +
"	LPAD(REGEXP_REPLACE(crediario_cliente.cpf, '[^0-9]', '', 'g'), 14, '0') AS \"07\",\n" +
"	LPAD(REGEXP_REPLACE('', '[^0-9]', '', 'g'), 07, '0') AS \"08\"	\n" +
"	\n" +
"FROM\n" +
"	loja, pre_venda, crediario_venda_pagamento, crediario_forma_pagamento_detalhado, crediario_cliente\n" +
"\n" +
"WHERE\n" +
"	loja.numero = "+loja+"\n" +
"	AND loja.numero = pre_venda.codigo_loja\n" +
"	AND pre_venda.codigo_cliente = crediario_cliente.codigo\n" +
"	AND crediario_forma_pagamento_detalhado.cod_bandeira IS NOT NULL AND crediario_forma_pagamento_detalhado.cod_operadora IS NOT NULL\n" +
"	AND crediario_venda_pagamento.cod_forma_pagamento_detalhado = crediario_forma_pagamento_detalhado.codigo\n" +
"	AND (crediario_venda_pagamento.valor - pre_venda.valor_final) > 0\n" +
"\n" +
"UNION\n" +
"\n" +
"SELECT\n" +
"	LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS \"02\",\n" +
"	LPAD(REGEXP_REPLACE(''/*lojas_ecfs.coo*/, '[^0-9]', '', 'g'), 06, '0') AS \"03\",\n" +
"	LPAD(REGEXP_REPLACE(''/*lojas_ecfs.ccf*/, '[^0-9]', '', 'g'), 06, '0') AS \"04\",\n" +
"	LPAD(CAST(CAST((crediario_venda_parcelamento.parcela_valor - crediario_venda_parcelamento.recebimento_valor) as NUMERIC(8,2)) as TEXT), 13, '0') AS \"05\",\n" +
"	TO_CHAR(crediario_venda_parcelamento.recebimento_data, 'YYYYMMDD') AS \"06\",\n" +
"	LPAD(REGEXP_REPLACE(crediario_cliente.cpf, '[^0-9]', '', 'g'), 14, '0') AS \"07\",\n" +
"	LPAD(REGEXP_REPLACE('', '[^0-9]', '', 'g'), 07, '0') AS \"08\"	\n" +
"	\n" +
"FROM\n" +
"	loja, pre_venda, crediario_venda_parcelamento, crediario_forma_pagamento_detalhado, crediario_cliente\n" +
"\n" +
"WHERE\n" +
"	loja.numero = "+loja+"\n" +
"	AND loja.numero = pre_venda.codigo_loja\n" +
"	AND pre_venda.codigo = crediario_venda_parcelamento.codigo_venda\n" +
"	AND pre_venda.codigo_cliente = crediario_cliente.codigo\n" +
"	AND crediario_forma_pagamento_detalhado.cod_bandeira IS NOT NULL AND crediario_forma_pagamento_detalhado.cod_operadora IS NOT NULL\n" +
"	AND crediario_venda_parcelamento.cod_forma_pagamento_detalhado = crediario_forma_pagamento_detalhado.codigo\n" +
"	AND (crediario_venda_parcelamento.parcela_valor - crediario_venda_parcelamento.recebimento_valor) > 0 ) inner_table\n" +
"\n" +
"WHERE loja.numero = "+loja+"\n" +
"\n" +
"GROUP BY loja.cnpj, loja.ie";
        Query query = sessionBR.createSQLQuery(sQuery).
                                addScalar("01", Hibernate.STRING).
                                addScalar("02", Hibernate.STRING).
                                addScalar("03", Hibernate.STRING).
                                addScalar("04", Hibernate.STRING).
                                addScalar("05", Hibernate.STRING);
        boolean ok = false;
        List<Object[]> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    
    /**
     * Tabelas mapeadas nesta funçao
     * 
     * 
     * @param loja
     * @return 
     */
    public static List<Object[]> getRegistroH9Zerado(int loja) {
        String sQuery = "SELECT \n" +
"	CAST('H9' as TEXT) AS \"01\",\n" +
"	LPAD(REGEXP_REPLACE(loja.cnpj, '[^0-9]', '', 'g'), 14, '0') AS \"02\",\n" +
"	RPAD(REGEXP_REPLACE(loja.ie, '[^0-9]', '', 'g'), 14, ' ') AS \"03\",\n" +
"	RPAD(REGEXP_REPLACE('', '[^0-9]', '', 'g'), 14, ' ') AS \"04\",\n" +
"	CAST('000000' as TEXT) AS \"05\"\n" +
"FROM loja\n" +
"WHERE loja.numero = "+loja+";";
        Query query = sessionBR.createSQLQuery(sQuery).
                                addScalar("01", Hibernate.STRING).
                                addScalar("02", Hibernate.STRING).
                                addScalar("03", Hibernate.STRING).
                                addScalar("04", Hibernate.STRING).
                                addScalar("05", Hibernate.STRING);
        boolean ok = false;
        List<Object[]> list = null;
        while (!ok) {
            try {
                list = query.list();
                ok = true;
            }
            catch (JDBCConnectionException e) {
                if (!Controller.confirmBox("Erro ao conectar com o Banco de Dados Central.\n"
                                         + "É recomendado tentar novamente.\n"
                                         + "Deseja tentar novamente?")) {
                    list = new ArrayList<>();
                    ok = true;
                }
            }
        }
        
        return list;
    }
    
    
    ////////BANCO LOCAL ///////////////////
    
    public static List<AliquotasImpressora> getAliquotas() {
        Query query = sessionBL.createQuery("SELECT a FROM AliquotasImpressora a ORDER BY a.id ASC");
        List<AliquotasImpressora> list = query.list();
        
        return list;
    }
    
    public static AliquotasImpressora getAliquota(float aliquota, int trib) {
        AliquotasImpressora ai = null;
        
        boolean iss = false;
        boolean icms = false;
        
        if (trib == Control.TRIB_ICMS) {
            icms = true;
        }
        else if (trib == Control.TRIB_ISS) {
            iss = true;
        }
        else {
            return null;
        }
        
        Query query = sessionBL.createQuery("SELECT a FROM AliquotasImpressora a WHERE a.aliquota = "+aliquota+ " "
                                            + "and a.icms is "+(icms?"true":"false") + " and a.iss is "+(iss?"true":"false"));
        List<AliquotasImpressora> list = query.list();
        
        for (AliquotasImpressora a:list) {
            ai = a;
            break;
        }
        
        return ai;
    }
    
    public static int gravaAliquota(float aliquota, int trib) throws Exception{
        Transaction t = sessionBL.beginTransaction();
        
        if (aliquota <= 0) {
            throw new Exception ("Aliquota não pode ser negativa.");
        }
        
        boolean iss = false;
        boolean icms = false;
        
        if (trib == Control.TRIB_ICMS) {
            icms = true;
        }
        else if (trib == Control.TRIB_ISS) {
            iss = true;
        }
        else {
            throw new Exception ("Erro ao gravar tributação");
        }
        
        AliquotasImpressora ai = new AliquotasImpressora();
        ai.setAliquota((double)aliquota);
        ai.setIcms(icms);
        ai.setIss(iss);
        
        try {
            sessionBL.save(ai);
            
            t.commit();
            if (t.wasCommitted()) {
                hqBL.refresh(ai);
                System.out.println("rg.getID(): " + ai.getId());
                return ai.getId();
            }
            else {
                throw new Exception ("Erro ao gravar Aliquota no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar Aliquota no banco");
        }
    }
    
    public static ConfiguracoesSistema getConfiguracoes() {
        
        ConfiguracoesSistema cs = null;
        
        Query query = sessionBL.createQuery("SELECT a FROM ConfiguracoesSistema a WHERE a.id = 1");
        List<ConfiguracoesSistema> listCS = query.list();
        
        for (ConfiguracoesSistema a:listCS) {
            cs = a;
            break;
        }
        if (cs != null) {
            cs.setMensagemPromocional(cs.getMensagemPromocional().trim());
            cs.setPortaSerial(cs.getPortaSerial().trim());
        }
        
        return cs;
    }
    
   
    public static EcfRelatoriosGerenciais getRelatorioGerencial(int tipo) {
        EcfRelatoriosGerenciais rg = null;
        
        Query query = sessionBL.createQuery("SELECT a FROM EcfRelatoriosGerenciais a WHERE a.tipo = "+tipo);
        List<EcfRelatoriosGerenciais> listRG = query.list();
        
        for (EcfRelatoriosGerenciais a:listRG) {
            rg = a;
        }
        
        return rg;
    }
    
    public static int gravaRelatorioGerencial(EcfRelatoriosGerenciais rg) throws Exception{
        Transaction t = sessionBL.beginTransaction();
        
        if (rg == null) {
            throw new Exception ("Erro ao gravar Relatório Gerencial no banco");
        }
        
        try {
            sessionBL.save(rg);
            
            t.commit();
            if (t.wasCommitted()) {
                hqBL.refresh(rg);
                System.out.println("rg.getID(): " + rg.getId());
                return rg.getId();
            }
            else {
                throw new Exception ("Erro ao gravar Relatório Gerencial no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar Relatório Gerencial no banco");
        }
    }
    
    public static DiaFiscal getUltimoDiaFiscalAberto() {
        DiaFiscal df = null;
        
        Query query = sessionBL.createQuery("SELECT a FROM DiaFiscal a "
                                            + "WHERE a.abreCaixa is NULL or a.fechaCaixa is NULL "
                                            + "ORDER BY a.id DESC");
        List<DiaFiscal> listDF = query.list();
        
        //Pega o dia de hoje
        //@todo melhorar isto.
        Calendar cToday = new GregorianCalendar();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");   
        Date data = null;
        try {
             data = format.parse(""+cToday.get(Calendar.DAY_OF_MONTH)+ "/"+
                                    (cToday.get(Calendar.MONTH)+1) + "/" +
                                    cToday.get(Calendar.YEAR));
        }
        catch (Exception e) {
            return null;
        }
        
        for (DiaFiscal d: listDF) {
            Date d1 = d.getDataHora();
            if (data.compareTo(d1) > 0) {
                df = d;
                break;
            }
        }
        
        return df;
    }
    
    public static DiaFiscal getDiaFiscal(long ctm) {
        Date givenDate = new Date(ctm);
        Calendar dtIni = Calendar.getInstance();
        Calendar dtEnd = Calendar.getInstance();
        try{
            dtIni.setTime(givenDate);
            dtIni.set(Calendar.HOUR_OF_DAY, 0);  
            dtIni.set(Calendar.MINUTE, 0);  
            dtIni.set(Calendar.SECOND, 1);  
            dtIni.set(Calendar.MILLISECOND, 0);  
            
            dtEnd.setTime(givenDate);
            dtEnd.set(Calendar.HOUR_OF_DAY, 23);  
            dtEnd.set(Calendar.MINUTE, 59);  
            dtEnd.set(Calendar.SECOND, 59);  
            dtEnd.set(Calendar.MILLISECOND, 999);
        }catch(Exception e){
            return null;
        }
        DiaFiscal df = null;
        
        Query query = sessionBL.createQuery("SELECT a FROM DiaFiscal a "
                + "WHERE a.dataHora >= :dtIni AND a.dataHora <= :dtEnd");
        
        query.setParameter("dtIni", dtIni.getTime());
        query.setParameter("dtEnd", dtEnd.getTime());
        List<DiaFiscal> listDF = query.list();
        
        for (DiaFiscal a:listDF) {
            df = a;
        }
        
        return df;
    }
    
    public static int gravaDiaFiscal(DiaFiscal df) throws Exception {
        Transaction t = null;
        
        AbreCaixa ac = df.getAbreCaixa();
        if (ac != null) {
            t = sessionBL.beginTransaction();
            sessionBL.save(ac);
            
            t.commit();
            if (t.wasCommitted()) {
                hqBL.refresh(ac);
            }
            else {
                throw new Exception ("Erro ao gravar dia fiscal no banco");
            }
        }
        df.setAbreCaixa(ac);
        
        FechaCaixa fc = df.getFechaCaixa();
        if (fc != null) {
            t = sessionBL.beginTransaction();
            sessionBL.save(fc);
            
            t.commit();
            if (t.wasCommitted()) {
                hqBL.refresh(fc);
            }
            else {
                throw new Exception ("Erro ao gravar dia fiscal no banco");
            }
        }
        df.setFechaCaixa(fc);
        
        t = sessionBL.beginTransaction();
        try {
            sessionBL.save(df);
            
            t.commit();
            if (t.wasCommitted()) {
                hqBL.refresh(df);
                return df.getId();
            }
            else {
                throw new Exception ("Erro ao gravar dia fiscal no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar dia fiscal no banco");
        }
    }
    
    public static boolean atualizaDiaFiscal(DiaFiscal df) {
        Transaction t = sessionBL.beginTransaction();
        
        try {
            sessionBL.update(df);
            t.commit();
        }
        catch (Exception e) {
            t.rollback();
            return false;
        }
        
        return true;
    }
    
    public static int gravaFechaCaixa(FechaCaixa fc) throws Exception {
        Transaction t = sessionBL.beginTransaction();
        
        if (fc == null) {
            throw new Exception("Erro ao gravar Fechamento do Caixa no banco");
        }
        
        
        try {
            sessionBL.save(fc);
            
            t.commit();
            if (t.wasCommitted()) {
                hqBL.refresh(fc);
                return fc.getId();
            }
            else {
                throw new Exception ("Erro ao gravar Fechamento do Caixa no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar Fechamento do Caixa no banco");
        }
    }
    

    public static int gravaAbreCaixa(AbreCaixa ac) throws Exception {
        Transaction t = sessionBL.beginTransaction();
        
        if (ac == null) {
            throw new Exception("Erro ao gravar Abertura do Caixa no banco");
        }
        
        try {
            sessionBL.save(ac);
            t.commit();
            
            if (t.wasCommitted()) {
                hqBL.refresh(ac);
                return ac.getId();
            }
            else {
                throw new Exception ("Erro ao gravar Abertura do Caixa no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar Abertura do Caixa no banco");
        }
    }
        
    public static int gravaReducaoZ(long tm) throws Exception {
        return gravaReducaoZ(DBControl.getDiaFiscal(tm), tm);
    }
    
    public static int gravaReducaoZ(DiaFiscal df, long tm) throws Exception {
        Transaction t = sessionBL.beginTransaction();
        
        if (df == null) {
            throw new Exception("Erro ao gravar Redução Z no banco");
        }
        
        EcfReducoesz er = new EcfReducoesz();
        er.setDataHora(new Date(tm));
        er.setDiaFiscal(df);
        
        
        try {
            sessionBL.save(er);
            t.commit();
            
            if (t.wasCommitted()) {
                hqBL.refresh(er);
                return er.getId();
            }
            else {
                throw new Exception ("Erro ao gravar Redução Z no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar Redução Z no banco");
        }
    }
    
    public static EcfReducoesz getUltimaReducaoZ() {
        EcfReducoesz erz = null;
        
        Query query = sessionBL.createQuery("SELECT a FROM EcfReducoesz a ORDER BY a.dataHora DESC");
        List<EcfReducoesz> listERZ = query.list();
        
        for (EcfReducoesz a:listERZ) {
            erz = a;
            return erz;
        }
        
        return erz;
    }
    
    public static EcfReducoesz getPenultimaReducaoZ() {
        EcfReducoesz erz = null;
        
        Query query = sessionBL.createQuery("SELECT a FROM EcfReducoesz a ORDER BY a.dataHora DESC");
        List<EcfReducoesz> listERZ = query.list();
        
        int i = 0;
        for (EcfReducoesz a:listERZ) {
            if (i == 1) {
                erz = a;
                return erz;
            }
            i++;
        }
        
        return erz;
    }
    
    public static int gravaLeituraX(long tm) throws Exception {
        Transaction t = sessionBL.beginTransaction();
        
        EcfLeiturasx el = new EcfLeiturasx();
        el.setDataHora(new Date(tm));
        
        try {
            sessionBL.save(el);
            t.commit();
            
            if (t.wasCommitted()) {
                hqBL.refresh(el);
                return el.getId();
            }
            else {
                throw new Exception ("Erro ao gravar Leitura X no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar Leitura X no banco");
        }
    }
    
    public static boolean atualizarConfiguracoes(ConfiguracoesSistema cs) {
        Transaction t = sessionBL.beginTransaction();
        
        try {
            sessionBL.update(cs);
            t.commit();
        }
        catch (Exception e) {
            t.rollback();
            return false;
        }
        
        return true;
    }
    
    /**
     * Pega pré-venda pendente da última sessão do sistema.
     * Esta função é usada para CANCELAR uma pré-venda que ficou em aberto
     * e/ou para cancelar seu CUPOM FISCAL.
     * Não é usada para o caminho natural da venda.
     * @return 
     */
    public static PrevendaAberta getPreVendaPendente() {
        PrevendaAberta pva = null;
        Query query = sessionBL.createQuery("SELECT a FROM PrevendaAberta a ORDER BY a.id ASC");
        List<PrevendaAberta> listPVA = query.list();
        
        for (PrevendaAberta a:listPVA) {
            pva = a;
        }
        
        return pva;
    }
    
    public static PrevendaAberta getPreVendaPendente(int id) {
        PrevendaAberta pva = null;
        Query query = sessionBL.createQuery("SELECT a FROM PrevendaAberta a WHERE a.id = " + id);
        List<PrevendaAberta> listPVA = query.list();
        
        for (PrevendaAberta a:listPVA) {
            pva = a;
            break;
        }
        
        return pva;
    }
    
    public static PrevendaAberta getPreVendaPendenteByFK(int fkBrPrevendaId) {
        PrevendaAberta pva = null;
        Query query = sessionBL.createQuery("SELECT a FROM PrevendaAberta a WHERE fk_br_prevenda_id = "+fkBrPrevendaId);
        List<PrevendaAberta> listPVA = query.list();
        
        for (PrevendaAberta a:listPVA) {
            pva = a;
        }
        
        return pva;
    }
    
    public static boolean removePreVendaPendente(int id) {
        Transaction t = sessionBL.beginTransaction();
        
        PrevendaAberta pva = getPreVendaPendente(id);
        
        try {
            sessionBL.delete(pva);
            t.commit();
            
            if (!t.wasCommitted()) {
                return false;
            }
            
        }
        catch (Exception e) {
            return false;
        }
        
        return true;
    }
    
    public static boolean atualizaPreVendaPendente(PrevendaAberta pva) {
        Transaction t = sessionBL.beginTransaction();
        
        try {
            sessionBL.update(pva);
            t.commit();
        }
        catch (Exception e) {
            t.rollback();
            return false;
        }
        
        return true;
    }
    
    public static int gravaPrevendaPendente(PrevendaAberta pva) throws Exception{
        Transaction t = sessionBL.beginTransaction();
        
        if (pva == null) {
            throw new Exception ("Erro ao gravar Pré-venda pendente no banco");
        }
        
        try {
            sessionBL.save(pva);
            t.commit();
            
            if (t.wasCommitted()) {
                hqBL.refresh(pva);
                return pva.getId();
            }
            else {
                throw new Exception ("Erro ao gravar Pré-venda pendente no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar Pré-venda pendente no banco");
        }
    }
    
    

    
    public static int gravaVenda(VendaPagamento vp) throws Exception{
        Transaction t = sessionBL.beginTransaction();
        
        if (vp == null) {
            throw new Exception ("Erro ao gravar Venda no banco");
        }
        
        try {
            sessionBL.save(vp);
            t.commit();
            
            if (t.wasCommitted()) {
                hqBL.refresh(vp);
                return vp.getId();
            }
            else {
                throw new Exception ("Erro ao gravar Venda no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar Venda no banco");
        }
    }
    
    public static boolean atualizaPagamentoDia(CrediarioFormaPagamentoDetalhado cfpd, float valor, int tipoPagamento) {
        PafMeiosPagamentoDia pmpd = null;
        
        Date d = Control.getDataImpressora();
        
        Calendar dtIni = Calendar.getInstance();
        Calendar dtEnd = Calendar.getInstance();
        try{
            dtIni.setTime(d);
            dtIni.set(Calendar.HOUR_OF_DAY, 0);  
            dtIni.set(Calendar.MINUTE, 0);  
            dtIni.set(Calendar.SECOND, 1);  
            dtIni.set(Calendar.MILLISECOND, 0);  
            
            dtEnd.setTime(d);
            dtEnd.set(Calendar.HOUR_OF_DAY, 23);  
            dtEnd.set(Calendar.MINUTE, 59);  
            dtEnd.set(Calendar.SECOND, 59);  
            dtEnd.set(Calendar.MILLISECOND, 999);
        }catch(Exception e){
            return Boolean.FALSE;
        }
        Query query = sessionBL.createQuery("SELECT a FROM "
                + "PafMeiosPagamentoDia a WHERE a.datahora >= :dtIni "
                + " AND a.datahora <= :dtEnd "
                + " AND a.tipoPagamento = "+tipoPagamento);
        query.setParameter("dtIni", dtIni.getTime());
        query.setParameter("dtEnd", dtEnd.getTime());
        
        List<PafMeiosPagamentoDia> listP = query.list();
        
        for (PafMeiosPagamentoDia a:listP) {
            if (a.getFkBrCodigoPagDetalhado() == cfpd.getCodigo()) {
                a.setValor(a.getValor().add(new BigDecimal(valor)));
                Transaction t = sessionBL.beginTransaction();
        
                try {
                    sessionBL.update(a);
                    t.commit();
                }
                catch (Exception e) {
                    t.rollback();
                    return false;
                }

                return true;
            }
        }
        
        //Não achou nenhum meio de pagamento deste tipo no dia.
        //Então cria um novo
        pmpd = new PafMeiosPagamentoDia();
        pmpd.setDatahora(d);
        pmpd.setFkBrCodigoPagDetalhado(cfpd.getCodigo());
        pmpd.setValor(new BigDecimal(valor));
        pmpd.setTipoDocumento(getTipoDocumento(tipoPagamento));
        Transaction t = sessionBL.beginTransaction();
        
        try {
            sessionBL.save(pmpd);
            t.commit();
        }
        catch (Exception e) {
            t.rollback();
            return false;
        }

        return true;
    }
    
    public static TipoDocumento getTipoDocumento(int id ) {
        TipoDocumento td = null;
        Query query = sessionBL.createQuery("SELECT a FROM TipoDocumento a WHERE a.id = "+id);
        List<TipoDocumento> listTD = query.list();
        
        for (TipoDocumento a:listTD) {
            td = a;
            break;
        }
        
        return td;
    }
    
    
    public static List<EcfExecutaveis> getEcfExecutaveis() {
        Query query = sessionBL.createQuery("SELECT a FROM EcfExecutaveis a ORDER BY a.id ASC");
        List<EcfExecutaveis> listEE = query.list();
        
        return listEE;
    }
    
    
    
    public static boolean atualizarEcfExecutaveis(List<EcfExecutaveis> lEE) {
        Transaction t = sessionBL.beginTransaction();
        
        try {
            for (EcfExecutaveis ee: lEE) {
                sessionBL.update(ee);
            }
            t.commit();
        }
        catch (Exception e) {
            t.rollback();
            return false;
        }
        
        return true;
    }
    
    
    public static EcfIdentificacao getEcfIdentificacao() {
        EcfIdentificacao ei = null;
        
        Query query = sessionBL.createQuery("SELECT a FROM EcfIdentificacao a WHERE a.id = 1");
        List<EcfIdentificacao> listEI = query.list();
        
        for (EcfIdentificacao a:listEI) {
            ei = a;
        }
        
        return ei;
    }
    
    
    
    
    /**
     * Verifica se há alguma venda pendente, proveniente de um fechamento errado da aplicação
     * 
     * Retorna o Codigo da venda pendente
     * @return 
     */
    public static Integer isVendaPendente() {
        Query query = sessionBL.createQuery("SELECT a FROM Vendas a ORDER BY a.id ASC");
        List<Vendas> listV = query.list();
        
        if (listV != null) {
        
            for (Vendas a:listV) {
                if (a.getStatus() != ListaDeVendas.STATUS_FINALIZA) {
                    return a.getCodigo();
                }
            }
        }

        return null;
    }
    
    
    
            
    public static int gravaSuprimento(float lValor, long lTimestamp) throws Exception {
        Transaction t = sessionBL.beginTransaction();
        
        DiaFiscal df = DBControl.getDiaFiscal(lTimestamp);
        
        if (df == null) {
            throw new Exception("Erro ao gravar suprimento no banco. Dia fiscal não criado.");
        }
        
        EcfSuprimentos es = new EcfSuprimentos();
        es.setValor(lValor);
        es.setDataHora(new Timestamp(lTimestamp));
        es.setDiaFiscal(df);
        
        try {
            sessionBL.save(es);
            t.commit();
            
            if (t.wasCommitted()) {
                hqBL.refresh(es);
                return es.getId();
            }
            else {
                throw new Exception ("Erro ao gravar suprimento no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar suprimento no banco");
        }
    }
    
    public static boolean removeSuprimento(int id) {
        Transaction t = sessionBL.beginTransaction();
        
        EcfSuprimentos es = new EcfSuprimentos();
        es.setId(id);
        
        try {
            sessionBL.delete(es);
            t.commit();
            
            if (!t.wasCommitted()) {
                return false;
            }
            
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    /*public static int gravaAberturaDeCaixa(long tm) throws Exception {
        Transaction t = sessionBL.beginTransaction();
        
        AbreCaixa ac = new AbreCaixa();
        ac.setDataHora(new Timestamp(tm));
        
        try {
            if (!hqBL.save(ac)) {
                t.rollback();
                throw new Exception ("Erro ao gravar abertura de caixa no banco");
            }
            
            t.commit();
            if (t.wasCommitted()) {
                hqBL.refresh(ac);
                System.out.println("es.getID(): " + ac.getId());
                return ac.getId();
            }
            else {
                throw new Exception ("Erro ao gravar abertura de caixa no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar abertura de caixa no banco");
        }
    }*/
    
    
    public static int gravaSangria(float lValor, long lTimestamp) throws Exception {
        Transaction t = sessionBL.beginTransaction();
        DiaFiscal df = DBControl.getDiaFiscal(lTimestamp);
        
        if (df == null) {
            throw new Exception("Erro ao gravar sangria no banco. Dia fiscal não criado.");
        }
        
        EcfSangrias es = new EcfSangrias();
        es.setValor(lValor);
        es.setDataHora(new Timestamp(lTimestamp));
        es.setDiaFiscal(df);
        
        try {
            sessionBL.save(es);
            t.commit();
            
            if (t.wasCommitted()) {
                hqBL.refresh(es);
                return es.getId();
            }
            else {
                throw new Exception ("Erro ao gravar sangria no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar sangria no banco");
        }
    }
    
    public static int gravaSangria(EcfSangrias es) throws Exception {
        Transaction t = sessionBL.beginTransaction();
        
        try {
            sessionBL.save(es);
            t.commit();
            
            if (t.wasCommitted()) {
                hqBL.refresh(es);
                return es.getId();
            }
            else {
                throw new Exception ("Erro ao gravar sangria no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar sangria no banco");
        }
    }
    
    public static boolean removeSangria(int id) {
        Transaction t = sessionBL.beginTransaction();
        
        EcfSangrias es = new EcfSangrias();
        es.setId(id);
        
        try {
            sessionBL.save(es);
            t.commit();
            
            if (!t.wasCommitted()) {
                return false;
            }
            
        }
        catch (Exception e) {
            return false;
        }
        
        return true;
    }
    
    /*public static int gravaFechamentoDeCaixa(long tm) throws Exception {
        Transaction t = sessionBL.beginTransaction();
        
        FechaCaixa fc = new FechaCaixa();
        fc.setDataHora(new Timestamp(tm));
        
        try {
            if (!hqBL.save(fc)) {
                t.rollback();
                throw new Exception ("Erro ao gravar fechamento de caixa no banco");
            }
            
            t.commit();
            if (t.wasCommitted()) {
                hqBL.refresh(fc);
                System.out.println("es.getID(): " + fc.getId());
                return fc.getId();
            }
            else {
                throw new Exception ("Erro ao gravar fechamento de caixa no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar fechamento de caixa no banco");
        }
    }*/
    
    /**
     * Grava novo comprador no banco (ID ignorado)
     * @param comprador
     * @return
     * @throws Exception 
     */
    public static int gravaComprador(EcfComprador comprador) throws Exception {
        Transaction t = sessionBL.beginTransaction();
        
        EcfComprador ec = new EcfComprador();
        ec.setCpfCnpj(comprador.getCpfCnpj());
        ec.setEndereco(comprador.getEndereco());
        ec.setNome(comprador.getNome());
        
        try {
            sessionBL.save(ec);
            t.commit();
            
            if (t.wasCommitted()) {
                hqBL.refresh(ec);
                return ec.getId();
            }
            else {
                throw new Exception ("Erro ao gravar sangria no banco");
            }
            
        }
        catch (Exception e) {
            t.rollback();
            throw new Exception ("Erro ao gravar suprimento no banco");
        }
    }
    
    public static boolean removeComprador(int id) {
        Transaction t = sessionBL.beginTransaction();
        
        EcfComprador ec = new EcfComprador();
        ec.setId(id);
        
        try {
            sessionBL.delete(ec);
            t.commit();
            
            if (!t.wasCommitted()) {
                return false;
            }
            
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    
    
    ////REGISTROS////
    public static int gravaRegR02(PafRegR02 p) throws Exception {
        if (gravaGenerico(p)) {
            hqBL.refresh(p);
            return p.getId();
        } else {
            throw new Exception("Erro ao gravar registro R02 no banco");
        }
    }
    
    public static int gravaRegR03(PafRegR03 p) throws Exception {
        if (gravaGenerico(p)) {
            hqBL.refresh(p);
            return p.getId();
        } else {
            throw new Exception("Erro ao gravar registro R03 no banco");
        }
    }
    
    public static int gravaRegR04(PafRegR04 p) throws Exception {
        if (gravaGenerico(p)) {
            hqBL.refresh(p);
            return p.getId();
        } else {
            throw new Exception("Erro ao gravar registro R04 no banco");
        }
    }
    
    public static int gravaRegR05(PafRegR05 p) throws Exception {
        if (gravaGenerico(p)) {
            hqBL.refresh(p);
            return p.getId();
        } else {
            throw new Exception("Erro ao gravar registro R05 no banco");
        }
    }
    
    public static int gravaRegR06(PafRegR06 p) throws Exception {
        if (gravaGenerico(p)) {
            hqBL.refresh(p);
            return p.getId();
        } else {
            throw new Exception("Erro ao gravar registro R06 no banco");
        }
    }
    
    public static int gravaRegR07(PafRegR07 p) throws Exception {
        if (gravaGenerico(p)) {
            hqBL.refresh(p);
            return p.getId();
        } else {
            throw new Exception("Erro ao gravar registro R06 no banco");
        }
    }
    
    public static PafRegR02 getUltimoRegR02() {
        PafRegR02 p = null;
        Query query = sessionBL.createQuery("SELECT a FROM PafRegR02 a ORDER BY a.id DESC");
        List<PafRegR02> listP = query.list();
        
        for (PafRegR02 a:listP) {
            p = a;
            break;
        }
        
        return p;
    }
    
    
    //// Genéricos ////
    public static boolean gravaGenerico(Serializable s) {
        Transaction t = sessionBL.beginTransaction();

        try {
            sessionBL.save(s);
            t.commit();

            if (t.wasCommitted()) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            t.rollback();
            return false;
        }
    }
    
    public static List<PafProduct> getPafProduct(){
        Query query = sessionBL.createQuery("SELECT a FROM PafProduct a");
        //ORDER BY a.gtin DESC
        List<PafProduct> list = query.list();
        
        return list;
    }
    
    public static PafProduct getPafProduct(String gtin){
        Query query = sessionBL.createQuery("SELECT a FROM PafProduct a where a.gtin = :pgtin ");
        query.setParameter("pgtin",gtin);
        //ORDER BY a.gtin DESC
        PafProduct product = (PafProduct) query.uniqueResult();
        
        return product;
    }
    
     public static PafProduct getPafProduct(String gtin, String qtd){
        Query query = sessionBL.createQuery("SELECT a FROM PafProduct a where a.gtin = :pgtin");
        query.setParameter("pgtin",gtin);
        //ORDER BY a.gtin DESC
        PafProduct product = (PafProduct) query.uniqueResult();
        
        return product;
    }
    
}
