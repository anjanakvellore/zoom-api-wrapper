package com.app.zoomapi.repo.cachehelpers;

import com.app.zoomapi.models.Credentials;
import com.app.zoomapi.repo.SQLiteGenericTableHandler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Offers table-specific functions for Credentials table
 */
public class CredentialsHelper {
    private SQLiteGenericTableHandler<Credentials> credentialsTableHandler = null;

    public CredentialsHelper(String path) throws SQLException {
        this.credentialsTableHandler = new SQLiteGenericTableHandler<>(path,Credentials::new);
    }

    /**
     * Inserts Credentials records to the table
     */
    public void insertCredentialsRecord(Credentials credentials) throws SQLException, IllegalAccessException {
        credentialsTableHandler.insertRow(credentials);
    }

    /**
     * Gets Credential record by Zoom Client Id
     */
    public Credentials getCredentialsRecordByZoomClientId(String zoomClientId) throws IllegalAccessException, NoSuchFieldException, SQLException {
        List<String> fields = Arrays.asList(new String[]{"zoomClientId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomClientId+"'"});
        return  (credentialsTableHandler.get(fields,keys).size()>0)? credentialsTableHandler.get(fields,keys).get(0):null;
    }

    /**
     * Deletes Credentials record by Zoom Client Id
     */
    public void deleteCredentialsRecordByZoomClientId(String zoomClientId) throws SQLException {
        List<String> fields = Arrays.asList(new String[]{"zoomClientId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomClientId+"'"});
        credentialsTableHandler.delete(fields,keys);
    }
}
