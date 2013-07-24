/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.ui;

import br.com.sinetic.pafecftef.action.AConfirmarPreVenda;
import br.com.sinetic.pafecftef.action.Controller;
import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.db.DBControl;
import br.com.sinetic.pafecftef.entities.EcfComprador;
import br.com.sinetic.pafecftef.entities.PafProduct;
import br.com.sinetic.pafecftef.remoteentities.PreVenda;
import br.com.sinetic.pafecftef.remoteentities.PreVendaProduto;
import br.com.sinetic.pafecftef.remoteentities.PreVendaServico;
import br.com.sinetic.pafecftef.remoteentities.Servico;
import br.com.sinetic.pafecftef.remoteentities.SineticControleEstoque;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import java.util.Set;

/**
 *
 * @author Windows8
 */
public class UIConfirmaPreVenda extends javax.swing.JPanel {

    private AConfirmarPreVenda aCPV;
    
    
    /**
     * Creates new form UIConfirmaPreVenda
     */
    public UIConfirmaPreVenda() {
        initComponents();
    }
    
    public void setAction(AConfirmarPreVenda a) {
        aCPV = a;
    }

    public boolean setPreVenda(PreVenda pv) {
        if (pv == null) {
            return false;
        }
        
        //Limpa a tabela
        ((DefaultTableModel)jtPreVenda.getModel()).setRowCount(0);
        
        //Adiciona os Produtos
        Set sPVP = pv.getPreVendaProdutos();
        Iterator it = sPVP.iterator();
        PreVendaProduto pvp;
        Object[] o = null;
        while(it.hasNext()) {
            pvp = (PreVendaProduto) it.next();
            //checa se o produto foi cancelado
            if (pvp.isStatus()) {
                SineticControleEstoque sce = pvp.getSineticControleEstoque();
                long cod = sce.getCodigoProdutoReferencia();
                String desc = Control.getDescricaoProduto(DBControl.getFirstProduto(cod));

                o = new Object[] {cod, desc, pvp.getQuantidade(), sce.getPafEcfUnidadeMedida().getSigla(),
                                "R$ " + (pvp.getValorInicial().floatValue()/pvp.getQuantidade()) , 
                                "R$ " + pvp.getValorInicial(), "R$ " + pvp.getDescontoValor(),
                                "R$ " + pvp.getValorFinal()};
                ((DefaultTableModel)jtPreVenda.getModel()).addRow(o);
            }
        }
        
        //Adiciona os Serviços
        Set sPVS = pv.getPreVendaServicos();
        it = sPVS.iterator();
        PreVendaServico pvs;
        o = null;
        while(it.hasNext()) {
            pvs = (PreVendaServico) it.next();
            //Checa se o serviço foi cancelado
            if (pvs.isStatus()) {
                Servico s = pvs.getServico();
                o = new Object[] {s.getCodigo(), s.getDescricao(), "1", s.getPafEcfUnidadeMedida().getSigla(),
                                "R$ " + (pvs.getValorInicial().floatValue()), 
                                "R$ " + pvs.getValorInicial(), "R$ " + pvs.getDescontoValor(),
                                "R$ " + pvs.getValorFinal()};
                ((DefaultTableModel)jtPreVenda.getModel()).addRow(o);
            }
        }
        
        //Atualiza o total
        atualizaTotal(pv);
        
        //Seleciona a primeira linha
        jtPreVenda.changeSelection(0, 0, false, false);
        
        return true;
    }
    
    public void setComprador(EcfComprador c) {
        if (c == null) {
            return;
        }
        
        if (c.getCpfCnpj() != null) {
            jtfCPF.setText(c.getCpfCnpj());
        }
        
        if (c.getNome() != null) {
            jtfNome.setText(c.getNome());
        }
        
        if (c.getEndereco() != null) {
            jtfEndereco.setText(c.getEndereco());
        }
        
    }
    
    private boolean atualizaComprador() {
        if (jtfCPF.getText() != null && !jtfCPF.getText().isEmpty()) {
            aCPV.atualizaComprador(jtfNome.getText(), jtfCPF.getText(), jtfEndereco.getText());
            return true;
        }
        else {
            return false;
        }
    }
    
    public void atualizaTotal(PreVenda pv) {
        jlSubtotal.setText(String.valueOf(pv.getValorInicial()));
        jlDesconto.setText(String.valueOf(pv.getDescontoValor()));
        jlTotal.setText(String.valueOf(pv.getValorFinal()));
    }
    
    private void validaDesconto() {
        
        //Verifica o valor do desconto
        float desconto = 0;
        try {
            desconto = Float.valueOf(jtfDesconto.getText());
        }
        catch (NumberFormatException e) {
            Controller.messageBox("Valor de desconto inválido!");
            return;
        }
        
        if (desconto <= 0) {
            Controller.messageBox("Desconto inválido!\nO valor não pode ser zero ou negativo!");
            return;
        }
        
        //Traduz senha e usuário
        String user = jtfUsuario.getText();
        char[] s = jpfSenha.getPassword();
        String senha = "";
        for (int i = 0; i < s.length; i++) {
            senha += s[i];
        }
        
       
        if (1==1) {//@todo aCPV.validaGerente(user, senha)) {
            if (aCPV.concedeDesconto(desconto)) {
                Controller.messageBox("Desconto concedido com sucesso!");
            }
            else {
                Controller.messageBox("Erro ao conceder desconto!");
            }
        }
        else {
            Controller.messageBox("Erro ao validar cadastro! \n Por favor, cheque o usuário e senha!");
        }
    }
    
    public void setDesconto(float desconto) {
        jlDesconto.setText("R$ " + String.valueOf(desconto));
        jtfDesconto.setText("0000");
    }
    
    public void descarregarTela() {
        jlDesconto.setText("R$ 0,00");
        jlSubtotal.setText("R$ 0,00");
        jlTotal.setText("R$ 0,00");
        jtfDesconto.setText("0.00");
        jpfSenha.setText("");
        jtfUsuario.setText("");
        jtfNome.setText("");
        jtfCPF.setText("");
        jtfEndereco.setText("");
        jcbIncluirCF.setSelected(false);
    }
    
    //@todo implementar
    private boolean checkCPFCNPJ(String cpfcnpj) {
        return true;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jbAdicionar = new javax.swing.JButton();
        jbRemover = new javax.swing.JButton();
        jtfCodigo = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jtPreVenda = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jlSubtotal = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jlTotal = new javax.swing.JLabel();
        jlDesconto = new javax.swing.JLabel();
        jpDesconto = new javax.swing.JPanel();
        jtfDesconto = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jtfUsuario = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jbOKDesconto = new javax.swing.JButton();
        jpfSenha = new javax.swing.JPasswordField();
        jbDesconto = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jtfNome = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jtfCPF = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jtfEndereco = new javax.swing.JTextField();
        jcbIncluirCF = new javax.swing.JCheckBox();
        jtfQnt = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jbConfirmar = new javax.swing.JButton();
        jbVoltar = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Pré-Venda"));

        jbAdicionar.setText("Adicionar Item");
        jbAdicionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAdicionarActionPerformed(evt);
            }
        });

        jbRemover.setText("Cancelar Item");
        jbRemover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbRemoverActionPerformed(evt);
            }
        });

        jLabel1.setText("Código do Item:");

        jtPreVenda.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jtPreVenda.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descrição", "Quantidade", "Unidade", "Valor Unitário", "Sub-Total", "Desconto", "TOTAL"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jtPreVenda);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Valor Final"));

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel2.setText("Sub-Total:");

        jlSubtotal.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jlSubtotal.setText("R$ 0,00");

        jLabel4.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel4.setText("Desconto:");

        jLabel5.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jLabel5.setText("TOTAL:");

        jlTotal.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jlTotal.setText("R$ 0,00");

        jlDesconto.setFont(new java.awt.Font("Calibri", 0, 24)); // NOI18N
        jlDesconto.setText("R$ 0,00");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel4)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(25, 25, 25)
                                .addComponent(jlSubtotal)
                                .addGap(2, 2, 2))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jlDesconto))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(25, 25, 25)
                        .addComponent(jlTotal)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jlSubtotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jlDesconto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jlTotal))
                .addContainerGap())
        );

        jpDesconto.setBorder(javax.swing.BorderFactory.createTitledBorder("Conceder desconto"));

        jtfDesconto.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jtfDesconto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jtfDesconto.setText("0.00");

        jLabel6.setFont(new java.awt.Font("Calibri", 0, 18)); // NOI18N
        jLabel6.setText("Valor:");

        jLabel3.setText("Usuário:");

        jtfUsuario.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel7.setText("Senha:");

        jbOKDesconto.setText("OK");
        jbOKDesconto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOKDescontoActionPerformed(evt);
            }
        });

        jpfSenha.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jpDescontoLayout = new javax.swing.GroupLayout(jpDesconto);
        jpDesconto.setLayout(jpDescontoLayout);
        jpDescontoLayout.setHorizontalGroup(
            jpDescontoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDescontoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpDescontoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpDescontoLayout.createSequentialGroup()
                        .addGroup(jpDescontoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3)
                            .addComponent(jLabel7))
                        .addGap(18, 18, 18)
                        .addGroup(jpDescontoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jtfUsuario)
                            .addComponent(jtfDesconto, javax.swing.GroupLayout.DEFAULT_SIZE, 113, Short.MAX_VALUE)
                            .addComponent(jpfSenha, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpDescontoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jbOKDesconto)))
                .addContainerGap())
        );
        jpDescontoLayout.setVerticalGroup(
            jpDescontoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpDescontoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpDescontoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jtfDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpDescontoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jtfUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpDescontoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jpfSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbOKDesconto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jbDesconto.setText("Desconto");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Identificação do Comprador"));

        jLabel9.setText("Nome / Razão Social:");

        jLabel10.setText("CPF / CNPJ:");

        jLabel11.setText("Endereço:");

        jcbIncluirCF.setText("Incluir no Cupom Fiscal");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jtfEndereco))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jLabel10)
                                        .addGap(53, 53, 53)))
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jtfNome, javax.swing.GroupLayout.DEFAULT_SIZE, 165, Short.MAX_VALUE)
                                    .addComponent(jtfCPF)))
                            .addComponent(jcbIncluirCF))))
                .addGap(23, 23, 23))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jtfNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jtfCPF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jtfEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jcbIncluirCF))
        );

        jLabel8.setText("Quantidade:");

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
                                .addComponent(jbRemover)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jbDesconto)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jtfQnt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jbAdicionar))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jpDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 204, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbAdicionar)
                    .addComponent(jbRemover)
                    .addComponent(jtfCodigo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jbDesconto)
                    .addComponent(jtfQnt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jbConfirmar.setText("Confirmar Pré-Venda");
        jbConfirmar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbConfirmarActionPerformed(evt);
            }
        });

        jbVoltar.setText("Voltar");
        jbVoltar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbVoltarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbVoltar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jbConfirmar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbConfirmar)
                    .addComponent(jbVoltar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbOKDescontoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOKDescontoActionPerformed
        validaDesconto();
    }//GEN-LAST:event_jbOKDescontoActionPerformed

    private void jbVoltarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbVoltarActionPerformed
        String msg = "Tem certeza que deseja retornar para a Lista de Vendas?";
        if (Controller.confirmBox(msg)) {
            if  (!aCPV.releasePreVenda()) {
                Controller.messageBox("Erro ao liberar pré-venda!");
            }
            Controller.mudarEstado(Controller.ESTADO_LVENDAS);
        }
    }//GEN-LAST:event_jbVoltarActionPerformed

    private void jbConfirmarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbConfirmarActionPerformed
        String msg = "Confirmar Pré-Venda?";
        if (Controller.confirmBox(msg)) {
            //Verifica sobre o comprador identificado
            if (jcbIncluirCF.isSelected()) {
                if (!atualizaComprador()) {
                    String sErro = "CPF/CNPJ vazio!";
                    Controller.messageBox(sErro);
                    return;
                }
            }
            else {
                aCPV.removeComprador();
            }
            //Muda de estado
            Controller.mudarEstado(Controller.ESTADO_FPAGAMENTO);
        }
    }//GEN-LAST:event_jbConfirmarActionPerformed

    private void jbRemoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbRemoverActionPerformed
        int row = jtPreVenda.getSelectedRow();
        if (row < 0) {
            Controller.messageBox("Nenhum item foi selecionado!");
            return;
        }
        
        int cod = -1;
        try {
             cod = (Integer)((DefaultTableModel)jtPreVenda.getModel()).getValueAt(row, 0);
        }
        catch (Exception e) {
            Controller.messageBox("Erro ao pegar código do item!");
            return;
        }
        
        if (cod == -1) {
            Controller.messageBox("Erro ao pegar código do item!");
            return;
        }
        
        
        if (Controller.confirmBox("Tem certeza que deseja cancelar o item: " + cod)) {
            if (aCPV.cancelaItem(cod)) {
                Controller.messageBox("Item cancelado com sucesso!");
            }
        }
    }//GEN-LAST:event_jbRemoverActionPerformed

    private void jbAdicionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAdicionarActionPerformed
        int qnt = 0;
        try {
            qnt = Integer.parseInt(jtfQnt.getText());
        }catch (Exception e) {
            Controller.messageBox("Quantidade inválida!");
            return;
        }
        
        if (jtfCodigo.getText().isEmpty()) {
            Controller.messageBox("O código não pode ser vazio!");
            return;
        }
        
        if (!aCPV.adicionaItem(jtfCodigo.getText(), qnt)) {
            Controller.messageBox("Erro ao adicionar item!");
            return;
        }
        
        PafProduct product = aCPV.getProduct(jtfCodigo.getText());
        if(product == null){
            Controller.messageBox("Produto não encontrado!");
            return;
        }
        
        if(aCPV.vendeItem(product, qnt)){
            Controller.messageBox("Erro na impressao do produto!");
            return;
        }
        
        Object[] o = null;
        
        o = new Object[] {product.getGtin(), product.getDescricao(), qnt, product.getUnidadeMedida(),
                                "R$ " + (product.getValor().floatValue()/qnt) , 
                                "R$ " + product.getValor(), "R$ 0.00",
                                "R$ " + product.getValor()};
       
       ((DefaultTableModel)jtPreVenda.getModel()).addRow(o);
    }//GEN-LAST:event_jbAdicionarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton jbAdicionar;
    private javax.swing.JButton jbConfirmar;
    private javax.swing.JButton jbDesconto;
    private javax.swing.JButton jbOKDesconto;
    private javax.swing.JButton jbRemover;
    private javax.swing.JButton jbVoltar;
    private javax.swing.JCheckBox jcbIncluirCF;
    private javax.swing.JLabel jlDesconto;
    private javax.swing.JLabel jlSubtotal;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JPanel jpDesconto;
    private javax.swing.JPasswordField jpfSenha;
    private javax.swing.JTable jtPreVenda;
    private javax.swing.JTextField jtfCPF;
    private javax.swing.JTextField jtfCodigo;
    private javax.swing.JTextField jtfDesconto;
    private javax.swing.JTextField jtfEndereco;
    private javax.swing.JTextField jtfNome;
    private javax.swing.JTextField jtfQnt;
    private javax.swing.JTextField jtfUsuario;
    // End of variables declaration//GEN-END:variables
}
