package Combate_Dialogo;

import java.io.Serializable;
import java.util.Random;

import Nucleo.Movimiento;

public class Enemigo implements Serializable {
    private static final long serialVersionUID = 1L;

    // --- Atributos del Enemigo ---
    private String nombre;
    private String descripcion;
    private int nivel;
    private int vitalidad;
    private int fuerza;
    private int agilidad;
    private int inteligencia;
    private Movimiento movimiento; // Movimiento del enemigo (puede ser un objeto de la clase Movimiento)

    private int vidaMaxima;
    private int vidaActual;
    private int xpOtorgada;

    private ClaseEnemigo claseEnemigo; // Clase del enemigo
    private char representacion; // Representación visual del enemigo en el mapa
    private Random random = new Random();

    // --- Constructor ---
    public Enemigo(String nombre, String descripcion, ClaseEnemigo claseEnemigo, int nivelJugador, int xInicial, int yInicial, int[][] mapa) {
        this.movimiento = new Movimiento(xInicial, yInicial, mapa); // Inicializar movimiento
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.claseEnemigo = claseEnemigo;

        // Generar nivel del enemigo (mínimo 1)
        this.nivel = Math.max(1, nivelJugador + random.nextInt(5) - 2);

        // Calcular atributos basados en la clase y el nivel
        int[] atributosBase = claseEnemigo.getAtributosBase();
        double[] crecimiento = claseEnemigo.getCrecimiento();

        this.vitalidad = calcularAtributo(atributosBase[0], crecimiento[0], nivel);
        this.fuerza = calcularAtributo(atributosBase[1], crecimiento[1], nivel);
        this.agilidad = calcularAtributo(atributosBase[2], crecimiento[2], nivel);
        this.inteligencia = calcularAtributo(atributosBase[3], crecimiento[3], nivel);

        // Calcular estadísticas derivadas
        this.vidaMaxima = vitalidad * 10;
        this.vidaActual = vidaMaxima;
        this.xpOtorgada = nivel * 50; // Ejemplo: 50 XP por nivel
        this.representacion = 'E'; // Representación por defecto
    }

    // --- Coleccion de clases temporales para el enemigo ---
    public static final ClaseEnemigo GUERRERO = new ClaseEnemigo("Guerrero", "Un luchador fuerte y resistente.",
    new int[]{10, 5, 3, 2}, new double[]{1.5, 1.2, 0.8, 0.5});
    public static final ClaseEnemigo MAGO = new ClaseEnemigo("Mago", "Un lanzador de hechizos poderoso.",
    new int[]{6, 2, 4, 8}, new double[]{1.0, 0.5, 1.0, 1.5});
    public static final ClaseEnemigo LADRON = new ClaseEnemigo("Ladrón", "Un experto en el sigilo y el robo.",  
    new int[]{8, 3, 6, 4}, new double[]{1.2, 0.8, 1.5, 0.7});
    public static final ClaseEnemigo CLERIC = new ClaseEnemigo("Clérigo", "Un sanador y protector.",
    new int[]{7, 4, 5, 6}, new double[]{1.3, 0.7, 1.0, 1.2});


    // --- Métodos de Combate ---
    public int atacar() {
        return fuerza / 10; // Ejemplo: El daño es proporcional a la fuerza
    }

    public void recibirDano(int dano) {
        this.vidaActual = Math.max(0, this.vidaActual - dano);
    }

    public boolean esquivar() {
        return random.nextInt(100) < (agilidad * 2); // Probabilidad de esquivar basada en agilidad
    }

    public boolean estaVivo() {
        return this.vidaActual > 0;
    }
    // --- Métodos de Movimiento ---
    public boolean mover(int dx, int dy) {
        return movimiento.mover(dx, dy);
    }

    public int[] getPosicion() {
        return movimiento.getPosicion();
    }

    public String tomarDecision() {
        if (inteligencia >= 10 && vidaActual < (vidaMaxima / 2)) {
            return "curarse"; // Si es inteligente y tiene poca vida, intenta curarse
        } else if (inteligencia >= 5 && random.nextBoolean()) {
            return "esquivar"; // Si es moderadamente inteligente, intenta esquivar
        } else {
            return "atacar"; // Por defecto, ataca
        }
    }

    // --- Métodos de Representación ---
    public char getRepresentacion() {
        return representacion;
    }

    public void setRepresentacion(char representacion) {
        this.representacion = representacion;
    }

    // --- Métodos auxiliares ---
    private int calcularAtributo(int base, double crecimiento, int nivel) {
        return (int) (base + (crecimiento * (nivel - 1)));
    }

    // --- Getters ---
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getNivel() {
        return nivel;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getVidaActual() {
        return vidaActual;
    }

    public int getXpOtorgada() {
        return xpOtorgada;
    }

    public ClaseEnemigo getClaseEnemigo() {
        return claseEnemigo;
    }

    // --- Setters ---
    public void setFuerza(int fuerza) {
        this.fuerza = Math.min(fuerza, 1000);
    }

    public void setAgilidad(int agilidad) {
        this.agilidad = Math.min(agilidad, 1000);
    }

    public void setVitalidad(int vitalidad) {
        this.vitalidad = Math.min(vitalidad, 1000);
    }

    @Override
    public String toString() {
        return nombre + " (Nivel " + nivel + ") - HP: " + vidaActual + "/" + vidaMaxima;
    }
}