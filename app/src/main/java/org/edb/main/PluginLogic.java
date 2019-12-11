package org.edb.main;

import org.edb.main.UI.SpecificConfigUIController;

import java.util.Date;
import java.util.List;
import java.util.Map;

public abstract class PluginLogic {
    protected String fxPath;
    protected String androidPath;

    public abstract void addSingleConfig(String attributeName, String attributeValue);

    public abstract void decodeFromMap(Map<String, String> decodeConfig);

    public abstract void removeSingleConfig(String singleConfig);

    public abstract void extractConfig(PluginConfigConverter pluginConfigConverter);

    public abstract void checkForLogic(EDBPlugin plugin,List<String> curWebsites, Date curTime);

    public abstract void initializeLogicBeforeStart();

    public String getFxPath() {
        return fxPath;
    }

    public abstract void addController(SpecificConfigUIController controller);

//    TODO 강한 UI의존성.
    public abstract void sendRequestFillUI();
}
