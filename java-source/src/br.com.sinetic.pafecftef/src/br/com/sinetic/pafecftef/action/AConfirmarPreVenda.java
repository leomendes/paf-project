/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.action;

import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.entities.EcfComprador;
import br.com.sinetic.pafecftef.entities.PafProduct;
import br.com.sinetic.pafecftef.entities.PrevendaAberta;
import br.com.sinetic.pafecftef.remoteentities.CrediarioCliente;
import br.com.sinetic.pafecftef.remoteentities.PreVenda;
import br.com.sinetic.pafecftef.remoteentities.PreVendaProduto;
import br.com.sinetic.pafecftef.remoteentities.PreVendaServico;
import br.com.sinetic.pafecftef.remoteentities.Servico;
import br.com.sinetic.pafecftef.remoteentities.SineticControleEstoque;
import br.com.sinetic.pafecftef.ui.UIConfirmaPreVenda;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JPanel;

/**
 *
 * @author Windows8
 */
public class AConfirmarPreVenda implements PanelAction {
    
    
    private UIConfirmaPreVenda uiCPV;
    
    private PreVenda actualPV;
    
    private EcfComprador comprador;
    
    public AConfirmarPreVenda() {
        uiCPV = new UIConfirmaPreVenda();
        uiCPV.setAction(this);
        
    }
    
    public boolean validaGerente() {
        //@todo fazer validação do gerente
        return true;
    }
    
    public boolean concedeDesconto(float d) {
        if (d >= actualPV.getValorInicial().floatValue()) {
            return false;
        }
        else if (d <= 0) {
            return false;
        }
        
        //Seta o desconto no objeto da tabela
        actualPV.setDescontoValor(new BigDecimal(d));
        actualPV.setValorFinal(new BigDecimal(actualPV.getValorInicial().floatValue() - d));
        
        if  (!DBControl.atualizaPreVenda(actualPV)) {
            return false;
        }
        
        //Atualiza a UI com o novo desconto
        uiCPV.setDesconto(d);
        
        //Atualiza os totalizadores
        uiCPV.atualizaTotal(actualPV);
        
        return true;
    }
    
    public boolean atualizaPreVenda() {
        if (actualPV == null) {
            return false;
        }
        return DBControl.atualizaPreVenda(actualPV);
    }
    
    public boolean validaGerente(String user, String senha) {
        if (user.isEmpty() || senha.isEmpty()) {
            return false;
        }
        
        return DBControl.validaGerente(user, senha);
    }
    
    /**
     * Método que CARREGA os componentes iniciais da tela
     * Deve ser usado apenas no início da tela.
     * @return Sucesso/Insucesso
     */
    public boolean carregarTela(int codPV) {
        PreVenda pv = DBControl.getPreVenda(codPV);
        
        if (pv == null) {
            return false;
        }
        
        //Seta a pré-venda atual
        actualPV = pv;
        
        //Cria pré-venda pendente no banco local
        Control.setaVendaPendente(actualPV.getCodigo(), Control.PV_STATUS_INICIADA);
        
        //Carrega a lista da pré-venda na tabela
        carregaPreVenda(actualPV);
        
        identificaComprador(actualPV.getCrediarioCliente());
        
        //Captura a pré-venda
        actualPV.setEmProgresso(true);
        DBControl.atualizaPreVenda(actualPV);
        
        Control.setaVendaPendente(actualPV.getCodigo(), Control.PV_STATUS_CAPTURADA);
        
        try {
            uiCPV.setVisible(true);
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    public PreVenda getPreVenda() {
        return actualPV;
    }
    
    public EcfComprador getComprador() {
        return comprador;
    }
    
    public boolean descarregarTela() {
        actualPV = null;
        comprador = null;
        uiCPV.descarregarTela();
        return true;
    }
    
    //Atualiza a listagem de vendas da lista
    private boolean carregaPreVenda (PreVenda pv) {
        
        if (pv == null) {
            System.out.println("Erro ao carregar pré-venda");
            return false;
        }
        
        //Seta a lista para a venda buscada
        uiCPV.setPreVenda(pv);
        
        return true;
        
    }
    
    private void identificaComprador(CrediarioCliente cc) {
        if (cc == null) {
            comprador = null;
            return;
        }
        
        //Evita passar nulos para a identificação
        String rua = cc.getEndRua()==null?"":cc.getEndRua();
        String num = cc.getEndNumero()==null?"":String.valueOf(cc.getEndNumero());
        String comp = cc.getEndComplemento()==null?"":cc.getEndComplemento();
        String bairro = cc.getBairro()==null?"":cc.getBairro();
        String cidade = cc.getCidade()==null?"":cc.getCidade();
        String estado = cc.getEstado()==null?"":cc.getEstado();
        
        String nome = cc.getNome()==null?"":cc.getNome();
        String cpf = cc.getCpf()==null?"":cc.getCpf();
        String endereco = (rua.isEmpty()?"":(rua+ ", "))+
                          (num.isEmpty()?"":(num+ ", "))+
                          (comp.isEmpty()?"":(comp+ ", "))+
                          (bairro.isEmpty()?"":(bairro+ ", "))+
                          (cidade.isEmpty()?"":(cidade+ ", "))+
                          (estado.isEmpty()?"":(estado));
        
       
        comprador = new EcfComprador();
        comprador.setCpfCnpj(cpf);
        comprador.setNome(nome);
        comprador.setEndereco(endereco);
        
        uiCPV.setComprador(comprador);
    }
    
    public void atualizaComprador(String nome, String cpfCnpj, String endereco) {
        comprador = new EcfComprador();
        comprador.setCpfCnpj(cpfCnpj);
        comprador.setNome(nome);
        comprador.setEndereco(endereco);
    }
    
    public void removeComprador() {
        comprador = null;
    }
   
    public boolean vendeItem(PafProduct product, Integer qtd){
        
        if(Control.vendeItem(product, qtd)){
            return Boolean.TRUE;
        }
        
        return Boolean.FALSE;
    }
   
    public boolean cancelaItem(int codigo) {
        //Adiciona os Produtos
        Set sPVP = actualPV.getPreVendaProdutos();
        Iterator it = sPVP.iterator();
        PreVendaProduto pvp;
        while(it.hasNext()) {
            pvp = (PreVendaProduto) it.next();
            SineticControleEstoque sce = pvp.getSineticControleEstoque();
            long cod = sce.getCodigoProdutoReferencia();
            if (codigo == cod) {
                pvp.setStatus(false);
                if (DBControl.atualizaPreVendaProduto(pvp)) {
                    //Altera a precificação da pré-venda
                    float itemValFin = pvp.getValorFinal().floatValue();
                    float valorIni = actualPV.getValorInicial().floatValue();
                    actualPV.setValorInicial(new BigDecimal(valorIni - itemValFin));
                    actualPV.setValorFinal(new BigDecimal(valorIni - itemValFin));
                    if (!DBControl.atualizaPreVenda(actualPV)) {
                        return false;
                    }
                    else {
                        //Atualiza a tabela de vendas
                        uiCPV.setPreVenda(actualPV);
                        return true;
                    }
                }
                else {
                    return false;
                }
            }
        }
        
        //Adiciona os Serviços
        Set sPVS = actualPV.getPreVendaServicos();
        it = sPVS.iterator();
        PreVendaServico pvs;
        while(it.hasNext()) {
            pvs = (PreVendaServico) it.next();
            Servico s = pvs.getServico();
            if (codigo == s.getCodigo()) {
                pvs.setStatus(false);
                if (DBControl.atualizaPreVendaServico(pvs)) { 
                    float itemValFin = pvs.getValorFinal().floatValue();
                    float valorIni = actualPV.getValorInicial().floatValue();
                    actualPV.setValorInicial(new BigDecimal(valorIni - itemValFin));
                    actualPV.setValorFinal(new BigDecimal(valorIni - itemValFin));
                    if (!DBControl.atualizaPreVenda(actualPV)) {
                        return false;
                    }
                    else {
                        //Atualiza a tabela de vendas
                        uiCPV.setPreVenda(actualPV);
                        return true;
                    }
                }
                else {
                    return false;
                }
            }
        }

        return false;
    }
    
    public boolean releasePreVenda() {
        actualPV.setEmProgresso(false);
        if (!DBControl.atualizaPreVenda(actualPV)) {
            return false;
        }
        PrevendaAberta pva = DBControl.getPreVendaPendente();
        if (pva == null) {
            return true;
        }
        else {
            if (DBControl.removePreVendaPendente(pva.getId())) {
                return true;
            }
            else {
                return false;
            }
        }
    }
    
    public boolean adicionaItem(String itemCod, int qnt) {
        PafProduct product = DBControl.getPafProduct(itemCod);
        if(product == null){
            return Boolean.FALSE;
        }
        
        return Boolean.TRUE;
    }
    
    public PafProduct getProduct(String itemCod) {
        PafProduct product = DBControl.getPafProduct(itemCod);
        if(product != null){
            return product;
        }
        
        return null;
    }
    
    
    @Override
    public JPanel getPanel() {
        return uiCPV;
    }
}
