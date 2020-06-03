package com.app.zoomapi.repo.cachehelpers;

import com.app.zoomapi.models.MemberMaster;
import com.app.zoomapi.repo.SQLiteGenericTableHandler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Offers table-specific functions for MemberMaster table
 */
public class MemberMasterHelper {
    private SQLiteGenericTableHandler<MemberMaster> memberMasterTableHandler = null;

    public MemberMasterHelper(String path) throws SQLException {
        this.memberMasterTableHandler = new SQLiteGenericTableHandler<>(path,MemberMaster::new);
    }

    /**
     * Inserts a MemberMaster record into the table.
     */
    public void insertMemberMasterRecord(MemberMaster memberMaster) throws Exception {
        memberMasterTableHandler.insertRow(memberMaster);
    }

    /**
     * Inserts a list of MemberMaster records into the table.
     */
    public void insertMemberMasterRecords(List<MemberMaster> memberMasterList) throws Exception {
        for (MemberMaster mem:memberMasterList){
            memberMasterTableHandler.insertRow(mem);
        }
    }

    /**
     * Gets all MemberMaster records associated with a Zoom memberId
     */
    public List<MemberMaster> getMemberMasterRecordsByZoomMemberId(String zoomMemberId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomMemberId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomMemberId+"'"});
        return this.memberMasterTableHandler.get(fields,keys);
    }

    /**
     * Gets a list of members for a given channel Id
     */
    public List<MemberMaster> getMemberMasterRecordsByChannelId(int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+channelId+"'"});
        return this.memberMasterTableHandler.get(fields,keys);
    }

    /**
     * Deletes a list of MemberMaster record by Channel Id
     */
    public void deleteMemberMasterRecordsByChannelId(List<MemberMaster> membersList) throws SQLException {
        for(MemberMaster mem:membersList){
            int cid = mem.getChannelId();
            List<String> fields = Arrays.asList(new String[]{"channelId"});
            List<String> keys = Arrays.asList(new String[]{String.valueOf(cid)});
            memberMasterTableHandler.delete(fields,keys);
        }
    }

    /**
     * Deletes a MemberMaster record by Channel Id
     */
    public void deleteMemberMasterRecordByChannelId(int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{String.valueOf(channelId)});
        memberMasterTableHandler.delete(fields,keys);
    }

    /**
     * Deletes a MemberMaster record by Zoom member Id
     */
    public void deleteMemberMasterRecordByZoomMemberId(String zoomMemberId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomMemberId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomMemberId+"'"});
        memberMasterTableHandler.delete(fields,keys);
    }

    /**
     * Deletes a MemberMaster record for a given channel Id and Email
     */
    public void deleteMemberMasterRecordsByChannelIdAndEmail(int channelId, String email) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId","email"});
        List<String> keys = Arrays.asList(new String[]{"'"+channelId+"'","'"+email+"'"});
        memberMasterTableHandler.delete(fields,keys);
    }
}
