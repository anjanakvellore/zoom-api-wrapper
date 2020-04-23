package com.app.zoomapi.utilities;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * all Utility functions are defined here
 */
public class Utility {

    /**
     * Require that the hashmap have the given keys
     * @param map hashmap to check
     * @param keys The keys to check hashmap for. This can either be a single
     *      *              string, or an iterable of strings
     * @return Throws exception if any one key is missing
     */
    public static void requireKeys(Map<String,?> map, List<String> keys) throws Exception {
        StringBuilder exceptionMessage = new StringBuilder();
        boolean allKeysPresent = true;
        for(String key:keys){
            if(!map.containsKey(key)){
                exceptionMessage.append(String.format("{0} must be set",key));
                exceptionMessage.append("\n");
                allKeysPresent = false;
            }
        }
        if(!allKeysPresent){
            throw new Exception(exceptionMessage.toString());
        }
    }

    /**
     * Convert date and datetime objects to a string. Note, this does not do any timezone conversion.
     * @param date The Date object to convert to a string
     * @return The string representation of the date
     */
    public static String dateToString(Date date){
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    /**
     * Create a new Zoom Client (Incomplete implementation)
     * @param key The Zooom.us API key
     * @param secret The Zooom.us API secret
     * @return
     */
    public static String generateJwt(String key, String secret){
        /* python equivalent:
        header = {"alg": "HS256", "typ": "JWT"}

        payload = {"iss": key, "exp": int(time.time() + 3600)}

        token = jwt.encode(payload, secret, algorithm="HS256", headers=header)
        return token.decode("utf-8")*/
        return null;
    }

    /**
     * Convert hashmap from Map<String,Object> to Map</String,String>
     * @param oldMap Map with Object as key value
     * @return new map with String as key value
     */
    public static Map<String,String> convertMap(Map<String,Object> oldMap){
        Map<String,String> newMap = new HashMap<>();
        for(Map.Entry<String,Object> entry:oldMap.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            newMap.put(key,String.valueOf(value));
        }
        return newMap;
    }
}
