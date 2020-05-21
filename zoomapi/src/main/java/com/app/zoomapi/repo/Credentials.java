package com.app.zoomapi.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Credentials {
    private DatabaseHelper db = new DatabaseHelper();
    public void createNewTable(String dbName) {

        //TODO check timestamp string
        String sql = "CREATE TABLE IF NOT EXISTS credentials (\n"
                + "	zoomClientId VARCHAR(50) NOT NULL,\n"
                + "	OauthToken VARCHAR(1000) NOT NULL\n"
                + "	refreshToken VARCHAR(1000) NOT NULL\n"
                + "	loginTime VARCHAR(50) NOT NULL\n"
                + ");";

        try (Connection conn = db.connect(dbName)){
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String dbName, String zoomClientId, String OauthToken,
                       String refreshToken, String loginTime) {
        String sql = "INSERT INTO credentials(zoomClientId,OauthToken,refreshToken,loginTime) " +
                "VALUES(?,?,?,?)";

        try (Connection conn = db.connect(dbName)){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, zoomClientId);
            pstmt.setString(2, OauthToken);
            pstmt.setString(3, refreshToken);
            pstmt.setString(4, loginTime);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
