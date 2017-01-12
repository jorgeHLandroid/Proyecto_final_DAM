package com.example.pc_jorge.app_mant.Utilities;

/**
 * Created by pc-Jorge on 23/08/2016.
 * Clase que modela objetos de tipo intervencion. Se describe la intervencion que se ha realizado
 * que puede ser una averia o un mantenimiento periodico, asi como la fecha y el importe. Guarda tambien
 * la ruta donde se encuentra la imagen asociada.
 */
public class Intervencion {
    private int  drawableImagenID;
    private String nombre,fecha,tipo,descripcion,rutaImagen,kilometrosString;
    long importe;
    int codigo,kilometros;


    public Intervencion(int codigo,String nombre, String fecha, String tipo, long importe,int kilometros, String descripcion,
                        String rutaImagen,int drawableImagenID) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.fecha = fecha;
        this.tipo = tipo;
        this.importe = importe;
        this.kilometros=kilometros;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.drawableImagenID = drawableImagenID;
    }


    public int getCodigo() {
        return codigo;
    }

    //Lo convierte a String para cargarlo en los Textview
    public String getKilometros() {
        kilometrosString=Integer.toString(kilometros);
        return kilometrosString;}

    public String getNombre() {
        return nombre;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public long getImporte() {
        return importe;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public int getDrawableImagenID() {
        return drawableImagenID;
    }


    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setImporte(long importe) {
        this.importe = importe;
    }

    public void setKilometros(int kilometros) {this.kilometros = kilometros;}

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public void setDrawableImagenID(int drawableImagenID) {
        this.drawableImagenID = drawableImagenID;
    }
}
