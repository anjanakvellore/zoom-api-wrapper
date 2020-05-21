package com.app.zoomapi.repo;

import java.sql.*;

//TODO refactor -> zoomapi
public class DatabaseHelper {

   /* public DatabaseHelper(){
        ChannelMaster channelMaster = new ChannelMaster();
        channelMaster.createNewTable(dbName);
    }*/

    /**
     * Connect to a sample database
     * @param dbName the database file name
     */
    public static void createNewDatabase(String dbName) {
        try (Connection conn = connect(dbName)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection connect(String dbName) {
        // SQLite connection string

        //Windows
        //String url = "jdbc:sqlite:C:/sqlite/db/" + fileName;

        //Mac
        String url = "jdbc:sqlite:/Users/santhiyan/sqlite/"+dbName;

        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }


    /*public ChannelMaster getChannelMaster(){
        return channelMaster;
    }*/
}
