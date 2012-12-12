/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.InternalFrames;

import javax.swing.JFrame;

/**
 *
 * @author rafael
 */
public interface ObservableFrame {
    
    public void addObserver(FrameObserver obs);
    
    public void notifyFrameObservers();
    
    
}
