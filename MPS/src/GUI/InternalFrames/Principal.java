/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import Cadastro.CadastroItensPeca;
import Cadastro.CadastroPecas;
import Cadastro.CadastroSemeadoras;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
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

    JDesktopPaneWithBackground jdp;
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

                addJInternalFrame(IFrameCadSemeadora.getInstance());
                centralizarInternalFrame(IFrameCadSemeadora.getInstance());
            }
        });

        JMenuItem rmSemeadora = new JMenuItem("Excluir Semeadora");
        JMenuItem chSemeadora = new JMenuItem("Alterar Semeadora");
        JMenuItem consSemeadora = new JMenuItem("Consultar Semeadora");

        menuSemeadora.add(addSemeadora);
        menuSemeadora.add(rmSemeadora);
        menuSemeadora.add(chSemeadora);
        menuSemeadora.add(consSemeadora);

        menus.add(menuSemeadora);

        this.setJMenuBar(menus);
    }

    public void addJInternalFrame(StartableInternalFrame jif) {

        if (!containsJInternalFrame(jif)) {

            this.jdp.add(jif);
        }

    }

    private boolean containsJInternalFrame(StartableInternalFrame jif) {

        for (JInternalFrame intFrame : jdp.getAllFrames()) {

            if (intFrame.equals(jif)) {

                return true;
            }
        }

        return false;
    }

    private void centralizarInternalFrame(StartableInternalFrame iFrame) {

        Dimension dmDesk = this.jdp.getSize();
        Dimension dmFrame = iFrame.getSize();

        iFrame.setLocation((dmDesk.width - dmFrame.width) / 2, (dmDesk.height - dmFrame.height) / 2);
        
        if(containsJInternalFrame(iFrame)){
            
            iFrame.setVisible(true);
            iFrame.startFrame();
        }
    }
}
