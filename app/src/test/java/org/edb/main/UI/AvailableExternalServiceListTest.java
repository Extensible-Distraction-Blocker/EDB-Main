package org.edb.main.UI;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;


public class AvailableExternalServiceListTest extends Application
{
    public Stage primaryStage;
    @Override
    public void start(Stage stage) throws Exception {
        primaryStage=stage;
        loadAvailableExternalServiceListFXML();
        createServiceList();//breakpoint
        showEmptyTableView();//breakpoint
        addTableRows();//breakpoint
    }

    public void loadAvailableExternalServiceListFXML()
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(BootApp.class.getResource("/fxml/improvedAvailableExternalServiceList.fxml"));
        Parent parent= null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(parent);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void createServiceList(){

    }

    public void showEmptyTableView(){

    }

    public void addTableRows()
    {

    }


}
