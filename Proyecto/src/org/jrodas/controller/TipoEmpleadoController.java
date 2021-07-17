package org.jrodas.controller;
import java.net.URL;
import java.security.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
import javax.swing.JOptionPane;
import org.jrodas.bean.TipoEmpleado;
import org.jrodas.db.Conexion;
import org.jrodas.system.MainApp;

public class TipoEmpleadoController implements Initializable{
    private MainApp escenarioPrincipal;
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<TipoEmpleado> listaTipoEmpleado;
    @FXML private TextField txtCodigoTipoEmpleado;
    @FXML private TextField txtDescripcionTipoEmpleado;
    @FXML private TableView tblCodigoTipoEmpleado;
    @FXML private TableColumn colCodigoTipoEmpleado;
    @FXML private TableColumn colDescripcionTipoEmpleado;
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
        tblCodigoTipoEmpleado.setItems(getTipoEmpleado());
        colCodigoTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,Integer>("CodigoTipoEmpleado"));
        colDescripcionTipoEmpleado.setCellValueFactory(new PropertyValueFactory<TipoEmpleado,String>("Descripcion"));
        }
    public void seleccionarElementos(){
        if(tblCodigoTipoEmpleado.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Seleccione antes un registro");
        }else{
        manejoControles();
        txtCodigoTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado()));
        txtDescripcionTipoEmpleado.setText(String.valueOf(((TipoEmpleado)tblCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getDescripcion()));
        }
    }
    public void desactivarControles(){
        txtCodigoTipoEmpleado.setDisable(true);
        txtDescripcionTipoEmpleado.setDisable(true);
        btnReporte.setDisable(true);
    }
    public void activarControles(){
        txtCodigoTipoEmpleado.setDisable(false);
        txtDescripcionTipoEmpleado.setDisable(false);
        txtCodigoTipoEmpleado.setEditable(true);
        txtDescripcionTipoEmpleado.setEditable(true);
    } 
    public void manejoControles(){
        txtCodigoTipoEmpleado.setDisable(false);
        txtDescripcionTipoEmpleado.setDisable(false);
        txtCodigoTipoEmpleado.setEditable(false);
        txtDescripcionTipoEmpleado.setEditable(false);
    }
    public void limpiarControles(){
        txtCodigoTipoEmpleado.setText("");
        txtDescripcionTipoEmpleado.setText("");
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
                if (tblCodigoTipoEmpleado.getSelectionModel().getSelectedItem() !=null){
                    int respuesto = JOptionPane.showConfirmDialog(null, "¿Realmente quiere eliminar el registro?","Eliminar Registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if (respuesto == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoEmpleado(?)}");
                            procedimiento.setInt(1, ((TipoEmpleado)tblCodigoTipoEmpleado.getSelectionModel().getSelectedItem()).getCodigoTipoEmpleado());
                            procedimiento.execute();
                            listaTipoEmpleado.remove(tblCodigoTipoEmpleado.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblCodigoTipoEmpleado.getSelectionModel().clearSelection();
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
        txtCodigoTipoEmpleado.setEditable(false);
        switch(tipoDeOperacion){
            case NINGUNO:               
                if(tblCodigoTipoEmpleado.getSelectionModel().getSelectedItem() !=null){
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
                btnModificar.setText("Modificar");
                btnReporte.setText("Cancelar");
                btnReporte.setDisable(true);
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ModificarTipoEmpleado(?,?)}");
            TipoEmpleado registro = (TipoEmpleado)tblCodigoTipoEmpleado.getSelectionModel().getSelectedItem();
            registro.setCodigoTipoEmpleado(Integer.parseInt(txtCodigoTipoEmpleado.getText()));
            registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            desactivarControles();
            limpiarControles();
            txtCodigoTipoEmpleado.setEditable(true);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void guardar(){
        TipoEmpleado registro = new TipoEmpleado();
        registro.setCodigoTipoEmpleado(Integer.parseInt(txtCodigoTipoEmpleado.getText()));
        registro.setDescripcion(txtDescripcionTipoEmpleado.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoEmpleado(?,?)}");
            procedimiento.setInt(1, registro.getCodigoTipoEmpleado());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            listaTipoEmpleado.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public ObservableList<TipoEmpleado> getTipoEmpleado(){
        ArrayList<TipoEmpleado> lista = new ArrayList<TipoEmpleado>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoEmpleado}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new TipoEmpleado(resultado.getInt("CodigoTipoEmpleado"),
                    resultado.getString("Descripcion")));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoEmpleado = FXCollections.observableArrayList(lista);
    }
    
    
    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }
    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    public void ventanaEmpleados(){
        escenarioPrincipal.ventanaEmpleados();
    }
    public void RegresoPrincipal(){
        escenarioPrincipal.menuPrincipal();
    }
}
