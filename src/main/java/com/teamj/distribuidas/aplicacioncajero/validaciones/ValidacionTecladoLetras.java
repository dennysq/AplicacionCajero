/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.teamj.distribuidas.aplicacioncajero.validaciones;

import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author DENNYS
 */
public class ValidacionTecladoLetras extends KeyAdapter {

    int length = 1;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (!Character.isLetterOrDigit(e.getKeyChar())) {
            if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE || e.getKeyChar() == KeyEvent.VK_DELETE) {
                e.consume();
            } else {
                if (e.getKeyChar() != KeyEvent.VK_SPACE) {
                    Toolkit.getDefaultToolkit().beep();
                    e.consume();
                }
            }
        }

    }
}
