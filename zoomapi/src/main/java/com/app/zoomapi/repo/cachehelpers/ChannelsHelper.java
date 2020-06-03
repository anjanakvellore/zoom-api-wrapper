package com.app.zoomapi.repo.cachehelpers;

import com.app.zoomapi.models.Channels;
import com.app.zoomapi.repo.SQLiteGenericTableHandler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class ChannelsHelper {
    private SQLiteGenericTableHandler<Channels> channelsTableHandler =  null;

    public ChannelsHelper(String path) throws SQLException {
        this.channelsTableHandler = new SQLiteGenericTableHandler<>(path,Channels::new);
    }

    //get all channels of a particular client
    public List<Channels> getChannelsByZoomClientId(String zoomClientId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomClientId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomClientId+"'"});
        return this.channelsTableHandler.get(fields,keys);
    }

    //delete all channels of a particular client
    public void deleteChannelsByZoomClientID(String zoomClientId) throws Exception{
        List<String> fields = Arrays.asList(new String[]{"zoomClientId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomClientId+"'"});
        channelsTableHandler.delete(fields,keys);
    }

    //delete all records of a channelId
    public void deleteChannelsByChannelId(int channelId) throws Exception{
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+channelId+"'"});
        channelsTableHandler.delete(fields,keys);
    }

    public void deleteChannelsByZoomClientIdAndChannelId(String zoomClientId, int channelId) throws Exception{
        List<String> fields = Arrays.asList(new String[]{"zoomClientId","channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomClientId+"'","'"+channelId+"'"});
        channelsTableHandler.delete(fields,keys);
    }

    public void insertChannels(List<Channels> channels) throws Exception {
        for(Channels ch:channels){
            channelsTableHandler.insertRow(ch);
        }
    }


   /* private DatabaseHelper db = new DatabaseHelper();
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
    }*/
}
