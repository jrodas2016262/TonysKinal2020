package org.jrodas.bean;


public class Empleados {
    private int codigoEmpleados;
    private String apellidoEmpleados;
    private String nombreEmpleados;
    private String direccionEmpleados;
    private int telefonoEmpleados;
    private int tipoEmpleados;
    
public Empleados(){
}

    public Empleados(int codigoEmpleados, String apellidoEmpleados, String nombreEmpleados, String direccionEmpleados, int telefonoEmpleados, int tipoEmpleados) {
        this.codigoEmpleados = codigoEmpleados;
        this.apellidoEmpleados = apellidoEmpleados;
        this.nombreEmpleados = nombreEmpleados;
        this.direccionEmpleados = direccionEmpleados;
        this.telefonoEmpleados = telefonoEmpleados;
        this.tipoEmpleados = tipoEmpleados;
    }

    public int getCodigoEmpleados() {
        return codigoEmpleados;
    }

    public void setCodigoEmpleados(int codigoEmpleados) {
        this.codigoEmpleados = codigoEmpleados;
    }

    public String getApellidoEmpleados() {
        return apellidoEmpleados;
    }

    public void setApellidoEmpleados(String apellidoEmpleados) {
        this.apellidoEmpleados = apellidoEmpleados;
    }

    public String getNombreEmpleados() {
        return nombreEmpleados;
    }

    public void setNombreEmpleados(String nombreEmpleados) {
        this.nombreEmpleados = nombreEmpleados;
    }

    public String getDireccionEmpleados() {
        return direccionEmpleados;
    }

    public void setDireccionEmpleados(String direccionEmpleados) {
        this.direccionEmpleados = direccionEmpleados;
    }

    public int getTelefonoEmpleados() {
        return telefonoEmpleados;
    }

    public void setTelefonoEmpleados(int telefonoEmpleados) {
        this.telefonoEmpleados = telefonoEmpleados;
    }

    public int getTipoEmpleados() {
        return tipoEmpleados;
    }

    public void setTipoEmpleados(int tipoEmpleados) {
        this.tipoEmpleados = tipoEmpleados;
    }

    @Override
    public String toString() {
        return codigoEmpleados + " | " + apellidoEmpleados + ", " + nombreEmpleados;
    }
}
