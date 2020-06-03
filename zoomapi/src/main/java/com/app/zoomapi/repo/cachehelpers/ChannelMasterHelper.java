package com.app.zoomapi.repo.cachehelpers;

import com.app.zoomapi.models.ChannelMaster;
import com.app.zoomapi.models.Channels;
import com.app.zoomapi.repo.SQLiteGenericTableHandler;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

/**
 * Offers table-specific functions for ChannelMaster table
 */
public class ChannelMasterHelper {

    private SQLiteGenericTableHandler<ChannelMaster> channelMasterTableHandler = null;

    public ChannelMasterHelper(String path) throws SQLException {
        this.channelMasterTableHandler = new SQLiteGenericTableHandler<>(path,ChannelMaster::new);
    }

    /**
     * Inserts a ChannelMaster record to the table
     */
    public void insertChannelMasterRecord(ChannelMaster channelMaster) throws Exception {
        channelMasterTableHandler.insertRow(channelMaster);
    }

    /**
     * Inserts list of ChannelMaster records to the table
     */
    public void insertChannelMasterRecords(List<ChannelMaster> channelMasterList) throws Exception {
        for (ChannelMaster ch:channelMasterList){
            channelMasterTableHandler.insertRow(ch);
        }
    }

    /**
     * Gets ChannelMaster records by zoom channel Id
     */
    public ChannelMaster getChannelMasterRecordByZoomChannelId(String zoomChannelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomChannelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomChannelId+"'"});
        return  (channelMasterTableHandler.get(fields,keys).size()>0)? channelMasterTableHandler.get(fields,keys).get(0):null;
    }

    /**
     * Gets ChannelMaster records by Channel Id
     */
    public ChannelMaster getChannelMasterRecordByChannelId(int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{String.valueOf(channelId)});
        return channelMasterTableHandler.get(fields, keys).get(0);
    }

    /**
     * Deletes ChannelMaster records by the channel Id passed by a Zoom Client
     */
    public void deleteChannelMasterRecordsByZoomClientId(List<Channels> channelsList) throws SQLException {
        for(Channels ch:channelsList){
            int cid = ch.getChannelId();
            List<String> fields = Arrays.asList(new String[]{"channelId"});
            List<String> keys = Arrays.asList(new String[]{String.valueOf(cid)});
            channelMasterTableHandler.delete(fields,keys);
        }
    }

    /**
     * Deletes ChannelMaster records of a particular zoom channel Id
     */
    public void deleteChannelMasterRecordByZoomChannelId(String zoomChannelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomChannelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomChannelId+"'"});
        channelMasterTableHandler.delete(fields,keys);
    }

    /**
     * Deletes ChannelMaster records of a particular local channel Id
     */
    public void deleteChannelMasterRecordByChannelId(int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+channelId+"'"});
        channelMasterTableHandler.delete(fields,keys);
    }
}