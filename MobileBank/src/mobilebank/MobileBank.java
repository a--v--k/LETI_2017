/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mobilebank;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Dron
 */
public class MobileBank extends Application {
    private final String USER_AGENT = "Mozilla/5.0";
    @Override
    public void start(Stage primaryStage) {
        
    }
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        Authorization AuthorizationFrame = new Authorization();
        
        AuthorizationFrame.setVisible(true);
    }
    
}
