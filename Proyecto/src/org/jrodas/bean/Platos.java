package org.jrodas.bean;


public class Platos {
    private int CodigoPlato;
    private int Cantidad;
    private String NombrePlato;
    private int PrecioPlato;
    private int TipoPlato;
    
public Platos(){
}

    public Platos(int CodigoPlato, int Cantidad, String NombrePlato, int PrecioPlato, int TipoPlato) {
        this.CodigoPlato = CodigoPlato;
        this.Cantidad = Cantidad;
        this.NombrePlato = NombrePlato;
        this.PrecioPlato = PrecioPlato;
        this.TipoPlato = TipoPlato;
    }

    public int getCodigoPlato() {
        return CodigoPlato;
    }

    public void setCodigoPlato(int CodigoPlato) {
        this.CodigoPlato = CodigoPlato;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    public String getNombrePlato() {
        return NombrePlato;
    }

    public void setNombrePlato(String NombrePlato) {
        this.NombrePlato = NombrePlato;
    }

    public int getPrecioPlato() {
        return PrecioPlato;
    }

    public void setPrecioPlato(int PrecioPlato) {
        this.PrecioPlato = PrecioPlato;
    }

    public int getTipoPlato() {
        return TipoPlato;
    }

    public void setTipoPlato(int TipoPlato) {
        this.TipoPlato = TipoPlato;
    }

    @Override
    public String toString() {
        return CodigoPlato + " | " + NombrePlato + " | Q. " + PrecioPlato;
    }
}


