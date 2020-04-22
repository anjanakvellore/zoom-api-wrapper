package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import javax.print.DocFlavor;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Zoom.us REST API Java client - Report Component
 * Component dealing with all report related matters
 */
public class ReportComponent extends BaseComponent{
    private static ReportComponent reportComponent = null;

    private ReportComponent(Map<String,String> config){
        super(config);
    }

    public static ReportComponent getReportComponent(Map<String,String> config){
        if(reportComponent== null){
            reportComponent = new ReportComponent(config);
        }
        return reportComponent;
    }

    public HttpResponse<String> getUserReport(Map<String,String> pathMap, Map<String,String> paramMap){
        List<String> pathKeys = Arrays.asList(new String[]{"user_id"});
        List<String> paramKeys = Arrays.asList(new String[]{"from","to"});
        if(Utility.requireKeys(pathMap,pathKeys) && (Utility.requireKeys(paramMap,paramKeys))){
            paramMap.put("from","start_time");
            paramMap.remove("start_time");
            paramMap.put("to","end_time");
            paramMap.remove("end_time");
            return getRequest(String.format("/report/users/%s/meetings",pathMap.get("user_id")),paramMap,null);
        }
        else{
            return null;
        }
    }

    //TODO start_time/end_time for Date to string?
    public HttpResponse<String> getAccountReport(Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"start_time","end_time"});
        if(Utility.requireKeys(paramMap,reqKeys)){
            paramMap.put("from","start_time");
            paramMap.remove("start_time");
            paramMap.put("to","end_time");
            paramMap.remove("end_time");
            return getRequest("/report/users",paramMap,null);
        }
        else{
            return null;
        }
    }
}