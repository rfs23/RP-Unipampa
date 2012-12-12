/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import CN.AlocacaoPeca;
import CN.Divisao;
import CN.ItemPeca;
import CN.Semeadora;
import CN.TipoAlocacao;
import CN.TipoPeca;
import Cadastro.CadastroItensPeca;
import Cadastro.CadastroSemeadoras;
import Exceções.InsercaoException;
import Utilitários.DateConversion;
import Utilitários.ModelString;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author rafael
 */
public class IFrameCadSemeadora extends StartableInternalFrame implements FrameObserver {

    private JPanel panelButtons;
    private JButton btConcluir;
    private JButton btCancelar;
    private JPanel panelArea;
    private JPanel panelDadosSemeadora;
    private JPanel panelDivisoesSemeadora;
    private JLabel lbCodSem;
    private JLabel lbModSem;
    private JTextField tfCodSem;
    private JLabel lbMarcaSem;
    private JTextField tfMarcaSem;
    private JLabel lbAnoSem;
    private JFormattedTextField ftfAnoSem;
    private JLabel lbDataRegistro;
    private JFormattedTextField ftfDataRegistro;
    private JTextField tfModelo;
    private JLabel lbDiscoDosador;
    private JComboBox<ModelString> cbDiscoDosador;
    private JScrollPane jScrollPane1;
    private JList<ModelString> listDivisoes;
    private JButton btAddDivisao;
    private JButton btAddDisco;
    private JButton btReloadDiscoDosador;
    private static final IFrameCadSemeadora frame = new IFrameCadSemeadora();
    private static CadastroSemeadoras cadSem = new PersistenciaPostgres().getPersistenciaSemeadora();
    private static CadastroItensPeca cadItemPeca = new PersistenciaPostgres().getPersistenciaItensPeca();
    private static Semeadora sem;

    private IFrameCadSemeadora() {

        panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        btConcluir = new JButton("Concluir");
        btCancelar = new JButton("Cancelar");
        panelArea = new JPanel(new BorderLayout());
        panelDadosSemeadora = new JPanel(new GridBagLayout());
        lbCodSem = new JLabel("Código:");
        lbModSem = new JLabel("Modelo:");
        tfCodSem = new JTextField();
        lbMarcaSem = new JLabel("Marca:");
        tfMarcaSem = new JTextField();
        lbAnoSem = new JLabel("Ano:");
        ftfAnoSem = new JFormattedTextField();
        lbDataRegistro = new JLabel("Data do registro:");
        ftfDataRegistro = new JFormattedTextField();
        panelDivisoesSemeadora = new JPanel(new GridBagLayout());
        tfModelo = new JTextField();
        lbDiscoDosador = new JLabel("Disco Dosador: ");
        cbDiscoDosador = new JComboBox<ModelString>();
        listDivisoes = new JList();
        jScrollPane1 = new JScrollPane(listDivisoes);
        btAddDivisao = new JButton("Adicionar Divisão");
        btAddDisco = new JButton("Adicionar Item");
        btReloadDiscoDosador = new JButton(new ImageIcon(this.getClass().getResource("../Button Reload.png")));


        tfCodSem.setEditable(false);

        panelDadosSemeadora.setBorder(new TitledBorder("Dados da Semeadora"));
        panelDivisoesSemeadora.setBorder(new TitledBorder("Divisões"));

        panelButtons.add(btConcluir);
        btConcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (inserirSemeadora()) {

                    JOptionPane.showMessageDialog(null, "Semeadora incluída com sucesso!", "Semeadora incluída", JOptionPane.INFORMATION_MESSAGE);
                    startFrame();
                }
            }
        });

        panelButtons.add(btCancelar);
        btCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
            }
        });

        configuraPanelDadosSemeadora();
        configuraPanelDivisoes();

        panelArea.add(panelDadosSemeadora, BorderLayout.NORTH);
        panelArea.add(panelDivisoesSemeadora, BorderLayout.CENTER);

        this.getContentPane().add(panelArea, BorderLayout.CENTER);
        this.getContentPane().add(panelButtons, BorderLayout.SOUTH);

        this.setTitle("Cadastro de Semeadora");
        this.setIconifiable(true);
        this.setClosable(true);
        this.setResizable(true);
        this.setSize(550, 500);
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setPreferredSize(new Dimension(500, 500));
    }

    public static IFrameCadSemeadora getInstance() {

        return frame;
    }

    private void configuraPanelDadosSemeadora() {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panelDadosSemeadora.add(lbCodSem, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 0, 0);
        tfCodSem.setPreferredSize(new Dimension(40, 27));
        panelDadosSemeadora.add(tfCodSem, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 0, 0, 0);
        panelDadosSemeadora.add(lbModSem, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 0, 0);
        tfModelo.setPreferredSize(new Dimension(150, 27));
        panelDadosSemeadora.add(tfModelo, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 0, 0);
        panelDadosSemeadora.add(lbAnoSem, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 0, 0);
        try {
            ftfAnoSem.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("####")));
        } catch (ParseException ex) {
            Logger.getLogger(IFrameCadSemeadora.class.getName()).log(Level.SEVERE, null, ex);
        }
        ftfAnoSem.setPreferredSize(new Dimension(80, 27));
        panelDadosSemeadora.add(ftfAnoSem, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 10, 0, 0);
        panelDadosSemeadora.add(lbDataRegistro, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 0, 0);
        try {
            ftfDataRegistro.setFormatterFactory(new DefaultFormatterFactory(new MaskFormatter("##/##/####")));
        } catch (ParseException ex) {
            Logger.getLogger(IFrameCadSemeadora.class.getName()).log(Level.SEVERE, null, ex);
        }
        ftfDataRegistro.setPreferredSize(new Dimension(100, 27));
        panelDadosSemeadora.add(ftfDataRegistro, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 5, 0, 0);
        panelDadosSemeadora.add(lbMarcaSem, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 0, 0);
        tfMarcaSem.setPreferredSize(new Dimension(150, 27));
        panelDadosSemeadora.add(tfMarcaSem, gbc);
    }

    private void configuraPanelDivisoes() {

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(10, 5, 0, 0);
        panelDivisoesSemeadora.add(lbDiscoDosador, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 0, 0);
        cbDiscoDosador.setPreferredSize(new Dimension(220, 25));
        panelDivisoesSemeadora.add(cbDiscoDosador, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 0, 0);
        btReloadDiscoDosador.setPreferredSize(new Dimension(30, 30));
        panelDivisoesSemeadora.add(btReloadDiscoDosador, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 0, 0);
        panelDivisoesSemeadora.add(btAddDisco, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 0, 0);
        btAddDivisao.addActionListener(new AddDivisaoListener());
        panelDivisoesSemeadora.add(btAddDivisao, gbc);

        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.fill = java.awt.GridBagConstraints.BOTH;
        gbc.anchor = java.awt.GridBagConstraints.WEST;
        //gbc.weightx = 1.0;
        //gbc.weighty = 1.0;
        gbc.insets = new java.awt.Insets(5, 10, 0, 0);
        listDivisoes.setModel(new DefaultListModel<ModelString>());
        panelDivisoesSemeadora.add(jScrollPane1, gbc);
    }

    @Override
    public void startFrame() {

        sem = new Semeadora("a", "b", (new Date().getYear() + 1900));

        this.tfCodSem.setText("" + cadSem.gerarCódigoSemeadora());
        this.ftfDataRegistro.setText(Utilitários.DateConversion.dateToString(new Date()));
        this.tfMarcaSem.setText("");
        this.tfModelo.setText("");
        ftfAnoSem.setText("");
        listDivisoes.setModel(new DefaultListModel<ModelString>());
        preencheComboDiscoDosador();
        this.ftfAnoSem.requestFocus();

    }

    private void preencheComboDiscoDosador() {

        cbDiscoDosador.removeAllItems();
        cadItemPeca.selectItensPecaNaoAlocadosByTipo(TipoPeca.DISCO_DOSADOR);

        for (ItemPeca ip : CadastroItensPeca.itensPeca.values()) {

            StringBuilder sb = new StringBuilder();
            sb.append(ip.getIdentificacao());
            sb.append(" - ");
            sb.append(ip.getPeca().getFabricante());
            sb.append(" - ");
            sb.append(ip.getTempoVidaUtilRestante());
            System.out.println(sb.toString());

            ModelString msIP = new ModelString(sb.toString(), ip.getIdentificacao());
            this.cbDiscoDosador.addItem(msIP);
        }
    }

    private boolean inserirSemeadora() {

        if (verificaAno(ftfAnoSem) && verificaCampoVazio(tfModelo) && verificaData(ftfDataRegistro) && verificaCampoVazio(tfMarcaSem) && verificaItemComboSelecionado(cbDiscoDosador)) {

            int cod = Integer.parseInt(tfCodSem.getText());
            int ano = Integer.parseInt(ftfAnoSem.getText());
            String marca = tfMarcaSem.getText();
            String modelo = tfModelo.getText();
            Date dataRegistro = DateConversion.stringToDate(ftfDataRegistro.getText());
            ItemPeca ip = cadItemPeca.selectItemPeca(cbDiscoDosador.getItemAt(cbDiscoDosador.getSelectedIndex()).getCodigo());
            
            try {

                sem.setAno(ano);
                sem.setDataRegistro(dataRegistro);
                sem.setMarca(marca);
                sem.setModelo(modelo);
                sem.setIdentificacao(cod);
                int codDiv = sem.nextCodeDivisao();
                sem.addDivisao(codDiv, "Semeadora", TipoAlocacao.Semeadora);
                sem.addPeca(codDiv, ip);
            } catch (Exception ex) {

                JOptionPane.showMessageDialog(null, ex.getMessage(), "Dado Incorreto", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            try {

                cadSem.insertSemeadora(sem);
            } catch (InsercaoException ie) {

                JOptionPane.showMessageDialog(null, ie.getMessage(), "Falha ao inserir semeadora", JOptionPane.WARNING_MESSAGE);
                JOptionPane.showMessageDialog(null, ie.getRTException().getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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

    private boolean verificaItemComboSelecionado(JComboBox combo) {

        if (combo.getSelectedIndex() == -1) {

            JOptionPane.showMessageDialog(null, "Deve-se selecionar um item!", "Item não selecionado", JOptionPane.WARNING_MESSAGE);
            combo.requestFocus();
            return false;
        }

        return true;
    }

    @Override
    public void update(JInternalFrame frame, Object obj) {

        this.setVisible(true);

        if (frame instanceof IFrameAddDivisao) {

            if (obj != null && obj instanceof Divisao) {

                Divisao div = (Divisao) obj;
                div.setNome(div.getTipoAloc() == TipoAlocacao.Linha ? "Linha " + sem.nextCodeDivisao() : "");
                div.setIdentificao(sem.nextCodeDivisao());
                addDivisao(div);
                ((DefaultListModel<ModelString>) listDivisoes.getModel()).addElement(new ModelString("" + div.getIdentificao() + " - " + div.getNome(), div.getIdentificao()));
            }
        }

        ativarAcoes(true);
    }

    private void addDivisao(Divisao div) {

        sem.addDivisao(div.getIdentificao(), div.getNome(), div.getTipoAloc());
        for (AlocacaoPeca aloc : div.listarPecas()) {

            sem.addPeca(div.getIdentificao(), aloc.getItemPeca());
        }
    }

    private IFrameCadSemeadora getThis() {

        return this;
    }

    private void centralizarFrame(JInternalFrame iFrame) {

        Dimension dmDesk = this.getParent().getSize();
        Dimension dmFrame = iFrame.getSize();

        iFrame.setLocation((dmDesk.width - dmFrame.width) / 2, (dmDesk.height - dmFrame.height) / 2);
    }

    private class AddDivisaoListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            //setVisible(false);
            IFrameAddDivisao.getInstance().addObserver(getThis());

            JDesktopPane jdp = (JDesktopPane) getThis().getParent();
            jdp.add(IFrameAddDivisao.getInstance());
            centralizarFrame(IFrameAddDivisao.getInstance());
            IFrameAddDivisao.getInstance().setVisible(true);
            IFrameAddDivisao.getInstance().startFrame();

            ativarAcoes(false);
        }
    }

    private void ativarAcoes(boolean ativ) {

        btConcluir.setEnabled(ativ);
        btCancelar.setEnabled(ativ);
        this.setClosable(ativ);
    }

    public static Semeadora getSemeadora() {

        return sem;
    }
}
