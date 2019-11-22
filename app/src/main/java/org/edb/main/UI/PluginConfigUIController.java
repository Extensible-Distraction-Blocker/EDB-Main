package org.edb.main.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.edb.main.Authorization;
import org.edb.main.ExternalService;
import org.edb.main.network.RestApiConnector;
import org.edb.main.network.get.getExternalServiceListResponse;
import org.edb.main.tempExternalService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PluginConfigUIController extends MainUIController implements Initializable{

    @FXML
    private TableView<ExternalService> userExternalServiceListView;
    @FXML
    private TableColumn<ExternalService, String> userExternalServiceTitle;
    @FXML
    private TableColumn<ExternalService, String> userExternalServiceUrl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //초기화

        loadUserExternelServiceList();


    }

    private ObservableList<ExternalService> userExternalData = FXCollections.observableArrayList();




    public ObservableList<ExternalService> getUserExternalData() {
        return userExternalData;

    }


    public void loadUserExternelServiceList() {

        System.out.print("\n외부서비스목록usertokentest\n");
        System.out.print(Authorization.getToken());
        System.out.print("\n");

        Call<getExternalServiceListResponse> getExternalServiceListResponseCall =
                RestApiConnector.getExternalServiceNetworkService().getExternalServiceListAPI(Authorization.getToken());

        getExternalServiceListResponseCall.enqueue(new Callback<getExternalServiceListResponse>() {

            @Override
            public void onResponse(Call<getExternalServiceListResponse> call, Response<getExternalServiceListResponse> response) {

                if (response.isSuccessful()) {
                    int status = response.body().getStatus();
                    if (status == 200) {
                        showExternalServiceTableList(response.body().getData());

                    }
                }
            }

            @Override
            public void onFailure(Call<getExternalServiceListResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }




            private void showExternalServiceTableList(ArrayList<tempExternalService> data) {
                for (tempExternalService value : data) {
                    userExternalData.add(new ExternalService(value.getName(), value.getUrl(),value.getExternal_service_idx()));
                }
                userExternalServiceTitle.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
                userExternalServiceUrl.setCellValueFactory(cellData -> cellData.getValue().urlProperty());
                userExternalServiceListView.setItems(userExternalData);

            }
        });


    }


}

