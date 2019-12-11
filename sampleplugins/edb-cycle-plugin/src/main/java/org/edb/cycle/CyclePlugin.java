package org.edb.cycle;

import org.edb.main.EDBPlugin;
import org.edb.main.PluginLogic;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class CyclePlugin extends EDBPlugin {
    private static final int pluginIdx=2;
    private static final String pluginName="CyclePlugin";

    public String getPluginConfigUIPath() {
        return null;
    }

    public void renewTrackingTarget() {
//        TODO renewTrackingTarget의 용도?

    }

    public void checkForLogics( List<String> curWebsites, Date curTime) {
        if(isRunning) {
            for (Map.Entry<String, PluginLogic> singleLogic : pluginLogics.entrySet()) {
                singleLogic.getValue().checkForLogic(this, curWebsites, curTime);
            }
        }
    }

    public int getPluginIdx() {
        return pluginIdx;
    }

    public String getPluginName() {
        return pluginName;
    }

    protected void onLifeCycleEnd() {

    }

    protected void onLifeCycleStart() {
        CycleLogic cycleLogic = (CycleLogic)pluginLogics.get("CycleLogic");
        cycleLogic.initializeLogicBeforeStart();
}
}
