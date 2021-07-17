package org.jrodas.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import org.jrodas.system.MainApp;


public class AcercaDeController implements Initializable {
     private MainApp escenarioPrincipal;

    public MainApp getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(MainApp escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    @Override
    public void initialize(URL url, ResourceBundle resources) {
       
    }    
    
    public void menuPrincipal(){
        
        escenarioPrincipal.menuPrincipal();
        
    }
   
}
