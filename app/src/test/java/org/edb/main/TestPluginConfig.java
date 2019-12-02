package org.edb.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TestPluginConfig extends PluginConfig {
    private int tempConfigInt;
    private String tempConfigStr;
    private ArrayList<String> tempConfigList;

    public TestPluginConfig(){
        tempConfigList = new ArrayList<String>();
    }


    @Override
    public void addSingleConfig(String attributeName, String attributeValue) {
        switch(attributeName){
            case "tempConfigInt":
                tempConfigInt = Integer.parseInt(attributeValue);
                break;
            case "tempConfigStr":
                tempConfigStr = attributeValue;
                break;
            case"tempConfigList":
                addToTempConfigList(attributeValue);
                break;
            default:
                System.out.println("error");
        }

    }

//    TODO arrayList가 원시자료형을 담지 않을 가능성 고려 필요.(개별 플러그인 개발자가 해야할 역할)

    private void addToTempConfigList(String attributeValue) {
        ArrayList<String> tempList = new ArrayList<String>(Arrays.asList(attributeValue.split(",")));
        tempConfigList.addAll(tempList);
    }

    @Override
    public void decodeFromMap(Map<String, String> decodeConfig) {

        tempConfigInt = Integer.parseInt(decodeConfig.get("tempConfigInt"));
        tempConfigStr = decodeConfig.get("tempConfigStr");

        tempConfigList = new ArrayList<String>(Arrays.asList(decodeConfig.get("tempConfigList").split(",")));
    }

    @Override
    public void removeSingleConfig(String singleConfig) {

    }

    @Override
    public void extractConfig(PluginConfigConverter pluginConfigConverter) {
        Map<String,String> attributesMap = new HashMap<String,String>();

        attributesMap.put("tempConfigInt",Integer.toString(tempConfigInt));
        attributesMap.put("tempConfigStr",tempConfigStr);


        pluginConfigConverter.addSingleConfig("TestPluginConfig", attributesMap);
    }

    private String convertListToSingleStr(){
        StringBuffer sbr = new StringBuffer();

        for (String str : tempConfigList) {
            sbr.append(str);
        }

        return sbr.toString();
    }
}
