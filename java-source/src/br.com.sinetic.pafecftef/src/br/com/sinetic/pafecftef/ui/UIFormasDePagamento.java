/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.ui;

import br.com.sinetic.pafecftef.action.AFormasDePagamento;
import br.com.sinetic.pafecftef.action.Controller;
import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.control.ItensVendidos;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.entities.EcfComprador;
import br.com.sinetic.pafecftef.remoteentities.CrediarioCliente;
import br.com.sinetic.pafecftef.remoteentities.CrediarioFormaPagamento;
import br.com.sinetic.pafecftef.remoteentities.CrediarioFormaPagamentoDetalhado;
import br.com.sinetic.pafecftef.remoteentities.PreVenda;
import br.com.sinetic.pafecftef.remoteentities.PreVendaProduto;
import br.com.sinetic.pafecftef.remoteentities.PreVendaServico;
import br.com.sinetic.pafecftef.remoteentities.Produtos;
import br.com.sinetic.pafecftef.remoteentities.Servico;
import br.com.sinetic.pafecftef.remoteentities.SineticControleEstoque;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Administrador
 */
public class UIFormasDePagamento extends javax.swing.JPanel {

    public static final int TAMANHO_JL_CF = 48;
            
    public static String newline = System.getProperty("line.separator");
    
    //Objeto pai
    private AFormasDePagamento aFP;
    
    //Modelo utilizado na lista do CF
    private DefaultListModel listaCF;
    //Modelo utilizado na lista dos meios de pagamento
    private DefaultListModel listaMP;
    
    
   
    /**
     * Creates new form UIFormasDePagamento
     */
    public UIFormasDePagamento() {
        initComponents();
        listaCF = new DefaultListModel();
        jlVenda.setModel(listaCF);  
        listaMP = new DefaultListModel();
        jlPagConf.setModel(listaMP);
        

    }

    public void setAction(AFormasDePagamento a) {
        aFP = a;
    }
    
    public boolean addListaVendaToCF(PreVenda pv) {
        if (pv == null) {
            return false;
        }
        
        //Limpa a lista atual
        clear();
        
        //Adiciona os elementos
        //CABECALHO
        listaCF.addElement("------------------------------------------------");
        listaCF.addElement("");
        listaCF.addElement(formataParaCF("CUPOM FISCAL", TAMANHO_JL_CF-12));
        listaCF.addElement("");
        listaCF.addElement("ITEM CÓD DESC  QTD.UN.VL_UNIT(R$) ST VL_ITEM(R$)");
        listaCF.addElement("------------------------------------------------");
        listaCF.addElement("");
        
        
        //Formata os produtos na lista
        Set sPVP = pv.getPreVendaProdutos();
        Iterator it = sPVP.iterator();
        PreVendaProduto pvp;
        int i = 1;
        while(it.hasNext()) {
            pvp = (PreVendaProduto) it.next();
            if (pvp.isStatus()) {
                SineticControleEstoque sce = pvp.getSineticControleEstoque();
                long cod = sce.getCodigoProdutoReferencia();
                String desc = Control.getDescricaoProduto(DBControl.getFirstProduto(cod));

                float vU = pvp.getValorInicial().floatValue()/pvp.getQuantidade();
                String iS = ""+i+"  "+cod + "  " + desc + " Quantidade: " + pvp.getQuantidade()+ " " + sce.getPafEcfUnidadeMedida().getSigla() + " " + " R$ " + vU;
                listaCF.addElement(iS);
                iS = "Subtotal Item: R$ " + pvp.getValorInicial();
                listaCF.addElement(iS);
                iS = "Desconto: R$ " + pvp.getDescontoValor();
                listaCF.addElement(iS);
                iS = "Total Item: R$ " + pvp.getValorFinal();
                listaCF.addElement(iS);
                listaCF.addElement(" ");
            }
            i++;
        }
        
        //Formata os serviços na lista
        //Adiciona os Serviços
        Set sPVS = pv.getPreVendaServicos();
        it = sPVS.iterator();
        PreVendaServico pvs;
        while(it.hasNext()) {
            pvs = (PreVendaServico) it.next();
            if (pvs.isStatus()) {
                Servico s = pvs.getServico();

                String iS = ""+i+"  "+s.getCodigo() + "  " + s.getDescricao() + " Quantidade: 1 U " + " R$ " + pvs.getValorInicial();
                listaCF.addElement(iS);
                iS = "Subtotal Item: R$ " + pvs.getValorInicial();
                listaCF.addElement(iS);
                iS = "Desconto: R$ " + pvs.getDescontoValor();
                listaCF.addElement(iS);
                iS = "Total Item: R$ " + pvs.getValorFinal();
                listaCF.addElement(iS);
                listaCF.addElement(" ");
            }
            i++;
        }
        
        
        jlValorTotalCompra.setText("R$ " + pv.getValorFinal());
        return true;
    }
    
    private void clear() {
        listaCF.clear();
        listaMP.clear();
    }
            

   
    
    private boolean checaValorPagNegativo() {
        String valor = jftfValor.getText();
        float f = 0;
        try {
            f = Float.parseFloat(valor);
        }
        catch (Exception e) {
            return false;
        }
        
        if (f < 0) {
            return false;
        }
        
        return true;
    }
    
    
    private String formataParaCF(String s, int tamanhoCampo) {
        if (s == null) {
            return null;
        }
        if (tamanhoCampo <= s.length()) {
            return s;
        }
        
        String ret = "";
        int len = s.length();
        int vazios = tamanhoCampo-len;
        int vE = vazios/2;
        int vD = vE - (vE*2 - vazios);
        
        for (int i = 0; i < vE; i++) {
            ret += " ";
        }
        ret += s;
        for (int i = 0; i < vD; i++) {
            ret = ret + " ";
        }
        
        return ret;
    }
    
    public void setPagamentoMeios(List<CrediarioFormaPagamento> lCFP) {
        Vector<String> v = new Vector();
        String s = null;
        for (CrediarioFormaPagamento cfp: lCFP) {
            s = cfp.getNome();
            v.add(s);
        }
        
        //Cria novo modelo
        ComboBoxModel cbm = new DefaultComboBoxModel(v);
        jcbMeiosDePag.setModel(cbm);
        
        if (v.size() > 0) {
            cbm.setSelectedItem(lCFP.get(0).getNome());
            aFP.meioPagamentoSelected(jcbMeiosDePag.getSelectedIndex());
        }
    }
    
    public void setPagamentoTipos(Set<CrediarioFormaPagamentoDetalhado> sCFPD) {
        Vector<String> v = new Vector();
        String s = null;
        
        if (sCFPD == null || sCFPD.isEmpty()) {
            jcbTipo.setEnabled(false);
        }
        else {
            jcbTipo.setEnabled(true);
            for (CrediarioFormaPagamentoDetalhado cfpd: sCFPD) {
                s = cfpd.getNome();
                v.add(s);
            }
        }
        
        //Cria novo modelo
        ComboBoxModel cbm = new DefaultComboBoxModel(v);
        jcbTipo.setModel(cbm);
        
        if (v.size() > 0) {
            cbm.setSelectedItem(v.get(0));
            aFP.tipoPagamentoSelected(jcbTipo.getSelectedIndex());
        }
    }
    
    public void setParcelas(int maxParcelas) {
        Vector<String> v = new Vector();
        String s = null;
        
        for (int i = 2; i <= maxParcelas; i++) {
            s = String.valueOf(i);
            v.add(s);
        }
        jcbParcelas.setEnabled(true);
        
        //Cria novo modelo
        ComboBoxModel cbm = new DefaultComboBoxModel(v);
        jcbParcelas.setModel(cbm);
        
        if (v.size() > 0) {
            cbm.setSelectedItem(v.get(v.size()-1));
        }
        
    }
    
    public void removeParcelas() {
        //Cria novo modelo
        ComboBoxModel cbm = new DefaultComboBoxModel();
        jcbParcelas.setModel(cbm);
        jcbParcelas.setEnabled(false);
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jlVenda = new javax.swing.JList();
        jLabel7 = new javax.swing.JLabel();
        jlValorTotalCompra = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jlPagConf = new javax.swing.JList();
        jButton3 = new javax.swing.JButton();
        jlTroco = new javax.swing.JLabel();
        jlTotalPag = new javax.swing.JLabel();
        jbFinalizaVenda = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jcbMeiosDePag = new javax.swing.JComboBox();
        jLabel8 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jbEfetuarPag = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jcbTipo = new javax.swing.JComboBox();
        jftfValor = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jcbParcelas = new javax.swing.JComboBox();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Cupom Fiscal"));

        jlVenda.setFont(new java.awt.Font("LettrGoth12 BT", 0, 12)); // NOI18N
        jScrollPane1.setViewportView(jlVenda);

        jLabel7.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("TOTAL:");

        jlValorTotalCompra.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jlValorTotalCompra.setText("R$ 0,00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlValorTotalCompra))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jlValorTotalCompra))
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Pagamentos Confirmados"));

        jLabel5.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel5.setText("Total Pagamentos:");

        jLabel6.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jLabel6.setText("Troco:");

        jScrollPane2.setViewportView(jlPagConf);

        jButton3.setText("Estorna Pagamento");

        jlTroco.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jlTroco.setText("R$ 0,00");

        jlTotalPag.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        jlTotalPag.setText("R$ 0,00");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel6))
                                .addGap(42, 42, 42)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jlTroco)
                                    .addComponent(jlTotalPag)))
                            .addComponent(jButton3))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jlTroco))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jlTotalPag))
                .addContainerGap())
        );

        jbFinalizaVenda.setFont(new java.awt.Font("Courier New", 1, 16)); // NOI18N
        jbFinalizaVenda.setText("Finalizar Venda");
        jbFinalizaVenda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFinalizaVendaActionPerformed(evt);
            }
        });

        jButton2.setText("Cancelar Cupom Fiscal");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Realizar Pagamento"));

        jcbMeiosDePag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbMeiosDePagActionPerformed(evt);
            }
        });

        jLabel8.setText("Meio de Pagamento:");

        jLabel12.setText("Valor:");

        jbEfetuarPag.setText("Efetuar Pagamento");
        jbEfetuarPag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbEfetuarPagActionPerformed(evt);
            }
        });

        jLabel1.setText("Tipo:");

        jcbTipo.setEnabled(false);
        jcbTipo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbTipoItemStateChanged(evt);
            }
        });
        jcbTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbTipoActionPerformed(evt);
            }
        });

        jftfValor.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jftfValor.setText("0");
        jftfValor.setToolTipText("");

        jLabel2.setText("Número de Parcelas:");

        jcbParcelas.setEnabled(false);
        jcbParcelas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbParcelasItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcbMeiosDePag, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jcbTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jbEfetuarPag, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jftfValor)
                    .addComponent(jcbParcelas, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel1)
                            .addComponent(jLabel12)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbMeiosDePag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jcbParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jftfValor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jbEfetuarPag)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jbFinalizaVenda, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jbFinalizaVenda))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbFinalizaVendaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFinalizaVendaActionPerformed
        if (aFP.checaValorTotalParaFecharCF()) {
            if (aFP.fechaCupom()) {
                Controller.messageBox("Venda concluída com sucesso!");
                Controller.mudarEstado(Controller.ESTADO_LVENDAS);
            }
            else {
                Controller.messageBox("Erro ao fechar o cupom fiscal!");
            }
        }
        else {
            Controller.messageBox("Não há pagamento suficiente para fechar o Cupom Fiscal!");
        }
    }//GEN-LAST:event_jbFinalizaVendaActionPerformed

    private void jcbMeiosDePagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbMeiosDePagActionPerformed
        aFP.meioPagamentoSelected(jcbMeiosDePag.getSelectedIndex());
    }//GEN-LAST:event_jcbMeiosDePagActionPerformed

    private void jbEfetuarPagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbEfetuarPagActionPerformed
        float valor;
        try  {
            valor = Float.valueOf((String)jftfValor.getText());
        }
        catch (Exception e) {
            Controller.messageBox("Valor inválido!");
            return;
        }
        
        if (valor <= 0) {
            Controller.messageBox("Valor inválido! \nValor não pode ser zero ou negativo.");
            return;
        }
        
        if (aFP.checaNovoPagamento(valor)) {
            
            int numParc = -1;
            if (jcbParcelas.getModel().getSize() > 0) {
                try {
                    numParc = Integer.valueOf((String)jcbParcelas.getSelectedItem());
                }
                catch (Exception e) {
                    Controller.messageBox("Valor de parcela inválido.");
                    return;                    
                }
            }
            
            int iMeio = jcbMeiosDePag.getSelectedIndex();
            int iTipo = jcbTipo.getSelectedIndex();
            if (aFP.efetuarPagamento(valor, iMeio, iTipo, numParc)) {
                String sTipo = aFP.getPagTipo(iMeio, iTipo);
                listaMP.addElement("" + aFP.getPagMeio(iMeio).toUpperCase() + (sTipo!=null?(" "+sTipo.toUpperCase()):"") + ": R$ "+ valor);
                //Atualiza o total pago
                jlTotalPag.setText("R$ " + aFP.getTotalPago());
                jlTroco.setText("R$ " + aFP.getTroco());
            }
        }
        else {
            Controller.messageBox("Não há necessidade de novo pagamento \n Favor fechar o Cupom Fiscal!");
        }
    }//GEN-LAST:event_jbEfetuarPagActionPerformed

    private void jcbTipoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbTipoItemStateChanged
        CrediarioFormaPagamentoDetalhado cfpd = aFP.getPagTipoDB(jcbTipo.getSelectedIndex());
        if (cfpd.getNumeroMaximoParcelas() != null) {
            System.out.println("Número de parcelas: " + cfpd.getNumeroMaximoParcelas());
        }
    }//GEN-LAST:event_jcbTipoItemStateChanged

    private void jcbParcelasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbParcelasItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_jcbParcelasItemStateChanged

    private void jcbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jcbTipoActionPerformed
        aFP.tipoPagamentoSelected(jcbTipo.getSelectedIndex());
    }//GEN-LAST:event_jcbTipoActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbEfetuarPag;
    private javax.swing.JButton jbFinalizaVenda;
    private javax.swing.JComboBox jcbMeiosDePag;
    private javax.swing.JComboBox jcbParcelas;
    private javax.swing.JComboBox jcbTipo;
    private javax.swing.JTextField jftfValor;
    private javax.swing.JList jlPagConf;
    private javax.swing.JLabel jlTotalPag;
    private javax.swing.JLabel jlTroco;
    private javax.swing.JLabel jlValorTotalCompra;
    private javax.swing.JList jlVenda;
    // End of variables declaration//GEN-END:variables
}
