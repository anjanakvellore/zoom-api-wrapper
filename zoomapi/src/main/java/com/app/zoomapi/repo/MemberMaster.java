package com.app.zoomapi.repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MemberMaster {
    private DatabaseHelper db = new DatabaseHelper();

    //TODO check access privilege
    public void createNewTable(String dbName) {

        String sql = "CREATE TABLE IF NOT EXISTS memberMaster (\n"
                + "	memberId integer PRIMARY KEY,\n"
                + "	zoomMemberId VARCHAR(250) NOT NULL,\n"
                + "	email VARCHAR(250) NOT NULL\n"
                + "	firstName VARCHAR(250) NOT NULL\n"
                + "	lastName VARCHAR(250) NOT NULL\n"
                + "	role VARCHAR(20) NOT NULL\n"
                + ");";

        try (Connection conn = db.connect(dbName)){
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String dbName, String zoomMemberId, String email, String firstName,
                       String lastName, String role) {
        String sql = "INSERT INTO memberMaster(zoomMemberId,email,firstName," +
                "lastName,role) VALUES(?,?,?,?,?)";

        try (Connection conn = db.connect(dbName)){
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, zoomMemberId);
            pstmt.setString(2, email);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, role);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
