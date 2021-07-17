package org.jrodas.bean;
public class Empresas {
    private int codigoEmpresa;
    private String nombreEmpresa;
    private String direccionEmpresa;
    private String telefono;

    public Empresas() {
    }

    public Empresas(int codigoEmpresa, String nombreEmpresa, String direccionEmpresa, String telefono) {
        this.codigoEmpresa = codigoEmpresa;
        this.nombreEmpresa = nombreEmpresa;
        this.direccionEmpresa = direccionEmpresa;
        this.telefono = telefono;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getDireccionEmpresa() {
        return direccionEmpresa;
    }

    public void setDireccionEmpresa(String direccionEmpresa) {
        this.direccionEmpresa = direccionEmpresa;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return getCodigoEmpresa() + " | " + getNombreEmpresa();
    }
}
