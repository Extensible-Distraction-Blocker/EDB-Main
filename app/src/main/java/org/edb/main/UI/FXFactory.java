package org.edb.main.UI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import org.edb.main.*;


public class FXFactory {
    private UIEventHandler uiEventHandler;
    private FXManipulator fxManipulator;
    private EDBPluginManager pluginManager;

    private static FXFactory fxFactory;

    public void setPluginManager(EDBPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }


    public static synchronized FXFactory getInstance(){
        if(fxFactory==null){
            fxFactory=new FXFactory();
        }
        return fxFactory;
    }

    private FXFactory() { }

    public void init(UIEventHandler uiEventHandler, FXManipulator fxManipulator){
        this.uiEventHandler=uiEventHandler;
        this.fxManipulator=fxManipulator;
    }

    public Parent loadMainUI(String path)throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(path));
        Parent parent =loader.load();

        loader.<MainUIController>getController().setPluginManager(pluginManager);
        loader.<MainUIController>getController().setUiEventHandler(uiEventHandler);
        loader.<MainUIController>getController().init();
        fxManipulator.setMainUIController(loader.getController());

        return parent;
    }

    public Parent loadLoginUI(String path)throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent parent = loader.load();

        loader.<LoginDialogController>getController().setUiEventHandler(uiEventHandler);
        fxManipulator.setLoginDialogController(loader.getController());

        return parent;

    }

    public Parent loadAvailableExternalServiceUI(String path)throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent parent = loader.load();

        loader.<AvailableExternalServiceListController>getController().setUiEventHandler(uiEventHandler);
        fxManipulator.setAvailableExternalServicelListController(loader.getController());
        loader.<AvailableExternalServiceListController>getController().init();

        return parent;
    }

    public Parent loadUserExternalServiceUI(String path) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent parent = loader.load();

        UserExternalServiceListController controller = loader.<UserExternalServiceListController>getController();
        controller.setUiEventHandler(uiEventHandler);
        fxManipulator.setUserExternalServicelListController(controller);
        controller.loadUserExternalServiceList();

        return parent;

    }

    public Parent loadPluginConfigUI(String path, int pluginIdx)throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent parent = loader.load();

        PluginConfigUIController controller = loader.<PluginConfigUIController>getController();
        controller.setUIEventHandler(uiEventHandler);
        controller.setPluginManager(pluginManager);
        controller.setPlugin(pluginManager.findEDBPlugin(pluginIdx));
        fxManipulator.setPluginConfigUIController(controller);
        controller.init();

        return parent;
    }

    public Parent loadSpecificPluginUIFromPluginLogic(PluginLogic logic)throws Exception{
//        logic과 같은 패키지 안에 있는 경로로부터 resource를 가져와야 한다.

        String path = logic.getFxPath();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
        Parent parent = loader.load();

        SpecificConfigUIController controller = loader.<SpecificConfigUIController>getController();
        controller.setPluginLogic(logic);
        logic.addController(controller);

        System.out.println("pluginConfigUI Loaded");
        return parent;
    }
}
