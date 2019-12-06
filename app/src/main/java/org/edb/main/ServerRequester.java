package org.edb.main;


import org.edb.main.network.JsonConverter;

public interface ServerRequester {

    public void requestAvailableExternalServices();
    public void requestUserExternalServices();
    public void requestExternalServiceDetails(int externalIdx);
    public void requestLogin(String id, String pw);

    public void requestUserPlugins();//load user plugin list
    public void requestAvailablePlugins();//load available plugin list
    public void requestPluginDetails(int pluginIdx);
    public void requestPostUserPlugin(int pluginIdx);

    public void setServerResponseHandler(ServerResponseHandler serverResponseHandler);


    public void requestSingup(String id, String pw,String email,String grade);

}
