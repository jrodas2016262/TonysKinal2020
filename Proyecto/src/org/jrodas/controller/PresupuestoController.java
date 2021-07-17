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
import org.jrodas.bean.Empresas;
import org.jrodas.bean.Presupuesto;
import org.jrodas.db.Conexion;
import org.jrodas.report.GenerarReporte;
import org.jrodas.system.MainApp;

public class PresupuestoController implements Initializable{
    private MainApp escenarioPrincipal;
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private DatePicker fecha;
    ObservableList<Presupuesto> listaPresupuesto;
    @FXML private TextField txtCodigoPresupuesto;
    @FXML private TextField txtCantidadPresupuesto;
    @FXML private GridPane grpFechaPresupuesto;
    @FXML private ComboBox cmbEmpresaPresupuesto;
    @FXML private TableView tblCodigoPresupuesto;
    @FXML private TableColumn colCodigoPresupuesto;
    @FXML private TableColumn colFechaPresupuesto;
    @FXML private TableColumn colCantidadPresupuesto;
    @FXML private TableColumn colCodigoEmpresaPresupuesto;
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
        grpFechaPresupuesto.add(fecha, 1, 1);
        fecha.getStylesheets().add("/org/jrodas/resource/DatePicker.css");
        cmbEmpresaPresupuesto.setItems(getEmpresa());    
    }
    public void cargarDatos(){
        desactivarControles();
        tblCodigoPresupuesto.setItems(getPresupuesto());
        colCodigoPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("CodigoPresupuesto"));
        colFechaPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Date>("FechaSolicitud"));
        colCantidadPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("CantidadPresupuesto"));
        colCodigoEmpresaPresupuesto.setCellValueFactory(new PropertyValueFactory<Presupuesto,Integer>("codigoEmpresa"));
    }
    public void seleccionarElemento(){
        if(tblCodigoPresupuesto.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Seleccione antes un registro");
        }else{
        activarControles();
        txtCodigoPresupuesto.setText(String.valueOf(((Presupuesto)tblCodigoPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto()));
        fecha.selectedDateProperty().set(((Presupuesto)tblCodigoPresupuesto.getSelectionModel().getSelectedItem()).getFechaSolicitud());
        txtCantidadPresupuesto.setText(String.valueOf(((Presupuesto)tblCodigoPresupuesto.getSelectionModel().getSelectedItem()).getCantidadPresupuesto()));
        cmbEmpresaPresupuesto.getSelectionModel().select(buscarEmpresa(((Presupuesto)tblCodigoPresupuesto.getSelectionModel().getSelectedItem()).getCodigoEmpresa()));
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
                if (tblCodigoPresupuesto.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Realmente quiere eliminar el registro?","Eliminar Registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);              
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPresupuesto(?)}");
                            procedimiento.setInt(1, ((Presupuesto)tblCodigoPresupuesto.getSelectionModel().getSelectedItem()).getCodigoPresupuesto());
                            procedimiento.execute();
                            listaPresupuesto.remove(tblCodigoPresupuesto.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblCodigoPresupuesto.getSelectionModel().clearSelection();
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
        txtCodigoPresupuesto.setDisable(true);
        cmbEmpresaPresupuesto.setDisable(true);
        switch(tipoDeOperacion){
            case NINGUNO:               
                if(tblCodigoPresupuesto.getSelectionModel().getSelectedItem() !=null){
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
                btnReporte.setText("Reporte");
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
                cmbEmpresaPresupuesto.setDisable(false);
                txtCodigoPresupuesto.setDisable(false);
                btnModificar.setText("Modificar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;            
        }
    }
    public void imprimirReporte(){
        if(cmbEmpresaPresupuesto.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "Para generar un REPORTE antes seleccione un valor");
        }else{
        Map parametros = new HashMap();
        int conEm = Integer.valueOf(((Empresas)cmbEmpresaPresupuesto.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        parametros.put("conEm", conEm);
        GenerarReporte.mostrarReporte("ReportePresupuesto.jasper", "Reporte de Presupuesto", parametros);
        }
    }
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ModificarPresupuesto(?,?,?)}");
            Presupuesto registro = (Presupuesto)tblCodigoPresupuesto.getSelectionModel().getSelectedItem();
            registro.setCodigoPresupuesto(Integer.parseInt(txtCodigoPresupuesto.getText()));
            registro.setFechaSolicitud(fecha.getSelectedDate());
            registro.setCantidadPresupuesto(Integer.parseInt(txtCantidadPresupuesto.getText()));
            procedimiento.setInt(1, registro.getCodigoEmpresa());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setInt(3, registro.getCantidadPresupuesto());
            procedimiento.execute();
            desactivarControles();
            limpiarControles();
            cmbEmpresaPresupuesto.setDisable(false);
            txtCodigoPresupuesto.setDisable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void guardar(){
        Presupuesto registro = new Presupuesto();
        registro.setCodigoPresupuesto(Integer.parseInt(txtCodigoPresupuesto.getText()));
        registro.setFechaSolicitud(fecha.getSelectedDate());
        registro.setCantidadPresupuesto(Integer.parseInt(txtCantidadPresupuesto.getText()));
        registro.setCodigoEmpresa(((Empresas)cmbEmpresaPresupuesto.getSelectionModel().getSelectedItem()).getCodigoEmpresa());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPresupuesto(?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoPresupuesto());
            procedimiento.setDate(2, new java.sql.Date(registro.getFechaSolicitud().getTime()));
            procedimiento.setInt(3, registro.getCantidadPresupuesto());
            procedimiento.setInt(4, registro.getCodigoEmpresa());
            procedimiento.execute();
            listaPresupuesto.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
        
    public void desactivarControles(){
        txtCodigoPresupuesto.setEditable(true);
        txtCantidadPresupuesto.setEditable(false);
        grpFechaPresupuesto.setDisable(true);
        cmbEmpresaPresupuesto.setEditable(false);
    }
    public void activarControles(){
        txtCodigoPresupuesto.setEditable(true);
        txtCantidadPresupuesto.setEditable(true);
        grpFechaPresupuesto.setDisable(false);
        cmbEmpresaPresupuesto.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoPresupuesto.setText("");
        txtCantidadPresupuesto.setText("");
        fecha.selectedDateProperty().set(null);
        cmbEmpresaPresupuesto.getSelectionModel().clearSelection();
        cmbEmpresaPresupuesto.getSelectionModel().select(null);
    }
    
    public ObservableList<Presupuesto> getPresupuesto(){
        ArrayList<Presupuesto> lista = new ArrayList<Presupuesto>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPresupuesto}");
            ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                lista.add(new Presupuesto(resultado.getInt("CodigoPresupuesto"), 
                        resultado.getDate("FechaSolicitud"), 
                        resultado.getInt("CantidadPresupuesto"),
                        resultado.getInt("F_CodigoEmpresa") ));
                }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPresupuesto = FXCollections.observableArrayList(lista);
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
    
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
        public void RegresoEmpresas(){
        escenarioPrincipal.ventanaEmpresas();
    }
        public void RegresoPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
