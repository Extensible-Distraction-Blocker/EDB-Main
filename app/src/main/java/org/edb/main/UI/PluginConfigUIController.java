package org.edb.main.UI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.edb.main.*;
import org.edb.main.model.TargetProgram;
import org.edb.main.model.TargetWebsite;
import org.edb.main.util.DateFormatter;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.util.*;

import static org.edb.main.BootApp.primaryStage;

public class PluginConfigUIController {

    private static double prefWidth = 463.0;

    @FXML
    private VBox configArea;
    @FXML
    private AnchorPane applyArea;
    @FXML
    private TextField targetWebsiteField;
    @FXML
    private TextField startHourField;
    @FXML
    private TextField startMinField;
    @FXML
    private TextField endHourField;
    @FXML
    private TextField endMinField;
    @FXML
    private TableView<FXTargetProgram> targetProgramTableView;
    @FXML
    private TableView<FXTargetWebsite> targetWebsiteTableView;
    @FXML
    private TableColumn<FXTargetProgram, String> targetProgramNameColumn;
    @FXML
    private TableColumn<FXTargetProgram, String> targetProgramPathColumn;
    @FXML
    private TableColumn<FXTargetWebsite, String> targetWebsiteURLColumn;
    @FXML
    private Button scheduleBtn;
    @FXML
    private Button modifyScheduleBtn;
    @FXML
    private AnchorPane scheduleArea;
    @FXML
    private Label scheduledTimeLbl;

    private EDBPlugin plugin;
    private EDBPluginManager pluginManager;
    private UIEventHandler uiEventHandler;
    private ObservableList<FXTargetProgram> fxTargetProgramObservableList = FXCollections.observableArrayList();
    private ObservableList<FXTargetWebsite> fxTargetWebsiteObservableList = FXCollections.observableArrayList();

    public void setPluginManager(EDBPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public void setUIEventHandler(UIEventHandler uiEventHandler) {
        this.uiEventHandler=uiEventHandler;
    }

    public void init(){
        targetProgramNameColumn.setCellValueFactory(cellData->cellData.getValue().targetNameProperty());
        targetProgramPathColumn.setCellValueFactory(cellData->cellData.getValue().targetPathProperty());
        targetWebsiteURLColumn.setCellValueFactory(cellData->cellData.getValue().targetURLProperty());


        targetProgramTableView.setItems(fxTargetProgramObservableList);
        targetWebsiteTableView.setItems(fxTargetWebsiteObservableList);
        fillPluginConfigUIContents();
    }

    public void fillPluginConfigUIContents(){
        loadTime();
        loadTargetPrograms();
        loadTargetWebsites();
        loadLogics();
    }

    private void loadTime() {
        if(plugin.isRunning()){
            Date startDate = plugin.getStartDate();
            Date endDate = plugin.getEndDate();
            loadDateOnUI(startDate,endDate);
        }
    }

    private void loadTargetPrograms() {
        for (TargetProgram singleProgram :
                plugin.getTargetPrograms().values()) {
          fxTargetProgramObservableList.add(new FXTargetProgram(singleProgram));
        }
    }

    private void loadTargetWebsites() {
        for (TargetWebsite singleWebsite :
                plugin.getTargetWebsites().values()) {
            fxTargetWebsiteObservableList.add(new FXTargetWebsite(singleWebsite));
        }
    }

    private void loadLogics() {
        FXFactory factory = FXFactory.getInstance();
        for (Map.Entry<String,PluginLogic> singleLogicEntry :
                plugin.getPluginLogics().entrySet()) {

            PluginLogic singleLogic = singleLogicEntry.getValue();

            Parent singleConfigUI = null;

            try {
                singleConfigUI = factory.loadSpecificPluginUIFromPluginLogic(singleLogic);
            } catch (Exception e) {
                e.printStackTrace();
            }

            attachSpecificLogicUIToCommonConfigUI(singleConfigUI);
            singleLogic.sendRequestFillUI();
        }
    }

    private void attachSpecificLogicUIToCommonConfigUI(Parent singleConfigUI) {
        double singleConfigUIHeight = singleConfigUI.prefHeight(prefWidth);
        widerConfigArea(singleConfigUIHeight);
//        붙이기.
        configArea.getChildren().add(singleConfigUI);
    }

    private void widerConfigArea(double singleConfigUIHeight) {
//        **TODO widerConfigArea
    }

    public void addTargetProgram(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files","*.*"));
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if(selectedFile==null)return;
        FXTargetProgram addedTargetProgram = new FXTargetProgram(selectedFile.getName(),selectedFile.getAbsolutePath());

        fxTargetProgramObservableList.add(addedTargetProgram);

        plugin.addTargetProgram(addedTargetProgram.convertToTargetProgram());
    }

    public void deleteTargetProgram(){

        FXTargetProgram selectedItem = targetProgramTableView.getSelectionModel().getSelectedItem();
        targetProgramTableView.getItems().remove(selectedItem);
        plugin.removeTargetProgram(selectedItem.convertToTargetProgram());

    }

    public void addTargetWebsite(){
        FXTargetWebsite addedTargetWebsite = new FXTargetWebsite(targetWebsiteField.getText());
        fxTargetWebsiteObservableList.add(addedTargetWebsite);
        plugin.addTargetWebsite(addedTargetWebsite.convertToTargetWebsite());
    }

    public void deleteTargetWebsite(){

        FXTargetWebsite selectedItem = targetWebsiteTableView.getSelectionModel().getSelectedItem();
        targetWebsiteTableView.getItems().remove(selectedItem);
        plugin.removeTargetWebsite(selectedItem.convertToTargetWebsite());
    }

    public void applyToServer(){
        uiEventHandler.onPostUserPlugin(plugin.getPluginIdx());
    }

    public void schedulePlugin(){

//        시작시간 = 시스템 시간과 textField 긁어와서 Date타입 만들기
        Date startDate = dateFromInt(Integer.parseInt(startHourField.getText()), Integer.parseInt(startMinField.getText()));
//        종료시간 = 동일
        Date endDate = dateFromInt(Integer.parseInt(endHourField.getText()),Integer.parseInt(endMinField.getText()));

//        Date타입을 plugin에 등록한다.
        plugin.applySchedule(startDate, endDate);

//        텍스트필드를 라벨로 바꾼다
//        취소버튼 보이게 하기
        loadDateOnUI(startDate, endDate);
    }

    private void loadDateOnUI(Date startDate, Date endDate) {
        scheduleArea.setVisible(false);
        scheduleBtn.setVisible(false);
        modifyScheduleBtn.setVisible(true);
        scheduledTimeLbl.setVisible(true);

        String scheduledTime = startDate.toString()+ "부터 " + endDate.toString()+"까지";
        scheduledTimeLbl.setText(scheduledTime);
    }

    private Date dateFromInt(int hour, int minute){
        // 시스템 시간 구하기
        Date time = new Date();

        //calendar에 시스템시간 지정하기
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time.getTime());

        //시간 더하기
        cal.add(Calendar.HOUR, hour);
        cal.add(Calendar.MINUTE, minute);

        //Date타입으로 변환하기
        //반환하기
        Date formattedTime = cal.getTime();
        try {
            formattedTime = DateFormatter.getSimpleFormattedDateFromDate(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

    public void modifySchedule(){
        scheduleArea.setVisible(true);
        scheduleBtn.setVisible(true);
        modifyScheduleBtn.setVisible(false);
        scheduledTimeLbl.setVisible(false);

    }

    public void onLifeCycleChanged(){
        if(plugin.isRunning()==false){
            modifySchedule();
        }
    }

    public void setPlugin(EDBPlugin plugin) {
        this.plugin = plugin;
    }

    public int getPluginIdx(){
        return plugin.getPluginIdx();
    }

}
