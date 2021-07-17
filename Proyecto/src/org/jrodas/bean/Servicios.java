package org.jrodas.bean;
import java.util.Date;

public class Servicios {
    private int codigoServicios;
    private Date fechaServicios;
    private String descripcionServicios;
    private String horaServicios;
    private String direccionServicios;
    private int telefono;
    private int codigoEmpresa;

public Servicios(){
}

    public Servicios(int codigoServicios, Date fechaServicios, String descripcionServicios, String horaServicios, String direccionServicios, int telefono, int codigoEmpresa) {
        this.codigoServicios = codigoServicios;
        this.fechaServicios = fechaServicios;
        this.descripcionServicios = descripcionServicios;
        this.horaServicios = horaServicios;
        this.direccionServicios = direccionServicios;
        this.telefono = telefono;
        this.codigoEmpresa = codigoEmpresa;
    }

    public int getCodigoServicios() {
        return codigoServicios;
    }

    public void setCodigoServicios(int codigoServicios) {
        this.codigoServicios = codigoServicios;
    }

    public Date getFechaServicios() {
        return fechaServicios;
    }

    public void setFechaServicios(Date fechaServicios) {
        this.fechaServicios = fechaServicios;
    }

    public String getDescripcionServicios() {
        return descripcionServicios;
    }

    public void setDescripcionServicios(String descripcionServicios) {
        this.descripcionServicios = descripcionServicios;
    }

    public String getHoraServicios() {
        return horaServicios;
    }

    public void setHoraServicios(String horaServicios) {
        this.horaServicios = horaServicios;
    }

    public String getDireccionServicios() {
        return direccionServicios;
    }

    public void setDireccionServicios(String direccionServicios) {
        this.direccionServicios = direccionServicios;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public int getCodigoEmpresa() {
        return codigoEmpresa;
    }

    public void setCodigoEmpresa(int codigoEmpresa) {
        this.codigoEmpresa = codigoEmpresa;
    }

    @Override
    public String toString() {
        return codigoServicios + " | " + descripcionServicios;
    }
}
