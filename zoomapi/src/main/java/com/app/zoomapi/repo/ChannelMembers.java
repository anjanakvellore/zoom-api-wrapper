package com.app.zoomapi.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ChannelMembers {
    private DatabaseHelper db = new DatabaseHelper();

    public void createNewTable(String dbName) {

        String sql = "CREATE TABLE IF NOT EXISTS channelMembers (\n"
                + "	channelId VARCHAR(50) NOT NULL,\n"
                + "	memberId VARCHAR(50) NOT NULL\n"
                + ");";

        try (Connection conn = db.connect(dbName)){
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String dbName, String channelId, String memberId) {
        String sql = "INSERT INTO channelMembers(channelId,memberId) VALUES(?,?)";

        try (Connection conn = db.connect(dbName)){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, channelId);
            pstmt.setString(2, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
