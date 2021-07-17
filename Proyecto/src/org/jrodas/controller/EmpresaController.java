package org.jrodas.controller;
import java.net.URL;
import java.security.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.jrodas.bean.*;
import org.jrodas.db.Conexion;
import org.jrodas.report.GenerarReporte;
import org.jrodas.system.*;

public class EmpresaController implements Initializable {
    private MainApp escenarioPrincipal;
    private enum Operaciones{NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO}
    private Operaciones tipoDeOperacion = Operaciones.NINGUNO; 
    private ObservableList<Empresas>listaEmpresas;
    @FXML private TextField txtCodigoEmpresas;
    @FXML private TextField txtNombreEmpresas;
    @FXML private TextField txtDireccionEmpresas;
    @FXML private TextField txtTelefonoEmpresas;
    @FXML private GridPane grpDatos;
    @FXML private TableView tblCodigoEmpresas;
    @FXML private TableColumn colCodigoEmpresas;
    @FXML private TableColumn colNombreEmpresas;
    @FXML private TableColumn colDireccionEmpresas;
    @FXML private TableColumn colTelefonoEmpresas;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnModificar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {        
        cargarDatos();
    }
    public void cargarDatos(){
        desactivarControles();
        tblCodigoEmpresas.setItems(getEmpresa());
        colCodigoEmpresas.setCellValueFactory(new PropertyValueFactory<Empresas, Integer>("CodigoEmpresa"));
        colNombreEmpresas.setCellValueFactory(new PropertyValueFactory<Empresas, String>("NombreEmpresa"));
        colDireccionEmpresas.setCellValueFactory(new PropertyValueFactory<Empresas, String>("DireccionEmpresa"));
        colTelefonoEmpresas.setCellValueFactory(new PropertyValueFactory<Empresas, Integer>("Telefono"));
    }
    public void seleccionarElementos(){
        if(tblCodigoEmpresas.getSelectionModel().getSelectedItem() == null){
           JOptionPane.showMessageDialog(null,"Seleccione antes un registro");
        }else{
            
        activarControles();
        txtCodigoEmpresas.setText(String.valueOf(((Empresas)tblCodigoEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        txtNombreEmpresas.setText(String.valueOf(((Empresas)tblCodigoEmpresas.getSelectionModel().getSelectedItem()).getNombreEmpresa()));
        txtDireccionEmpresas.setText(String.valueOf(((Empresas)tblCodigoEmpresas.getSelectionModel().getSelectedItem()).getDireccionEmpresa()));
        txtTelefonoEmpresas.setText(String.valueOf(((Empresas)tblCodigoEmpresas.getSelectionModel().getSelectedItem()).getTelefono()));
        }
    }    
    public void desactivarControles(){
        txtCodigoEmpresas.setDisable(true);
        txtNombreEmpresas.setDisable(true);
        txtDireccionEmpresas.setDisable(true);
        txtTelefonoEmpresas.setDisable(true);
        grpDatos.setDisable(true);
    }
    public void activarControles(){
        txtCodigoEmpresas.setDisable(false);
        grpDatos.setDisable(false);
        txtNombreEmpresas.setDisable(false);
        txtDireccionEmpresas.setDisable(false);
        txtTelefonoEmpresas.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoEmpresas.setText("");
        txtNombreEmpresas.setText("");
        txtDireccionEmpresas.setText("");
        txtTelefonoEmpresas.setText("");
    }
    public void nuevo(){
        txtCodigoEmpresas.setEditable(true);
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnModificar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = Operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = Operaciones.NINGUNO;
                cargarDatos();
            break;
        }
    }
    public void eliminar(){
        switch(tipoDeOperacion){
            case GUARDAR:
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = Operaciones.NINGUNO;
                limpiarControles();
            break;
            default:
                if (tblCodigoEmpresas.getSelectionModel().getSelectedItem() !=null){
                    int respuesto = JOptionPane.showConfirmDialog(null, "¿Realmente quiere eliminar el registro?","Eliminar Registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesto == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarEmpresas(?)}");
                            procedimiento.setInt(1, ((Empresas)tblCodigoEmpresas.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
                            procedimiento.execute();
                            listaEmpresas.remove(tblCodigoEmpresas.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblCodigoEmpresas.getSelectionModel().clearSelection();
                            desactivarControles();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Seleccione antes un registro");
                }
        }
    }
    
    public void guardar(){
        Empresas registro = new Empresas();
        registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresas.getText()));
        registro.setNombreEmpresa(txtNombreEmpresas.getText());
        registro.setDireccionEmpresa(txtDireccionEmpresas.getText());
        registro.setTelefono(txtTelefonoEmpresas.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpresas(?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoEmpresa());
            procedimiento.setString(2, registro.getNombreEmpresa());
            procedimiento.setString(3, registro.getDireccionEmpresa());
            procedimiento.setString(4, registro.getTelefono());
            procedimiento.execute();
            listaEmpresas.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void modificar(){
        txtCodigoEmpresas.setEditable(false);
        switch(tipoDeOperacion){
            case NINGUNO:               
                if(tblCodigoEmpresas.getSelectionModel().getSelectedItem() !=null){
                    btnModificar.setText("Cambiar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = Operaciones.ACTUALIZAR;
                }else{
                    JOptionPane.showMessageDialog(null, "Seleccione antes un registro");
                }
                break;
            case ACTUALIZAR:
                actualizar();
                limpiarControles();
                desactivarControles();
                btnModificar.setText("Modificar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = Operaciones.NINGUNO;
                    cargarDatos();
            break;           
        }
    }
    public void reporte(){
    switch(tipoDeOperacion){
            case ACTUALIZAR:
                txtCodigoEmpresas.setEditable(true);
                //desactivarControles();
                btnModificar.setText("Modificar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = Operaciones.NINGUNO;
                //limpiarControles();
                break;
            case NINGUNO:
                imprimirReporte();
                break;
        }
    }
    
    public void imprimirReporte(){
        Map parametros = new HashMap();
        parametros.put("CodigoEmpresa", null);
        GenerarReporte.mostrarReporte("ReporteEmpresas.jasper", "Reporte De Empresa", null);
    }
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ModificarEmpresas(?,?,?,?)}");
            Empresas registro = (Empresas)tblCodigoEmpresas.getSelectionModel().getSelectedItem();
            registro.setCodigoEmpresa(Integer.parseInt(txtCodigoEmpresas.getText()));
            registro.setNombreEmpresa(txtNombreEmpresas.getText());
            registro.setDireccionEmpresa(txtDireccionEmpresas.getText());
            registro.setTelefono(txtTelefonoEmpresas.getText());
            procedimiento.setInt(1, registro.getCodigoEmpresa());
            procedimiento.setString(2, registro.getNombreEmpresa());
            procedimiento.setString(3, registro.getDireccionEmpresa());
            procedimiento.setString(4, registro.getTelefono());
            procedimiento.execute();
            desactivarControles();
            limpiarControles();
            txtCodigoEmpresas.setEditable(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<Empresas> getEmpresa(){
        ArrayList<Empresas> lista = new ArrayList<Empresas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas()}");
            ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                lista.add(new Empresas(resultado.getInt("codigoEmpresa"), 
                        resultado.getString("nombreEmpresa"), 
                        resultado.getString("DireccionEmpresa"), 
                        resultado.getString("telefono")));
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpresas = FXCollections.observableArrayList(lista);
    }

    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }       
    public void ventanaServicios(){
        escenarioPrincipal.ventanaServicios();
    }
    public void ventanaPresupuesto(){
        escenarioPrincipal.ventanaPresupuesto();
    }
    public void RegresoPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    public void Reporte(){
        imprimirReporte();
    }    
}
