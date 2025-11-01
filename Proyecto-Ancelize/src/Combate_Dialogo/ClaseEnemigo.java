package Combate_Dialogo;

public class ClaseEnemigo implements java.io.Serializable {
    private static final long serialVersionUID = 1L; // Versión de serialización
    private String nombre;
    private String descripcion;
    private int[] atributosBase; // Atributos iniciales: [Vitalidad, Fuerza, Agilidad, Inteligencia]
    private double[] crecimiento; // Crecimiento por nivel: [Vitalidad, Fuerza, Agilidad, Inteligencia]

    public ClaseEnemigo(String nombre, String descripcion, int[] atributosBase, double[] crecimiento) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.atributosBase = atributosBase.clone();
        this.crecimiento = crecimiento.clone();
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int[] getAtributosBase() {
        return atributosBase.clone();
    }

    public double[] getCrecimiento() {
        return crecimiento.clone();
    }
}