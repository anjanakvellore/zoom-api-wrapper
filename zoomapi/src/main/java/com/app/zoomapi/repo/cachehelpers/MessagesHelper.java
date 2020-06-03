package com.app.zoomapi.repo.cachehelpers;

import com.app.zoomapi.models.Messages;
import com.app.zoomapi.repo.SQLiteGenericTableHandler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Offers table-specific functions for Messages table
 */
public class MessagesHelper {
    private SQLiteGenericTableHandler<Messages> messagesTableHandler = null;

    public MessagesHelper(String path) throws SQLException {
        this.messagesTableHandler = new SQLiteGenericTableHandler<>(path,Messages::new);
    }

    /**
     * Inserts messages record into the table
     */
    public void insertMessagesRecord(Messages message) throws Exception {
        messagesTableHandler.insertRow(message);
    }

    /**
     * Inserts a list of messages records into the table
     */
    public void insertMessagesRecords(List<Messages> messagesList) throws Exception {
        for (Messages mes:messagesList){
            messagesTableHandler.insertRow(mes);
        }
    }

    /**
     * Gets a list of messages records for a given channel Id
     */
    public List<Messages> getMessagesRecordsByChannelId(int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+channelId+"'"});
        return this.messagesTableHandler.get(fields,keys);
    }

    /**
     * Gets a list of messages records for a given ChannelId and Sender
     */
    public List<Messages> getMessagesRecordsBySenderAndChannel(String sender,int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"sender","channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+sender+"'","'"+channelId+"'"});
        return this.messagesTableHandler.get(fields,keys);
    }

    /**
     * Gets a messages record for a given message Id
     */
    public Messages getMessagesRecordByZoomMessageId(String zoomMessageId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomMessageId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomMessageId+"'"});
        return (messagesTableHandler.get(fields,keys).size()>0)? messagesTableHandler.get(fields,keys).get(0):null;
    }

    /**
     * Deletes messages records for a given channel Id
     */
    public void deleteMessagesRecordByChannelId(int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{String.valueOf(channelId)});
        messagesTableHandler.delete(fields,keys);
    }

    /**
     * Deletes messages records for a given message Id
     */
    public void deleteMessagesRecordByZoomMessageId(String zoomMessageId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomMessageId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomMessageId+"'"});
        messagesTableHandler.delete(fields,keys);
    }

    /**
     * Deletes messages records for a given channel Id and sender
     */
    public void deleteMessagesRecordBySenderAndChannel(String sender, int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"sender","channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+sender+"'","'"+channelId+"'"});
        messagesTableHandler.delete(fields,keys);
    }
}
