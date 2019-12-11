package org.edb.main.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.edb.main.BootApp;
import org.edb.main.EDBPlugin;
import org.edb.main.EDBPluginManager;
import org.edb.main.UIEventHandler;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class MainUIController{
    //    @FXML
    private HBox rootContainer;
    @FXML
    private AnchorPane userPane;
    @FXML
    private Button loginBtn;
    @FXML
    private Button registerBtn;
    @FXML
    private Label userIdLbl;
    @FXML
    private ComboBox<String> pluginComboBox;

    @FXML
    private HBox centerUI;
    @FXML
    public Label getExternalServiceListButton;

    private Stage loginDialog;
    private UIEventHandler uiEventHandler;
    private ObservableList<String> pluginNames = FXCollections.observableArrayList();
    private Map<String,Integer> pluginNameIdxMap;
    private EDBPluginManager pluginManager;

    @FXML
    public Label postExternalServiceListButton;

    public Stage primaryStage;

    public void init(){
        pluginComboBox.setItems(pluginNames);
        pluginNameIdxMap= new HashMap<String,Integer>();
        fillPluginNameIdxMap();
        fillObservableList();
    }

    private void fillPluginNameIdxMap() {

        Map<Integer, EDBPlugin> pluginMap = pluginManager.getPlugins();
        for (Map.Entry<Integer, EDBPlugin> entry :
                pluginMap.entrySet()) {
            pluginNameIdxMap.put(entry.getValue().getPluginName(),entry.getKey());
        }
    }

    private void fillObservableList() {
        for (String pluginName :
                pluginNameIdxMap.keySet()) {
            pluginNames.add(pluginName);
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setUiEventHandler(UIEventHandler uiEventHandler) {
        if(uiEventHandler==null){
            System.out.println("UIeventHandler null");
        }
        this.uiEventHandler = uiEventHandler;
    }

    public void changeCenterUI(Parent parent){
        clearCenterUI();
        Scene tempScene=BootApp.getPrimaryStage().getScene();
        HBox hbox=(HBox)tempScene.lookup("#centerUI");
        hbox.getChildren().add(parent);

    }

    private void clearCenterUI(){
        centerUI.getChildren().clear();
        System.out.println("clearCenterUI\n");
    }

    public void showLoginDialog(ActionEvent event) throws Exception {
        loginDialog = new Stage(StageStyle.DECORATED);
        loginDialog.initModality(Modality.WINDOW_MODAL);
        loginDialog.initOwner(primaryStage);
        loginDialog.setTitle("Login");

        Parent parent = FXFactory.getInstance().loadLoginUI("/fxml/loginDialog.fxml");

        Scene scene = new Scene(parent);

        loginDialog.setScene(scene);
        loginDialog.setResizable(false);
        loginDialog.show();
    }

    public void showRegisterDialog(ActionEvent event) throws RuntimeException {

    }

    public void showAvailableExternalServiceList() {

        try {
            Parent parent = FXFactory.getInstance().loadAvailableExternalServiceUI("/fxml/AvailableExternalServiceList.fxml");
            changeCenterUI(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showUserExternalServiceList(){
        try {
            Parent parent = FXFactory.getInstance().loadUserExternalServiceUI("/fxml/UserExternalServiceList.fxml");
            changeCenterUI(parent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeLoginDialog() {
        if(loginDialog!=null) {
            loginDialog.close();
        }
    }

    public void setUILoggedIn(String id) {
        loginBtn.setDisable(true);
        userIdLbl.setVisible(true);
        userIdLbl.setText(id);
    }

    public void setPluginManager(EDBPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void onComboBoxSelected(){
        String selectedPluginName = pluginComboBox.getValue();
        Integer selectedPluginIdx = pluginNameIdxMap.get(selectedPluginName);
        Parent pluginConfigUI=null;
        try {
            pluginConfigUI = FXFactory.getInstance().loadPluginConfigUI("/fxml/PluginConfigUI.fxml",selectedPluginIdx);
        } catch (Exception e) {
            e.printStackTrace();
        }
        changeCenterUI(pluginConfigUI);
    }

}
