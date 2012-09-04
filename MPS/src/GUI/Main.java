/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author pc
 */
public class Main{

    private static BufferedImage bi; 
 
    public static void main(String[] args){ 
        try{ 
            loadImage(); 
 
            SwingUtilities.invokeLater(new Runnable(){ 
                @Override 
                public void run(){ 
                    createAndShowGUI(); 
                } 
            }); 
        } 
        catch (IOException e){ 
            // handle exception 
        } 
    } 
 
    private static void loadImage() throws IOException{ 
        bi = ImageIO.read(new File("src" + File.separator + "GUI" + File.separator +  "Penguins.jpg")); 
    } 
 
    private static void createAndShowGUI(){ 
        final JFrame frame = new JFrame(); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
 
        final JPanel panel = new JPanel(){ 
            @Override 
            protected void paintComponent(Graphics g){ 
                Graphics g2 = g.create(); 
                g2.drawImage(bi, 0, 0, getWidth(), getHeight(), null); 
                g2.dispose(); 
            } 
 
            @Override 
            public Dimension getPreferredSize(){ 
                return new Dimension(bi.getWidth(), bi.getHeight()); 
            } 
        }; 
        
        //panel.setLayout(new BorderLayout());
        panel.add(new JMenuBar().add(new JMenu("Teste")), BorderLayout.NORTH);
        frame.add(panel); 
        frame.pack(); 
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true); 
    } 
}
