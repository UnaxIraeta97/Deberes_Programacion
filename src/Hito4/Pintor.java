package Hito4;

public class Pintor {
    private int id;
    private String nombre;
    private boolean premiado;

    public Pintor(int id, String nombre, boolean premiado) {
        this.id = id;
        this.nombre = nombre;
        this.premiado = premiado;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isPremiado() {
        return premiado;
    }

    @Override
    public String toString() {
        return nombre;
    }
}