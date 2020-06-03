package com.app.zoomapi.repo.cachehelpers;

import com.app.zoomapi.models.User;
import com.app.zoomapi.repo.SQLiteGenericTableHandler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class UserHelper {
    private SQLiteGenericTableHandler<User> userTableHandler = null;

    public UserHelper(String path) throws SQLException {
        this.userTableHandler = new SQLiteGenericTableHandler<>(path,User::new);
    }

    public void insertUserRecord(User user) throws Exception {
        userTableHandler.insertRow(user);
    }

    public User getUserRecordByZoomClientId(String zoomClientId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomClientId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomClientId+"'"});
        return  (userTableHandler.get(fields,keys).size()>0)? userTableHandler.get(fields,keys).get(0):null;
    }

    public User getUserRecordByEmail(String email) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"email"});
        List<String> keys = Arrays.asList(new String[]{"'"+email+"'"});
        return  (userTableHandler.get(fields,keys).size()>0)? userTableHandler.get(fields,keys).get(0):null;
    }

    public User getUserRecordByUserId(String userId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"userId"});
        List<String> keys = Arrays.asList(new String[]{"'"+userId+"'"});
        return  (userTableHandler.get(fields,keys).size()>0)? userTableHandler.get(fields,keys).get(0):null;
    }

    public void deleteUserRecordByUserId(String userId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"userId"});
        List<String> keys = Arrays.asList(new String[]{String.valueOf(userId)});
        userTableHandler.delete(fields,keys);
    }

    public void deleteUserRecordByEmail(String email) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"email"});
        List<String> keys = Arrays.asList(new String[]{"'"+email+"'"});
        userTableHandler.delete(fields,keys);
    }

    public void deleteUserRecordByZoomClientId(String zoomClientId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomClientId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomClientId+"'"});
        userTableHandler.delete(fields,keys);
    }
}
