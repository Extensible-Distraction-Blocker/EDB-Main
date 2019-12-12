package org.edb.main.samplePlugin.cycle;

import org.edb.main.samplePlugin.cycle.ui.CyclePluginConfigUIController;
import org.edb.main.EDBPlugin;
import org.edb.main.Platform.WindowsNativeExecutor;
import org.edb.main.PluginConfigConverter;
import org.edb.main.PluginLogic;
import org.edb.main.UI.SpecificConfigUIController;
import org.edb.main.model.TargetProgram;
import org.edb.main.model.TargetWebsite;
import org.edb.main.util.DateFormatter;

import java.text.ParseException;
import java.util.*;

public class CycleLogic extends PluginLogic {

    private int focusCycle=30;
//    min단위
    private int restCycle=5;
    private int wildCardLimit=15;
    private boolean wildCardUsed=false;
    private Date finishingTime;
    private CycleMode curMode;
    private List<Integer> targetExternalServices;
//    TODO UI의존성.
    private CyclePluginConfigUIController controller;

    public CycleLogic() {
        this.targetExternalServices = new ArrayList<Integer>();
        fxPath = "cycleLogicUI.fxml";
    }

    public void addSingleConfig(String attributeName, String attributeValue) {
//        TODO addSingleConfig 삭제 고려,
//        어차피 특정 pluginConfigController는 각 플러그인에 강한 결합되므로 getter, setter로 대체가능할듯.

    }

    public void decodeFromMap(Map<String, String> decodeConfig) {

        focusCycle = Integer.parseInt(decodeConfig.get("focusCycle"));
        restCycle = Integer.parseInt(decodeConfig.get("restCycle"));
        wildCardLimit = Integer.parseInt(decodeConfig.get("wildCardLimit"));

        try {
            finishingTime = DateFormatter.getSimpleFormattedDateFromString(decodeConfig.get("finishingTime"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        curMode = CycleMode.valueOf(decodeConfig.get("curMode"));

        List<String> strTargetIndexes = Arrays.asList(decodeConfig.get("tempConfigList").split(","));
        targetExternalServices = new ArrayList<Integer>();
        for (String strIndex:strTargetIndexes) {
            Integer tempInteger = new Integer(strIndex);
            targetExternalServices.add(tempInteger);
        }
    }

    public void removeSingleConfig(String singleConfig) {
//        TODO removeSingleConfig 삭제 고려
    }

    public void removeFromTargetExternalServices(int targetExternalIdx){
        targetExternalServices.remove(new Integer(targetExternalIdx));
    }

    public void extractConfig(PluginConfigConverter pluginConfigConverter) {
        Map<String,String> attributesMap = new HashMap<String,String>();

        attributesMap.put("focusCycle",Integer.toString(focusCycle));
        attributesMap.put("restCycle",Integer.toString(restCycle));
        attributesMap.put("wildCardLimit",Integer.toString(wildCardLimit));

        attributesMap.put("finishingTime",DateFormatter.getSimpleFormattedStringFromDate(finishingTime));
        attributesMap.put("curMode",curMode.toString());
        attributesMap.put("targetExternalServices",removeBracket(targetExternalServices.toString()));

        pluginConfigConverter.addSingleConfig("CycleLogic", attributesMap);
    }

    private String removeBracket(String string) {
        if(string.startsWith("[")){
            return string.substring(1,string.length()-1);
        }
        return string;
    }

    @Override
    public void checkForLogic(EDBPlugin plugin, List<String> curWebsites, Date curTime) {
        checkAndChangeMode(curTime);
        if(curMode == CycleMode.FOCUS){
            checkForPrograms(plugin.getTargetPrograms());
            checkForWebsites(plugin.getTargetWebsites(), curWebsites);
        }
    }

    private void checkAndChangeMode(Date curTime) {
//        이름자체로부터, 메소드가 2개의 역할을 한다는 암시.. 리팩토링 사항

        int nextCycle;
//        curTime이 Date보다 뒤라면 즉, curTime이 Date보다 크다면
        if(curTime.compareTo(finishingTime)==1){
            if(curMode==CycleMode.FOCUS){
                curMode = CycleMode.REST;
                nextCycle = restCycle;
                renewDate(nextCycle);
            }
            else{
                curMode = CycleMode.FOCUS;
                nextCycle = focusCycle;
                renewDate(nextCycle);
            }
            controller.renewUI();
        }
    }


    private void checkForPrograms(Map<String, TargetProgram> targetPrograms) {
        ArrayList<String> targetProgramList = new ArrayList<String>(targetPrograms.keySet());
        WindowsNativeExecutor nativeExecutor = new WindowsNativeExecutor();
        nativeExecutor.taskKillWithProcessNames(targetProgramList);

//        TODO WindowNativeExecutor 의존성 제거
//        OSNativeExecutor에게 일치하는 프로그램 종료 요청한다.(static?)
//        전달 매개변수는 targetProgram의 리스트
    }

    private void checkForWebsites(Map<String, TargetWebsite> targetWebsites, List<String> curWebsites) {
        List<String> commonPrograms = new ArrayList(curWebsites);
        commonPrograms.retainAll(targetWebsites.keySet());
//        TODO OSNativeExecutor 개발 후 일치 웹사이트 종료요청
//        OSNativeExecutor에게 일치하는 웹사이트 종료 요청한다.(static?)
//        전달 매개변수는 targetWebsite의 리스트 (나중에 정의 변경될 가능성 예방하여 String으로는 보내지 않는다)
    }

    public void initializeLogicBeforeStart() {
        setCurMode(CycleMode.FOCUS);
        renewDate(focusCycle);
        controller.onPluginStart();
    }

    public void addController(SpecificConfigUIController controller) {
        this.controller = (CyclePluginConfigUIController)controller;
    }

    public void sendRequestFillUI() {
        controller.onSpecificConfigUILoaded();
    }

    public void renewDate(int nextCycle) {
        Date curTime = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(curTime);
        cal.add(Calendar.MINUTE,nextCycle);
        finishingTime = cal.getTime();
    }

    public void useWildCard(){
        wildCardUsed = true;
        curMode = CycleMode.WILDCARD;
        renewDate(wildCardLimit);
        wildCardLimit=0;
        controller.renewUI();
    }

//
//    setter
//
    public void setFocusCycle(int focusCycle) {
        this.focusCycle = focusCycle;
    }

    public void setRestCycle(int restCycle) {
        this.restCycle = restCycle;
    }

    public void setWildCardLimit(int wildCardLimit) {
        this.wildCardLimit = wildCardLimit;
    }

    public void setFinishingTime(Date finishingTime) {
        this.finishingTime = finishingTime;
    }

    public void setCurMode(CycleMode curMode) {
        this.curMode = curMode;
    }

    public void addToTargetExternalServices(Integer targetExternalIdx) {
        targetExternalServices.add(targetExternalIdx);
    }

//
//    getter
//
    public int getFocusCycle() {
        return focusCycle;
    }

    public int getRestCycle() {
        return restCycle;
    }

    public int getWildCardLimit() {
        return wildCardLimit;
    }

    public Date getFinishingTime() {
        return finishingTime;
    }

    public CycleMode getCurMode() {
        return curMode;
    }

    public List<Integer> getTargetExternalServices() {
        return targetExternalServices;
    }

    public boolean isWildCardUsed() {
        return wildCardUsed;
    }

}
