package com.app.zoomapi.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Channels {
    private DatabaseHelper db = new DatabaseHelper();
    public void createNewTable(String dbName) {

        String sql = "CREATE TABLE IF NOT EXISTS channels (\n"
                + "	zoomClientId VARCHAR(50) NOT NULL,\n"
                + "	channelId VARCHAR(50) NOT NULL\n"
                + ");";

        try (Connection conn = db.connect(dbName)){
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String dbName, String zoomClientId, String channelId) {
        String sql = "INSERT INTO channels(zoomClientId,channelId) VALUES(?,?)";

        try (Connection conn = db.connect(dbName)){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, zoomClientId);
            pstmt.setString(2, channelId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
