/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import Cadastro.CadastroItensPeca;
import Cadastro.CadastroPecas;
import Cadastro.CadastroSemeadoras;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 *
 * @author rafael
 */
public class Principal extends JFrame {

    private JDesktopPaneWithBackground jdp;
    public static CadastroSemeadoras cadSem;
    public static CadastroItensPeca cadItemPeca;
    public static CadastroPecas cadPeca;

    public Principal() {

        try {
            UIManager.setLookAndFeel(new NimbusLookAndFeel());
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }

        jdp = new JDesktopPaneWithBackground(new BackgroundBorder());
        jdp.setLayout(new GridBagLayout());

        this.setLayout(new BorderLayout());
        this.getContentPane().add(jdp, BorderLayout.CENTER);
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("MPS - Manutenção Preditiva de Semeadoras");
        this.setVisible(true);

        configuraMenu();

        cadSem = new PersistenciaPostgres().getPersistenciaSemeadora();
        cadItemPeca = new PersistenciaPostgres().getPersistenciaItensPeca();
        cadPeca = new PersistenciaPostgres().getPersistenciaPeca();

        if (cadSem == null || cadItemPeca == null || cadPeca == null) {

            JOptionPane.showMessageDialog(null, "Não foi possível estabelecer conexão com o banco de dados (Postgres). Verifique se ele foi iniciado.", "Falha na conexão com Banco de Dados", JOptionPane.WARNING_MESSAGE);
            this.dispose();
        }

        jdp.setLayout(null);
    }

    private void configuraMenu() {

        JMenuBar menus = new JMenuBar();
        JMenu menuSemeadora = new JMenu("Semeadoras");
        JMenuItem addSemeadora = new JMenuItem("Incluir Semeadora");
        addSemeadora.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                jdp.add(IFrameCadSemeadora.getInstance());
                jdp.centralizarInternalFrame(IFrameCadSemeadora.getInstance());
                IFrameCadSemeadora.getInstance().startFrame();
            }
        });

        JMenuItem rmSemeadora = new JMenuItem("Excluir Semeadora");
        rmSemeadora.addActionListener(new openConsultaSemeadora());
        
        JMenuItem chSemeadora = new JMenuItem("Alterar Semeadora");
        chSemeadora.addActionListener(new openConsultaSemeadora());
        
        JMenuItem consSemeadora = new JMenuItem("Consultar Semeadora");
        consSemeadora.addActionListener(new openConsultaSemeadora());

        menuSemeadora.add(addSemeadora);
        menuSemeadora.add(rmSemeadora);
        menuSemeadora.add(chSemeadora);
        menuSemeadora.add(consSemeadora);

        menus.add(menuSemeadora);

        this.setJMenuBar(menus);
    }

    
    private class openConsultaSemeadora implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            
            jdp.add(IFrameConsSemeadora.getInstance());
            jdp.centralizarInternalFrame(IFrameConsSemeadora.getInstance());
            IFrameConsSemeadora.getInstance().startFrame();
            //IFrameConsSemeadora.getInstance().setVisible(true);
        }
        
        
    }
}
