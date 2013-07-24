/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sinetic.pafecftef.ui;

import br.com.sinetic.pafecftef.action.AListaVendas;
import br.com.sinetic.pafecftef.action.ATelaPrincipal;
import br.com.sinetic.pafecftef.action.Controller;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import br.com.sinetic.pafecftef.control.Control;
import br.com.sinetic.pafecftef.control.Registros;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.Insets;
import java.beans.PropertyVetoException;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.WindowConstants;

/**
 *
 * @author Administrador
 */
public class UITelaPrincipal extends javax.swing.JFrame {

    public ATelaPrincipal aTP;
    
    private javax.swing.GroupLayout layout;
    private JPanel jpActual;
    
    private BufferedImage biGouveiaIcon;
    
    private static final String PATH_ICO_ABRECAIXA = "res/icons/abrecaixa.png";
    private static final String PATH_ICO_FECHACAIXA = "res/icons/fechacaixa.png";
    private static final String PATH_ICO_LEITURAX = "res/icons/leituraX.png";
    private static final String PATH_ICO_SUPRIMENTO = "res/icons/suprimento.png";
    private static final String PATH_ICO_SANGRIA = "res/icons/sangria.png";
    private static final String PATH_ICO_SAIR = "res/icons/sair.png";
    private static final String PATH_ICO_CONFIG = "res/icons/configuracoes.png";
    private static final String PATH_ICO_GOUVEIA = "res/icons/iconeAplicacao.png";
    
    /**
     * Creates new form TelaPrincipal
     */
    public UITelaPrincipal() {
        initComponents();
        jlInfo.setText("");
        setInitialPanel();
        setIcons();
    }
    
    public void setAction(ATelaPrincipal a) {
        aTP = a;
    }

    private void setInitialPanel() {
        //Remove o Layout anterior
        this.getContentPane().removeAll();        
        
        layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpBarra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jpBarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        
        jpActual = jpPrincipal;
        
    }
    
    private void setIcons() {
        ImageIcon iiAC = new ImageIcon(PATH_ICO_ABRECAIXA);
        ImageIcon iiFC = new ImageIcon(PATH_ICO_FECHACAIXA);
        ImageIcon iiLX = new ImageIcon(PATH_ICO_LEITURAX);
        ImageIcon iiSa = new ImageIcon(PATH_ICO_SANGRIA);
        ImageIcon iiSu = new ImageIcon(PATH_ICO_SUPRIMENTO);
        ImageIcon iiS = new ImageIcon(PATH_ICO_SAIR);
        ImageIcon iiC = new ImageIcon(PATH_ICO_CONFIG);
        biGouveiaIcon = null;
        try {
            biGouveiaIcon = ImageIO.read(new File(PATH_ICO_GOUVEIA));
        } catch (IOException e) {
        }
        
        int LB_SIZE = 23;
        int W_SIZE = 30;
        
        jbAbreCaixa.setIcon(iiAC);
        setButtonMargins(jbAbreCaixa);
        setButtonSize(jbAbreCaixa, iiAC.getIconWidth() + W_SIZE, iiAC.getIconHeight() + LB_SIZE);
        
        jbSangria.setIcon(iiSa);
        setButtonMargins(jbSangria);
        setButtonSize(jbSangria, iiSa.getIconWidth() + W_SIZE, iiSa.getIconHeight() + LB_SIZE);
        
        jbSuprimento.setIcon(iiSu);
        setButtonMargins(jbSuprimento);
        setButtonSize(jbSuprimento, iiSu.getIconWidth() + W_SIZE, iiSu.getIconHeight() + LB_SIZE);
        
        jbFechaCaixa.setIcon(iiFC);
        setButtonMargins(jbFechaCaixa);
        setButtonSize(jbFechaCaixa, iiFC.getIconWidth() + W_SIZE, iiFC.getIconHeight() + LB_SIZE);
        
        jbConfig.setIcon(iiC);
        setButtonMargins(jbConfig);
        setButtonSize(jbConfig, iiC.getIconWidth() + W_SIZE, iiC.getIconHeight() + LB_SIZE);
        
        jbLeituraX.setIcon(iiLX);
        setButtonMargins(jbLeituraX);
        setButtonSize(jbLeituraX, iiLX.getIconWidth() + W_SIZE, iiLX.getIconHeight() + LB_SIZE);
        
        jbSair.setIcon(iiS);
        setButtonMargins(jbSair);
        setButtonSize(jbSair, iiS.getIconWidth() + W_SIZE, iiS.getIconHeight() + LB_SIZE);
        
        //Seta o ícone principal
        this.setIconImage(biGouveiaIcon);
    }
    
    private void setButtonMargins(JButton jb) {
        jb.setMargin(new Insets(2, 6, 2, 6));
    }
    
    private void setButtonSize(JButton jb, int w, int h) {
        Dimension d = new Dimension(w, h);
        jb.setSize(d);
        jb.setMaximumSize(d);
        jb.setMinimumSize(d);
        jb.setPreferredSize(d);
    }
    
    public void setPanel(JPanel jp) {
        layout.replace(jpActual, jp);
        jpActual = jp;
    }
    
    public void setDialog(JDialog jd) {
        removeCloseButton(jd);
        
        //jd.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        jd.setAlwaysOnTop(true);
        jd.setLocation(Controller.getPosX(jd.getWidth()),jd.getHeight());
        jd.setVisible(true);
     }
    
    public static void removeCloseButton(Component comp) {
        if (comp instanceof JMenu) {
            Component[] children = ((JMenu) comp).getMenuComponents();
            for (int i = 0; i < children.length; ++i)
                removeCloseButton(children[i]);
        }
        else if (comp instanceof AbstractButton) {
            Action action = ((AbstractButton) comp).getAction();
            String cmd = (action == null) ? "" : action.toString();
            if (cmd.contains("CloseAction")) {
                comp.getParent().remove(comp);
            }
        }
        else if (comp instanceof Container) {
            Component[] children = ((Container) comp).getComponents();
            for (int i = 0; i < children.length; ++i)
                removeCloseButton(children[i]);
        }
    }
    
   
    public void setInfo(String s) {
        if (s != null) {
            jlInfo.setText(s);
        }
        
    }
    
    public String getInfo() {
        return jlInfo.getText();
    }
    
    public int getUIWidth() {
        return this.getWidth();
    }
    
    public int getUIHeigth() {
        return this.getHeight();
    }
    
    public int getMenuBarHeight() {
        return jpBarra.getHeight();
    }
    
    public int getStatusBarHeight() {
        return jpInfo.getHeight();
    }
    
    public Frame getMainFrame() {
        return this;
    }
    
    private void quit() {
        
        if (Controller.isAbleToQuit()) {
            if (Controller.confirmBox("Tem certeza que deseja fechar a aplicação?")) {
                br.com.sinetic.pafecftef.control.Control.stop();
                System.exit(1);
            }
        }
        else {
            Controller.messageBox("Você não pode fechar a aplicação pois existem ações de venda pendentes.");
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpInfo = new javax.swing.JPanel();
        jlInfo = new javax.swing.JLabel();
        jpPrincipal = new javax.swing.JPanel();
        jpBarra = new javax.swing.JPanel();
        jbLeituraX = new javax.swing.JButton();
        jbSair = new javax.swing.JButton();
        jbAbreCaixa = new javax.swing.JButton();
        jbFechaCaixa = new javax.swing.JButton();
        jbSuprimento = new javax.swing.JButton();
        jbSangria = new javax.swing.JButton();
        jbConfig = new javax.swing.JButton();
        jbCrediario = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem37 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Frente de Loja - Gouveia");
        setBackground(new java.awt.Color(125, 161, 233));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jpInfo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jpInfo.setMinimumSize(new java.awt.Dimension(780, 21));

        jlInfo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jlInfo.setText("Teste");

        javax.swing.GroupLayout jpInfoLayout = new javax.swing.GroupLayout(jpInfo);
        jpInfo.setLayout(jpInfoLayout);
        jpInfoLayout.setHorizontalGroup(
            jpInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpInfoLayout.createSequentialGroup()
                .addComponent(jlInfo)
                .addGap(0, 963, Short.MAX_VALUE))
        );
        jpInfoLayout.setVerticalGroup(
            jpInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpInfoLayout.createSequentialGroup()
                .addGap(0, 9, Short.MAX_VALUE)
                .addComponent(jlInfo))
        );

        jpPrincipal.setMaximumSize(null);
        jpPrincipal.setMinimumSize(null);

        javax.swing.GroupLayout jpPrincipalLayout = new javax.swing.GroupLayout(jpPrincipal);
        jpPrincipal.setLayout(jpPrincipalLayout);
        jpPrincipalLayout.setHorizontalGroup(
            jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jpPrincipalLayout.setVerticalGroup(
            jpPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 611, Short.MAX_VALUE)
        );

        jpBarra.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jbLeituraX.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jbLeituraX.setText("Leitura X");
        jbLeituraX.setFocusable(false);
        jbLeituraX.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbLeituraX.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbLeituraX.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbLeituraXActionPerformed(evt);
            }
        });

        jbSair.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jbSair.setText("Sair");
        jbSair.setFocusable(false);
        jbSair.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbSair.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSairActionPerformed(evt);
            }
        });

        jbAbreCaixa.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jbAbreCaixa.setText("Abrir Caixa");
        jbAbreCaixa.setFocusable(false);
        jbAbreCaixa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbAbreCaixa.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbAbreCaixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbAbreCaixaActionPerformed(evt);
            }
        });

        jbFechaCaixa.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jbFechaCaixa.setText("Fechar Caixa");
        jbFechaCaixa.setFocusable(false);
        jbFechaCaixa.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbFechaCaixa.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbFechaCaixa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbFechaCaixaActionPerformed(evt);
            }
        });

        jbSuprimento.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jbSuprimento.setText("Suprimento");
        jbSuprimento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbSuprimento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbSuprimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSuprimentoActionPerformed(evt);
            }
        });

        jbSangria.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jbSangria.setText("Sangria");
        jbSangria.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbSangria.setMaximumSize(new java.awt.Dimension(85, 23));
        jbSangria.setMinimumSize(new java.awt.Dimension(85, 23));
        jbSangria.setPreferredSize(new java.awt.Dimension(85, 23));
        jbSangria.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbSangria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSangriaActionPerformed(evt);
            }
        });

        jbConfig.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jbConfig.setText("Configurações");
        jbConfig.setFocusable(false);
        jbConfig.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbConfig.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbConfigActionPerformed(evt);
            }
        });

        jbCrediario.setFont(new java.awt.Font("Calibri", 0, 11)); // NOI18N
        jbCrediario.setText("Crediário");
        jbCrediario.setFocusable(false);
        jbCrediario.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbCrediario.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jbCrediario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCrediarioActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpBarraLayout = new javax.swing.GroupLayout(jpBarra);
        jpBarra.setLayout(jpBarraLayout);
        jpBarraLayout.setHorizontalGroup(
            jpBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpBarraLayout.createSequentialGroup()
                .addComponent(jbAbreCaixa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbSuprimento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbSangria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbFechaCaixa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbCrediario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbLeituraX)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbConfig)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jbSair)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jpBarraLayout.setVerticalGroup(
            jpBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jbLeituraX)
            .addComponent(jbAbreCaixa)
            .addComponent(jbFechaCaixa)
            .addComponent(jbSair)
            .addGroup(jpBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jbSuprimento)
                .addComponent(jbSangria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jbConfig)
            .addComponent(jbCrediario)
        );

        jMenu1.setText("File");

        jMenuItem1.setText("Sair");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Vendas");

        jMenuItem7.setText("Lista de Vendas");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem7);

        jMenuItem3.setText("Lista de Produtos");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem3);

        jMenuItem37.setText("Cadastro de Produtos");
        jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem37ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem37);

        jMenuBar1.add(jMenu4);

        jMenu2.setText("Menu Fiscal");

        jMenuItem4.setText("LX");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem8.setText("LMFC");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        jMenuItem9.setText("LMFS");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem9);

        jMenuItem10.setText("Espelho MFD");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem10);

        jMenuItem11.setText("Arquivo MFD");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem11);

        jMenuItem12.setText("Tab. Prod");
        jMenu2.add(jMenuItem12);

        jMenuItem6.setText("Estoque");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem13.setText("Movimento por ECF");
        jMenu2.add(jMenuItem13);

        jMenuItem14.setText("Meios de Pagto.");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem14);

        jMenuItem15.setText("DAV Emitidos");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem15);

        jMenuItem21.setText("Encerrantes");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem21);

        jMenuItem22.setText("Transf. Mesas");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem22);

        jMenuItem23.setText("Mesas Abertas");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem23);

        jMenuItem24.setText("Manifesto Fiscal de Viagem");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem24);

        jMenuItem16.setText("Vendas do Período");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem16);

        jMenuItem25.setText("Cupom de Embarque");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem25);

        jMenuItem26.setText("Leitura do Movimento Diário de Cupom de Embarque");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem26);

        jMenuItem27.setText("Cupom de Embarque Gratuidade");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem27);

        jMenuItem28.setText("Leitura do Movimento Diário de Cupom de Embarque Gratuidade");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem28);

        jMenuItem29.setText("Leitura do Movimento Diário");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem29);

        jMenuItem20.setText("Identificação do PAF-ECF");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem20);

        jMenuItem30.setText("Abastecimentos Pendentes");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem30);

        jMenuItem31.setText("Vendas do Período");
        jMenu2.add(jMenuItem31);

        jMenuItem17.setText("Tab. Índice Técnico Produção");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem17);

        jMenuItem18.setText("Parâmetros de Configuração");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem18);

        jMenuItem32.setText("Pedágios");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem32);

        jMenuItem33.setText("Manutenção de Bomba");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem33);

        jMenuItem34.setText("Identificação de TP para BP ida-e-volta");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem34);

        jMenuItem19.setText("Troco em Cartão");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem19);

        jMenuBar1.add(jMenu2);

        jMenu5.setText("Configurações");

        jMenuItem5.setText("Alíquotas");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem5);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("Outros");

        jMenuItem35.setText("Checar se ECF está ligado.");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem35);

        jMenuItem36.setText("Relatório de Merc. e Serv.");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem36);

        jMenuBar1.add(jMenu6);

        jMenu3.setText("Ajuda");

        jMenuItem2.setText("Sobre");
        jMenu3.add(jMenuItem2);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jpBarra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jpBarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jpInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        aTP.menuLeituraX();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        Controller.mudarEstado(Controller.ESTADO_LVENDAS);
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        quit();
    }//GEN-LAST:event_formWindowClosed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        Controller.mudarEstado(Controller.ESTADO_LMFC);
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        Controller.mudarEstado(Controller.ESTADO_LMFS);
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        Controller.mudarEstado(Controller.ESTADO_ARQMFD);
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        aTP.mensagemTITP();
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jbSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSairActionPerformed
        quit();
            
    }//GEN-LAST:event_jbSairActionPerformed

    private void jbLeituraXActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbLeituraXActionPerformed
        aTP.menuLeituraX();
    }//GEN-LAST:event_jbLeituraXActionPerformed

    private void jbAbreCaixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbAbreCaixaActionPerformed
        Controller.mudarEstado(Controller.ESTADO_ABRECAIXA);
    }//GEN-LAST:event_jbAbreCaixaActionPerformed

    private void jbSuprimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSuprimentoActionPerformed
        Controller.mudarEstado(Controller.ESTADO_SUPRIMENTO);
    }//GEN-LAST:event_jbSuprimentoActionPerformed

    private void jbFechaCaixaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbFechaCaixaActionPerformed
        Controller.mudarEstado(Controller.ESTADO_FECHACAIXA);
    }//GEN-LAST:event_jbFechaCaixaActionPerformed

    private void jbSangriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbSangriaActionPerformed
        Controller.mudarEstado(Controller.ESTADO_SANGRIA);
    }//GEN-LAST:event_jbSangriaActionPerformed

    private void jbConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbConfigActionPerformed
        Controller.mudarEstado(Controller.ESTADO_CONFIGURACOES);
    }//GEN-LAST:event_jbConfigActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        aTP.menuIdentificacaoPAFECF();
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        Controller.mudarEstado(Controller.ESTADO_ESPELHOMFD);
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        Controller.mudarEstado(Controller.ESTADO_ARQESTOQUE);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        aTP.funcaoNaoSuportada();
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        aTP.checarECF();
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jbCrediarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCrediarioActionPerformed
        String ret = Controller.inputBox("Digite o número da venda");
        int codigo = -1;
        try {
            codigo = Integer.valueOf(ret);
        }
        catch (Exception e) {
            Controller.messageBox("Número inválido!");
            return;
        }
        
        if (aTP.checaCrediario(codigo)) {
            Controller.mudarEstado(Controller.ESTADO_CREDIARIO);
        }
        else {
            Controller.messageBox("Venda não encontrada!");
        }
    }//GEN-LAST:event_jbCrediarioActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        String arquivo = System.getProperty("user.dir")+"\\ArquivoConfiguracao.txt";
        if (Control.arquivoConfiguracao(arquivo)) {
            Controller.messageBox("Arquivo de Configuração gerado com sucesso!\n"+
                                   "Local: "+ arquivo);
        }
        else {
            Controller.messageBox("Falha ao gerar arquivo de configuração!");
        }
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        Controller.mudarEstado(Controller.ESTADO_MEIOS_PAGAMENTO);
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        Controller.mudarEstado(Controller.ESTADO_ALIQUOTAS);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        if (Registros.geraArquivoTabMercServ()) {
            Controller.messageBox("Arquivo gerado com sucesso!");
        }
        else {
            Controller.messageBox("Erro ao gerar o arquivo!");
        }
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
        if (Registros.geraArquivoTrocoCartao()) {  
            Controller.messageBox("Arquivo gerado com sucesso!");
        }
        else {
            Controller.messageBox("Erro ao gerar arquivo!");
        }
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        Controller.mudarEstado(Controller.ESTADO_PRODUTO_SERVICO);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed
        Controller.mudarEstado(Controller.ESTADO_CAD_PRODUTO_SERVICO);
    }//GEN-LAST:event_jMenuItem37ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UITelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UITelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UITelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UITelaPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UITelaPrincipal().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JButton jbAbreCaixa;
    private javax.swing.JButton jbConfig;
    private javax.swing.JButton jbCrediario;
    private javax.swing.JButton jbFechaCaixa;
    private javax.swing.JButton jbLeituraX;
    private javax.swing.JButton jbSair;
    private javax.swing.JButton jbSangria;
    private javax.swing.JButton jbSuprimento;
    private javax.swing.JLabel jlInfo;
    private javax.swing.JPanel jpBarra;
    private javax.swing.JPanel jpInfo;
    private javax.swing.JPanel jpPrincipal;
    // End of variables declaration//GEN-END:variables
}
