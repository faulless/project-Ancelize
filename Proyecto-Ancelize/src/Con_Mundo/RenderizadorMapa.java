package Con_Mundo;


import java.awt.*;
import java.awt.image.BufferedImage;

public class RenderizadorMapa {
    private int[][] mapa;
    private GestorSprites gestorSprites;

    public RenderizadorMapa(int[][] mapa, GestorSprites gestorSprites) {
        this.mapa = mapa;
        this.gestorSprites = gestorSprites;
    }

    public void renderizar(Graphics g) {
        int tamañoCelda = 64; // Tamaño de cada celda en píxeles
        int subCelda = 16; // Tamaño de cada subcelda en píxeles

        for (int y = 0; y < mapa.length; y++) {
            for (int x = 0; x < mapa[y].length; x++) {
                int idSprite = mapa[y][x];

                // Si la celda es suelo (id 1), usar subtexturas aleatorias
                if (idSprite == 1) {
                    for (int subY = 0; subY < tamañoCelda; subY += subCelda) {
                        for (int subX = 0; subX < tamañoCelda; subX += subCelda) {
                            BufferedImage subtextura = gestorSprites.getSubtexturaAleatoria(idSprite);
                            if (subtextura != null) {
                                g.drawImage(subtextura, x * tamañoCelda + subX, y * tamañoCelda + subY, subCelda, subCelda, null);
                            }
                        }
                    }
                } else {
                    // Dibujar celdas completas para otros tipos
                    BufferedImage sprite = gestorSprites.getSprite(idSprite);
                    if (sprite != null) {
                        g.drawImage(sprite, x * tamañoCelda, y * tamañoCelda, tamañoCelda, tamañoCelda, null);
                    }
                }
            }
        }
    }
}