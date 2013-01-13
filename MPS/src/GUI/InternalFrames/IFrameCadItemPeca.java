/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import Cadastro.CadastroItensPeca;
import Utilitários.ModelString;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;

/**
 *
 * @author rafael
 */
public class IFrameCadItemPeca extends StartableInternalFrame {

    private JPanel panelCadPecaDados;
    private JLabel lbCadPecaCod;
    private JTextField tfCadPecaCod;
    private JLabel lbCadPecaTipo;
    private JComboBox<ModelString> cbCadPecaTipo;
    private JLabel lbCadPecaDataAquis;
    private JFormattedTextField ftfCadPecaDataAquis;
    private JLabel lbCadPecaFabricante;
    private JTextField tfCadPecaFabricante;
    private JLabel lbCadPecaDataReg;
    private JFormattedTextField ftfCadPecaDataReg;
    private JLabel lbCadPecaTVUR;
    private JSlider sldCadPecaTVUR;
    private JLabel lbCadPecaQtd;
    private JSpinner spnQtd;
    private JLabel lbCadPecaAno;
    private JFormattedTextField ftfCadPecaAno;
    private JLabel lbCadPecaEstadoSldTVUR;
    private JPanel panelCadPecaButtons;
    private JButton btCadPecaIncluir;
    private JButton btCadPecaCancelar;
    private static CadastroItensPeca cadIPeca;
    private static IFrameCadItemPeca frameCadIPeca = new IFrameCadItemPeca();
    
    static{
        
        cadIPeca = new PersistenciaPostgres().getPersistenciaItensPeca();
    }
    
    private IFrameCadItemPeca(){
        
        panelCadPecaDados = new JPanel(new GridBagLayout());
        lbCadPecaCod = new JLabel("Código:");
        tfCadPecaCod = new JTextField();
        lbCadPecaTipo = new JLabel("Tipo:");
        cbCadPecaTipo = new JComboBox<ModelString>(new DefaultComboBoxModel<ModelString>());
        lbCadPecaDataAquis = new JLabel("Data de Aquisição:");
        try {
            ftfCadPecaDataAquis = new JFormattedTextField(new DefaultFormatterFactory(new MaskFormatter("##/##/####")));
        } catch (ParseException ex) {
            Logger.getLogger(IFrameCadItemPeca.class.getName()).log(Level.SEVERE, null, ex);
        }
        lbCadPecaFabricante = new JLabel("Fabricante");
        tfCadPecaFabricante = new JTextField();
        lbCadPecaDataReg = new JLabel("Data de Registro");
        try {
            ftfCadPecaDataReg = new JFormattedTextField(new DefaultFormatterFactory(new MaskFormatter("##/##/####")));
        } catch (ParseException ex) {
            Logger.getLogger(IFrameCadItemPeca.class.getName()).log(Level.SEVERE, null, ex);
        }
        lbCadPecaTVUR = new JLabel("Tempo de Vida Útil Restante:");
        sldCadPecaTVUR = new JSlider(JSlider.HORIZONTAL);
        lbCadPecaQtd = new JLabel("Quantidade:");
        spnQtd = new JSpinner(new SpinnerNumberModel(1, 1, 30, 1));
        lbCadPecaAno = new JLabel("Ano:");
        try {
            ftfCadPecaAno = new JFormattedTextField(new DefaultFormatterFactory(new MaskFormatter("####")));
        } catch (ParseException ex) {
            Logger.getLogger(IFrameCadItemPeca.class.getName()).log(Level.SEVERE, null, ex);
        }
        lbCadPecaEstadoSldTVUR = new JLabel();
        panelCadPecaButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 5));
        btCadPecaIncluir = new JButton("Incluir");
        btCadPecaCancelar = new JButton("Cancelar");
        
        configuraPanelDados();
        configuraPanelButtons();
        
        this.setClosable(true);
        this.setIconifiable(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setSize(500, 500);
    }

    public static IFrameCadItemPeca getInstance(){
        
        return frameCadIPeca;
    }
    
    @Override
    public void startFrame(){
        
        
    }
    
    private void configuraPanelDados(){
        
        
    }
    
    private void configuraPanelButtons(){
        
        
    }
}
