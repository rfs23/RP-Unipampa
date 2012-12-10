/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.border.Border;

/**
 *
 * @author rafael
 */
public class BackgroundBorder implements Border{
    
    private BufferedImage bgImage;

    public BackgroundBorder(){
        
        try{
            
            bgImage = ImageIO.read(new URL(this.getClass().getResource("../Imagens/EDX_6000-2C_001_d2_100827.jpg").toString()));
        }catch (Exception ex){
            
            System.err.println(ex.getMessage());
        }
    }
    
    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        
        g.drawImage(bgImage, (x + (width - bgImage.getWidth())/2), (y + (height - bgImage.getHeight())/2), null);
        
    }

    @Override
    public Insets getBorderInsets(Component c) {
        
        return new Insets(0, 0, 0, 0);
    }

    @Override
    public boolean isBorderOpaque() {
        
        return false;
    }
    
}
