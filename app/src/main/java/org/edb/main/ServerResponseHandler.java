package org.edb.main;

import org.edb.main.model.PluginModel;
import org.edb.main.network.JsonConverter;
import org.edb.main.network.TempJsonConverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

public class ServerResponseHandler {
    private UIManipulator uiManipulator;
    private EDBPluginManager pluginManager;
    private ServerRequester serverRequester;

    public void setPluginManager(EDBPluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    public ServerResponseHandler(UIManipulator uiManipulator) {
        this.uiManipulator = uiManipulator;
    }

    public void setServerRequester(ServerRequester serverRequester) {
        this.serverRequester = serverRequester;
    }

    public void handleLoginResponse(boolean successful, String id, String token) {
        if(successful) {
            User.login(id,token);
            User user = User.getUser();

            serverRequester.requestUserExternalServices();
//            serverRequester.requestPluginConfigs();
            uiManipulator.onLoginSuccessful(id);
        }
        else uiManipulator.onLoginUnsuccessful();
    }

    public void handleAvailableExternalServiceResponse(ArrayList<ExternalService> data) {
        uiManipulator.onResponseAvailableExternalServices(data);
    }

    public void handleUserExternalServiceResponse(ArrayList<ExternalService> data) {
        ExternalServiceManager.getExternalServiceManager().loadExternalServices(data);

        Set<Integer> externalIdxSets = ExternalServiceManager.getExternalServiceManager().getExternalServices().keySet();
        Iterator<Integer> externalIdxItr = externalIdxSets.iterator();

        while(externalIdxItr.hasNext()){
            serverRequester.requestExternalServiceDetails(externalIdxItr.next());
        }
    }

    public void handleExternalServiceDetailsResponse(int externalIdx, ArrayList<ExternalServiceDetail> data) {

        ExternalServiceManager.getExternalServiceManager().loadExternalServiceDetail(externalIdx,data);
    }


    // load plugin user list
    public void handleUserPluginResponse(ArrayList<PluginModel> data){uiManipulator.onResponseUserPlugins(data);}
    // load Available plugin  list
    public void handleAvailablePluginResponse(ArrayList<PluginModel> data) { uiManipulator.onResponseAvailablePlugins(data);}

    public void handlePluginDetailsResponse(int pluginIdx, PluginModel data, PluginConfigConverter pluginConfigConverter) {
        pluginManager.applyConfigsFromServer(pluginIdx, data, pluginConfigConverter);
//        uiManipulator.onResponsePluginDetails(pluginIdx, data);
//        ui적용은 ui에서의 요청이 있을때 pluginManager로부터 요청한다.
    }

    //serverResponseHandler.handlePostUserPluginResponse(sucessful,jsonConverter);
    //post configuration
    public void handlePostUserPluginResponse(int pluginIdx){

    }


    //회원가입
    public void handleSignupResponse(boolean success) {
        if(success){
            System.out.print("success!!!!");
        }
        uiManipulator.onResponseUserSignup(success);
    }

}
