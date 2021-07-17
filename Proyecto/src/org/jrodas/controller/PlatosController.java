package org.jrodas.controller;
import java.net.URL;
import java.security.Principal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
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
import org.jrodas.bean.*;
import org.jrodas.db.Conexion;
import org.jrodas.report.GenerarReporte;
import org.jrodas.system.MainApp;

public class PlatosController implements Initializable{
    private MainApp escenarioPrincipal;
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<TipoPlato> listaTipoPlato;
    ObservableList<Platos> listaPlatos;
    @FXML private TextField txtCodigoPlatos;
    @FXML private TextField txtCantidadPlatos;
    @FXML private TextField txtNombrePlatos;
    @FXML private TextField txtPrecioPlatos;
    @FXML private ComboBox tmbTipoPlatos;
    @FXML private TableView tblCodigoPlatos;
    @FXML private TableColumn colCodigoPlatos;
    @FXML private TableColumn colCantidadPlatos;
    @FXML private TableColumn colNombrePlatos;
    @FXML private TableColumn colPrecioPlatos;
    @FXML private TableColumn colTipoPlatos;
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnModificar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        tmbTipoPlatos.setItems(getTipoPlato());
    }       
    public void cargarDatos(){
        desactivarControles();
        tblCodigoPlatos.setItems(getPlatos());
        colCodigoPlatos.setCellValueFactory(new PropertyValueFactory<Platos,Integer>("CodigoPlato"));
        colCantidadPlatos.setCellValueFactory(new PropertyValueFactory<Platos,Integer>("Cantidad"));
        colNombrePlatos.setCellValueFactory(new PropertyValueFactory<Platos,String>("NombrePlato"));
        colPrecioPlatos.setCellValueFactory(new PropertyValueFactory<Platos,String>("PrecioPlato"));
        colTipoPlatos.setCellValueFactory(new PropertyValueFactory<Platos,Integer>("CodigoTipoPlato"));
    }
    public void seleccionarElemento(){
        if(tblCodigoPlatos.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Seleccione antes un registro");
        }else{
            activarControles();
            txtCodigoPlatos.setText(String.valueOf(((Platos)tblCodigoPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
            txtCantidadPlatos.setText(String.valueOf(((Platos)tblCodigoPlatos.getSelectionModel().getSelectedItem()).getCantidad()));
            txtNombrePlatos.setText(((Platos)tblCodigoPlatos.getSelectionModel().getSelectedItem()).getNombrePlato());
            txtPrecioPlatos.setText(String.valueOf(((Platos)tblCodigoPlatos.getSelectionModel().getSelectedItem()).getPrecioPlato()));
            tmbTipoPlatos.getSelectionModel().select(buscarTipoPlato(((Platos)tblCodigoPlatos.getSelectionModel().getSelectedItem()).getTipoPlato()));
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
                tipoDeOperacion = operaciones.NINGUNO;
                limpiarControles();
                break;
            default:
                if (tblCodigoPlatos.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Realmente quiere eliminar el registro?","Eliminar Registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);              
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarPlatos(?)}");
                            procedimiento.setInt(1, ((Platos)tblCodigoPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato());
                            procedimiento.execute();
                            listaPlatos.remove(tblCodigoPlatos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblCodigoPlatos.getSelectionModel().clearSelection();
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
        txtCodigoPlatos.setDisable(true);
        switch(tipoDeOperacion){
            case NINGUNO:               
                if(tblCodigoPlatos.getSelectionModel().getSelectedItem() !=null){
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
            tmbTipoPlatos.setDisable(false);
            txtCodigoPlatos.setDisable(false);
            btnModificar.setText("Modificar");
            btnReporte.setText("Reporte");
            btnNuevo.setDisable(false);
            btnEliminar.setDisable(false);
            tipoDeOperacion = operaciones.NINGUNO;
            break;
        }
    }
    
    
    public void imprimirReporte(){
        if(tblCodigoPlatos.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null, "Para generar un REPORTE antes seleccione un valor");
        }else{
           Map parametros = new HashMap();
           Platos registro = (Platos)tblCodigoPlatos.getSelectionModel().getSelectedItem();
           int codPlato = registro.getCodigoPlato();
           parametros.put("codPlato", codPlato);
           GenerarReporte.mostrarReporte("ReporteP.jasper", "Reporte Platos", parametros);
        }
    }
    
    
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ModificarPlatos(?,?,?,?)}");
            Platos registro = (Platos)tblCodigoPlatos.getSelectionModel().getSelectedItem();
            registro.setCodigoPlato(Integer.parseInt(txtCodigoPlatos.getText()));
            registro.setCantidad(Integer.parseInt(txtCantidadPlatos.getText()));
            registro.setNombrePlato(txtNombrePlatos.getText());
            registro.setPrecioPlato(Integer.parseInt(txtPrecioPlatos.getText()));
            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getNombrePlato());
            procedimiento.setInt(4, registro.getPrecioPlato());
            procedimiento.execute();
            desactivarControles();
            limpiarControles();
            txtCodigoPlatos.setDisable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void guardar(){
        Platos registro = new Platos();
        registro.setCodigoPlato(Integer.parseInt(txtCodigoPlatos.getText()));
        registro.setCantidad(Integer.parseInt(txtCantidadPlatos.getText()));
        registro.setNombrePlato(txtNombrePlatos.getText());
        registro.setPrecioPlato(Integer.parseInt(txtPrecioPlatos.getText()));
        registro.setTipoPlato(((TipoPlato)tmbTipoPlatos.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarPlatos(?,?,?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoPlato());
            procedimiento.setInt(2, registro.getCantidad());
            procedimiento.setString(3, registro.getNombrePlato());
            procedimiento.setInt(4, registro.getPrecioPlato());
            procedimiento.setInt(5, registro.getTipoPlato());
            procedimiento.execute();
            listaPlatos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<Platos> getPlatos(){
        ArrayList<Platos> lista = new ArrayList<Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarPlatos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Platos(resultado.getInt("CodigoPlato"),
                        resultado.getInt("Cantidad"),
                        resultado.getString("NombrePlato"),
                        resultado.getInt("PrecioPlato"),
                        resultado.getInt("F_TipoPlato")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaPlatos = FXCollections.observableArrayList(lista);
    }
    
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlato}");
            ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new TipoPlato(resultado.getInt("codigoTipoPlato"),
                            resultado.getString("descripcion")));
                }
    
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
    }
    public TipoPlato buscarTipoPlato(int codTipoPlato){
        TipoPlato resultado = null;
        try{           
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarTipoPLato(?)}");
            procedimiento.setInt(1, codTipoPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new TipoPlato(registro.getInt("CodigoTipoPlato"),
                        registro.getString("Descripcion"));                           
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void desactivarControles(){
        txtCodigoPlatos.setDisable(true);
        txtCantidadPlatos.setDisable(true);
        txtNombrePlatos.setDisable(true);
        txtPrecioPlatos.setDisable(true);
        tmbTipoPlatos.setEditable(false);
        tmbTipoPlatos.setDisable(true);
    }
    public void activarControles(){
        txtCodigoPlatos.setDisable(false);
        txtCantidadPlatos.setDisable(false);
        txtNombrePlatos.setDisable(false);
        txtPrecioPlatos.setDisable(false);
        tmbTipoPlatos.setDisable(false);
    }
    public void limpiarControles(){
        txtCodigoPlatos.setText("");
        txtCantidadPlatos.setText("");
        txtNombrePlatos.setText("");
        txtPrecioPlatos.setText("");
        tmbTipoPlatos.getSelectionModel().clearSelection();
        tmbTipoPlatos.getSelectionModel().select(null);
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
    public void RegresoTipoPlato(){
        escenarioPrincipal.ventanaTipoPlato();
    }
}
