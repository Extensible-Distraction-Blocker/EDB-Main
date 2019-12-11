package org.edb.main;

import org.edb.main.model.TargetProgram;
import org.edb.main.model.TargetWebsite;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TestEDBPlugin extends EDBPlugin {

    private static final String pluginName ="TestEDBPlugin";

    private static final int pluginIdx=1;

    public TestEDBPlugin(){
        pluginLogics = new HashMap<String, PluginLogic>();
        targetPrograms = new HashMap<String, TargetProgram>();
        targetWebsites = new HashMap<String, TargetWebsite>();
        TestPluginLogic testPluginConfig = new TestPluginLogic();
        pluginLogics.put("TestPluginLogic", testPluginConfig);

    }


    @Override
    public String getPluginConfigUIPath() {
        return null;
    }

    @Override
    public void renewTrackingTarget() {

    }

    @Override
    public void checkForLogics(List<String> curWebsites, Date curTime) {

    }

    @Override
    public int getPluginIdx() {
        return pluginIdx;
    }

    @Override
    public String getPluginName() {
        return pluginName;
    }

    @Override
    protected void onLifeCycleEnd() {

    }


    @Override
    protected void onLifeCycleStart() {

    }

}
