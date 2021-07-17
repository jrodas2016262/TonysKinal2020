package org.jrodas.controller;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.security.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
import javafx.scene.layout.GridPane;
import javax.swing.JOptionPane;
import org.jrodas.bean.*;
import org.jrodas.db.Conexion;
import org.jrodas.report.GenerarReporte;
import org.jrodas.system.MainApp;

public class ServiciosController implements Initializable{
    private MainApp escenarioPrincipal;
    private ObservableList<Servicios> listaServicios;
    private ObservableList getEmpresa;
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Empresas> listaEmpresas;
    private DatePicker fecha;
    @FXML private TextField txtCodigoServicios;
    @FXML private TextField txtDescripcionServicios;
    @FXML private TextField txtHoraServicios;
    @FXML private TextField txtDireccionServicios;
    @FXML private TextField txtTelefonoServicios;
    @FXML private GridPane grpFechaServicios;
    @FXML private ComboBox cmbEmpresaServicios;
    @FXML private TableView tblCodigoServicios;
    @FXML private TableColumn colCodigoServicios;
    @FXML private TableColumn colFechaServicios;
    @FXML private TableColumn colDescripcionServicios;
    @FXML private TableColumn colHoraServicios;
    @FXML private TableColumn colDireccionServicios;
    @FXML private TableColumn colTelefonoServicios;
    @FXML private TableColumn colCodigoEmpresaServicios;
    @FXML private Button btnNuevo;            
    @FXML private Button btnEliminar;   
    @FXML private Button btnModificar;
    @FXML private Button btnReporte;  
            
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        grpFechaServicios.add(fecha, 1, 1);
        fecha.getStylesheets().add("/org/jrodas/resource/DatePicker.css");
        cmbEmpresaServicios.setItems(getEmpresa());
    }
    public void cargarDatos(){
        desactivarControles();
        tblCodigoServicios.setItems(getServicios());
        colCodigoServicios.setCellValueFactory(new PropertyValueFactory<Servicios,Integer>("CodigoServicios"));
        colFechaServicios.setCellValueFactory(new PropertyValueFactory<Servicios,Date>("FechaServicios"));
        colDescripcionServicios.setCellValueFactory(new PropertyValueFactory<Servicios,String>("DescripcionServicios"));
        colHoraServicios.setCellValueFactory(new PropertyValueFactory<Servicios,String>("HoraServicios"));
        colDireccionServicios.setCellValueFactory(new PropertyValueFactory<Servicios,String>("DireccionServicios"));
        colTelefonoServicios.setCellValueFactory(new PropertyValueFactory<Servicios,Integer>("Telefono"));
        colCodigoEmpresaServicios.setCellValueFactory(new PropertyValueFactory<Servicios,Integer>("CodigoEmpresa"));
    }
    public void seleccionarElemento(){
        if(tblCodigoServicios.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Seleccione antes un registro");
        }else{
        activarControles();
        txtCodigoServicios.setText(String.valueOf(((Servicios)tblCodigoServicios.getSelectionModel().getSelectedItem()).getCodigoServicios()));
        fecha.selectedDateProperty().set(((Servicios)tblCodigoServicios.getSelectionModel().getSelectedItem()).getFechaServicios());
        txtDescripcionServicios.setText(String.valueOf(((Servicios)tblCodigoServicios.getSelectionModel().getSelectedItem()).getDescripcionServicios()));
        txtHoraServicios.setText(String.valueOf(((Servicios)tblCodigoServicios.getSelectionModel().getSelectedItem()).getHoraServicios()));
        txtDireccionServicios.setText(String.valueOf(((Servicios)tblCodigoServicios.getSelectionModel().getSelectedItem()).getDireccionServicios()));        
        txtTelefonoServicios.setText(String.valueOf(((Servicios)tblCodigoServicios.getSelectionModel().getSelectedItem()).getTelefono()));
        cmbEmpresaServicios.getSelectionModel().select(buscarEmpresa(((Servicios)tblCodigoServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
        }
    }
    public void nuevo(){
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEliminar.setDisable(false);
                btnModificar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            case GUARDAR:
                guardar();
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEliminar.setDisable(false);
                btnModificar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
                
        }
    }
    public void guardar(){
        Servicios registro = new Servicios();
        registro.setCodigoServicios(Integer.parseInt(txtCodigoServicios.getText()));
        registro.setFechaServicios(fecha.getSelectedDate());
        registro.setDescripcionServicios(txtDescripcionServicios.getText());
        registro.setHoraServicios(txtHoraServicios.getText());
        registro.setDireccionServicios(txtDireccionServicios.getText());
        registro.setTelefono(Integer.parseInt(txtTelefonoServicios.getText()));
        registro.setCodigoEmpresa(((Empresas)cmbEmpresaServicios.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios(?,?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoServicios());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicios().getTime()));
            procedimiento.setString(3, registro.getDescripcionServicios());
            procedimiento.setString(4, registro.getHoraServicios());
            procedimiento.setString(5, registro.getDireccionServicios());
            procedimiento.setInt(6, registro.getTelefono());
            procedimiento.setInt(7, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaServicios.add(registro);
        }catch(Exception e){
            e.printStackTrace();
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
                if (tblCodigoServicios.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Realmente quiere eliminar el registro?","Eliminar Registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);              
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios(?)}");
                            procedimiento.setInt(1, ((Servicios)tblCodigoServicios.getSelectionModel().getSelectedItem()).getCodigoServicios());
                            procedimiento.execute();
                            listaServicios.remove(tblCodigoServicios.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblCodigoServicios.getSelectionModel().clearSelection();
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
        txtCodigoServicios.setDisable(true);
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblCodigoServicios.getSelectionModel().getSelectedItem() !=null){
                    btnModificar.setText("Cambiar");
                    btnReporte.setText("Cancelar");
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
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
            break;
        }
    }
    
    public void reporte(){
    switch(tipoDeOperacion){
            case NINGUNO:
                imprimirReporte();
            break;
            case ACTUALIZAR:
                cmbEmpresaServicios.setDisable(false);
                txtCodigoServicios.setDisable(false);
                btnModificar.setText("Modificar");
                btnReporte.setText("Cancelar");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void imprimirReporte(){
        if(cmbEmpresaServicios.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "Para generar un REPORTE antes seleccione un valor");
        }else{
           Map parametros = new HashMap();
           Servicios registro = (Servicios)tblCodigoServicios.getSelectionModel().getSelectedItem();
           int codServicio = registro.getCodigoServicios();
           parametros.put("codServicio", codServicio);
           GenerarReporte.mostrarReporte("ReportePlatos.jasper", "Reporte Servicios", parametros);
        }
    }
    
    public void actualizar(){       
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ModificarServicios(?,?,?,?,?,?)}");
            Servicios registro = (Servicios)tblCodigoServicios.getSelectionModel().getSelectedItem();
            registro.setCodigoServicios(Integer.parseInt(txtCodigoServicios.getText()));
            registro.setFechaServicios(fecha.getSelectedDate());
            registro.setDescripcionServicios(txtDescripcionServicios.getText());
            registro.setHoraServicios(txtHoraServicios.getText());
            registro.setDireccionServicios(txtDireccionServicios.getText());
            registro.setTelefono(Integer.parseInt(txtTelefonoServicios.getText()));            
            procedimiento.setInt(1, registro.getCodigoServicios());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaServicios().getTime()));
            procedimiento.setString(3, registro.getDescripcionServicios());
            procedimiento.setString(4, registro.getHoraServicios());
            procedimiento.setString(5, registro.getDireccionServicios());
            procedimiento.setInt(6, registro.getTelefono());
            procedimiento.execute();
            desactivarControles();
            limpiarControles();
            cmbEmpresaServicios.setDisable(false);
            txtCodigoServicios.setDisable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public ObservableList<Servicios> getServicios(){
        ArrayList<Servicios> lista = new ArrayList<Servicios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios(resultado.getInt("CodigoServicios"),
                        resultado.getDate("FechaServicios"),
                        resultado.getString("DescripcionServicios"),
                        resultado.getString("HoraServicios"),
                        resultado.getString("DireccionServicios"),
                        resultado.getInt("Telefono"),
                        resultado.getInt("F_CodigoEmpresa")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicios = FXCollections.observableArrayList(lista);
    }
    public ObservableList<Empresas> getEmpresa(){
        ArrayList<Empresas> lista = new ArrayList<Empresas>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpresas}");
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
        ObservableList<Empresas> listaEmpresa;
        return listaEmpresa = FXCollections.observableArrayList(lista);
    }
    
    public Empresas buscarEmpresa(int codigoEmpresa){
        Empresas resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpresas(?)}");
            procedimiento.setInt(1, codigoEmpresa);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empresas(registro.getInt("CodigoEmpresa"),
                        registro.getString("NombreEmpresa"),
                        registro.getString("DireccionEmpresa"),
                        registro.getString("Telefono"));
                }
            }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void desactivarControles(){
        txtCodigoServicios.setEditable(true);
        txtDescripcionServicios.setEditable(false);
        txtHoraServicios.setEditable(false);
        txtDireccionServicios.setEditable(false);
        txtTelefonoServicios.setEditable(false);
        grpFechaServicios.setDisable(true);
        cmbEmpresaServicios.setDisable(true);
        cmbEmpresaServicios.setEditable(false);
    }
    public void activarControles(){
        txtCodigoServicios.setEditable(true);
        txtDescripcionServicios.setEditable(true);
        txtHoraServicios.setEditable(true);
        txtDireccionServicios.setEditable(true);
        txtTelefonoServicios.setEditable(true);
        grpFechaServicios.setDisable(false);
        cmbEmpresaServicios.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoServicios.setText("");
        txtDescripcionServicios.setText("");
        txtHoraServicios.setText("");
        txtDireccionServicios.setText("");
        txtTelefonoServicios.setText("");
        fecha.selectedDateProperty().set(null);
        cmbEmpresaServicios.getSelectionModel().clearSelection();
        cmbEmpresaServicios.getSelectionModel().select(null);
    }
    
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }    
    public void RegresoEmpresa(){
        escenarioPrincipal.ventanaEmpresas();
    }
    public void RegresoPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
