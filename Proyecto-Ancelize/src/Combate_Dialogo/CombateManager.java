package Combate_Dialogo;

import Interfases_usuario.JuegoGUI;
import Nucleo.Jugador;

public class CombateManager implements java.io.Serializable {
    private static final long serialVersionUID = 1L; // Versión de serialización

    // --- Atributos principales ---
    private Jugador jugador;
    private Enemigo enemigoActual;
    private JuegoGUI juegoGUI;
    private boolean turnoJugador;

    // --- Constructor ---
    public CombateManager(Jugador jugador, JuegoGUI juegoGUI) {
        this.jugador = jugador;
        this.juegoGUI = juegoGUI;
        this.turnoJugador = true; // El jugador comienza el combate
    }

    // --- Métodos principales ---
    public void iniciarCombate(Enemigo enemigo) {
        this.enemigoActual = enemigo;
        this.turnoJugador = true;
        juegoGUI.actualizarNarrativa("¡Inicias combate contra " + enemigo.getNombre() + "!");
        mostrarEstadoCombate();
    }

    public void turnoJugador(String accion) {
        if (!turnoJugador || enemigoActual == null || !enemigoActual.estaVivo()) {
            juegoGUI.actualizarNarrativa("No puedes realizar esta acción en este momento.");
            return;
        }

        switch (accion.toLowerCase()) {
            case "atacar":
                realizarAtaqueJugador();
                break;

            case "usar objeto":
                usarObjeto();
                break;

            case "huir":
                intentarHuir();
                break;

            default:
                juegoGUI.actualizarNarrativa("Acción no válida. Intenta 'atacar', 'usar objeto' o 'huir'.");
                return;
        }

        if (enemigoActual.estaVivo()) {
            turnoJugador = false;
            turnoEnemigo();
        } else {
            juegoGUI.actualizarNarrativa("¡Has derrotado a " + enemigoActual.getNombre() + "!");
            finalizarCombate(true);
        }
    }

    private void turnoEnemigo() {
        if (enemigoActual == null || !enemigoActual.estaVivo() || !jugador.estaVivo()) return;

        int dañoEnemigo = enemigoActual.atacar();
        jugador.recibirDaño(dañoEnemigo);
        juegoGUI.actualizarNarrativa(enemigoActual.getNombre() + " te atacó y causó " + dañoEnemigo + " de daño.");

        if (!jugador.estaVivo()) {
            juegoGUI.actualizarNarrativa("¡Has sido derrotado!");
            finalizarCombate(false);
        } else {
            turnoJugador = true;
            mostrarEstadoCombate();
        }
    }

    private void finalizarCombate(boolean victoriaJugador) {
        if (victoriaJugador) {
            jugador.ganarExperiencia(enemigoActual.getXpOtorgada());
            juegoGUI.actualizarNarrativa("Ganaste " + enemigoActual.getXpOtorgada() + " puntos de experiencia.");
        }
        enemigoActual = null;
        turnoJugador = true;
    }

    // --- Métodos auxiliares ---
    private void realizarAtaqueJugador() {
        int dañoJugador = jugador.atacar();
        enemigoActual.recibirDano(dañoJugador);
        juegoGUI.actualizarNarrativa("Atacaste a " + enemigoActual.getNombre() + " y causaste " + dañoJugador + " de daño.");
    }

    private void usarObjeto() {
        // Implementar lógica para usar un objeto del inventario
        juegoGUI.actualizarNarrativa("Selecciona un objeto para usar. (Falta implementar)");
    }

    private void intentarHuir() {
        if (Math.random() > 0.5) {
            juegoGUI.actualizarNarrativa("¡Lograste huir del combate!");
            finalizarCombate(false);
        } else {
            juegoGUI.actualizarNarrativa("Intentaste huir, pero no lo lograste.");
        }
    }

    private void mostrarEstadoCombate() {
        juegoGUI.actualizarNarrativa(
            "Estado del combate:\n" +
            "Jugador: HP " + jugador.getVidaActual() + "/" + jugador.getVidaMaxima() + "\n" +
            "Enemigo: HP " + enemigoActual.getVidaActual() + "/" + enemigoActual.getVidaMaxima()
        );
    }
}