package org.edb.main;

import org.edb.main.Platform.OSNativeExecutor;
import org.edb.main.Platform.WindowsNativeExecutor;
import org.edb.main.model.PluginModel;
import org.edb.main.model.TargetProgram;
import org.edb.main.samplePlugin.cycle.CyclePlugin;
import org.edb.main.util.DateFormatter;
import org.pf4j.DefaultPluginManager;
import org.pf4j.PluginManager;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EDBPluginManager {

    private Map<Integer,EDBPlugin> plugins;
    private UIManipulator manipulator;


    public void setManipulator(UIManipulator manipulator) {
        this.manipulator = manipulator;
    }

    public EDBPluginManager(){
        plugins = new HashMap<Integer, EDBPlugin>();
        loadPlugins();
    }

    private void loadPlugins() {
//        loadPluginsWithPF4J();
        loadSamplePlugin();
    }

    private void loadSamplePlugin() {
        CyclePlugin cyclePlugin = new CyclePlugin();
        plugins.put(cyclePlugin.getPluginIdx(),cyclePlugin);
    }

    private void loadPluginsWithPF4J() {
        PluginManager pluginManager = new DefaultPluginManager();
        List<EDBPlugin> pluginList = pluginManager.getExtensions(EDBPlugin.class);
        System.out.println(String.format("Found %d extensions for extension point", pluginList.size()));
        for (EDBPlugin plugin :
                pluginList) {
            plugins.put(plugin.getPluginIdx(),plugin);
        }
    }

    public String findPluginConfigUIPath(Integer pluginIdx){

        return plugins.get(pluginIdx).getPluginConfigUIPath();
    }

    public void collectConfigs(int pluginIdx, PluginConfigConverter pluginConfigConverter) {

        EDBPlugin collectTargetPlugin = plugins.get(pluginIdx);
        collectTargetPlugin.extractConfigs(pluginConfigConverter);
    }

    public void applyConfigsFromServer(int pluginIdx, PluginModel data, PluginConfigConverter pluginConfigConverter){
        plugins.get(pluginIdx).decodeConfigs(data,pluginConfigConverter);
    }

//    TODO scan을 호출해주는 곳이 없다.
//    별도의 스레드 만들어서 순회해줘야 하나?
    public void scan(){
        Date curTime = new Date();
        try {
            curTime = DateFormatter.getSimpleFormattedDateFromDate(curTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        OSNativeExecutor osNativeExecutor = new WindowsNativeExecutor();
        List<String> curPrograms = osNativeExecutor.getCurPrograms();
        List<String> curWebsites = osNativeExecutor.getCurWebsites();
        for (EDBPlugin singlePlugin :
                plugins.values()) {
            boolean cycleChanged = singlePlugin.checkLifeCycle(curTime);
            if(cycleChanged){
//                TODO 주석처리됨
//                manipulator.onPluginLifeCycleChanged(singlePlugin.getPluginIdx());
            }
            singlePlugin.checkForLogics(curWebsites,curTime);
        }
    }

    public EDBPlugin findEDBPlugin(int pluginIdx){
        return plugins.get(pluginIdx);
    }

    public Map<Integer, EDBPlugin> getPlugins() {
        return plugins;
    }
}
