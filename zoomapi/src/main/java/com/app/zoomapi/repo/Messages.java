package com.app.zoomapi.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Messages {
    private DatabaseHelper db = new DatabaseHelper();

    //TODO check access privilege
    public void createNewTable(String dbName) {

        String sql = "CREATE TABLE IF NOT EXISTS messages (\n"
                + "	channelId INTEGER PRIMARY KEY,\n"
                + "	zoomMessageId VARCHAR(250) NOT NULL,\n"
                + "	message VARCHAR(1000) NOT NULL\n"
                + "	sender VARCHAR(250) NOT NULL\n"
                + "	messageDateTime VARCHAR(250) NOT NULL\n"
                + "	messageTimestamp INTEGER NOT NULL\n"
                + ");";

        try (Connection conn = db.connect(dbName)){
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String dbName, String channelId, String zoomMessageId, String message, String sender,
                       String messageDateTime, int messageTimestamp) {
        String sql = "INSERT INTO messages(channelId,zoomMessageId,message,sender," +
                "messageDateTime,messageTimestamp) VALUES(?,?,?,?,?,?)";

        try (Connection conn = db.connect(dbName)){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, channelId);
            pstmt.setString(2, zoomMessageId);
            pstmt.setString(3, message);
            pstmt.setString(4, messageDateTime);
            pstmt.setInt(3, messageTimestamp);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
