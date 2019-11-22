package org.edb.main.UI;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.edb.main.*;
import org.edb.main.network.RestApiConnector;
import org.edb.main.network.get.getExternalServiceDetailListResponse;
import org.edb.main.network.get.getExternalServiceListResponse;
import org.edb.main.network.post.postExternalServiceResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ImprovedUserExternalServiceListController implements Initializable {

    @FXML
    private TableView<ExternalService> userExternalServiceListView;
    @FXML
    private TableColumn<ExternalService, String> userExternalServiceTitle;
    @FXML
    private TableColumn<ExternalService, String> userExternalServiceUrl;

    private ObservableList<ExternalService> userExternalData = FXCollections.observableArrayList();

    @FXML
    private TableView<ExternalServiceDetail> externalServiceDetailView;
    @FXML
    private TableColumn<ExternalServiceDetail, String> externalServiceDetailTitle;
    @FXML
    private TableColumn<ExternalServiceDetail, String> externalServiceDetail_IF_ARCHIEVE;

    @FXML
    private TableColumn<ExternalServiceDetail, Boolean> externalServiceDetailChkBox;

    @FXML
    private Button postExternalDetailBtn;

    private ObservableList<ExternalServiceDetail> externalDetailData = FXCollections.observableArrayList();

    public ArrayList<Integer[]> list = new ArrayList<Integer[]>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //초기화
        userExternalServiceTitle.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        userExternalServiceUrl.setCellValueFactory(cellData -> cellData.getValue().urlProperty());

        loadUserExternalServiceList();

        //외부서비스목록테이블 선택시 목표달성테이블 띄우기
        userExternalServiceListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> showDetails(newValue));

        externalServiceDetailTitle.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        externalServiceDetail_IF_ARCHIEVE.setCellValueFactory(cellData -> cellData.getValue().ifachieveProperty());

        externalServiceDetailChkBox.setCellValueFactory(new PropertyValueFactory<ExternalServiceDetail, Boolean>("check"));

    }

    public TableView<ExternalService> getUserExternalServiceListView() {
        return userExternalServiceListView;
    }

    public TableColumn<ExternalService, String> getUserExternalServiceTitle() {
        return userExternalServiceTitle;
    }

    public TableColumn<ExternalService, String> getUserExternalServiceUrl() {
        return userExternalServiceUrl;
    }

    public ObservableList<ExternalService> getUserExternalData() {
        return userExternalData;

    }


    public void loadUserExternalServiceList() {
        String token;


        System.out.print("\nuser\n");
        System.out.print(User.getUser().getToken());
        System.out.print(User.getUser().getId());

        try {
            token = User.getUser().getToken();
        } catch (RuntimeException runtimeException) {
            token = "dummy";
        }

        System.out.print("\n외부서비스목록usertokentest\n");
        System.out.print(token);
        System.out.print("\n");

        Call<getExternalServiceListResponse> getExternalServiceListResponseCall =
                RestApiConnector.getExternalServiceNetworkService().getExternalServiceListAPI(token);

        getExternalServiceListResponseCall.enqueue(new Callback<getExternalServiceListResponse>() {

            private ImprovedUserExternalServiceListController controller;

            private Callback<getExternalServiceListResponse> init(ImprovedUserExternalServiceListController controller) {
                this.controller = controller;
                return this;
            }

            @Override
            public void onResponse(Call<getExternalServiceListResponse> call, Response<getExternalServiceListResponse> response) {
                try {
                    Platform.runLater(() -> {
                        System.out.println("in runLater\n");
                        if (response.isSuccessful()) {
                            int status = response.body().getStatus();
                            if (status == 200) {

                                controller.handleUserExternalServiceResponse(response.body().getData());

                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<getExternalServiceListResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }

        }.init(this));


    }

    public void handleUserExternalServiceResponse(ArrayList<tempExternalService> data) {

        convertUserExternalServiceToRows(data);
        addUserExternalServiceRowsToTableView();
    }

    public void convertUserExternalServiceToRows(ArrayList<tempExternalService> data) {

        for (tempExternalService value : data) {
            userExternalData.add(new ExternalService(value.getName(), value.getUrl(),value.getExternal_service_idx()));
        }
    }

    public void addUserExternalServiceRowsToTableView(){
        userExternalServiceListView.setItems(userExternalData);
    }


    public void showDetails(ExternalService clickValue) {

        System.out.print("\n특정 IDX 클릭\n");
        System.out.print(clickValue.getIdx());
        //선택한 외부서비스의 external_idx
        loadDetailList(clickValue.getIdx());
    }

    public void loadDetailList(int externalIdx) {
        String token;
        System.out.print("\nuser\n");
        System.out.print(User.getUser().getToken());
        System.out.print(User.getUser().getId());

        try {
            token = User.getUser().getToken();
        } catch (RuntimeException runtimeException) {
            token = "dummy";
        }

        //선택한 외부서비스의 목표달성테이블 데이터받기
        //요청값 externalIdx,token //응답값  external_service_detail_idx(목표달성idx),name (목표달성이름),if_achieve(달성여부 1 = 목표 달성 , 0 = 목표달성x)
        //@GET("external/detail/{externalIdx}")

        //데이터 받기 성공시 테이블과 체크박스 보여주기
        Call<getExternalServiceDetailListResponse> getExternalServiceDetailListResponseCall =
                RestApiConnector.getExternalServiceNetworkService().getExternalServiceDetailListAPI(externalIdx, token);

        getExternalServiceDetailListResponseCall.enqueue(new Callback<getExternalServiceDetailListResponse>() {

            private ImprovedUserExternalServiceListController controller;

            private Callback<getExternalServiceDetailListResponse> init(ImprovedUserExternalServiceListController controller) {
                this.controller = controller;
                return this;
            }

            @Override
            public void onResponse(Call<getExternalServiceDetailListResponse> call, Response<getExternalServiceDetailListResponse> response) {
                try {
                    Platform.runLater(() -> {
                        System.out.println("in runLater\n");
                        if (response.isSuccessful()) {
                            int status = response.body().getStatus();
                            if (status == 200) {
                                System.out.print("detail result test\n");
                                System.out.print(response.body().getData());
                                //데이터 받기 성공시 테이블과 체크박스 보여주기
                                controller.showExternalServiceDetailTableList(externalIdx, response.body().getData());

                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<getExternalServiceDetailListResponse> call, Throwable throwable) {
                System.out.print("error\n");
                System.out.println(throwable);
            }

        }.init(this));
    }


    public void showExternalServiceDetailTableList(int externalIdx, ArrayList<tempExternalServiceDetail> data) {

        externalServiceDetailView.setVisible(true);
        externalServiceDetailTitle.setVisible(true);
        externalServiceDetail_IF_ARCHIEVE.setVisible(true);
        postExternalDetailBtn.setVisible(true);

        externalDetailData.clear();

        convertExternalServiceDetailToRows(data);

        //tablecolumn에 담길 tablecell을 만드는 cellFactory의 구현
        externalServiceDetailChkBox.setCellFactory(column -> new TableCell<ExternalServiceDetail, Boolean>() {
            @Override
            protected void updateItem(Boolean check, boolean empty) {
                super.updateItem(check, empty);
                //check : 새롭게 입력된 값?
                //empty : 연결된 데이터가 있는지 여부?
                if (check == null || empty) {
                    setGraphic(null);
                } else {
                    //체크박스생성
                    //체크박스 클릭시 list 2차원 배열에 externalIdx 와 externalDetailIdx 를 저장함
                    // (이후에 postExternalDetailBtn 버튼 클릭 시에 저장된 list를 이용해 목표 달성 갱신)
                    CheckBox box = new CheckBox();
                    BooleanProperty checked = (BooleanProperty) column.getCellObservableValue(getIndex());

                    ExternalServiceDetail cn = (ExternalServiceDetail) column.getTableView().getItems().get(getIndex());
                    if (checked.get()) {
//                        System.out.println(cn.idxProperty() + " is Checked!");

                        list.add(new Integer[]{externalIdx, cn.idxProperty().get()});


                    } else {
//                        System.out.println(cn.idxProperty() + " is Unchecked!");
                        // 선택해제된것이라면 remove의 구현도 필요?
                    }
                    box.setSelected(checked.get());
                    box.selectedProperty().bindBidirectional(checked);
                    setGraphic(box);
                }
            }
        });
        //externalIdx를 받아올수 있는 다른방법을 고려한뒤 initialize 메소드로 이동 필요해보임..


        //externalServiceDetailView.getColumns().add(externalServiceDetailChkBox);
        //externalServiceDetailView.setEditable(true);
        addExternalServiceDetailToTable();
    }

    private void convertExternalServiceDetailToRows(ArrayList<tempExternalServiceDetail> data) {
        for (tempExternalServiceDetail value : data) {
            externalDetailData.add(new ExternalServiceDetail(value.getExternal_service_detail_idx(), value.getName(), value.getIf_achieve()));
        }
    }

    private void addExternalServiceDetailToTable() {
        externalServiceDetailView.setItems(externalDetailData);
    }

    public void postExternalDetailRequest() {

        String token;

        System.out.print("\npostTest\n");
        System.out.print(User.getUser().getToken());

        try {
            token = User.getUser().getToken();
        } catch (RuntimeException runtimeException) {
            token = "dummy";
        }
//체크박스 클릭시 list 2차원 배열에 externalIdx 와 externalDetailIdx 를 저장
//(이후에 postExternalDetailBtn 버튼 클릭 시에 저장된 list를 이용해 목표 달성 갱신)

        //    @PUT("external/{externalIdx}/{externalDetailIdx}")
        //요청값: externalIdx, externalDetailIdx, token
        //응답값: statuscode, message, success여부

        for(int i = 0;i< list.size();i++){

            Call<postExternalServiceResponse> postExternalServiceDetailResponseCall =
                    RestApiConnector.getExternalServiceNetworkService().postExternalServiceListAPI(list.get(i)[0], list.get(i)[1], token);

            postExternalServiceDetailResponseCall.enqueue(new Callback<postExternalServiceResponse>() {

                private ImprovedUserExternalServiceListController controller;

                private Callback<postExternalServiceResponse> init(ImprovedUserExternalServiceListController controller) {
                    this.controller = controller;
                    return this;
                }

                @Override
                public void onResponse(Call<postExternalServiceResponse> call, Response<postExternalServiceResponse> response) {
                    try {
                        Platform.runLater(() -> {
                            System.out.println("in runLater\n");
                            if (response.isSuccessful()) {
                                int status = response.body().getStatus();
                                if (status == 200) {
                                    System.out.print("detail result test\n");
                                    System.out.print(response.body().getMessage());
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<postExternalServiceResponse> call, Throwable throwable) {
                    System.out.print("error\n");
                    System.out.println(throwable);
                }

            });


        }
    }

}