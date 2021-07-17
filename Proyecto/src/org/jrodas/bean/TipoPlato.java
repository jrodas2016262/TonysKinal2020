package org.jrodas.bean;

public class TipoPlato {
    private int codigoTipoPlato;
    private String descripcion;
    
public TipoPlato(){
}        

    public TipoPlato(int codigoTipoPlato, String descrpcion) {
        this.codigoTipoPlato = codigoTipoPlato;
        this.descripcion = descrpcion;
    }

    public int getCodigoTipoPlato() {
        return codigoTipoPlato;
    }

    public void setCodigoTipoPlato(int codigoTipoPlato) {
        this.codigoTipoPlato = codigoTipoPlato;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descrpcion) {
        this.descripcion = descrpcion;
    }

    @Override
    public String toString() {
        return codigoTipoPlato + " | " + descripcion;
    }
}
