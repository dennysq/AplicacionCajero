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
public class ValidacionTecladoLenght extends KeyAdapter {

     int  lenght;

    public ValidacionTecladoLenght(int _lenght) {
        lenght = _lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public int getLenght() {
        return lenght;
    }

    @Override

    public void keyTyped(KeyEvent evt) {
        if (evt.getSource() instanceof JTextField) {
            String cadena = ((JTextField) evt.getSource()).getText() + evt.getKeyChar();

            if (cadena.length() > lenght) {
                evt.consume();
            }

        }

    }

}
