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
import org.jrodas.bean.Productos;
import org.jrodas.db.Conexion;
import org.jrodas.system.MainApp;

public class ProductosController implements Initializable{
    private MainApp escenarioPrincipal;
    private enum operaciones {NUEVO,GUARDAR,ELIMINAR,EDITAR,ACTUALIZAR,CANCELAR,NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    ObservableList<Productos> listaProductos;
    @FXML private TextField txtCodigoProductos;
    @FXML private TextField txtNombreProductos;
    @FXML private TextField txtCantidadProductos;
    @FXML private TableView tblCodigoProductos;
    @FXML private TableColumn colCodigoProductos;
    @FXML private TableColumn colNombreProductos;
    @FXML private TableColumn colCantidadProductos;
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
    tblCodigoProductos.setItems(getProductos());
    colCodigoProductos.setCellValueFactory(new PropertyValueFactory<Productos,Integer>("CodigoProducto"));
    colNombreProductos.setCellValueFactory(new PropertyValueFactory<Productos,String>("NombreProducto"));
    colCantidadProductos.setCellValueFactory(new PropertyValueFactory<Productos,Integer>("Cantidad"));
    }
    public void seleccionarElemento(){
        if(tblCodigoProductos.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Seleccione antes un registro");
        }else{
            activarControles();
            txtCodigoProductos.setText(String.valueOf(((Productos)tblCodigoProductos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
            txtNombreProductos.setText(String.valueOf(((Productos)tblCodigoProductos.getSelectionModel().getSelectedItem()).getNombreProducto()));
            txtCantidadProductos.setText(String.valueOf(((Productos)tblCodigoProductos.getSelectionModel().getSelectedItem()).getCantidad()));
        }
    }
    public void nuevo(){
        txtCodigoProductos.setEditable(true);
        switch(tipoDeOperacion){
            case NINGUNO:
                limpiarControles();
                activarControles();
                habilitarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnReporte.setDisable(true);
                btnModificar.setDisable(true);
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
                btnReporte.setDisable(false);
                btnModificar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                limpiarControles();
            break;
            default:
                if(tblCodigoProductos.getSelectionModel().getSelectedItem() != null){
                    int res = JOptionPane.showConfirmDialog(null, "¿Realmente quiere eliminar el registro?","Eliminar Registro",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(res == JOptionPane.YES_OPTION){
                        try{
                            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_EliminarProductos(?)}");
                            procedimiento.setInt(1, ((Productos)tblCodigoProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
                            procedimiento.execute();
                            listaProductos.remove(tblCodigoProductos.getSelectionModel().getSelectedIndex());
                            limpiarControles();
                            tblCodigoProductos.getSelectionModel().clearSelection();
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
        txtCodigoProductos.setDisable(true);
        switch(tipoDeOperacion){
            case NINGUNO:
                if(tblCodigoProductos.getSelectionModel().getSelectedItem() != null){
                    btnReporte.setDisable(false);
                    btnModificar.setText("Cambiar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    activarControles();
                    habilitarControles();
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
        btnModificar.setText("Modificar");
        btnReporte.setText("Cancelar");
        btnNuevo.setDisable(false);
        btnEliminar.setDisable(false);
        btnReporte.setDisable(true);
        tipoDeOperacion = operaciones.NINGUNO;
        cargarDatos();
    }
    public void guardar(){
        Productos registro = new Productos();
        registro.setCodigoProducto(Integer.parseInt(txtCodigoProductos.getText()));
        registro.setNombreProducto(txtNombreProductos.getText());
        registro.setCantidad(Integer.parseInt(txtCantidadProductos.getText()));
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_AgregarProductos(?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.execute();
            listaProductos.add(registro);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void actualizar(){
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ModificarProductos(?,?,?)}");
            Productos registro = (Productos)tblCodigoProductos.getSelectionModel().getSelectedItem();
            registro.setCodigoProducto(Integer.parseInt(txtCodigoProductos.getText()));
            registro.setNombreProducto(txtNombreProductos.getText());
            registro.setCantidad(Integer.parseInt(txtCantidadProductos.getText()));
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getNombreProducto());
            procedimiento.setInt(3, registro.getCantidad());
            procedimiento.execute();
            txtCodigoProductos.setDisable(false);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<Productos> getProductos(){
        ArrayList<Productos> lista = new ArrayList<Productos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Productos(resultado.getInt("CodigoProducto"),
                        resultado.getString("NombreProducto"),
                        resultado.getInt("Cantidad")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProductos = FXCollections.observableArrayList(lista);
    }
    public void limpiarControles(){
        txtCodigoProductos.setText("");
        txtNombreProductos.setText("");
        txtCantidadProductos.setText("");
    }
    public void activarControles(){
        txtCodigoProductos.setDisable(false);
        txtNombreProductos.setDisable(false);
        txtCantidadProductos.setDisable(false);
    }
    public void desactivarControles(){
        txtCodigoProductos.setDisable(true);
        txtNombreProductos.setDisable(true);
        txtCantidadProductos.setDisable(true);
        txtCodigoProductos.setEditable(false);
        txtNombreProductos.setEditable(false);
        txtCantidadProductos.setEditable(false);
        btnReporte.setDisable(true);
    }
    public void habilitarControles(){
        txtCodigoProductos.setEditable(true);
        txtNombreProductos.setEditable(true);
        txtCantidadProductos.setEditable(true);
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
