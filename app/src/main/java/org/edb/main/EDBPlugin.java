package org.edb.main;

import org.edb.main.model.PluginModel;
import org.edb.main.model.TargetProgram;
import org.edb.main.model.TargetWebsite;
import org.edb.main.util.DateFormatter;
import org.pf4j.ExtensionPoint;

import java.text.ParseException;
import java.util.*;

public abstract class EDBPlugin implements ExtensionPoint {
    protected Map<String, PluginLogic> pluginLogics;
    protected Map<String, TargetProgram> targetPrograms;
    protected Map<String, TargetWebsite> targetWebsites;

    protected Date startDate;

    protected Date endDate;

    protected boolean isRunning=false;


    public abstract String getPluginConfigUIPath();

    public abstract void renewTrackingTarget();
    public abstract void checkForLogics(List<String> curWebsites, Date curTime);

    public abstract int getPluginIdx();
    public abstract String getPluginName();
    protected abstract void onLifeCycleEnd();
    protected abstract void onLifeCycleStart();

    public EDBPlugin(){
        pluginLogics = new HashMap<String,PluginLogic>();
        targetPrograms = new HashMap<String, TargetProgram>();
        targetWebsites = new HashMap<String, TargetWebsite>();
        Date date= new Date();
        startDate=date;
        endDate=date;
    }

    public void startPluginTime(){
        onLifeCycleStart();
        isRunning=true;
    }

    public void endPluginTime(){
        onLifeCycleEnd();
        isRunning=false;
    }

    public  void extractConfigs(PluginConfigConverter pluginConfigConverter){
        pluginConfigConverter.setSchedule(startDate, endDate);
        pluginConfigConverter.setTargetPrograms(targetPrograms);
        pluginConfigConverter.setTargetWebsites(targetWebsites);
        for (Map.Entry<String, PluginLogic> entry : pluginLogics.entrySet()){
            entry.getValue().extractConfig(pluginConfigConverter);
        }
    }


    public boolean checkLifeCycle(Date curTime){
        boolean lifeCycleChanged=false;
        if(isRunning) {
            if (curTime.compareTo(endDate) == -1) {
                endPluginTime();
                lifeCycleChanged=true;
            }
        }
        else {
            if(curTime.compareTo(startDate)==1){
                startPluginTime();
                lifeCycleChanged=true;
            }
        }
        return lifeCycleChanged;
    }

    public void decodeConfigs(PluginModel data, PluginConfigConverter pluginConfigConverter){
        Date from = new Date();
        Date to = new Date();
        try {
            from = DateFormatter.getSimpleFormattedDateFromString(data.getStart_time());
            to = DateFormatter.getSimpleFormattedDateFromString(data.getEnd_time());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        this.startDate = from;
        this.endDate = to;

        pluginConfigConverter.convertStrConfigs(data.getConfiguration());
        targetPrograms = pluginConfigConverter.getTargetPrograms();
        targetWebsites = pluginConfigConverter.getTargetWebsites();
        Map<String,Map<String,String>> strConfigsMap = pluginConfigConverter.getPluginConfigMap();

        for (Map.Entry<String, Map<String,String>> entry : strConfigsMap.entrySet()) {
            pluginLogics.get(entry.getKey()).decodeFromMap(entry.getValue());
        }
    }

    public void applySchedule(Date startDate, Date endDate){
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void addTargetProgram(TargetProgram targetProgram) {
        targetPrograms.put(targetProgram.getTargetName(),targetProgram);
    }

    public void removeTargetProgram(TargetProgram targetProgram) {
        targetPrograms.remove(targetProgram.getTargetName());
    }

    public void addTargetWebsite(TargetWebsite targetWebsite) {
        targetWebsites.put(targetWebsite.getTargetURL(),targetWebsite);
    }

    public void removeTargetWebsite(TargetWebsite targetWebsite) {
        targetWebsites.remove(targetWebsite.getTargetURL());
    }

    public void applySingleConfig(String configName, String attributeName, String config){
        pluginLogics.get(configName).addSingleConfig(attributeName,config);
    }

    public Map<String, TargetProgram> getTargetPrograms() {
        return targetPrograms;
    }

    public Map<String, TargetWebsite> getTargetWebsites() {
        return targetWebsites;
    }

    public PluginLogic getSinglePluginLogicFromClassName(String logicName){
        return pluginLogics.get(logicName);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Map<String, PluginLogic> getPluginLogics() {
        return pluginLogics;
    }

}

