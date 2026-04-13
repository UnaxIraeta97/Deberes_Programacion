package Hito4;

public class Pintura {
    private int id;
    private String titulo;
    private String fecha;
    private String archivo;
    private int visitas;
    private int idPintor;

    public Pintura(int id, String titulo, String fecha, String archivo, int visitas, int idPintor) {
        this.id = id;
        this.titulo = titulo;
        this.fecha = fecha;
        this.archivo = archivo;
        this.visitas = visitas;
        this.idPintor = idPintor;
    }

    public String getArchivo() {
        return archivo;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return titulo;
    }
}