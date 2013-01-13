/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import CN.AlocacaoPeca;
import CN.Divisao;
import CN.ItemPeca;
import CN.Semeadora;
import Cadastro.CadastroItensPeca;
import Cadastro.CadastroSemeadoras;
import Exceções.AtualizacaoException;
import Exceções.DelecaoException;
import Utilitários.DateConversion;
import Utilitários.ModelString;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author rafael
 */
public class IFrameConsSemeadora extends StartableInternalFrame {

    private JPanel panelButtonsConsultaSemeadora;
    private JButton btConsultaSemSalvar;
    private JButton btConsultaSemAlterar;
    private JButton btConsultaSemExcluir;
    private JButton btConsultaSemCancelar;
    private JPanel panelPesquisarSemeadora;
    private JScrollPane jScrollPane2;
    private JList<ModelString> listConsultaSemSemeadoras;
    private JLabel lbConsultaSemSemeadora;
    private JComboBox<ModelString> cbConsultaSemTipoCons;
    private JLabel lbConsultaSemPesquisar;
    private JTextField tfConsultaSemeadoraPesquisar;
    private JPanel panelConsultaSemeadora;
    private JPanel panelDadosSemeadoraConsulta;
    private JLabel lbCodSemConsulta;
    private JLabel lbModSemConsulta;
    private JTextField tfCodSemConsulta;
    private JLabel lbMarcaSemConsulta;
    private JTextField tfMarcaSemConsulta;
    private JLabel lbAnoSemConsulta;
    private JFormattedTextField ftfAnoSemConsulta;
    private JLabel lbDataRegistroConsulta;
    private JFormattedTextField ftfDataRegistroConsulta;
    private JTextField tfModeloConsulta;
    private JPanel panelDivPecasSemeadora;
    private JLabel lbConsultaSemDivisao;
    private JLabel lbConsultaSemPeça;
    private JScrollPane jScrollPane3;
    private JList<ModelString> listConsultaSemDivisoes;
    private JScrollPane jScrollPane4;
    private JList<ModelString> listConsultaSemPeca;
    private JButton btConsultaSemDadosPeca;
    private static final IFrameConsSemeadora iFrameConsSemeadora = new IFrameConsSemeadora();
    private static CadastroSemeadoras cadSem;
    private static CadastroItensPeca cadItensPeca;

    static {

        cadSem = new PersistenciaPostgres().getPersistenciaSemeadora();
        cadItensPeca = new PersistenciaPostgres().getPersistenciaItensPeca();
    }

    private IFrameConsSemeadora() {

        panelButtonsConsultaSemeadora = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btConsultaSemSalvar = new JButton("Salvar");
        btConsultaSemAlterar = new JButton("Alterar");
        btConsultaSemExcluir = new JButton("Excluir");
        btConsultaSemCancelar = new JButton("Cancelar");
        panelPesquisarSemeadora = new JPanel(new GridBagLayout());
        jScrollPane2 = new JScrollPane();
        listConsultaSemSemeadoras = new JList();
        lbConsultaSemSemeadora = new JLabel("Semeadoras");
        cbConsultaSemTipoCons = new JComboBox();
        lbConsultaSemPesquisar = new JLabel("Pesquisar");
        tfConsultaSemeadoraPesquisar = new JTextField();
        panelConsultaSemeadora = new JPanel(new BorderLayout());
        panelDadosSemeadoraConsulta = new JPanel(new GridBagLayout());
        lbCodSemConsulta = new JLabel("Código: ");
        lbModSemConsulta = new JLabel("Modelo: ");
        tfCodSemConsulta = new JTextField();
        lbMarcaSemConsulta = new JLabel("Marca: ");
        tfMarcaSemConsulta = new JTextField();
        lbAnoSemConsulta = new JLabel("Ano: ");
        try {
            ftfAnoSemConsulta = new JFormattedTextField(new DefaultFormatterFactory(new MaskFormatter("####")));
        } catch (ParseException ex) {
            Logger.getLogger(IFrameConsSemeadora.class.getName()).log(Level.SEVERE, null, ex);
        }
        lbDataRegistroConsulta = new JLabel("Data de Registro:");
        try {
            ftfDataRegistroConsulta = new JFormattedTextField(new DefaultFormatterFactory(new MaskFormatter("##/##/####")));
        } catch (ParseException ex) {
            Logger.getLogger(IFrameConsSemeadora.class.getName()).log(Level.SEVERE, null, ex);
        }
        tfModeloConsulta = new JTextField();
        panelDivPecasSemeadora = new JPanel(new GridBagLayout());
        lbConsultaSemDivisao = new JLabel("Divisão:");
        lbConsultaSemPeça = new JLabel("Peça:");
        jScrollPane3 = new JScrollPane();
        listConsultaSemDivisoes = new JList();
        jScrollPane4 = new JScrollPane();
        listConsultaSemPeca = new JList();
        btConsultaSemDadosPeca = new JButton("Dados da Peça");

        configuraPanelDadosSemeadora();
        configuraPanelDivisoesPecas();
        configuraPanelOperacoes();
        configuraPanelPesquisar();

        this.getContentPane().add(panelConsultaSemeadora, BorderLayout.CENTER);

        this.setClosable(true);
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setTitle("Consultar Semeadora");
        //this.setPreferredSize(new java.awt.Dimension(850, 600));
        this.setSize(new java.awt.Dimension(850, 520));
        this.addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                cadSem.clearList();
                //código aqui
            }

            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }

            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
    }

    private void configuraPanelOperacoes() {

        btConsultaSemSalvar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                if (JOptionPane.showConfirmDialog(null, "Confirma a alteração?", "Confirmação", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION && alterarSemeadora()) {

                    JOptionPane.showMessageDialog(null, "Semeadora alterada!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                    startFrame();
                }
            }
        });
        panelButtonsConsultaSemeadora.add(btConsultaSemSalvar);

        btConsultaSemAlterar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                if (listConsultaSemSemeadoras.getSelectedIndex() != -1) {

                    habilitaEdicaoCampos(true);
                    ajustaBotoesOperacoes(false);
                    listConsultaSemSemeadoras.setEnabled(false);
                    tfConsultaSemeadoraPesquisar.setEditable(false);
                    ftfAnoSemConsulta.requestFocus();
                } else {

                    JOptionPane.showMessageDialog(null, "Deve-se selecionar uma semeadora antes!", "Semeadora não selecionada", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panelButtonsConsultaSemeadora.add(btConsultaSemAlterar);

        btConsultaSemExcluir.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                if (listConsultaSemSemeadoras.getSelectedIndex() != -1) {

                    if (JOptionPane.showConfirmDialog(null, "Deseja realmente excluir a semeadora? \n" + "Excluir a semeadora implica em perder todos as atividades e manutenções relacionadas a ela!", "Confirmação de exclusão", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

                        try {

                            cadSem.deleteSemeadora(obtemSemeadoraSelecionada().getIdentificacao());
                            JOptionPane.showMessageDialog(null, "Semeadora excluída", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                        } catch (DelecaoException de) {

                            JOptionPane.showMessageDialog(null, de.getMessage(), "Falha", JOptionPane.WARNING_MESSAGE);
                            JOptionPane.showMessageDialog(null, de.getRTException().getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                        }
                        startFrame();
                    }
                } else {

                    JOptionPane.showMessageDialog(null, "Deve-se selecionar uma semeadora antes!", "Semeadora não selecionada", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panelButtonsConsultaSemeadora.add(btConsultaSemExcluir);

        btConsultaSemCancelar.setText("Cancelar");
        btConsultaSemCancelar.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                habilitaEdicaoCampos(false);
                ajustaBotoesOperacoes(true);
                listConsultaSemSemeadoras.setEnabled(true);
                tfConsultaSemeadoraPesquisar.setEditable(true);
            }
        });
        panelButtonsConsultaSemeadora.add(btConsultaSemCancelar);

        this.getContentPane().add(panelButtonsConsultaSemeadora, java.awt.BorderLayout.SOUTH);
    }

    private void configuraPanelPesquisar() {

        panelPesquisarSemeadora.setBorder(javax.swing.BorderFactory.createTitledBorder("Pesquisar Semeadora"));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        cbConsultaSemTipoCons.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Identificação", "Modelo", "Marca", "Ano"}));
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        panelPesquisarSemeadora.add(cbConsultaSemTipoCons, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        panelPesquisarSemeadora.add(lbConsultaSemPesquisar, gridBagConstraints);

        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        tfConsultaSemeadoraPesquisar.setColumns(10);
        tfConsultaSemeadoraPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {

                if (!"".equals(tfConsultaSemeadoraPesquisar.getText())) {

                    Object valor = tfConsultaSemeadoraPesquisar.getText();
                    try {

                        valor = Integer.parseInt(valor.toString());
                    } catch (NumberFormatException nfe) {
                    }

                    preencheListaSemeadoras(pesquisarSemeadora(cbConsultaSemTipoCons.getSelectedItem().toString(), valor));
                } else {

                    cadSem.listSemeadoras();
                    preencheListaSemeadoras(CadastroSemeadoras.semeadoras.values());
                }

            }
        });
        panelPesquisarSemeadora.add(tfConsultaSemeadoraPesquisar, gridBagConstraints);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelPesquisarSemeadora.add(lbConsultaSemSemeadora, gridBagConstraints);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(280, 200));
        listConsultaSemSemeadoras.setPreferredSize(new java.awt.Dimension(350, 40));
        listConsultaSemSemeadoras.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {

                if (listConsultaSemSemeadoras.getSelectedIndex() != -1) {

                    preencheCamposSemeadora(obtemSemeadoraSelecionada());
                } else {

                    limpaCamposConsultaSemeadora();
                }
            }
        });
        jScrollPane2.setViewportView(listConsultaSemSemeadoras);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 0, 0);
        panelPesquisarSemeadora.add(jScrollPane2, gridBagConstraints);

        this.getContentPane().add(panelPesquisarSemeadora, java.awt.BorderLayout.WEST);
    }

    private void configuraPanelDadosSemeadora() {

        panelDadosSemeadoraConsulta.setBorder(javax.swing.BorderFactory.createTitledBorder("Dados da Semeadora"));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        panelDadosSemeadoraConsulta.add(lbCodSemConsulta, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(tfCodSemConsulta, gridBagConstraints);

        gridBagConstraints.ipadx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(lbAnoSemConsulta, gridBagConstraints);

        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(ftfAnoSemConsulta, gridBagConstraints);

        gridBagConstraints.ipadx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(8, 0, 0, 0);
        panelDadosSemeadoraConsulta.add(lbModSemConsulta, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 175;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(tfModeloConsulta, gridBagConstraints);

        gridBagConstraints.ipadx = 0;
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(lbDataRegistroConsulta, gridBagConstraints);

        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(ftfDataRegistroConsulta, gridBagConstraints);

        gridBagConstraints.ipadx = 0;
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        panelDadosSemeadoraConsulta.add(lbMarcaSemConsulta, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 175;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 0, 0);
        panelDadosSemeadoraConsulta.add(tfMarcaSemConsulta, gridBagConstraints);

        panelConsultaSemeadora.add(panelDadosSemeadoraConsulta, java.awt.BorderLayout.NORTH);
    }

    private void configuraPanelDivisoesPecas() {

        panelDivPecasSemeadora.setBorder(javax.swing.BorderFactory.createTitledBorder("Divisões e Peças"));

        GridBagConstraints gridBagConstraints = new GridBagConstraints();

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        panelDivPecasSemeadora.add(lbConsultaSemDivisao, gridBagConstraints);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        panelDivPecasSemeadora.add(lbConsultaSemPeça, gridBagConstraints);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(200, 200));

        listConsultaSemDivisoes.setPreferredSize(new java.awt.Dimension(200, 200));
        listConsultaSemDivisoes.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            @Override
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {

                if (listConsultaSemDivisoes.getSelectedIndex() != -1 && listConsultaSemSemeadoras.getSelectedIndex() != -1) {

                    ModelString ms = listConsultaSemSemeadoras.getSelectedValue();
                    Semeadora sem = CadastroSemeadoras.semeadoras.get(ms.getCodigo());
                    ModelString msDiv = listConsultaSemDivisoes.getSelectedValue();
                    preencheListaPecas(sem.selecionarDivisao(msDiv.getCodigo()).listarPecas());
                } else {

                    listConsultaSemPeca.setModel(new DefaultListModel());
                }
            }
        });
        jScrollPane3.setViewportView(listConsultaSemDivisoes);

        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelDivPecasSemeadora.add(jScrollPane3, gridBagConstraints);

        jScrollPane4.setPreferredSize(new java.awt.Dimension(200, 200));

        listConsultaSemPeca.setPreferredSize(new java.awt.Dimension(250, 200));
        listConsultaSemPeca.setValueIsAdjusting(true);
        jScrollPane4.setViewportView(listConsultaSemPeca);

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
        panelDivPecasSemeadora.add(jScrollPane4, gridBagConstraints);

        btConsultaSemDadosPeca.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {

                if (listConsultaSemSemeadoras.getSelectedIndex() != -1 && listConsultaSemPeca.getSelectedIndex() != -1) {

                    Container cont = getThis().getParent();
                    Semeadora sem = cadSem.selectSemeadora(listConsultaSemSemeadoras.getSelectedValue().getCodigo());
                    IFrameConsPecaSemeadora consPeca = new IFrameConsPecaSemeadora(sem.selecionarPeca(listConsultaSemPeca.getSelectedValue().getCodigo()));
                    cont.add(consPeca);
                    consPeca.setVisible(true);
                }else{
                    
                    JOptionPane.showMessageDialog(null, "Deve-se selecionar uma peça antes!", "Peça não selecionada", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        btConsultaSemDadosPeca.setPreferredSize(new java.awt.Dimension(70, 25));
        panelDivPecasSemeadora.add(btConsultaSemDadosPeca, gridBagConstraints);

        panelConsultaSemeadora.add(panelDivPecasSemeadora, java.awt.BorderLayout.CENTER);
    }

    public static IFrameConsSemeadora getInstance() {

        return iFrameConsSemeadora;
    }

    @Override
    public void startFrame() {

        tfCodSemConsulta.setEditable(false);
        habilitaEdicaoCampos(false);
        limpaCamposConsultaSemeadora();
        cadSem.listSemeadoras();
        preencheListaSemeadoras(CadastroSemeadoras.semeadoras.values());
        tfConsultaSemeadoraPesquisar.setEditable(true);
        tfConsultaSemeadoraPesquisar.setText("");
        listConsultaSemSemeadoras.setEnabled(true);
        ajustaBotoesOperacoes(true);
        tfConsultaSemeadoraPesquisar.requestFocus();
    }

    private void habilitaEdicaoCampos(boolean habilita) {

        tfMarcaSemConsulta.setEditable(habilita);
        tfModeloConsulta.setEditable(habilita);
        ftfAnoSemConsulta.setEditable(habilita);
        ftfDataRegistroConsulta.setEditable(habilita);
    }

    private void limpaCamposConsultaSemeadora() {

        tfCodSemConsulta.setText("");
        tfMarcaSemConsulta.setText("");
        tfModeloConsulta.setText("");
        ftfAnoSemConsulta.setText("");
        ftfDataRegistroConsulta.setText("");
        listConsultaSemDivisoes.setModel(new DefaultListModel<ModelString>());
        listConsultaSemPeca.setModel(new DefaultListModel<ModelString>());
    }

    private void preencheListaSemeadoras(Collection<Semeadora> semeadoras) {

        DefaultListModel<ModelString> dls = new DefaultListModel<ModelString>();

        for (Semeadora sem : semeadoras) {

            StringBuilder sb = new StringBuilder();
            sb.append(sem.getIdentificacao());
            sb.append(" - ");
            sb.append(sem.getModelo());
            sb.append(" - ");
            sb.append(sem.getMarca());
            sb.append(" - ");
            sb.append(sem.getAno());

            ModelString msSem = new ModelString(sb.toString(), sem.getIdentificacao());
            dls.addElement(msSem);
        }

        listConsultaSemSemeadoras.setModel(dls);
    }

    private void ajustaBotoesOperacoes(boolean habilita) {

        btConsultaSemAlterar.setVisible(habilita);
        btConsultaSemExcluir.setVisible(habilita);
        btConsultaSemCancelar.setVisible(!habilita);
        btConsultaSemSalvar.setVisible(!habilita);
    }

    private Collection<Semeadora> pesquisarSemeadora(String atributoBusca, Object valor) {

        ArrayList<Semeadora> semeadoras = new ArrayList<Semeadora>();

        if (atributoBusca.equals("Identificação") && valor instanceof Integer) {

            cadSem.selectSemeadora((Integer) valor);
            if (CadastroSemeadoras.semeadoras.get(valor) != null) {

                semeadoras.add(CadastroSemeadoras.semeadoras.get((Integer) valor));
            }
        } else if (atributoBusca.equals("Marca") && valor instanceof String) {

            cadSem.selectSemeadorasByMarca(valor.toString());
            semeadoras.addAll(CadastroSemeadoras.semeadoras.values());
        } else if (atributoBusca.equals("Modelo") && valor instanceof String) {

            cadSem.selectSemeadorasByModelo(valor.toString());
            semeadoras.addAll(CadastroSemeadoras.semeadoras.values());
        } else if (atributoBusca.equals("Ano") && valor instanceof Integer) {

            cadSem.selectSemeadorasByAno((Integer) valor);
            semeadoras.addAll(CadastroSemeadoras.semeadoras.values());
        }

        return semeadoras;
    }

    private Semeadora obtemSemeadoraSelecionada() {

        ModelString ms = listConsultaSemSemeadoras.getSelectedValue();
        return CadastroSemeadoras.semeadoras.get(ms.getCodigo());
    }

    private void preencheCamposSemeadora(Semeadora sem) {

        tfCodSemConsulta.setText("" + sem.getIdentificacao());
        ftfAnoSemConsulta.setText("" + sem.getAno());
        tfMarcaSemConsulta.setText(sem.getMarca());
        tfModeloConsulta.setText(sem.getModelo());
        ftfDataRegistroConsulta.setText(DateConversion.dateToString(sem.getDataRegistro()));
        preencheListaDivisoes(sem.listarDivisoes());
    }

    private void preencheListaDivisoes(Collection<Divisao> divisoes) {

        DefaultListModel<ModelString> dlm = new DefaultListModel<ModelString>();

        for (Divisao div : divisoes) {

            StringBuilder builder = new StringBuilder();
            builder.append(div.getIdentificao());
            builder.append(" - ");
            builder.append(div.getNome());
            ModelString ms = new ModelString(builder.toString(), div.getIdentificao());

            dlm.addElement(ms);
        }

        listConsultaSemDivisoes.setModel(dlm);
    }

    private void preencheListaPecas(Collection<AlocacaoPeca> alocacoes) {

        DefaultListModel<ModelString> dlm = new DefaultListModel<ModelString>();

        for (AlocacaoPeca aloc : alocacoes) {

            StringBuilder builder = new StringBuilder();
            builder.append(aloc.getItemPeca().getIdentificacao());
            builder.append(" - ");
            builder.append(aloc.getItemPeca().getPeca().getTipo().toString());
            builder.append(" - ");
            builder.append(aloc.getTempoVidaUtil());

            ModelString ms = new ModelString(builder.toString(), aloc.getItemPeca().getIdentificacao());
            dlm.addElement(ms);
        }

        listConsultaSemPeca.setModel(dlm);
    }

    private boolean alterarSemeadora() {

        if (verificaAno(ftfAnoSemConsulta) && verificaData(ftfDataRegistroConsulta) && verificaCampoVazio(tfMarcaSemConsulta) && verificaCampoVazio(tfModeloConsulta)) {

            Semeadora sem = obtemSemeadoraSelecionada();
            try {

                sem.setIdentificacao(Integer.parseInt(tfCodSemConsulta.getText()));
                sem.setAno(Integer.parseInt(ftfAnoSemConsulta.getText()));
                sem.setDataRegistro(DateConversion.stringToDate(ftfDataRegistroConsulta.getText()));
                sem.setMarca(tfMarcaSemConsulta.getText());
                sem.setModelo(tfModeloConsulta.getText());
            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, ex.getMessage(), "Inconsistência", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            try {

                cadSem.updateSemeadora(sem.getIdentificacao(), sem);
            } catch (AtualizacaoException ae) {

                JOptionPane.showMessageDialog(null, ae.getMessage(), "Falha", JOptionPane.WARNING_MESSAGE);
                JOptionPane.showMessageDialog(null, ae.getException().getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        }

        return false;
    }

    private boolean verificaAno(JTextField tfAno) throws NumberFormatException {

        try {

            Integer.parseInt(tfAno.getText());
            return true;
        } catch (NumberFormatException nfe) {

            JOptionPane.showMessageDialog(null, "O ano informado é inválido. O ano é composto por 4 dígitos!");
            tfAno.requestFocus();
            return false;
        }
    }

    private boolean verificaCampoVazio(JTextField tfCampo) {

        try {

            if (tfCampo.getText().equals("")) {

                JOptionPane.showMessageDialog(null, "Não é admitido campo vazio");
                tfCampo.requestFocus();
                return false;
            }

            return true;
        } catch (RuntimeException rte) {

            JOptionPane.showMessageDialog(null, rte.getMessage(), null, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    private boolean verificaData(JTextField tfData) {

        try {

            DateConversion.stringToDate(tfData.getText());
            return true;
        } catch (RuntimeException ex) {

            JOptionPane.showMessageDialog(null, "A data informada é inválida. \n" + "Deve seguir o modelo ##/##/####");
            tfData.requestFocus();
            return false;
        }
    }

    private IFrameConsSemeadora getThis() {

        return this;
    }
}
