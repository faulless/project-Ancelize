package Con_Mundo;



import java.io.Serializable;

import Combate_Dialogo.Enemigo;

import Nucleo.Jugador;

public class Mundo implements Serializable {
    private int[][] mapa;
    private GestorSprites gestorSprites;
    private int jugadorX;
    private int jugadorY;
    private static final long serialVersionUID = 1L;


    // futura implementacion de los objetos

    public Mundo(int tamaño) {
        this.mapa = new int[tamaño][tamaño];
        this.gestorSprites = new GestorSprites();

        inicializarMapa();
        cargarSprites();
    }

    // --- Inicialización del mapa ---
    private void inicializarMapa() {
        for (int y = 0; y < mapa.length; y++) {
            for (int x = 0; x < mapa[y].length; x++) {
                mapa[y][x] = 2; // Por ejemplo, todo el mapa es pasto
            }
        }
        generarHexagono(mapa.length / 2, mapa[0].length / 2, 10, 1); // Hexágono con radio 10 y valor 1 (suelo)
    
    }

    private void cargarSprites() {
        gestorSprites.cargarSprite(0, "recursos_graficos/Sprites/Vacio.png");
        gestorSprites.cargarSprite(1, "recursos_graficos/Sprites/fondo.png");
        gestorSprites.cargarSprite(2, "recursos_graficos/Sprites/Suelos(64x64).png");
        gestorSprites.cargarSprite(3, "recursos_graficos/Sprites/Pastos(64x64).png");
        // Cargar subtexturas para el suelo (id 1) si tienes un tileset
        gestorSprites.cargarSubtexturas(1, "recursos_graficos/Sprites/Suelos(64x64).png", 16, 16);
    }






    private void generarHexagono(int centroX, int centroY, int radio, int valor) {
        for (int y = -radio; y <= radio; y++) {
            for (int x = -radio; x <= radio; x++) {
                int dx = Math.abs(x);
                int dy = Math.abs(y);
                if (dx + dy <= radio) { // Condición para formar un hexágono
                    int posX = centroX + x;
                    int posY = centroY + y;

                    // Verificar que las coordenadas estén dentro de los límites del mapa
                    if (posX >= 0 && posX < mapa[0].length && posY >= 0 && posY < mapa.length) {
                        mapa[posY][posX] = valor; // Asignar el valor al hexágono
                    }
                }
            }
        }
    }

    // --- Movimiento del jugador ---
    public boolean moverJugador(int dx, int dy) {
        int nuevoX = jugadorX + dx;
        int nuevoY = jugadorY + dy;

        // Verificar límites del mapa
        if (nuevoX < 0 || nuevoX >= mapa[0].length || nuevoY < 0 || nuevoY >= mapa.length) {
            return false; // Movimiento inválido
        }

        // Verificar si la celda es transitable
        if (!esCeldaTransitable(mapa[nuevoY][nuevoX])) {
            return false; // Movimiento bloqueado
        }

        // Actualizar posición del jugador
        jugadorX = nuevoX;
        jugadorY = nuevoY;
        return true;
    }

    private boolean esCeldaTransitable(int idCelda) {
        // Definir qué celdas son transitables
        return idCelda == 1; // Solo el pasto (id 1) es transitable
    }

    // --- Interacciones ---
    public boolean interactuar(Jugador jugador, Interfases_usuario.JuegoGUI juegoGUI, Combate_Dialogo.CombateManager combateManager) {
        // Verificar el contenido de la celda actual
        int idCelda = mapa[jugadorY][jugadorX];

        switch (idCelda) {
            case 3: // Árbol
                juegoGUI.actualizarNarrativa("Es un árbol. No puedes interactuar con él.");
                return false;

            case 4: // Enemigo
                juegoGUI.actualizarNarrativa("¡Un enemigo aparece!");
                combateManager.iniciarCombate(new Enemigo("Goblin", "Un pequeño goblin travieso.", Enemigo.GUERRERO, jugador.getNivel(), jugadorX, jugadorY, mapa));
                return true;

            default:
                juegoGUI.actualizarNarrativa("No hay nada con lo que interactuar aquí.");
                return false;
        }
    }

    // --- Métodos auxiliares ---
    public void colocarJugador(int x, int y) {
        jugadorX = x;
        jugadorY = y;
    }

    // --- Getters ---
    public int[][] getMapa() {
        return mapa;
    }

    public GestorSprites getGestorSprites() {
        return gestorSprites;
    }

    public int getJugadorX() {
        return jugadorX;
    }

    public int getJugadorY() {
        return jugadorY;
    }
}