/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

/**
 *
 * @author rafael
 */
public class JDesktopPaneWithBackground extends JDesktopPane {

    public JDesktopPaneWithBackground(BackgroundBorder bg) {

        this.setBorder(bg);
    }

    @Override
    public Component add(Component comp) {

        if (comp instanceof JInternalFrame) {

            if (!containsJInternalFrame((JInternalFrame) comp)) {

                super.add(comp);
            }
        }

        return null;
    }

    private boolean containsJInternalFrame(JInternalFrame jif) {

        for (JInternalFrame intFrame : this.getAllFrames()) {

            if (intFrame.equals(jif)) {

                return true;
            }
        }

        return false;
    }

    public void centralizarInternalFrame(JInternalFrame iFrame) {

        Dimension dmDesk = this.getSize();
        Dimension dmFrame = iFrame.getSize();

        iFrame.setLocation((dmDesk.width - dmFrame.width) / 2, (dmDesk.height - dmFrame.height) / 2);

        if (containsJInternalFrame(iFrame)) {

            iFrame.setVisible(true);
        }
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
