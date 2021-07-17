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
import org.jrodas.bean.*;
import org.jrodas.db.Conexion;
import org.jrodas.system.MainApp;

public class Servicios_has_PlatosController implements Initializable{
    private MainApp escenarioPrincipal;
    private ObservableList<Servicios> listaServicios;
    ObservableList<Servicios_has_Platos> listaServicios_has_platos;
    ObservableList<Platos> listaPlatos;
    @FXML private ComboBox cmbCodigoServicios;
    @FXML private ComboBox cmbCodigoPlato;
    @FXML private TableColumn colServicios;
    @FXML private TableColumn colPlato;
    @FXML private TableView tblServiciosHasPlatos;
    
    public void cargarDatos(){
        desactivarControles();
        tblServiciosHasPlatos.setItems(getServicios_has_platos());
        colServicios.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos,Integer>("CodigoServicios"));
        colPlato.setCellValueFactory(new PropertyValueFactory<Servicios_has_Platos,Integer>("CodigoPlato"));
    }
    
    public void seleccionarElemento(){
        if(tblServiciosHasPlatos.getSelectionModel().getSelectedItem() == null){
            JOptionPane.showMessageDialog(null,"Seleccione antes un registro");
        }else{ 
        activarControles();
        cmbCodigoServicios.getSelectionModel().select(buscarServicio(((Servicios_has_Platos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoServicio()));
        cmbCodigoPlato.getSelectionModel().select(buscarPlato(((Servicios_has_Platos)tblServiciosHasPlatos.getSelectionModel().getSelectedItem()).getCodigoPlato()));
        }
    }
    
    public ObservableList<Servicios_has_Platos> getServicios_has_platos(){
        ArrayList<Servicios_has_Platos> lista = new ArrayList<Servicios_has_Platos>();
        try{
            PreparedStatement procedimiento = Conexion.getInstance().getConexion().prepareCall("{call sp_ListarServicios_has_platos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Servicios_has_Platos(resultado.getInt("servicios_codigoServicio"),
                        resultado.getInt("plato_codigoPlato")));
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }
        return listaServicios_has_platos = FXCollections.observableArrayList(lista);
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
    public void desactivarControles(){
        cmbCodigoServicios.setDisable(true);
        cmbCodigoPlato.setDisable(true);
        cmbCodigoServicios.setEditable(false);
        cmbCodigoPlato.setEditable(false);
    }
    public void activarControles(){
        cmbCodigoServicios.setDisable(false);
        cmbCodigoPlato.setDisable(false);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoServicios.setItems(getServicios());
        cmbCodigoPlato.setItems(getPlatos());
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
