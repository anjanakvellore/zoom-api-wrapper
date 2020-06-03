package com.app.zoomapi.repo.cachehelpers;

import com.app.zoomapi.models.Channels;
import com.app.zoomapi.repo.SQLiteGenericTableHandler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Offers table-specific functions for Channels table
 */
public class ChannelsHelper {
    private SQLiteGenericTableHandler<Channels> channelsTableHandler =  null;

    public ChannelsHelper(String path) throws SQLException {
        this.channelsTableHandler = new SQLiteGenericTableHandler<>(path,Channels::new);
    }

    /**
     * Inserts a list of channels in the Channels table
     */
    public void insertChannels(List<Channels> channels) throws Exception {
        for(Channels ch:channels){
            channelsTableHandler.insertRow(ch);
        }
    }

    /**
     * Gets all channels for a Zoom Client Id
     */
    public List<Channels> getChannelsByZoomClientId(String zoomClientId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomClientId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomClientId+"'"});
        return this.channelsTableHandler.get(fields,keys);
    }

    /**
     * Deletes all Channels by Zoom Client Id
     */
    public void deleteChannelsByZoomClientID(String zoomClientId) throws Exception{
        List<String> fields = Arrays.asList(new String[]{"zoomClientId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomClientId+"'"});
        channelsTableHandler.delete(fields,keys);
    }

    /**
     * Deletes all Channels by Channel Id
     */
    public void deleteChannelsByChannelId(int channelId) throws Exception{
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+channelId+"'"});
        channelsTableHandler.delete(fields,keys);
    }

    /**
     * Deletes all Channels by Zoom Client Id for a given Channel Id
     */
    public void deleteChannelsByZoomClientIdAndChannelId(String zoomClientId, int channelId) throws Exception{
        List<String> fields = Arrays.asList(new String[]{"zoomClientId","channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomClientId+"'","'"+channelId+"'"});
        channelsTableHandler.delete(fields,keys);
    }
}
