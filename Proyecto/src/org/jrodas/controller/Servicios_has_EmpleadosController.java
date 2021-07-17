package org.jrodas.controller;

import eu.schudt.javafx.controls.calendar.DatePicker;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import org.jrodas.system.MainApp;


public class Servicios_has_EmpleadosController implements Initializable{
    private MainApp escenarioPrincipal;
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Servicios> listaServicios;
    ObservableList<Empleados> listaEmpleados;
    ObservableList<Servicios_has_Empleados> listaServicios_has_Empleados;
    private DatePicker fecha;
    @FXML private ComboBox cmbCodigoServicios;
    @FXML private ComboBox cmbCodigoEmpleado;
    @FXML private GridPane grpFechaEvento;
    @FXML private TextField txtSHE;
    @FXML private TextField txtHoraEvento;
    @FXML private TextField txtLugarEvento;
    @FXML private TableView tblServiciosHasEmpleados;
    @FXML private TableColumn colServicios;
    @FXML private TableColumn colEmpleado;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colHora;
    @FXML private TableColumn colLugarEvento;
    @FXML private TableColumn colCodigoSHE;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnModificar;
    @FXML private Button btnCancelar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyy-MM-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        grpFechaEvento.add(fecha, 1, 3);
        fecha.getStylesheets().add("/org/jrodas/resource/DatePicker.css");
        cmbCodigoServicios.setItems(getServicios());
        cmbCodigoEmpleado.setItems(getEmpleados());
        cargarDatos();
    }
    public void cargarDatos(){
        desactivarControles();
        tblServiciosHasEmpleados.setItems(getServicios_has_Empleados());
        colCodigoSHE.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Integer>("CodigoSHE"));
        colServicios.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Integer>("CodigoServicios"));
        colEmpleado.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Integer>("CodigoEmpleados"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,Date>("fechaEvento"));
        colHora.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,String>("horaEvento"));
        colLugarEvento.setCellValueFactory(new PropertyValueFactory<Servicios_has_Empleados,String>("lugarEvento"));
    }
    public void seleccionarElemento(){
        if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Seleccione antes un registro");
        }else{
        activarControles();
        txtSHE.setText(String.valueOf(((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoSHE()));
        txtHoraEvento.setText(String.valueOf(((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getHoraEvento()));
        fecha.selectedDateProperty().set(((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getFechaEvento());
        txtLugarEvento.setText(String.valueOf(((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getLugarEvento()));
        cmbCodigoServicios.getSelectionModel().select(buscarServicio(((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoServicios()));
        cmbCodigoEmpleado.getSelectionModel().select(buscarEmpleados(((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoEmpleados()));
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
                btnCancelar.setDisable(true);
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
                btnCancelar.setDisable(false);
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
                btnCancelar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                limpiarControles();
                break;
            default:
                if (tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Realmente quiere eliminar el registro?","Eliminar Registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);              
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarServicios_has_empledos(?)}");
                            procedimiento.setInt(1, ((Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem()).getCodigoSHE());
                            procedimiento.execute();
                            listaServicios_has_Empleados.remove(tblServiciosHasEmpleados.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblServiciosHasEmpleados.getSelectionModel().clearSelection();
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
        txtSHE.setDisable(true);
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblServiciosHasEmpleados.getSelectionModel().getSelectedItem() !=null){
                    btnModificar.setText("Cambiar");
                    btnCancelar.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    btnCancelar.setDisable(false);
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
                btnCancelar.setText("Cancelar");
                btnCancelar.setDisable(true);
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
                txtSHE.setDisable(false);
                btnModificar.setText("Modificar");
                btnCancelar.setText("Cancelar");
                btnCancelar.setDisable(true);
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void actualizar(){       
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ModificarServicios_has_empledos(?,?,?,?)}");
            Servicios_has_Empleados registro = (Servicios_has_Empleados)tblServiciosHasEmpleados.getSelectionModel().getSelectedItem();
            registro.setCodigoSHE(Integer.parseInt(txtSHE.getText()));
            registro.setFechaEvento(fecha.getSelectedDate());
            registro.setHoraEvento(txtHoraEvento.getText());
            registro.setLugarEvento(txtLugarEvento.getText());
            procedimiento.setInt(1, registro.getCodigoSHE());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(3, registro.getHoraEvento());
            procedimiento.setString(4, registro.getLugarEvento());
            procedimiento.execute();
            desactivarControles();
            limpiarControles();
                txtSHE.setDisable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void guardar(){
        Servicios_has_Empleados registro = new Servicios_has_Empleados();
        registro.setCodigoSHE(Integer.parseInt(txtSHE.getText()));
        registro.setCodigoServicios(((Servicios)cmbCodigoServicios.getSelectionModel().getSelectedItem()).getCodigoServicios());
        registro.setCodigoEmpleados(((Empleados)cmbCodigoEmpleado.getSelectionModel().getSelectedItem()).getCodigoEmpleados());        
        registro.setFechaEvento(fecha.getSelectedDate());
        registro.setHoraEvento(txtHoraEvento.getText());
        registro.setLugarEvento(txtLugarEvento.getText());          
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarServicios_has_empledos(?,?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoSHE());
            procedimiento.setInt(2, registro.getCodigoServicios());
            procedimiento.setInt(3, registro.getCodigoEmpleados());
            procedimiento.setDate(4, new java.sql.Date(registro.getFechaEvento().getTime()));
            procedimiento.setString(5, registro.getHoraEvento());
            procedimiento.setString(6, registro.getLugarEvento());
            procedimiento.execute();
            listaServicios_has_Empleados.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void limpiarControles(){
        cmbCodigoServicios.getSelectionModel().select(null);
        cmbCodigoServicios.getSelectionModel().clearSelection();
        cmbCodigoEmpleado.getSelectionModel().select(null);
        cmbCodigoEmpleado.getSelectionModel().clearSelection();
        fecha.selectedDateProperty().set(null);
        txtHoraEvento.setText("");
        txtLugarEvento.setText("");
        txtSHE.setText("");
    }
    public void desactivarControles(){
        cmbCodigoServicios.setDisable(true);
        cmbCodigoEmpleado.setDisable(true);
        cmbCodigoServicios.setEditable(false);
        cmbCodigoEmpleado.setEditable(false);
        grpFechaEvento.setDisable(true);
        txtHoraEvento.setDisable(true);
        txtLugarEvento.setDisable(true);
        txtSHE.setDisable(true);
        btnCancelar.setDisable(true);
    }
    public void activarControles(){
        txtSHE.setDisable(false);
        cmbCodigoServicios.setDisable(false);
        cmbCodigoEmpleado.setDisable(false);
        grpFechaEvento.setDisable(false);
        txtHoraEvento.setDisable(false);
        txtLugarEvento.setDisable(false);
    }
    
    public ObservableList<Servicios_has_Empleados> getServicios_has_Empleados(){
        ArrayList<Servicios_has_Empleados> listaSHE = new ArrayList<Servicios_has_Empleados>();
        try{
            PreparedStatement prod = Conexion.getInstance().getConexion().prepareCall("call sp_ListarServicios_has_empleados");
            ResultSet resultado = prod.executeQuery();
            while(resultado.next()){
                listaSHE.add(new Servicios_has_Empleados(resultado.getInt("codigoSHE"),
                            resultado.getInt("servicios_CodigoServicios"),
                            resultado.getInt("empleados_CodigoEmpleados"),
                            resultado.getDate("fechaEvento"),
                            resultado.getString("horaEvento"),
                            resultado.getString("lugarEvento")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicios_has_Empleados = FXCollections.observableArrayList(listaSHE);
    }
    
    public ObservableList<Servicios> getServicios(){
        ArrayList<Servicios> listaS = new ArrayList<Servicios>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                listaS.add(new Servicios(resultado.getInt("CodigoServicios"),
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
        return listaServicios = FXCollections.observableArrayList(listaS);
    }
    public ObservableList<Empleados> getEmpleados(){
        ArrayList<Empleados> listaE = new ArrayList<Empleados>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarEmpleados}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
            listaE.add(new Empleados(resultado.getInt("CodigoEmpleados"),
                    resultado.getString("ApellidoEmpleados"),
                    resultado.getString("NombreEmpleados"),
                    resultado.getString("DireccionEmpleados"),
                    resultado.getInt("TelefonoEmpleados"),
                    resultado.getInt("F_TipoEmpleados")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaEmpleados = FXCollections.observableArrayList(listaE);
    }
    public Servicios buscarServicio(int codServicio){
        Servicios resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarServicios(?)}");
            procedimiento.setInt(1, codServicio);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Servicios(registro.getInt("CodigoServicios"),
                        registro.getDate("FechaServicios"),
                        registro.getString("DescripcionServicios"),
                        registro.getString("HoraServicios"),
                        registro.getString("DireccionServicios"),
                        registro.getInt("Telefono"),
                        registro.getInt("F_CodigoEmpresa"));
                }
            }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }  
    public Empleados buscarEmpleados(int codEmpleado){
        Empleados resultado = null;
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarEmpleados(?)}");
            procedimiento.setInt(1, codEmpleado);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Empleados(registro.getInt("CodigoEmpleados"),
                        registro.getString("ApellidoEmpleados"),
                        registro.getString("NombreEmpleados"),
                        registro.getString("DireccionEmpleados"),
                        registro.getInt("TelefonoEmpleados"),
                        registro.getInt("F_TipoEmpleados"));
                }
            }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }   
    
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void RegresoPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
    
}
