package org.edb.main.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import org.edb.main.UIEventHandler;

import org.edb.main.ExternalService;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AvailableExternalServiceListController{
    @FXML
    private TableView<FXExternalService> availableExternalServiceListView;
    @FXML
    private TableColumn<FXExternalService, String> availableExternalServiceTitle;
    @FXML
    private TableColumn<FXExternalService, String> availableExternalServiceUrl;

    private ObservableList<FXExternalService> availableExternalData = FXCollections.observableArrayList();

    private UIEventHandler uiEventHandler;

    public void setUiEventHandler(UIEventHandler uiEventHandler) {
        this.uiEventHandler = uiEventHandler;
    }

    public ObservableList<FXExternalService> getAvailableExternalData() {
        return availableExternalData;

    }

    public void init(){
        //초기화
        availableExternalServiceTitle.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        availableExternalServiceUrl.setCellValueFactory(cellData -> cellData.getValue().urlProperty());

        loadAvailableExternalServiceList();
    }

    public void loadAvailableExternalServiceList() {
        uiEventHandler.onAvailableExternalServiceListLoaded();
    }

    public void handleExternalServiceResponse(ArrayList<ExternalService> data){
        convertToRows(data);
        addRowsToTableView();
    }

    public void convertToRows(ArrayList<ExternalService> data) {

        for (ExternalService value : data) {
            availableExternalData.add(new FXExternalService(value.getName(), value.getUrl(),value.getExternal_service_idx()));
        }
    }

    public void addRowsToTableView(){
        availableExternalServiceListView.setItems(availableExternalData);
    }


}
