package org.jrodas.controller;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.swing.JOptionPane;
import org.jrodas.bean.Platos;
import org.jrodas.bean.Productos;
import org.jrodas.bean.Productos_has_Platos;
import org.jrodas.db.Conexion;
import org.jrodas.system.MainApp;

public class Productos_has_PlatosController implements Initializable{
    private MainApp escenarioPrincipal;
    ObservableList<Productos_has_Platos> listaProductos_has_Platos;
    ObservableList<Productos> listaProductos;
    ObservableList<Platos> listaPlatos;
    @FXML private ComboBox cmbCodigoProducto;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableView tblProductosHasPlatos;
    @FXML private TableColumn colProducto;
    @FXML private TableColumn colPlato;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoProducto.setItems(getProductos());
        cmbCodigoPlato.setItems(getPlatos());
    }
    public void cargarDatos(){
        desactivarControles();
        tblProductosHasPlatos.setItems(getProductos_has_Platos());
        colProducto.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("CodigoPlato"));
        colPlato.setCellValueFactory(new PropertyValueFactory<Productos_has_Platos, Integer>("CodigoProducto"));
    }
    public void seleccionarElemento(){
        if(tblProductosHasPlatos.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Seleccione antes un registro");
        }else{
        activarControles();
        cmbCodigoProducto.getSelectionModel().select(buscarProducto(((Productos_has_Platos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoProducto()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Productos_has_Platos)tblProductosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        }
    }
    public ObservableList<Productos_has_Platos> getProductos_has_Platos(){
        ArrayList<Productos_has_Platos> listaPHP = new ArrayList<Productos_has_Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarProductos_has_platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                listaPHP.add(new Productos_has_Platos(resultado.getInt("productos_CodigoProducto"),
                        resultado.getInt("platos_CodigoPlato")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaProductos_has_Platos = FXCollections.observableArrayList(listaPHP);
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
    public Platos buscarPlato(int codPlato){
        Platos resultado = null;
        try{           
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarPLatos(?)}");
            procedimiento.setInt(1, codPlato);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Platos(registro.getInt("CodigoPlato"),
                        registro.getInt("Cantidad"),
                        registro.getString("NombrePlato"),
                        registro.getInt("PrecioPlato"),
                        registro.getInt("F_TipoPlato"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    public Productos buscarProducto(int codProduc){
        Productos resultado = null;
        try{           
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_BuscarProductos(?)}");
            procedimiento.setInt(1, codProduc);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next()){
                resultado = new Productos(registro.getInt("CodigoProducto"),
                        registro.getString("NombreProducto"),
                        registro.getInt("Cantidad"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return resultado;
    }
    public void desactivarControles(){
        cmbCodigoProducto.setDisable(true);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoProducto.setEditable(false);
        cmbCodigoPlato.setEditable(false);
    }
    public void activarControles(){
        cmbCodigoProducto.setDisable(false);
        cmbCodigoPlato.setDisable(false);
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
