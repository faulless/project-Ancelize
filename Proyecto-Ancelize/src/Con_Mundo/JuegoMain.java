package Con_Mundo;

import Combate_Dialogo.CombateManager;
import Interfases_usuario.JuegoGUI;
import Nucleo.Jugador;

import java.io.Serializable;
import javax.swing.Timer;
import java.awt.image.BufferedImage;
import java.lang.reflect.Method;

public class JuegoMain implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- Atributos principales ---
    private Mundo mundo;
    private Jugador jugador;
    private JuegoGUI juegoGUI;
    private CombateManager combateManager;
    private transient Timer animacionTimer;

    private boolean juegoTerminado;

    // --- ultimo mensaje mostrado ---
    private String ultimoMensajeNarrativo = "";

    // --- Constructor ---
    public JuegoMain(Jugador jugador, JuegoGUI juegoGUI) {
        this.jugador = jugador;
        this.juegoGUI = juegoGUI;

        // Crear el mundo
        inicializarMundo();

        // Inicializar el combate manager
        combateManager = new CombateManager(jugador, juegoGUI);

        // Configurar la GUI
        juegoGUI.setJuegoMain(this);

        // Mostrar bienvenida y estado inicial
        mostrarBienvenida();
        actualizarEstadoJugador();

        // Iniciar el timer de animacion (actualiza animacion y repinta)
        iniciarTimerAnimacion();
    }


    // --- Métodos principales ---
    private void iniciarTimerAnimacion() {
        int delayMs = 100; // ~10 FPS de animación; ajustar a 60ms para ~16FPS si se desea
        animacionTimer = new Timer(delayMs, e -> {
            try {
                if (juegoTerminado) {
                    animacionTimer.stop();
                    return;
                }
                // Actualizar animación del jugador (si está configurada)
                try {
                    jugador.actualizarAnimacion();
                } catch (Throwable t) {
                    // proteger por si algo falla en animación
                }

                // Intentar pasar frame actual al PanelJuego2D si existe un getter en JuegoGUI
                try {
                    Method m = juegoGUI.getClass().getMethod("getPanelJuego2D");
                    Object panel = m.invoke(juegoGUI);
                    if (panel != null) {
                        // Intentar invocar setPlayerSprite(BufferedImage)
                        try {
                            BufferedImage frame = jugador.obtenerFrameActual();
                            Method setSprite = panel.getClass().getMethod("setPlayerSprite", BufferedImage.class);
                            setSprite.invoke(panel, frame);
                        } catch (NoSuchMethodException ns) {
                            // si el panel no tiene setPlayerSprite(BufferedImage), intentar setJugadorRef
                            try {
                                Method setRef = panel.getClass().getMethod("setJugadorRef", Jugador.class);
                                setRef.invoke(panel, jugador);
                            } catch (NoSuchMethodException ns2) {
                                // no hay método compatible; fallback abajo
                            }
                        }
                    }
                } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException ex) {
                    // JuegoGUI no provee getPanelJuego2D — seguir con fallback
                } catch (Throwable reflectionEx) {
                    // Ignorar errores reflectivos para evitar crash
                }

                // Finalmente, pedir a la GUI que actualice el mapa (método que ya usas)
                try {
                    juegoGUI.actualizarMapa();
                } catch (Throwable t) {
                    // proteger si actualizarMapa no existe o falla
                }
            } catch (Throwable outer) {
                // proteger el timer contra excepciones inesperadas
                System.err.println("Error en timer de animación: " + outer.getMessage());
            }
        });
        animacionTimer.setRepeats(true);
        animacionTimer.start();
    }

    public void moverJugador(int dx, int dy) {
        if (juegoTerminado) {
            actualizarNarrativa("El juego ha terminado. No puedes moverte.");
            return;
        }

        // Delegar el movimiento al mundo
        boolean movimientoExitoso = mundo.moverJugador(dx, dy);

        if (movimientoExitoso) {
            // Actualizar el mapa y narrativa
            juegoGUI.actualizarMapa();
            actualizarNarrativa("Te has movido a (" + mundo.getJugadorX() + ", " + mundo.getJugadorY() + ").");

            // Verificar interacciones en la nueva posición
            verificarInteracciones();
        } else {
            actualizarNarrativa("No puedes moverte en esa dirección.");
        }
    }

    public void interactuar() {
        if (juegoTerminado) {
            actualizarNarrativa("El juego ha terminado. No puedes interactuar.");
            return;
        }

        // Delegar la interacción al mundo
        boolean interactuado = mundo.interactuar(jugador, juegoGUI, combateManager);

        if (!interactuado) {
            actualizarNarrativa("No hay nada con lo que interactuar cerca.");
        }
    }
    // por implementar: inventario del jugador y sus objetos
    
    
    public void mostrarEstadoJugador() {
        // Delegar la obtención del estado al jugador
        String estado = jugador.getEstadoResumido();
        juegoGUI.actualizarEstadoJugador(estado);
    }

    public void terminarJuego() {
        juegoTerminado = true;
        if (animacionTimer != null) animacionTimer.stop();
        actualizarNarrativa("El juego ha terminado.");
        try {
            juegoGUI.mostrarNotificacion("¡Gracias por jugar!");
        } catch (Throwable t) { }
    }

    // --- Métodos auxiliares ---
    private void inicializarMundo() {
        mundo = new Mundo(10); // Crear un mundo de 10x10
        mundo.colocarJugador(0,0); // Colocar al jugador en el mapa
    }

    private void mostrarBienvenida() {
        actualizarNarrativa("¡Bienvenido a la aventura, " + jugador.getNombre() + "!");
    }

    private void actualizarEstadoJugador() {
        juegoGUI.actualizarEstadoJugador(jugador.getEstadoResumido());
    }

    private void verificarInteracciones() {
        // Delegar la verificación de interacciones al mundo
        boolean interactuado = mundo.interactuar(jugador, juegoGUI, combateManager);
        if (interactuado) {
            actualizarNarrativa("Has interactuado con éxito.");
        } else {
            actualizarNarrativa("No hay nada con lo que interactuar.");
        }
    }
    private void actualizarNarrativa(String mensaje) {
        if (!mensaje.equals(ultimoMensajeNarrativo)) {
            juegoGUI.actualizarNarrativa(mensaje);
            ultimoMensajeNarrativo = mensaje;
        }
    }

    // --- Getters y Setters ---
    public Mundo getMundo() {
        return mundo;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public JuegoGUI getJuegoGUI() {
        return juegoGUI;
    }

    public CombateManager getCombateManager() {
        return combateManager;
    }

    public boolean isJuegoTerminado() {
        return juegoTerminado;
    }

    public void setJuegoTerminado(boolean juegoTerminado) {
        this.juegoTerminado = juegoTerminado;
    }
}