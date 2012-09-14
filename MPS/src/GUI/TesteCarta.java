/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseEvent;
import javax.swing.*;
/**
 *
 * @author rafael
 */
public class TesteCarta extends JFrame{
    
    JPanel panel1 = new JPanel();
    JPanel panel2 = new JPanel();
    JLabel lbCarta = new JLabel();
    
    
    public TesteCarta(){
        
        this.setLayout(new GridLayout(1,2,0,0));
       
        panel2.setLayout(new BorderLayout());
        
        
        panel2.add(lbCarta, BorderLayout.CENTER);
        
        
        this.getContentPane().add(panel1);
        this.getContentPane().add(panel2);
        
        this.setSize(200, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        ImageIcon imgCarta = new ImageIcon("large_clipart_quenclub_nifterdotcom.gif");
        lbCarta.setIcon(new ImageIcon(imgCarta.getImage().getScaledInstance(lbCarta.getWidth(), lbCarta.getHeight(), Image.SCALE_DEFAULT)));
        panel2.addComponentListener(new java.awt.event.ComponentAdapter() {
            
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                atualizar(evt);
            }
        });
        
        lbCarta.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                
                removeCarta(evt, lbCarta);
            }
        });
            
    }
    
    public void atualizar(java.awt.event.ComponentEvent evt){
        
        ImageIcon imgCarta = new ImageIcon("large_clipart_quenclub_nifterdotcom.gif");
        lbCarta.setIcon(new ImageIcon(imgCarta.getImage().getScaledInstance(lbCarta.getWidth(), lbCarta.getHeight(), Image.SCALE_DEFAULT)));
        this.validate();
        this.repaint();
    }
    
    public void removeCarta(MouseEvent evt, JLabel lbCarta){
        
        panel2.remove(lbCarta);
        this.validate();
        this.repaint();
    }
    
    
    
    public static void main(String[] args){
        
        TesteCarta t = new TesteCarta();
        //t.atualizar();
    }
}
