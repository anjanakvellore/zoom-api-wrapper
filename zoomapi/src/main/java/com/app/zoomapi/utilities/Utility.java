package com.app.zoomapi.utilities;


import java.util.List;
import java.util.Map;

//all utility functions can be defined here
public class Utility {
    public static boolean requireKeys(Map<String,Object> map, List<String> keys){
        StringBuilder exceptionMessage = new StringBuilder();
        boolean allKeysPresent = true;
        for(String key:keys){
            if(!map.containsKey(key)){
                exceptionMessage.append(String.format("{0} must be set",key));
                exceptionMessage.append("\n");
                allKeysPresent = false;
            }
        }
        if(allKeysPresent){
            return true;
        }
        else{
            System.out.println(exceptionMessage.toString());
            return false;
        }

    }

}
