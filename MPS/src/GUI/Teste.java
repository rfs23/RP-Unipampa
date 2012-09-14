/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

/**
 *
 * @author rafael
 */
public class Teste extends JFrame{
    
    public Teste(){
        
        this.setLayout(new GridLayout(1,2,0,0));
        JPanel panel1 = new JPanel(new BorderLayout());
        JPanel panel2 = new JPanel(new BorderLayout());
        
        JButton bt1 = new JButton("Teste");
        panel1.add(bt1, BorderLayout.CENTER);
        panel2.add(bt1, BorderLayout.CENTER);
        //panel2.remove(bt1);
        
        System.out.println(bt1.getParent());
        this.getContentPane().add(panel1);
        this.getContentPane().add(panel2);
        
        this.setSize(500, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    public static void main (String[] args){
        
        new Teste();
    }
}
