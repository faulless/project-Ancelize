package src.Principal;
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;
import java.awt.BorderLayout;


public class Pantalla extends Canvas {
    private static final long serialVersionUID = 1L;

    private static JFrame ventana;
    private static final int ANCHO = 800;
    private static final int ALTO = 600;
    private static final String titulo = "Dark Souls 3.5 fan game";

    private Pantalla() {
        ventana = new JFrame(titulo);
        ventana.setResizable(false);
        ventana.setPreferredSize(new Dimension(ANCHO, ALTO));
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new BorderLayout());
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.add(this);
        ventana.setVisible(true);
    }
}