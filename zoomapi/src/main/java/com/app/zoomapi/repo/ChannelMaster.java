package com.app.zoomapi.repo;

import java.sql.*;

public class ChannelMaster {
    private DatabaseHelper db = new DatabaseHelper();
    /*private static ChannelMaster channelMaster = null;

    public static ChannelMaster getChannelMaster(){
        if(channelMaster == null){
            channelMaster = new ChannelMaster();
        }
        return channelMaster;
    }*/

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
    }
}