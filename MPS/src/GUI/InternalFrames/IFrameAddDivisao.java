/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import CN.Divisao;
import CN.ItemPeca;
import CN.TipoAlocacao;
import CN.TipoPeca;
import Cadastro.CadastroItensPeca;
import Utilitários.ModelString;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

/**
 *
 * @author rafael
 */
public class IFrameAddDivisao extends StartableInternalFrame implements ObservableFrame {

    //Implementar observer para Principal (adddivisao e talvez onClosing)
    private Set<FrameObserver> frameObservers;
    private JPanel panelPecasDivisao;
    private JLabel lbDiscoCortePalhada;
    private JComboBox<ModelString> cbDiscoCortePalhada;
    private JLabel lbEjetorSementes;
    private JComboBox<ModelString> cbEjetorSementes;
    private JLabel lbPonteira;
    private JComboBox<ModelString> cbPonteira;
    private JLabel lbRolamento;
    private JComboBox<ModelString> cbRolamento;
    private JLabel lbDosadorFertilizante;
    private JComboBox<ModelString> cbDosadorFertilizante;
    private JLabel lbDiscoDuploTosado;
    private JComboBox<ModelString> cbDiscoDuploTosado;
    private JButton btAddDiscoCortePalhada;
    private JButton btAddEjetorSementes;
    private JButton btAddPonteira;
    private JButton btAddRolamento;
    private JButton btAddDosadorFertilizante;
    private JButton btAddDiscoDuploTosado;
    private JButton btDivSemReloadDCortePalhada;
    private JButton btDivSemReloadEjSemente;
    private JButton btDivSemReloadPonteira;
    private JButton btDivSemReloadRolamento;
    private JButton btDivSemReloadDosFertilizante;
    private JButton btDivSemReloadDDuploTosado;
    private JPanel panelDivisaoButtons;
    private JButton btIncluirDivisao;
    private JButton btCancelarDivisao;
    private boolean addFlag;
    private static IFrameAddDivisao ifAddDivisao = new IFrameAddDivisao();
    private static CadastroItensPeca cadItensPeca;

    static {

        cadItensPeca = new PersistenciaPostgres().getPersistenciaItensPeca();
    }

    private IFrameAddDivisao() {

        Icon icon = new ImageIcon((this.getClass().getResource("../Button Reload.png")));

        panelPecasDivisao = new JPanel(new GridBagLayout());
        lbDiscoCortePalhada = new JLabel("Disco de Corte de Palhada:");
        cbDiscoCortePalhada = new JComboBox<ModelString>();
        lbEjetorSementes = new JLabel("Ejetor de Sementes:");
        cbEjetorSementes = new JComboBox<ModelString>();
        lbPonteira = new JLabel("Ponteira:");
        cbPonteira = new JComboBox<ModelString>();
        lbRolamento = new JLabel("Rolamento:");
        cbRolamento = new JComboBox<ModelString>();
        lbDosadorFertilizante = new JLabel("Dosador de Fertilizante:");
        cbDosadorFertilizante = new JComboBox<ModelString>();
        lbDiscoDuploTosado = new JLabel("Disco Duplo de Tosado:");
        cbDiscoDuploTosado = new JComboBox<ModelString>();
        btAddDiscoCortePalhada = new JButton("Adicionar Item");
        btAddEjetorSementes = new JButton("Adicionar Item");
        btAddPonteira = new JButton("Adicionar Item");
        btAddRolamento = new JButton("Adicionar Item");
        btAddDosadorFertilizante = new JButton("Adicionar Item");
        btAddDiscoDuploTosado = new JButton("Adicionar Item");
        btDivSemReloadDCortePalhada = new JButton(icon);
        btDivSemReloadEjSemente = new JButton(icon);
        btDivSemReloadPonteira = new JButton(icon);
        btDivSemReloadRolamento = new JButton(icon);
        btDivSemReloadDosFertilizante = new JButton(icon);
        btDivSemReloadDDuploTosado = new JButton(icon);
        panelDivisaoButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btIncluirDivisao = new JButton("Incluir Divisão");
        btCancelarDivisao = new JButton("Cancelar");

        frameObservers = new HashSet<FrameObserver>();

        this.setLayout(new BorderLayout());

        configuraPanelPecas();

        btIncluirDivisao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (verificaItemComboSelecionado(cbDiscoCortePalhada)
                        && verificaItemComboSelecionado(cbDiscoDuploTosado)
                        && verificaItemComboSelecionado(cbDosadorFertilizante)
                        && verificaItemComboSelecionado(cbEjetorSementes)
                        && verificaItemComboSelecionado(cbPonteira)
                        && verificaItemComboSelecionado(cbRolamento)) {

                    addFlag = true;
                    dispose();
                }
            }
        });
        this.panelDivisaoButtons.add(btIncluirDivisao);

        btCancelarDivisao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                dispose();
            }
        });
        this.panelDivisaoButtons.add(btCancelarDivisao);

        this.getContentPane().add(panelDivisaoButtons, BorderLayout.SOUTH);
        this.getContentPane().add(panelPecasDivisao, BorderLayout.CENTER);

        this.setIconifiable(true);
        this.setClosable(true);
        this.setResizable(true);
        this.setTitle("Adicionar Linha de Semeadura");
        this.setSize(650, 400);
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);

        this.addInternalFrameListener(new InternalFrameListener() {
            @Override
            public void internalFrameOpened(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameClosing(InternalFrameEvent e) {

                notifyFrameObservers();
            }

            @Override
            public void internalFrameClosed(InternalFrameEvent e) {

                notifyFrameObservers();
            }

            @Override
            public void internalFrameIconified(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameDeiconified(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameActivated(InternalFrameEvent e) {
            }

            @Override
            public void internalFrameDeactivated(InternalFrameEvent e) {
            }
        });
    }

    private void configuraPanelPecas() {

        this.panelPecasDivisao.setBorder(new TitledBorder("Peças da Divisão"));

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 5, 0, 0);
        this.panelPecasDivisao.add(lbDiscoCortePalhada, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        this.cbDiscoCortePalhada.setPreferredSize(new Dimension(250, 27));
        this.panelPecasDivisao.add(cbDiscoCortePalhada, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        this.btDivSemReloadDCortePalhada.setPreferredSize(new Dimension(30, 30));
        this.panelPecasDivisao.add(btDivSemReloadDCortePalhada, gbc);

        gbc.gridx = 3;
        gbc.gridy = 0;
        this.panelPecasDivisao.add(this.btAddDiscoCortePalhada, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        this.panelPecasDivisao.add(lbDiscoDuploTosado, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        this.cbDiscoDuploTosado.setPreferredSize(new Dimension(250, 27));
        this.panelPecasDivisao.add(cbDiscoDuploTosado, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        this.btDivSemReloadDDuploTosado.setPreferredSize(new Dimension(30, 30));
        this.panelPecasDivisao.add(btDivSemReloadDDuploTosado, gbc);

        gbc.gridx = 3;
        gbc.gridy = 1;
        this.panelPecasDivisao.add(this.btAddDiscoDuploTosado, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        this.panelPecasDivisao.add(lbDosadorFertilizante, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        this.cbDosadorFertilizante.setPreferredSize(new Dimension(250, 27));
        this.panelPecasDivisao.add(cbDosadorFertilizante, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        this.btDivSemReloadDosFertilizante.setPreferredSize(new Dimension(30, 30));
        this.panelPecasDivisao.add(btDivSemReloadDosFertilizante, gbc);

        gbc.gridx = 3;
        gbc.gridy = 2;
        this.panelPecasDivisao.add(this.btAddDosadorFertilizante, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        this.panelPecasDivisao.add(lbEjetorSementes, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        this.cbEjetorSementes.setPreferredSize(new Dimension(250, 27));
        this.panelPecasDivisao.add(cbEjetorSementes, gbc);

        gbc.gridx = 2;
        gbc.gridy = 3;
        this.btDivSemReloadEjSemente.setPreferredSize(new Dimension(30, 30));
        this.panelPecasDivisao.add(btDivSemReloadEjSemente, gbc);

        gbc.gridx = 3;
        gbc.gridy = 3;
        this.panelPecasDivisao.add(this.btAddEjetorSementes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        this.panelPecasDivisao.add(lbPonteira, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        this.cbPonteira.setPreferredSize(new Dimension(250, 27));
        this.panelPecasDivisao.add(cbPonteira, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        this.btDivSemReloadPonteira.setPreferredSize(new Dimension(30, 30));
        this.panelPecasDivisao.add(btDivSemReloadPonteira, gbc);

        gbc.gridx = 3;
        gbc.gridy = 4;
        this.panelPecasDivisao.add(this.btAddPonteira, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        this.panelPecasDivisao.add(lbRolamento, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        this.cbRolamento.setPreferredSize(new Dimension(250, 27));
        this.panelPecasDivisao.add(cbRolamento, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        this.btDivSemReloadRolamento.setPreferredSize(new Dimension(30, 30));
        this.panelPecasDivisao.add(btDivSemReloadRolamento, gbc);

        gbc.gridx = 3;
        gbc.gridy = 5;
        this.panelPecasDivisao.add(this.btAddRolamento, gbc);
    }

    @Override
    public void addObserver(FrameObserver obs) {

        this.frameObservers.add(obs);
    }

    @Override
    public void notifyFrameObservers() {

        for (FrameObserver fo : frameObservers) {

            fo.update(this, addFlag ? createDivisao() : null);
        }
    }

    @Override
    public void startFrame() {

        addFlag = false;

        preencheComboPecas(cbDiscoCortePalhada, TipoPeca.DISCO_DE_CORTE_DE_PALHADA);
        preencheComboPecas(cbDiscoDuploTosado, TipoPeca.DISCO_DUPLO_DE_TOSADO);
        preencheComboPecas(cbDosadorFertilizante, TipoPeca.DOSADOR_DE_FERTILIZANTE);
        preencheComboPecas(cbEjetorSementes, TipoPeca.EJETOR_DE_SEMENTES);
        preencheComboPecas(cbPonteira, TipoPeca.PONTEIRA);
        preencheComboPecas(cbRolamento, TipoPeca.ROLAMENTO);
    }

    private void preencheComboPecas(JComboBox<ModelString> combo, TipoPeca tp) {

        combo.removeAllItems();
        cadItensPeca.selectItensPecaNaoAlocadosByTipo(tp);

        for (ItemPeca ip : CadastroItensPeca.itensPeca.values()) {

            if (IFrameCadSemeadora.getSemeadora().selecionarPeca(ip.getIdentificacao()) == null) {
                
                StringBuilder sb = new StringBuilder();
                sb.append(ip.getIdentificacao());
                sb.append(" - ");
                sb.append(ip.getPeca().getFabricante());
                sb.append(" - ");
                sb.append(ip.getTempoVidaUtilRestante());

                ModelString msIP = new ModelString(sb.toString(), ip.getIdentificacao());

                combo.addItem(msIP);
            }
        }
    }

    private Divisao createDivisao() {

        Divisao div = new Divisao("Linha", TipoAlocacao.Linha);
        div.addPeca(cadItensPeca.selectItemPeca(cbDiscoCortePalhada.getItemAt(cbDiscoCortePalhada.getSelectedIndex()).getCodigo()));
        div.addPeca(cadItensPeca.selectItemPeca(cbDiscoDuploTosado.getItemAt(cbDiscoDuploTosado.getSelectedIndex()).getCodigo()));
        div.addPeca(cadItensPeca.selectItemPeca(cbDosadorFertilizante.getItemAt(cbDosadorFertilizante.getSelectedIndex()).getCodigo()));
        div.addPeca(cadItensPeca.selectItemPeca(cbEjetorSementes.getItemAt(cbEjetorSementes.getSelectedIndex()).getCodigo()));
        div.addPeca(cadItensPeca.selectItemPeca(cbPonteira.getItemAt(cbPonteira.getSelectedIndex()).getCodigo()));
        div.addPeca(cadItensPeca.selectItemPeca(cbRolamento.getItemAt(cbRolamento.getSelectedIndex()).getCodigo()));

        return div;
    }

    private boolean verificaItemComboSelecionado(JComboBox combo) {

        if (combo.getSelectedIndex() == -1) {

            JOptionPane.showMessageDialog(null, "Deve-se selecionar um item!", "Item não selecionado", JOptionPane.WARNING_MESSAGE);
            combo.requestFocus();
            return false;
        }

        return true;
    }

    public static IFrameAddDivisao getInstance() {

        return ifAddDivisao;
    }
}
