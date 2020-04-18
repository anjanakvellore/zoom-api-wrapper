package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecordingComponent extends BaseComponent {
    private static RecordingComponent recordingComponent = null;

    private RecordingComponent(Map<String,String> config){
        super(config);
    }

    public static RecordingComponent getRecordingComponent(Map<String,String> config){
        if(recordingComponent== null){
            recordingComponent = new RecordingComponent(config);
        }
        return recordingComponent;
    }

    public HttpResponse<String> list(Map<String,String> pathMap,Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"user_id"});
        if(Utility.requireKeys(pathMap,reqKeys)){
            return getRequest(String.format("/users/%s/recordings",pathMap.get("user_id")),paramMap,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> get(Map<String,String> pathMap,Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"meeting_id"});
        if(Utility.requireKeys(pathMap,reqKeys)){
            return getRequest(String.format("/meetings/%s/recordings",pathMap.get("meeting_id")),paramMap,null);
        }
        else{
            return null;
        }
    }

    public HttpResponse<String> delete(Map<String,String> pathMap,Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"meeting_id"});
        if(Utility.requireKeys(pathMap,reqKeys)){
            return deleteRequest(String.format("/meetings/%s/recordings",pathMap.get("meeting_id")),paramMap,null,(String)null,null);
        }
        else{
            return null;
        }
    }
}
