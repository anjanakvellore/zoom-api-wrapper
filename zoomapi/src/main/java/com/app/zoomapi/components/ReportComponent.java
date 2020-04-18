package com.app.zoomapi.components;

import com.app.zoomapi.utilities.Utility;

import javax.print.DocFlavor;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

    //check with Kaj if we need to include start_time/end_time or should it be passed from client?
    public HttpResponse<String> getUserReport(Map<String,String> pathMap, Map<String,String> paramMap){
        List<String> reqKeys = Arrays.asList(new String[]{"user_id"});
        if(Utility.requireKeys(pathMap,reqKeys)){
            return getRequest(String.format("/report/users/%s/meetings",pathMap.get("user_id")),paramMap,null);
        }
        else{
            return null;
        }
    }

    //include start_time/end_time or should it be passed from client?
    public HttpResponse<String> getAccountReport(Map<String,String> paramMap){
        return getRequest("/report/users",paramMap,null);
    }


}
