package org.edb.main;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.edb.main.EDBPluginManager;
import org.edb.main.ServerResponseHandler;
import org.edb.main.UI.FXFactory;
import org.edb.main.UI.FXManipulator;
import org.edb.main.UIEventHandler;
import org.edb.main.UIManipulator;
import org.edb.main.network.JsonConverter;
import org.edb.main.network.RestAPIRequester;
import org.edb.main.network.TempJsonConverter;

import java.io.IOException;

import static javafx.application.Platform.exit;

public class BootApp extends Application {

    public static Stage primaryStage;

    public static HBox rootLayout;

    public BootApp() {

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("EDB-Main");

        initComponentRoots();
        initRootLayout();
    }

    public void initComponentRoots(){
//        **TODO pluginManager작업이 fxml 생성작업보다 우선해야 한다.
        FXManipulator fxManipulator= new FXManipulator();
        EDBPluginManager edbPluginManager = new EDBPluginManager();
        edbPluginManager.setManipulator(fxManipulator);
        ServerResponseHandler serverResponseHandler=new ServerResponseHandler(fxManipulator);

        RestAPIRequester restAPIRequester = new RestAPIRequester();
        restAPIRequester.setServerResponseHandler(serverResponseHandler);
        restAPIRequester.setPluginManager(edbPluginManager);

        serverResponseHandler.setServerRequester(restAPIRequester);
        serverResponseHandler.setPluginManager(edbPluginManager);

        UIEventHandler uiEventHandler=new UIEventHandler(restAPIRequester);
        uiEventHandler.setUiManipulator(fxManipulator);

        FXFactory.getInstance().init(uiEventHandler,fxManipulator);
        FXFactory.getInstance().setPluginManager(edbPluginManager);
    }

    public void initRootLayout() {
        FXFactory fxFactory= FXFactory.getInstance();
        Parent parent = null;
        try {
            parent =fxFactory.loadMainUI("/fxml/MainUI.fxml");
            Scene scene = new Scene(parent);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            exit();
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }




}