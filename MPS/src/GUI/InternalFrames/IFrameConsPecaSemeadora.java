/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import CN.AlocacaoPeca;
import Utilitários.DateConversion;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 *
 * @author rafael
 */
public class IFrameConsPecaSemeadora extends JInternalFrame {

    private JLabel lbDadosPeçaTipo;
    private JTextField tfDadosPeçaTipo;
    private JLabel lbDadosPeçaFabricante;
    private JTextField tfDadosPeçaFabricante;
    private JLabel lbDadosPeçaAno;
    private JTextField tfDadosPeçaAno;
    private JLabel lbDadosPeçaDataAquis;
    private JTextField tfDadosPeçaDataAquis;
    private JLabel lbDadosPeçaTVUR;
    private JTextField tfdadosPeçaTVUR;
    private JButton btDadosPeçaSair;
    private JLabel lbDadosPecaDataAloc;
    private JTextField tfDadosPeçaDataAloc;

    public IFrameConsPecaSemeadora(AlocacaoPeca peca) {

        lbDadosPeçaTipo = new JLabel("Tipo:");
        tfDadosPeçaTipo = new JTextField();
        lbDadosPeçaFabricante = new JLabel("Fabricante:");
        tfDadosPeçaFabricante = new JTextField();
        lbDadosPeçaAno = new JLabel("Ano:");
        tfDadosPeçaAno = new JTextField();
        lbDadosPeçaDataAquis = new JLabel("Data de Aquisição:");
        tfDadosPeçaDataAquis = new JTextField();
        lbDadosPeçaTVUR = new JLabel("Tempo de Vida Útil Restante:");
        tfdadosPeçaTVUR = new JTextField();
        btDadosPeçaSair = new JButton("Sair");
        lbDadosPecaDataAloc = new JLabel("Data de Alocação:");
        tfDadosPeçaDataAloc = new JTextField();

        this.setClosable(true);
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setTitle("Dados da Peça");

        configuraCampos();
        this.setSize(550, 350);
        this.setVisible(true);
        preencheDadosPeca(peca);
    }

    private void configuraCampos() {

        this.getContentPane().setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = java.awt.GridBagConstraints.LINE_END;
        gbc.insets = new java.awt.Insets(20, 10, 0, 0);
        this.getContentPane().add(lbDadosPeçaTipo, gbc);

        tfDadosPeçaTipo.setEditable(false);
        tfDadosPeçaTipo.setPreferredSize(new java.awt.Dimension(200, 27));
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(20, 5, 0, 0);
        this.getContentPane().add(tfDadosPeçaTipo, gbc);

        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.LINE_END;
        gbc.insets = new java.awt.Insets(15, 10, 0, 0);
        this.getContentPane().add(lbDadosPeçaFabricante, gbc);

        tfDadosPeçaFabricante.setEditable(false);
        tfDadosPeçaFabricante.setPreferredSize(new java.awt.Dimension(150, 27));
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.LINE_START;
        gbc.insets = new java.awt.Insets(15, 5, 0, 0);
        this.getContentPane().add(tfDadosPeçaFabricante, gbc);

        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.LINE_END;
        gbc.insets = new java.awt.Insets(15, 10, 0, 0);
        this.getContentPane().add(lbDadosPeçaAno, gbc);

        tfDadosPeçaAno.setEditable(false);
        tfDadosPeçaAno.setPreferredSize(new java.awt.Dimension(70, 27));
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.LINE_START;
        gbc.insets = new java.awt.Insets(15, 5, 0, 0);
        this.getContentPane().add(tfDadosPeçaAno, gbc);

        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.LINE_END;
        gbc.insets = new java.awt.Insets(15, 12, 0, 0);
        this.getContentPane().add(lbDadosPeçaDataAquis, gbc);

        tfDadosPeçaDataAquis.setEditable(false);
        gbc = new java.awt.GridBagConstraints();
        gbc.anchor = java.awt.GridBagConstraints.LINE_END;
        gbc.insets = new java.awt.Insets(20, 10, 0, 0);
        this.getContentPane().add(lbDadosPeçaTipo, gbc);

        tfDadosPeçaTipo.setEditable(false);
        tfDadosPeçaTipo.setPreferredSize(new java.awt.Dimension(200, 27));
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gbc.insets = new java.awt.Insets(20, 5, 0, 0);
        this.getContentPane().add(tfDadosPeçaTipo, gbc);

        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.LINE_END;
        gbc.insets = new java.awt.Insets(15, 10, 0, 0);
        this.getContentPane().add(lbDadosPeçaFabricante, gbc);

        tfDadosPeçaFabricante.setEditable(false);
        tfDadosPeçaFabricante.setPreferredSize(new java.awt.Dimension(150, 27));
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = java.awt.GridBagConstraints.LINE_START;
        gbc.insets = new java.awt.Insets(15, 5, 0, 0);
        this.getContentPane().add(tfDadosPeçaFabricante, gbc);

        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.LINE_END;
        gbc.insets = new java.awt.Insets(15, 10, 0, 0);
        this.getContentPane().add(lbDadosPeçaAno, gbc);

        tfDadosPeçaAno.setEditable(false);
        tfDadosPeçaAno.setPreferredSize(new java.awt.Dimension(70, 27));
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.LINE_START;
        gbc.insets = new java.awt.Insets(15, 5, 0, 0);
        this.getContentPane().add(tfDadosPeçaAno, gbc);

        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.LINE_END;
        gbc.insets = new java.awt.Insets(15, 12, 0, 0);
        this.getContentPane().add(lbDadosPeçaDataAquis, gbc);

        tfDadosPeçaDataAquis.setEditable(false);
        tfDadosPeçaDataAquis.setPreferredSize(new java.awt.Dimension(100, 27));
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.anchor = java.awt.GridBagConstraints.LINE_START;
        gbc.insets = new java.awt.Insets(15, 5, 0, 0);
        this.getContentPane().add(tfDadosPeçaDataAquis, gbc);

        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = java.awt.GridBagConstraints.LINE_END;
        gbc.insets = new java.awt.Insets(15, 10, 0, 0);
        this.getContentPane().add(lbDadosPeçaTVUR, gbc);

        tfdadosPeçaTVUR.setEditable(false);
        tfdadosPeçaTVUR.setPreferredSize(new java.awt.Dimension(50, 27));
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.anchor = java.awt.GridBagConstraints.LINE_START;
        gbc.insets = new java.awt.Insets(15, 5, 0, 0);
        this.getContentPane().add(tfdadosPeçaTVUR, gbc);

        btDadosPeçaSair.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dispose();
            }
        });
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 5;
        this.getContentPane().add(btDadosPeçaSair, gbc);

        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = java.awt.GridBagConstraints.LINE_END;
        gbc.insets = new java.awt.Insets(15, 10, 0, 0);
        this.getContentPane().add(lbDadosPecaDataAloc, gbc);

        tfDadosPeçaDataAloc.setEditable(false);
        tfDadosPeçaDataAloc.setPreferredSize(new java.awt.Dimension(100, 27));
        gbc = new java.awt.GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.anchor = java.awt.GridBagConstraints.LAST_LINE_START;
        gbc.insets = new java.awt.Insets(15, 5, 0, 0);
        this.getContentPane().add(tfDadosPeçaDataAloc, gbc);
    }

    private void preencheDadosPeca(AlocacaoPeca alocPeca) {

        if (alocPeca != null) {
            
            this.tfDadosPeçaTipo.setText(alocPeca.getItemPeca().getPeca().getTipo().toString());
            this.tfDadosPeçaFabricante.setText(alocPeca.getItemPeca().getPeca().getFabricante());
            this.tfDadosPeçaDataAloc.setText(DateConversion.dateToString(alocPeca.getDataInclusaoItemPeca()));
            this.tfDadosPeçaAno.setText("" + alocPeca.getItemPeca().getAnoFab());
            this.tfDadosPeçaDataAquis.setText(DateConversion.dateToString(alocPeca.getItemPeca().getDataAquis()));
            this.tfdadosPeçaTVUR.setText("" + alocPeca.getItemPeca().getTempoVidaUtilRestante());
        }
    }
}
