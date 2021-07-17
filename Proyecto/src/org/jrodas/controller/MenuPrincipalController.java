package org.jrodas.controller;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javax.swing.JOptionPane;
import org.jrodas.system.MainApp;
import org.jrodas.db.Conexion;
import org.jrodas.controller.EmpresaController;

public class MenuPrincipalController implements Initializable {
    private MainApp escenarioPrincipal;

    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void acercaDe(){
        escenarioPrincipal.acercaDe();
    }
    public void ventanaEmpresas(){
        escenarioPrincipal.ventanaEmpresas();
    }
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
    public void ventanaPlatos(){
        escenarioPrincipal.ventanaPlatos();
    }
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    public void ventanaProductos(){
        escenarioPrincipal.ventanaProductos();
    }
    public void ventanaServicios(){
        escenarioPrincipal.ventanaServicios();
    }
    public void ventanaTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    public void ventanaTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
    public void Servicios_has_Platos(){
        escenarioPrincipal.Servicios_has_Platos();
    }
    public void Servicios_has_Empleados(){
        escenarioPrincipal.Servicios_has_Empleados();
    }
    public void Productos_has_Platos(){
        escenarioPrincipal.Productos_has_Platos();
    }
    public void reporteEmpresas(){
        EmpresaController empresa = new EmpresaController();
        empresa.Reporte();
    }
    public void reportePresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
        JOptionPane.showMessageDialog(null, "Para generar un REPORTE antes seleccione una Empresa");
    }
    public void reportePlatos(){
        escenarioPrincipal.ventanaServicios();
        JOptionPane.showMessageDialog(null, "Para generar un REPORTE antes seleccione un registro valido");
    }
    public void reporteP(){
        escenarioPrincipal.ventanaPlatos();
        JOptionPane.showMessageDialog(null, "Para generar un REPORTE antes seleccione un valor");
    }
    @Override
    public void initialize(URL url, ResourceBundle resourses) {

    } 
    
    
}
