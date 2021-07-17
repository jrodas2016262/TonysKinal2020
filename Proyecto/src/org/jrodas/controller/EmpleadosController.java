package org.jrodas.controller;
import java.net.URL;
import java.security.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.jrodas.bean.Empleados;
import org.jrodas.bean.TipoEmpleado;
import org.jrodas.db.Conexion;
import org.jrodas.system.MainApp;

public class EmpleadosController implements Initializable{
    private MainApp escenarioPrincipal;
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<Empleados> listaEmpleados;
    ObservableList<TipoEmpleado> listaTipoEmpleado;
    @FXML private TextField txtCodigoEmpleados;
    @FXML private TextField txtNombreEmpleados;
    @FXML private TextField txtApellidoEmpleados;
    @FXML private TextField txtDireccionEmpleados;
    @FXML private TextField txtTelefonoEmpleados;
    @FXML private ComboBox cmbTipoEmpleados;
    @FXML private TableView tblCodigoEmpleados;
    @FXML private TableColumn colCodigoEmpleados;
    @FXML private TableColumn colNombreEmpleados;
    @FXML private TableColumn colApellidoEmpleados;
    @FXML private TableColumn colDireccionEmpleados;
    @FXML private TableColumn colTelefonoEmpleados;
    @FXML private TableColumn colTipoEmpleados;
    @FXML private Button btnNuevo;            
    @FXML private Button btnEliminar;   
    @FXML private Button btnModificar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {        
        cargarDatos();
        cmbTipoEmpleados.setItems(getTipoEmpleado());
    }    
    public void cargarDatos(){
        desactivarControles();
        tblCodigoEmpleados.setItems(getEmpleados());
        colCodigoEmpleados.setCellValueFactory(new PropertyValueFactory<Empleados,Integer>("CodigoEmpleados"));
        colNombreEmpleados.setCellValueFactory(new PropertyValueFactory<Empleados,String>("NombreEmpleados"));
        colApellidoEmpleados.setCellValueFactory(new PropertyValueFactory<Empleados,String>("ApellidoEmpleados"));
        colDireccionEmpleados.setCellValueFactory(new PropertyValueFactory<Empleados,String>("DireccionEmpleados"));
        colTelefonoEmpleados.setCellValueFactory(new PropertyValueFactory<Empleados,Integer>("TelefonoEmpleados"));
        colTipoEmpleados.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,Integer>("CodigoTipoEmpleado"));
    }
    public void seleccionarElementos(){
        if(tblCodigoEmpleados.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Seleccione antes un registro");
        }else{
            activarControles();
            txtCodigoEmpleados.setText(String.valueOf(((Empleados)tblCodigoEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleados()));
            txtNombreEmpleados.setText(((Empleados)tblCodigoEmpleados.getSelectionModel().getSelectedItem()).getNombreEmpleados());
            txtApellidoEmpleados.setText(((Empleados)tblCodigoEmpleados.getSelectionModel().getSelectedItem()).getApellidoEmpleados());
            txtDireccionEmpleados.setText(((Empleados)tblCodigoEmpleados.getSelectionModel().getSelectedItem()).getDireccionEmpleados());
            txtTelefonoEmpleados.setText(String.valueOf(((Empleados)tblCodigoEmpleados.getSelectionModel().getSelectedItem()).getTelefonoEmpleados()));
            cmbTipoEmpleados.getSelectionModel().select(buscarTipoEmpleado(((Empleados)tblCodigoEmpleados.getSelectionModel().getSelectedItem()).getTipoEmpleados()));
        }
    }
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnModificar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
            break;
            case GUARDAR:
                guardar();
                limpiarControles();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
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
                tipoDeOperacion = operaciones.NINGUNO;
                limpiarControles();
            break;
            default:
                if (tblCodigoEmpleados.getSelectionModel().getSelectedItem() !=null){
                    int respuesto = JOptionPane.showConfirmDialog(null, "¿Realmente quiere eliminar el registro?","Eliminar Registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesto == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoEmpleado(?)}");
                            procedimiento.setInt(1, ((Empleados)tblCodigoEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleados());
                            procedimiento.execute();
                            listaEmpleados.remove(tblCodigoEmpleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblCodigoEmpleados.getSelectionModel().clearSelection();
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
    public void modificar(){
        txtCodigoEmpleados.setDisable(true);
        switch(tipoDeOperacion){
            case NINGUNO:               
                if(tblCodigoEmpleados.getSelectionModel().getSelectedItem() !=null){
                    btnModificar.setText("Cambiar");
                    btnReporte.setText("Cancelar");
                    btnReporte.setDisable(false);
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    tipoDeOperacion = operaciones.ACTUALIZAR;
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
                btnReporte.setDisable(true);
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
            break;           
        }
    }
    public void reporte(){
    switch(tipoDeOperacion){
            case ACTUALIZAR:
                txtCodigoEmpleados.setDisable(false);
                btnModificar.setText("Modificar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ModificarEmpleados(?,?,?,?,?)}");
            Empleados registro = (Empleados)tblCodigoEmpleados.getSelectionModel().getSelectedItem();
            registro.setCodigoEmpleados(Integer.parseInt(txtCodigoEmpleados.getText()));
            registro.setApellidoEmpleados(txtApellidoEmpleados.getText());
            registro.setNombreEmpleados(txtNombreEmpleados.getText());
            registro.setDireccionEmpleados(txtDireccionEmpleados.getText());
            registro.setTelefonoEmpleados(Integer.parseInt(txtTelefonoEmpleados.getText()));
            procedimiento.setInt(1, registro.getCodigoEmpleados());
            procedimiento.setString(2, registro.getApellidoEmpleados());
            procedimiento.setString(3, registro.getNombreEmpleados());
            procedimiento.setString(4, registro.getDireccionEmpleados());
            procedimiento.setInt(5, registro.getTelefonoEmpleados());
            procedimiento.execute();
            desactivarControles();
            limpiarControles();
            txtCodigoEmpleados.setDisable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
   
    public void guardar(){
        Empleados registro = new Empleados();
        registro.setCodigoEmpleados(Integer.parseInt(txtCodigoEmpleados.getText()));
        registro.setApellidoEmpleados(txtApellidoEmpleados.getText());
        registro.setNombreEmpleados(txtNombreEmpleados.getText());
        registro.setDireccionEmpleados(txtDireccionEmpleados.getText());
        registro.setTelefonoEmpleados(Integer.parseInt(txtTelefonoEmpleados.getText()));
        registro.setTipoEmpleados(((TipoEmpleado)cmbTipoEmpleados.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarEmpleados(?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoEmpleados());
            procedimiento.setString(2, registro.getApellidoEmpleados());
            procedimiento.setString(3, registro.getNombreEmpleados());
            procedimiento.setString(4, registro.getDireccionEmpleados());
            procedimiento.setInt(5, registro.getTelefonoEmpleados());
            procedimiento.setInt(6, registro.getTipoEmpleados());
            procedimiento.execute();
            listaEmpleados.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public ObservableList<Empleados> getEmpleados(){
        ArrayList<Empleados> lista = new ArrayList<Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            lista.add(new Empleados(resultado.getInt("CodigoEmpleados"),
                    resultado.getString("ApellidoEmpleados"),
                    resultado.getString("NombreEmpleados"),
                    resultado.getString("DireccionEmpleados"),
                    resultado.getInt("TelefonoEmpleados"),
                    resultado.getInt("F_TipoEmpleados")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleados = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> listaE = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleado}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                listaE.add(new TipoEmpleado(resultado.getInt("CodigoTipoEmpleado"),
                    resultado.getString("Descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(listaE);
    }   
    
    public TipoEmpleado buscarTipoEmpleado(int codTipoEmpleado){
        TipoEmpleado res = null;
        try{           
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoEmpleado(?)}");
            procedimiento.setInt(1, codTipoEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                res = new TipoEmpleado(registro.getInt("CodigoTipoEmpleado"),
                        registro.getString("Descripcion"));                           
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return res;
    }        
    public void desactivarControles(){
        txtCodigoEmpleados.setDisable(true);
        txtNombreEmpleados.setDisable(true);
        txtApellidoEmpleados.setDisable(true);
        txtDireccionEmpleados.setDisable(true);
        txtTelefonoEmpleados.setDisable(true);
        cmbTipoEmpleados.setEditable(false);
        cmbTipoEmpleados.setDisable(true);
        btnReporte.setDisable(true);
    }
    public void activarControles(){
        txtCodigoEmpleados.setDisable(false);
        txtNombreEmpleados.setDisable(false);
        txtApellidoEmpleados.setDisable(false);
        txtDireccionEmpleados.setDisable(false);
        txtTelefonoEmpleados.setDisable(false);
        cmbTipoEmpleados.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoEmpleados.setText("");
        txtNombreEmpleados.setText("");
        txtApellidoEmpleados.setText("");
        txtDireccionEmpleados.setText("");
        txtTelefonoEmpleados.setText("");
        cmbTipoEmpleados.getSelectionModel().clearSelection();
        cmbTipoEmpleados.getSelectionModel().select(null);
    }    
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }    
    public void RegresoTipoEmpleado(){
        escenarioPrincipal.ventanaTipoEmpleado();
    }
    public void RegresoPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
