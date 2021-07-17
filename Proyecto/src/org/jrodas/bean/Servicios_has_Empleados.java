package org.jrodas.bean;
import java.util.Date;
import org.jrodas.bean.Servicios;

public class Servicios_has_Empleados {
    private int codigoSHE;
    private int codigoServicios;
    private int codigoEmpleados;
    private Date fechaEvento;
    private String horaEvento;
    private String lugarEvento;
    
public Servicios_has_Empleados(){
}

    public Servicios_has_Empleados(int codigoSHE, int codigoServicios, int codigoEmpleados, Date fechaEvento, String horaEvento, String lugarEvento) {
        this.codigoSHE = codigoSHE;
        this.codigoServicios = codigoServicios;
        this.codigoEmpleados = codigoEmpleados;
        this.fechaEvento = fechaEvento;
        this.horaEvento = horaEvento;
        this.lugarEvento = lugarEvento;
    }

    public int getCodigoSHE() {
        return codigoSHE;
    }

    public void setCodigoSHE(int codigoSHE) {
        this.codigoSHE = codigoSHE;
    }

    public int getCodigoServicios() {
        return codigoServicios;
    }

    public void setCodigoServicios(int codigoServicios) {
        this.codigoServicios = codigoServicios;
    }

    public int getCodigoEmpleados() {
        return codigoEmpleados;
    }

    public void setCodigoEmpleados(int codigoEmpleados) {
        this.codigoEmpleados = codigoEmpleados;
    }

    public Date getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Date fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getHoraEvento() {
        return horaEvento;
    }

    public void setHoraEvento(String horaEvento) {
        this.horaEvento = horaEvento;
    }

    public String getLugarEvento() {
        return lugarEvento;
    }

    public void setLugarEvento(String lugarEvento) {
        this.lugarEvento = lugarEvento;
    }

    
}
