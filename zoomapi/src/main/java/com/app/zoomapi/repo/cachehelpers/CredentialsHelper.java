package com.app.zoomapi.repo.cachehelpers;

import com.app.zoomapi.models.Credentials;
import com.app.zoomapi.repo.SQLiteGenericTableHandler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class CredentialsHelper {
    private SQLiteGenericTableHandler<Credentials> credentialsTableHandler = null;

    public CredentialsHelper(String path) throws SQLException {
        this.credentialsTableHandler = new SQLiteGenericTableHandler<>(path,Credentials::new);
    }

    public void insertCredentialsRecord(Credentials credentials) throws Exception {
        credentialsTableHandler.insertRow(credentials);
    }

    public Credentials getCredentialsRecordByUserId(String userId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"userId"});
        List<String> keys = Arrays.asList(new String[]{"'"+userId+"'"});
        return  (credentialsTableHandler.get(fields,keys).size()>0)? credentialsTableHandler.get(fields,keys).get(0):null;
    }

    public Credentials getCredentialsRecordByZoomClientId(String zoomClientId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomClientId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomClientId+"'"});
        return  (credentialsTableHandler.get(fields,keys).size()>0)? credentialsTableHandler.get(fields,keys).get(0):null;
    }

    public Credentials getCredentialsRecordByEmail(String email) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"email"});
        List<String> keys = Arrays.asList(new String[]{"'"+email+"'"});
        return  (credentialsTableHandler.get(fields,keys).size()>0)? credentialsTableHandler.get(fields,keys).get(0):null;
    }

    public void deleteCredentialsRecordByUserId(String userId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"userId"});
        List<String> keys = Arrays.asList(new String[]{String.valueOf(userId)});
        credentialsTableHandler.delete(fields,keys);
    }

    public void deleteCredentialsRecordByZoomClientId(String zoomClientId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomClientId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomClientId+"'"});
        credentialsTableHandler.delete(fields,keys);
    }

    public void deleteCredentialsRecordByEmail(String email) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"email"});
        List<String> keys = Arrays.asList(new String[]{"'"+email+"'"});
        credentialsTableHandler.delete(fields,keys);
    }
}
