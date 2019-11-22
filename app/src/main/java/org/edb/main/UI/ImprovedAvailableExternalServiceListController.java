package org.edb.main.UI;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import jdk.nashorn.internal.codegen.CompilerConstants;
import org.edb.main.ExternalService;
import org.edb.main.User;
import org.edb.main.network.RestApiConnector;
import org.edb.main.network.get.getAvailableExternalServiceResponse;
import org.edb.main.network.get.getExternalServiceListResponse;
import org.edb.main.tempExternalService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ImprovedAvailableExternalServiceListController implements Initializable {
    @FXML
    private TableView<ExternalService> availableExternalServiceListView;
    @FXML
    private TableColumn<ExternalService, String> availableExternalServiceTitle;
    @FXML
    private TableColumn<ExternalService, String> availableExternalServiceUrl;

    private ObservableList<ExternalService> availableExternalData = FXCollections.observableArrayList();

    public TableView<ExternalService> getAvailableExternalServiceListView() {
        return availableExternalServiceListView;
    }

    public TableColumn<ExternalService, String> getAvailableExternalServiceTitle() {
        return availableExternalServiceTitle;
    }

    public TableColumn<ExternalService, String> getAvailableExternalServiceUrl() {
        return availableExternalServiceUrl;
    }


    public ObservableList<ExternalService> getAvailableExternalData() {
        return availableExternalData;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //초기화
        availableExternalServiceTitle.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        availableExternalServiceUrl.setCellValueFactory(cellData -> cellData.getValue().urlProperty());

        loadAvailableExternalServiceList();
    }


    public void loadAvailableExternalServiceList() {
        String token;
        try {
            token = User.getUser().getToken();
        }
        catch(RuntimeException runtimeException){
            token="dummy";
        }

        System.out.print("\n외부서비스등록sertokentest\n");
        System.out.print(token);

        Call<getAvailableExternalServiceResponse> getAvailableExternalServiceResponseCall =
                RestApiConnector.getExternalServiceNetworkService().getAvailableExternalServiceListAPI();

        getAvailableExternalServiceResponseCall.enqueue(new Callback<getAvailableExternalServiceResponse>() {

            private ImprovedAvailableExternalServiceListController controller;

            private Callback<getAvailableExternalServiceResponse> init(ImprovedAvailableExternalServiceListController controller){
                this.controller=controller;
                return this;
            }

            @Override
            public void onResponse(Call<getAvailableExternalServiceResponse> call, Response<getAvailableExternalServiceResponse> response) {

                try {
                    Platform.runLater(() -> {
                        System.out.println("in runLater\n");
                        if (response.isSuccessful()) {
                            int status = response.body().getStatus();
                            if (status == 200) {
                                System.out.print("\navilable service\n");

                                controller.handleExternalServiceResponse(response.body().getData());
                            }
                        }
                    });
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<getAvailableExternalServiceResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }


        }.init(this));

    }

    public void handleExternalServiceResponse(ArrayList<tempExternalService> data){
        convertToRows(data);
        addRowsToTableView();
    }

    public void convertToRows(ArrayList<tempExternalService> data) {

        for (tempExternalService value : data) {
            availableExternalData.add(new ExternalService(value.getName(), value.getUrl(),value.getExternal_service_idx()));
        }
    }

    public void addRowsToTableView(){
        availableExternalServiceListView.setItems(availableExternalData);
    }


}
