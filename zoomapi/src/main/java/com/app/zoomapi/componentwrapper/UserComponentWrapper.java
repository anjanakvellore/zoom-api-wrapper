package com.app.zoomapi.componentwrapper;

import com.app.zoomapi.components.UserComponent;
import com.app.zoomapi.repo.cachehelpers.*;
import com.app.zoomapi.utilities.Utility;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.http.HttpResponse;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class UserComponentWrapper {

    private UserComponent userComponent = null;
    private static UserComponentWrapper userComponentWrapper = null;
    private CredentialsHelper credentialsHelper = null;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private UserComponentWrapper(UserComponent userComponent,String dbPath) throws SQLException {
        this.userComponent = userComponent;
        this.credentialsHelper = new CredentialsHelper(dbPath);
    }

    public static UserComponentWrapper getUserComponentWrapper(UserComponent userComponent, String dbPath) throws SQLException {
        if(userComponentWrapper == null){
            userComponentWrapper = new UserComponentWrapper(userComponent,dbPath);
        }
        return userComponentWrapper;
    }

    public HttpResponse<String> get(Map<String,Object> pathMap, Map<String,Object> initialParamMap){
        try{
            HttpResponse<String> response = this.userComponent.get(pathMap,initialParamMap);
            if(response.statusCode() == 200){
                JsonObject userResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                String userId = userResponse.get("id").getAsString();
                String firstName = userResponse.get("first_name").getAsString();
                String lastName = userResponse.get("last_name").getAsString();
                String email = userResponse.getAsJsonObject().get("email").getAsString();
                //credentialsHelper.insertCredentialsRecord();
            }
            return response;
        }
        catch(Exception ex){
            return Utility.getStringHttpResponse(400,ex.getMessage());
        }
    }

}
