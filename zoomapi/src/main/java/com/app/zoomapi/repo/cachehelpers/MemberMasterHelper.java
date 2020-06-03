package com.app.zoomapi.repo.cachehelpers;

import com.app.zoomapi.models.MemberMaster;
import com.app.zoomapi.repo.SQLiteGenericTableHandler;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class MemberMasterHelper {
    private SQLiteGenericTableHandler<MemberMaster> memberMasterTableHandler = null;

    public MemberMasterHelper(String path) throws SQLException {
        this.memberMasterTableHandler = new SQLiteGenericTableHandler<>(path,MemberMaster::new);
    }

    public void insertMemberMasterRecord(MemberMaster memberMaster) throws Exception {
        memberMasterTableHandler.insertRow(memberMaster);
    }

    public void insertMemberMasterRecords(List<MemberMaster> memberMasterList) throws Exception {
        for (MemberMaster mem:memberMasterList){
            memberMasterTableHandler.insertRow(mem);
        }
    }

    /**
     * Gets all channel id records associated with a Zoom memberId
     * @param zoomMemberId
     * @return
     * @throws Exception
     */
    public List<MemberMaster> getMemberMasterRecordsByZoomMemberId(String zoomMemberId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomMemberId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomMemberId+"'"});
        return this.memberMasterTableHandler.get(fields,keys);
        //return  (memberMasterTableHandler.get(fields,keys).size()>0)? memberMasterTableHandler.get(fields,keys).get(0):null;
    }

    /**
     * Gets a list of members for a given channel Id
     * @param channelId
     * @return
     * @throws Exception
     */
    public List<MemberMaster> getMemberMasterRecordsByChannelId(int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{"'"+channelId+"'"});
        return this.memberMasterTableHandler.get(fields,keys);
    }

    public void deleteMemberMasterRecordsByChannelId(List<MemberMaster> membersList) throws SQLException {
        for(MemberMaster mem:membersList){
            int cid = mem.getChannelId();
            List<String> fields = Arrays.asList(new String[]{"channelId"});
            List<String> keys = Arrays.asList(new String[]{String.valueOf(cid)});
            memberMasterTableHandler.delete(fields,keys);
        }
    }

    public void deleteMemberMasterRecordByChannelId(int channelId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId"});
        List<String> keys = Arrays.asList(new String[]{String.valueOf(channelId)});
        memberMasterTableHandler.delete(fields,keys);
    }

    public void deleteMemberMasterRecordByZoomMemberId(String zoomMemberId) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"zoomMemberId"});
        List<String> keys = Arrays.asList(new String[]{"'"+zoomMemberId+"'"});
        memberMasterTableHandler.delete(fields,keys);
    }

    public void deleteMemberMasterRecordsByChannelIdAndEmail(int channelId, String email) throws Exception {
        List<String> fields = Arrays.asList(new String[]{"channelId","email"});
        List<String> keys = Arrays.asList(new String[]{"'"+channelId+"'","'"+email+"'"});
        memberMasterTableHandler.delete(fields,keys);
    }
}
