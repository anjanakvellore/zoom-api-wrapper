package com.app.zoomapi.events.process;

import com.app.zoomapi.events.EventFramework;
import com.app.zoomapi.models.Member;

import java.util.List;
import java.util.stream.Collectors;

public class ProcessMemberEvents {
    public void findNewMembers(List<Member> allMembers, List<Member> currentState){
        if(currentState!=null){
            //System.out.println(channelName);

            List<Member> newMembers = allMembers.stream().filter(x->
                    currentState.stream().noneMatch(y->y.getMemberId().equals(x.getMemberId())
                    )).collect(Collectors.toList());

            if(newMembers.size()>0){
                for(Member newMember:newMembers){
                    EventFramework.triggerNewMemberEvent(newMember);
                }
            }
        }
    }
}
