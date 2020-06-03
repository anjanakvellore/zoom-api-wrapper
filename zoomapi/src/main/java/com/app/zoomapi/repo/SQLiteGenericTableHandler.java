package com.app.zoomapi.repo;

import com.app.zoomapi.repo.annotations.PrimaryKey;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.*;
import java.util.function.Supplier;

public class SQLiteGenericTableHandler<T> {

    private static Connection connection = null;
    private Class cls = null;
    private Supplier<T> supplier = null;
    Map<String,Field> fieldMap = null;

    public SQLiteGenericTableHandler(String fileName,Supplier<T> supplier) throws SQLException {
        this.supplier = supplier;
        this.cls = supplier.get().getClass();
        connection  = getConnection(fileName);
        Field[] fields = this.cls.getDeclaredFields();
        this.fieldMap = new LinkedHashMap<>();
        for(Field field:fields){
            this.fieldMap.put(field.getName(),field);
        }
        createTable();
    }


    private Connection getConnection(String fileName){
        String url = "jdbc:sqlite:"+ fileName;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        }
        catch (Exception ex){
            //TODO remove print!
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return conn;
    }

    private void createTable() throws SQLException {
        /*
        String sql = "CREATE TABLE IF NOT EXISTS channelMaster (\n"
                + "	channelId integer PRIMARY KEY,\n"
                + "	zoomChannelId VARCHAR(100) NOT NULL,\n"
                + "	channelName VARCHAR(250) NOT NULL\n"
                + "	channelType integer NOT NULL\n"
                + ");";
         */
        String sql = "CREATE TABLE IF NOT EXISTS "+this.cls.getSimpleName()+"(";
        List<Field> primaryKeyFields = new ArrayList<>();
        List<Field> fields = new ArrayList<>(this.fieldMap.values());
        for (int i = 0;i<fields.size();i++){
            Field field = fields.get(i);
            if(field.isAnnotationPresent(PrimaryKey.class)){
                primaryKeyFields.add(field);
            }
            sql = sql + field.getName() + " "+mapDataType(field.getType())+" ";
            if(i!=fields.size() - 1)
                sql = sql + ", ";
        }

        if(primaryKeyFields.size()>0){
            sql = sql + ", PRIMARY KEY(";
            for(int i=0;i<primaryKeyFields.size();i++){
                sql = sql + primaryKeyFields.get(i).getName();
                if(i!=primaryKeyFields.size()-1){
                    sql = sql + ",";
                }
            }
            sql = sql + ")";
        }
        sql = sql + ");";

        Statement stmt = connection.createStatement();
        stmt.execute(sql);

    }

    //ToDo: add more conditions for different data types
    public boolean insertRow(T row) throws Exception{
        List<String> str = Collections.nCopies(fieldMap.size(),"?");
        List<String> columnNames =new ArrayList<>(fieldMap.keySet());
        String sql = "INSERT INTO "+this.cls.getSimpleName()+" ("+String.join(",",columnNames)+") VALUES("+String.join(",",str)+")";
        PreparedStatement pstmt = connection.prepareStatement(sql);
        int i=1;
        for(String col:fieldMap.keySet()){
            Field field = fieldMap.get(col);
            field.setAccessible(true);
            if (field.getType().equals(int.class)) {
                pstmt.setInt(i,(int)field.get(row));
            }
            else if(field.getType().equals(String.class)){
                pstmt.setString(i,field.get(row).toString());
            }
            else if(field.getType().equals(Boolean.class)){
                pstmt.setBoolean(i,(boolean)field.get(row));
            }
            field.setAccessible(false);
            i++;
        }
        pstmt.executeUpdate();
        return true;
    }

    public List<T> get(List<String> fields,List<String> keys) throws NoSuchFieldException, IllegalAccessException, SQLException {
        if(fields.size()!=keys.size())
            return new ArrayList<T>();
        List<String> terms = new ArrayList<>();

        for(int i=0;i<fields.size();i++){
            terms.add(fields.get(i)+" = "+keys.get(i));
        }

        String where = String.join(" and ",terms);
        return get(where);
    }

    public List<T> get(String where) throws SQLException, NoSuchFieldException, IllegalAccessException {
        String query = "";
        if(where!=null)
            query = String.format("SELECT * FROM %s WHERE %s",this.cls.getSimpleName(),where);
        else
            query = String.format("SELECT * FROM %s",this.cls.getSimpleName());

        PreparedStatement stmt  = connection.prepareStatement(query);
        return  doQuery(stmt);
    }

    //ToDo: add more conditions for different data types
    private List<T> doQuery(PreparedStatement stmt) throws SQLException, IllegalAccessException {
        ResultSet resultSet = stmt.executeQuery();
        List<T> result = new ArrayList<>();
        while (resultSet.next()){
            T row = supplier.get();
            for(String col:fieldMap.keySet()) {
                Field field = fieldMap.get(col);
                field.setAccessible(true);
                if (field.getType().equals(int.class)) {
                    field.set(row, resultSet.getInt(col));
                } else if (field.getType().equals(String.class)) {
                    field.set(row, resultSet.getString(col));
                } else if (field.getType().equals(Boolean.class)) {
                    field.set(row, resultSet.getBoolean(col));
                }
                field.setAccessible(false);
            }
            result.add(row);

        }

        return result;

    }

    public boolean delete(List<String> fields, List<String> keys) throws SQLException {
        if(fields.size()!=keys.size())
            return false;

        List<String> terms = new ArrayList<>();
        for(int i=0;i<fields.size();i++){
            terms.add(fields.get(i)+" = "+keys.get(i));
        }

        String where = String.join(" and ",terms);
        String query = String.format("DELETE FROM %s WHERE %s",this.cls.getSimpleName(),where);
        return delete(query);

    }

    public boolean delete(String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
        return true;
    }

    private String mapDataType(Type type){
        if (int.class.equals(type)) {
            return "INTEGER";
        } else if (String.class.equals(type)) {
            return "VARCHAR(1000)";
        }
        else{
            return "String";
        }
    }

}


