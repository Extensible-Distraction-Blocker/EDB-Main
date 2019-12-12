package org.edb.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ExternalServiceManager {
    private static ExternalServiceManager externalServiceManager;

    private Map<Integer, ExternalService> externalServices;
    private Map<Integer, Iterable<ExternalServiceDetail>> externalServiceDetails;


    private ExternalServiceManager(){
        externalServices=Collections.synchronizedMap(new HashMap<Integer,ExternalService>());
        externalServiceDetails=Collections.synchronizedMap(new HashMap<Integer,Iterable<ExternalServiceDetail>>());
    }

    public synchronized static ExternalServiceManager getExternalServiceManager(){
        if(externalServiceManager==null){
            externalServiceManager= new ExternalServiceManager();
        }

        return externalServiceManager;
    }

    public Map<Integer,ExternalService> getExternalServices(){
        return Collections.unmodifiableMap(externalServices);
    }

    public Iterable<ExternalServiceDetail> getExternalServiceDetail(int externalIdx){
        return externalServiceDetails.get(externalIdx);
    }

    public Map<Integer,Iterable<ExternalServiceDetail>> getExternalServiceDetails(){
        return Collections.unmodifiableMap(externalServiceDetails);
    }


    public void loadExternalServices(Iterable<ExternalService> data){
        for (ExternalService externalService:data) {
            externalServices.put(externalService.getExternal_service_idx(),externalService);
        }
    }

    public void loadExternalServiceDetail(int externalIdx, Iterable<ExternalServiceDetail> data){
        System.out.println("loaded external details "+externalIdx);
        System.out.println(data.toString());

        externalServiceDetails.put(externalIdx,data);
    }

}
