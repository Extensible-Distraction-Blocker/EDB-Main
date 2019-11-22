package org.edb.main.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.edb.main.ExternalService;
import org.edb.main.network.RestApiConnector;
import org.edb.main.network.get.getAvailableExternalServiceResponse;
import org.edb.main.tempExternalService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class postExternalServiceController extends MainUIController implements Initializable {
    @FXML
    private TableView<ExternalService> availableExternalServiceListView;
    @FXML
    private TableColumn<ExternalService, String> availableExternalServiceTitle;
    @FXML
    private TableColumn<ExternalService, String> availableExternalServiceUrl;

    private ObservableList<ExternalService> availableExternalData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //초기화
        loadAvailableExternalServiceList();
    }


    public ObservableList<ExternalService> getAvailableExternalData() {
        return availableExternalData;

    }


    public void loadAvailableExternalServiceList() {

        System.out.print("\n외부서비스등록sertokentest\n");

        Call<getAvailableExternalServiceResponse> getAvailableExternalServiceResponseCall =
                RestApiConnector.getExternalServiceNetworkService().getAvailableExternalServiceListAPI();

        getAvailableExternalServiceResponseCall.enqueue(new Callback<getAvailableExternalServiceResponse>() {

            @Override
            public void onResponse(Call<getAvailableExternalServiceResponse> call, Response<getAvailableExternalServiceResponse> response) {

                if (response.isSuccessful()) {
                    int status = response.body().getStatus();
                    if (status == 200) {
                        System.out.print("\navilable service\n");

                        availableExternalServiceTableList(response.body().getData());

                    }
                }
            }

            @Override
            public void onFailure(Call<getAvailableExternalServiceResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }

            private void availableExternalServiceTableList(ArrayList<tempExternalService> data) {

                for (tempExternalService value : data) {
                    availableExternalData.add(new ExternalService(value.getName(), value.getUrl(),value.getExternal_service_idx()));
                }

                availableExternalServiceTitle.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
                availableExternalServiceUrl.setCellValueFactory(cellData -> cellData.getValue().urlProperty());
                availableExternalServiceListView.setItems(availableExternalData);
            }
        });

    }

}

