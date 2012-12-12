/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import javax.swing.JInternalFrame;

/**
 *
 * @author rafael
 */
public interface FrameObserver {
    
    public void update(JInternalFrame frame, Object obj);
}
