package Combate_Dialogo;

import java.util.HashMap;
import java.util.Map;

import Nucleo.Movimiento;

public class PNJ  implements java.io.Serializable {
    private static final long serialVersionUID = 1L; // Versión de serialización
    private String id;
    private String nombre;
    private String descripcion;
    private Movimiento movimiento; // Movimiento del PNJ (puede ser un objeto de la clase Movimiento)
    private Map<String, String> dialogos; // Clave: contexto, Valor: diálogo

    public PNJ(String id, String nombre, String descripcion, int x, int y, int[][] mapa) {
        // Inicializar atributos del PNJ
        this.nombre = nombre;
        this.movimiento = new Movimiento(x, y, mapa); // Inicializar movimiento
        this.id = id;
        this.descripcion = descripcion;
        this.dialogos = new HashMap<>();
    }

    // --- Métodos de Gestión de Diálogos ---
    public void agregarDialogo(String contexto, String dialogo) {
        if (contexto != null && dialogo != null) {
            dialogos.put(contexto.toLowerCase(), dialogo);
        }
    }

    public String getDialogo(String contexto) {
        if (contexto == null) return "No tengo nada que decir.";
        return dialogos.getOrDefault(contexto.toLowerCase(), "No tengo nada que decir sobre eso.");
    }
    // Método para el movimiento del PNJ
    public boolean mover(int dx, int dy) {
        return movimiento.mover(dx, dy);
    }

    public int[] getPosicion() {
        return movimiento.getPosicion();
    }

    // --- Getters ---
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return nombre + ": " + descripcion;
    }
}