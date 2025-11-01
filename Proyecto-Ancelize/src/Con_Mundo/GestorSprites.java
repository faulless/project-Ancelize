package Con_Mundo;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GestorSprites {
    private Map<Integer, BufferedImage> sprites;
    private Map<Integer, BufferedImage[]> subtexturas;

    public GestorSprites() {
        sprites = new HashMap<>();
        subtexturas = new HashMap<>();
    }

    public void cargarSprite(int id, String rutaArchivo) {
        try {
            BufferedImage sprite = ImageIO.read(new File(rutaArchivo));
            sprites.put(id, sprite);
        } catch (IOException e) {
            System.err.println("Error al cargar el sprite con ID " + id + ": " + e.getMessage());
        }
    }

    public void cargarSubtexturas(int id, String rutaArchivo, int subAncho, int subAlto) {
        try {
            BufferedImage sprite = ImageIO.read(new File(rutaArchivo));
            int columnas = sprite.getWidth() / subAncho;
            int filas = sprite.getHeight() / subAlto;
            BufferedImage[] subtexturasArray = new BufferedImage[columnas * filas];

            for (int y = 0; y < filas; y++) {
                for (int x = 0; x < columnas; x++) {
                    subtexturasArray[y * columnas + x] = sprite.getSubimage(x * subAncho, y * subAlto, subAncho, subAlto);
                }
            }

            subtexturas.put(id, subtexturasArray);
        } catch (IOException e) {
            System.err.println("Error al cargar subtexturas con ID " + id + ": " + e.getMessage());
        }
    }

    // Obtener sprite por ID (String version)
    public BufferedImage getSprite(String spriteName) {
        return sprites.getOrDefault(spriteName, null);
    }
    // Obtener sprite por ID (int version)
    public BufferedImage getSprite(int spriteId) {
        return sprites.getOrDefault(spriteId, null);
    }

    public BufferedImage getSubtexturaAleatoria(int id) {
        BufferedImage[] subtexturasArray = subtexturas.get(id);
        if (subtexturasArray != null && subtexturasArray.length > 0) {
            int indiceAleatorio = (int) (Math.random() * subtexturasArray.length);
            return subtexturasArray[indiceAleatorio];
        }
        return null;
    }
}