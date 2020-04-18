package com.app.zoomapi.utilities;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;
import java.util.List;
import java.util.Map;

//all utility functions can be defined here
public class Utility {
    public static boolean requireKeys(Map<String,?> map, List<String> keys){
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

    public static String dateToString(Date date){
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }


}
