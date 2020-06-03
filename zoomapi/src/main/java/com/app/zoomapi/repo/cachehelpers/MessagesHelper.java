package com.app.zoomapi.repo.cachehelpers;

import com.app.zoomapi.models.Messages;
import com.app.zoomapi.repo.SQLiteGenericTableHandler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MessagesHelper {
    private SQLiteGenericTableHandler<Messages> messagesTableHandler = null;

    public MessagesHelper(String path) throws SQLException {
        this.messagesTableHandler = new SQLiteGenericTableHandler<>(path,Messages::new);
    }

    public void insertMessagesRecord(Messages message) throws Exception {
        messagesTableHandler.insertRow(message);
    }

    public void insertMessagesRecords(List<Messages> messagesList) throws Exception {
        for (Messages mes:messagesList){
            messagesTableHandler.insertRow(mes);
        }
    }

    public List<Messages> getMessagesRecordsByChannelId(int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+channelId+"'"});
        return this.messagesTableHandler.get(fields,keys);
    }

    public List<Messages> getMessagesRecordsBySenderAndChannel(String sender,int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"sender","channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+sender+"'","'"+channelId+"'"});
        return this.messagesTableHandler.get(fields,keys);
    }

    public Messages getMessagesRecordByZoomMessageId(String zoomMessageId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomMessageId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomMessageId+"'"});
        return (messagesTableHandler.get(fields,keys).size()>0)? messagesTableHandler.get(fields,keys).get(0):null;
    }

    public void deleteMessagesRecordByChannelId(int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{String.valueOf(channelId)});
        messagesTableHandler.delete(fields,keys);
    }

    public void deleteMessagesRecordByZoomMessageId(String zoomMessageId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomMessageId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomMessageId+"'"});
        messagesTableHandler.delete(fields,keys);
    }

    public void deleteMessagesRecordBySenderAndChannel(String sender, int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"sender","channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+sender+"'","'"+channelId+"'"});
        messagesTableHandler.delete(fields,keys);
    }
}
