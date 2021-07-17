package org.jrodas.system;
import java.io.InputStream;
import java.security.Principal;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.jrodas.controller.*;


public class MainApp extends Application {  
    private final String PAQUETE_VISTA = "/org/jrodas/view/";
    private Stage escenarioPrincipal;
    private Scene escena;
    @Override
    public void start(Stage escenarioPrincipal) {
        this.escenarioPrincipal=escenarioPrincipal;
        this.escenarioPrincipal.setTitle("Tony's Kinal 2020");
        menuPrincipal();
        escenarioPrincipal.show();        
    }
    public void menuPrincipal(){
        try {
            MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("VistaPrincipalView.fxml",617,353);
            menuPrincipal.setEscenarioPrincipal(this); 
        } catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    public void acercaDe(){
        try{
            AcercaDeController acercade = (AcercaDeController) cambiarEscena("AcercaDeView.fxml",400,200);    
            acercade.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();    
        }
    }
    
    public void ventanaEmpleados(){
        try {
            EmpleadosController empleadosController = (EmpleadosController)cambiarEscena("VistaEmpleadosView.fxml",800,457);
            empleadosController.setEscenarioPrincipal(this); 
        } catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    public void ventanaEmpresas() {
        try {
            EmpresaController empresasController = (EmpresaController)cambiarEscena("VistaEmpresasView.fxml",697,400);
            empresasController.setEscenarioPrincipal(this); 
        } catch(Exception e){
            e.printStackTrace();
        } 
    }
    
    public void ventanaPlatos(){
        try{
            PlatosController platosController = (PlatosController)cambiarEscena("VistaPlatosView.fxml",696,400);
            platosController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void ventanaPresupuesto(){
        try{
            PresupuestoController presupuestoController = (PresupuestoController)cambiarEscena("VistaPresupuestoView.fxml",697,400);
            presupuestoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void ventanaProductos(){
        try{
            ProductosController productosController = (ProductosController)cambiarEscena("VistaProductosView.fxml",657,375);
            productosController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    
    public void ventanaServicios(){
        try{
            ServiciosController serviciosController = (ServiciosController)cambiarEscena("VistaServiciosView.fxml",871,497);
            serviciosController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoEmpleado(){
        try{
            TipoEmpleadoController tipoEmpleadoController = (TipoEmpleadoController)cambiarEscena("VistaTipoEmpleadoView.fxml",657,375);
            tipoEmpleadoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void ventanaTipoPlato(){
        try{
            TipoPlatoController tipoPlatoController = (TipoPlatoController)cambiarEscena("VistaTipoPlatoView.fxml",657,375);
            tipoPlatoController.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void Servicios_has_Platos(){
        try{
            Servicios_has_PlatosController serviciosHasPlatos = (Servicios_has_PlatosController)cambiarEscena("VistaServiciosHasPlatosView.fxml",630,369);
            serviciosHasPlatos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void Servicios_has_Empleados(){
        try{
            Servicios_has_EmpleadosController serviciosHasEmpleados = (Servicios_has_EmpleadosController)cambiarEscena("VistaServiciosHasEmpleadosView.fxml",775,449);
            serviciosHasEmpleados.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void Productos_has_Platos(){
        try{
            Productos_has_PlatosController productosHasPlatos = (Productos_has_PlatosController)cambiarEscena("VistaProductosHasPlatosView.fxml",630,369);
            productosHasPlatos.setEscenarioPrincipal(this);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{
        Initializable resultado = null;
        FXMLLoader cargadorFXML=new FXMLLoader();
        InputStream archivo=MainApp.class.getResourceAsStream(PAQUETE_VISTA+fxml);
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory());
        cargadorFXML.setLocation(MainApp.class.getResource(PAQUETE_VISTA+fxml));
        escena=new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto);
        escenarioPrincipal.setScene(escena);
        escenarioPrincipal.sizeToScene();
        resultado = (Initializable) cargadorFXML.getController();
        return resultado;
    }



}
