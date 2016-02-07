/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamj.distribuidas.aplicacioncajero.validaciones;

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author DENNYS
 */
public class ValidacionTecladoNum extends KeyAdapter {


    
    
    @Override
    
    public void keyTyped(KeyEvent evt)
    {
        if (!Character.isDigit(evt.getKeyChar()) ) {
            if (evt.getKeyChar() == KeyEvent.VK_BACK_SPACE || evt.getKeyChar() == KeyEvent.VK_DELETE) {
                evt.consume();
            } else {
                Toolkit.getDefaultToolkit().beep();
                evt.consume();
            }

        }
}
    
    
}
