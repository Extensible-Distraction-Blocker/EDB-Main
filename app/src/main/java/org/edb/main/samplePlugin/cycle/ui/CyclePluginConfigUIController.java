package org.edb.main.samplePlugin.cycle.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.edb.main.PluginLogic;
import org.edb.main.UI.SpecificConfigUIController;
import org.edb.main.samplePlugin.cycle.CycleLogic;

import java.net.URL;
import java.util.ResourceBundle;

public class CyclePluginConfigUIController implements SpecificConfigUIController {
//    default width가 463이다.

    private CycleLogic cycleLogic;

    @FXML
    private TextField focusCycleField;
    @FXML
    private TextField restCycleField;
    @FXML
    private TextField wildCardField;
    @FXML
    private Label remainingWildCardLbl;
    @FXML
    private Label curModeLbl;
    @FXML
    private Label nextCycleTimeLbl;
    @FXML
    private Button wildCardBtn;
    @FXML
    private Label focusCycleLbl;
    @FXML
    private Label restCycleLbl;
    @FXML
    private Label wildCardLimitLbl;


    public void applyTimeConfig(){
        applyFocusCycleConfig();
        applyRestCycleConfig();
        applyWildCardConfig();
        loadFromLogic();
    }

    private void applyFocusCycleConfig() {
        String strInFocusCycleField = focusCycleField.getText();
        int focusCycle = Integer.parseInt(strInFocusCycleField);
        cycleLogic.setFocusCycle(focusCycle);

    }

    private void applyRestCycleConfig() {
        String strInRestCycleField = restCycleField.getText();
        int restCycle = Integer.parseInt(strInRestCycleField);
        cycleLogic.setRestCycle(restCycle);
    }

    private void applyWildCardConfig() {
        String strInWildCardCycleField = wildCardField.getText();
        int wildCardLimit = Integer.parseInt(strInWildCardCycleField);
        cycleLogic.setWildCardLimit(wildCardLimit);
    }

    public void setPluginLogic(PluginLogic logic) {

    }

    public void onPluginStart() {
        renewCurStateUI();
        wildCardBtn.setVisible(true);
    }

    public void renewUI() {
        renewCurStateUI();
    }


    private void renewCurStateUI(){
        if(cycleLogic.isWildCardUsed()){
            remainingWildCardLbl.setText("사용불가");
        }
        else{
            remainingWildCardLbl.setText(Integer.toString(cycleLogic.getWildCardLimit())+"분");
        }
        nextCycleTimeLbl.setText(cycleLogic.getFinishingTime().toString());
        curModeLbl.setText(cycleLogic.getCurMode().toString());
    }

    public void onPluginFinished() {
//        현재상황 UI들 실행되지 않음으로 초기화
        remainingWildCardLbl.setText("실행되지않음");
        curModeLbl.setText("실행되지않음");
        nextCycleTimeLbl.setText("실행되지않음");
        wildCardBtn.setVisible(false);
    }

    public void onWildCardBtnClicked(){
        if(cycleLogic.isWildCardUsed()==false){
            cycleLogic.useWildCard();
        }
    }

    public void onSpecificConfigUILoaded() {
        loadFromLogic();
    }

    private void loadFromLogic() {
        focusCycleLbl.setText(Integer.toString(cycleLogic.getFocusCycle()));
        restCycleLbl.setText(Integer.toString(cycleLogic.getRestCycle()));
        wildCardLimitLbl.setText(Integer.toString(cycleLogic.getWildCardLimit()));
    }
}
