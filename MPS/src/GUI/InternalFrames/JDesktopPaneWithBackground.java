/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;

/**
 *
 * @author rafael
 */
public class JDesktopPaneWithBackground extends JDesktopPane {

    public JDesktopPaneWithBackground(BackgroundBorder bg) {
        
        this.setBorder(bg);
    }

    /*@Override
    public void paintComponent(Graphics gr) {

        if (backImage == null) {

            super.paintComponent(gr);
        } else {

            Graphics2D g2d = (Graphics2D) gr;

            //scale the image to fit the size of the Panel
            double mw = backImage.getWidth(null);
            double mh = backImage.getHeight(null);

            double sw = getWidth() / mw;
            double sh = getHeight() / mh;

            g2d.scale(sw, sh);
            g2d.drawImage(backImage, 0, 0, this);
        }
    }*/
}
