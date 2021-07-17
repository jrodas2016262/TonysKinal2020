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
import org.jrodas.bean.TipoPlato;
import org.jrodas.db.Conexion;
import org.jrodas.system.MainApp;

public class TipoPlatoController implements Initializable{
    private MainApp escenarioPrincipal;
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<TipoPlato> listaTipoPlato;
    @FXML private TextField txtCodigoTipoPlato;
    @FXML private TextField txtDescripcionTipoPlato;
    @FXML private TableView tblCodigoTipoPlato;
    @FXML private TableColumn colCodigoTipoPlato;
    @FXML private TableColumn colDescripcionTipoPlato;
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
        tblCodigoTipoPlato.setItems(getTipoPlato());
        colCodigoTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato,Integer>("CodigoTipoPlato"));
        colDescripcionTipoPlato.setCellValueFactory(new PropertyValueFactory<TipoPlato,String>("Descripcion"));
    }
    public void seleccionarElementos(){
        if(tblCodigoTipoPlato.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Seleccione antes un registro");
        }else{
        activarControles();
        txtCodigoTipoPlato.setText(String.valueOf(((TipoPlato)tblCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato()));
        txtDescripcionTipoPlato.setText(String.valueOf(((TipoPlato)tblCodigoTipoPlato.getSelectionModel().getSelectedItem()).getDescripcion()));
        }
    }
    public ObservableList<TipoPlato> getTipoPlato(){
        ArrayList<TipoPlato> lista = new ArrayList<TipoPlato>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarTipoPlato}");
            ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next()){
                    lista.add(new TipoPlato(resultado.getInt("CodigoTipoPlato"),
                        resultado.getString("Descripcion")));
                }
    
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaTipoPlato = FXCollections.observableArrayList(lista);
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
                if (tblCodigoTipoPlato.getSelectionModel().getSelectedItem() !=null){
                    int respuesta = JOptionPane.showConfirmDialog(null,"¿Realmente quiere eliminar el registro?","Eliminar Registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);              
                    if (respuesta == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarTipoPLato(?)}");
                            procedimiento.setInt(1, ((TipoPlato)tblCodigoTipoPlato.getSelectionModel().getSelectedItem()).getCodigoTipoPlato());
                            procedimiento.execute();
                            listaTipoPlato.remove(tblCodigoTipoPlato.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblCodigoTipoPlato.getSelectionModel().clearSelection();
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
    txtCodigoTipoPlato.setEditable(false);
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblCodigoTipoPlato.getSelectionModel().getSelectedItem() != null){
                    btnReporte.setDisable(false);
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
                btnReporte.setDisable(true);
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
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    public void guardar(){
        TipoPlato registro = new TipoPlato();
        registro.setCodigoTipoPlato(Integer.parseInt(txtCodigoTipoPlato.getText()));
        registro.setDescripcion(txtDescripcionTipoPlato.getText());
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarTipoPlato(?,?)}");
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            listaTipoPlato.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ModificarTipoPlato(?,?)}");
            TipoPlato registro = (TipoPlato)tblCodigoTipoPlato.getSelectionModel().getSelectedItem();
            registro.setCodigoTipoPlato(Integer.parseInt(txtCodigoTipoPlato.getText()));
            registro.setDescripcion(txtDescripcionTipoPlato.getText());
            procedimiento.setInt(1, registro.getCodigoTipoPlato());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.execute();
            desactivarControles();
            limpiarControles();
            txtCodigoTipoPlato.setDisable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void desactivarControles(){
        txtCodigoTipoPlato.setDisable(true);
        txtDescripcionTipoPlato.setDisable(true); 
        btnReporte.setDisable(true);
    }
    public void activarControles(){
        txtCodigoTipoPlato.setDisable(false);
        txtDescripcionTipoPlato.setDisable(false);
        txtCodigoTipoPlato.setEditable(true);
        txtDescripcionTipoPlato.setEditable(true);        
    }    
    public void limpiarControles(){
        txtCodigoTipoPlato.setText("");
        txtDescripcionTipoPlato.setText("");
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
    public void ventanaPlatos(){
        escenarioPrincipal.ventanaPlatos();
    }
    
}
