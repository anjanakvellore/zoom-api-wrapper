package com.app.zoomapi.repo.cachehelpers;

import com.app.zoomapi.models.ChannelMaster;
import com.app.zoomapi.models.Channels;
import com.app.zoomapi.repo.SQLiteGenericTableHandler;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class ChannelMasterHelper {

    private SQLiteGenericTableHandler<ChannelMaster> channelMasterTableHandler = null;

    public ChannelMasterHelper(String path) throws SQLException {
        this.channelMasterTableHandler = new SQLiteGenericTableHandler<>(path,ChannelMaster::new);
    }

    public void deleteChannelMasterRecordsByZoomClientId(List<Channels> channelsList) throws SQLException {
        for(Channels ch:channelsList){
            int cid = ch.getChannelId();
            List<String> fields = Arrays.asList(new String[]{"channelId"});
            List<String> keys = Arrays.asList(new String[]{String.valueOf(cid)});
            channelMasterTableHandler.delete(fields,keys);
        }
    }

    public void insertChannelMasterRecords(List<ChannelMaster> channelMasterList) throws Exception {
        for (ChannelMaster ch:channelMasterList){
            channelMasterTableHandler.insertRow(ch);
        }
    }

    public ChannelMaster getChannelMasterRecordByChannelId(int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{String.valueOf(channelId)});
        return channelMasterTableHandler.get(fields, keys).get(0);
    }


    public void deleteChannelMasterRecordByZoomChannelId(String zoomChannelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomChannelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomChannelId+"'"});
        channelMasterTableHandler.delete(fields,keys);
    }

    public void insertChannelMasterRecord(ChannelMaster channelMaster) throws Exception {
        channelMasterTableHandler.insertRow(channelMaster);
    }

    public ChannelMaster getChannelMasterRecordByZoomChannelId(String zoomChannelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomChannelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomChannelId+"'"});
        return  (channelMasterTableHandler.get(fields,keys).size()>0)? channelMasterTableHandler.get(fields,keys).get(0):null;
    }



    /*private DatabaseHelper db = new DatabaseHelper();
    *//*private static ChannelMaster channelMaster = null;

    public static ChannelMaster getChannelMaster(){
        if(channelMaster == null){
            channelMaster = new ChannelMaster();
        }
        return channelMaster;
    }*//*

    //TODO check access privilege
    public void createNewTable(String dbName) {

        String sql = "CREATE TABLE IF NOT EXISTS channelMaster (\n"
                + "	channelId integer PRIMARY KEY,\n"
                + "	zoomChannelId VARCHAR(100) NOT NULL,\n"
                + "	channelName VARCHAR(250) NOT NULL\n"
                + "	channelType integer NOT NULL\n"
                + ");";

        try (Connection conn = db.connect(dbName)){
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String dbName, String zoomChannelId, String channelName, int channelType) {
        String sql = "INSERT INTO channelMaster(zoomChannelId,channelName,channelType) VALUES(?,?,?)";

        try (Connection conn = db.connect(dbName)){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, zoomChannelId);
            pstmt.setString(2, channelName);
            pstmt.setInt(3, channelType);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }*/
}