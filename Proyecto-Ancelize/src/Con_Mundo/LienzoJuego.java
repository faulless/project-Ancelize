package Con_Mundo;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Map;
import java.util.HashMap;

public class LienzoJuego extends JPanel {
    private BufferedImage fondo; // Imagen de fondo
    private int anchoPantalla;
    private int altoPantalla;

    // Cache para evitar recargas innecesarias
    private final Map<String, BufferedImage> imageCache = new HashMap<>();

    // Sprite del jugador
    private BufferedImage spriteJugador = null;
    private String rutaSpriteJugador = "recursos_graficos/Sprites/Personajes_creados/Sprites_en_uso/Carlos.png";
    private int jugadorX = 0;
    private int jugadorY = 0;


    public LienzoJuego(int anchoPantalla, int altoPantalla) {
        this.anchoPantalla = anchoPantalla;
        this.altoPantalla = altoPantalla;
        setPreferredSize(new Dimension(anchoPantalla, altoPantalla));
        cargarFondo("recursos_graficos/Sprites/Terreno/fondo.png"); // Ruta del fondo predeterminado
    }

    // Método para cargar el fondo (usa cache)
    private void cargarFondo(String ruta) {
        fondo = cargarImagenConCache(ruta);
    }

    // Carga una imagen usando la cache
    public BufferedImage cargarImagenConCache(String ruta) {
        if (ruta == null) return null;
        BufferedImage img = imageCache.get(ruta);
        if (img != null) return img;
        try {
            img = ImageIO.read(new File(ruta));
            imageCache.put(ruta, img);
            return img;
        } catch (IOException e) {
            System.err.println("Error al cargar imagen '" + ruta + "': " + e.getMessage());
            return null;
        }
    }

    // Permite asignar sprite del jugador (BufferedImage)
    public void setPlayerSprite(BufferedImage sprite) {
        this.spriteJugador = sprite;
        repaint();
    }

    // Alternativa: asignar sprite por ruta (usa cache)
    public void setPlayerSprite(String ruta) {
        setPlayerSprite(cargarImagenConCache(ruta));
    }

    // Posición en píxeles
    public void setPlayerPosition(int x, int y) {
        this.jugadorX = x;
        this.jugadorY = y;
        repaint();
    }

    // Método para dibujar en el lienzo
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dibujar el fondo primero
        dibujarFondo(g);

        // Dibujar elementos adicionales después
        dibujarElementos(g);
    }

    // Método para dibujar el fondo
    private void dibujarFondo(Graphics g) {
        if (fondo != null) {
            g.drawImage(fondo, 0, 0, anchoPantalla, altoPantalla, null);
        } else {
            // Si no hay fondo, usar un color de fondo predeterminado
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, anchoPantalla, altoPantalla);
        }
    }

    // Método para dibujar elementos adicionales (sprites o gráficos dinámicos)
    private void dibujarElementos(Graphics g) {
        // Aquí se dibujarán los elementos del juego (personajes, enemigos, etc.)
        // Ejemplo de un círculo generado dinámicamente:
        g.setColor(Color.RED);
        g.fillOval(100, 100, 50, 50);

        // Ejemplo de un sprite cargado dinámicamente:
        BufferedImage sprite = cargarSprite("recursos_graficos/Sprites/Entidades/Cabezon.png");
        if (sprite != null) {
            g.drawImage(sprite, 200, 200, 64, 64, null); // Dibujar el sprite en (200, 200)
        }
    }

    // Método para cargar un sprite desde un archivo (compatibilidad)
    private BufferedImage cargarSprite(String ruta) {
        return cargarImagenConCache(ruta);
    }
}