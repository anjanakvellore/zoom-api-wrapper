package com.app.zoomapi.events.process;

import com.app.zoomapi.events.EventFramework;
import com.app.zoomapi.models.Message;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class to find new messages & updated messages by comparing with the
 * recent message list, stored in current state.
 */
public class ProcessMessageEvents {

    public void findNewMessages(List<Message> allMessages,List<Message> currentState,String channelName){
        List<Message> newMessages = allMessages.stream().filter(x->
                currentState.stream().noneMatch(y->y.getMessageId().equals(x.getMessageId())
                )).collect(Collectors.toList());

        if(newMessages.size()>0){
            for(Message newMessage:newMessages){
                EventFramework.triggerNewMessageEvent(newMessage,channelName);
            }
        }
    }

    public void findUpdatedMessages(List<Message> allMessages,List<Message> currentState, String channelName){
        List<Message> updatedMessages = allMessages.stream().filter(x->
                currentState.stream().anyMatch(y->y.getMessageId().equals(x.getMessageId()) && !(y.getMessage().equals(x.getMessage()))))
                .collect(Collectors.toList());

        if(updatedMessages.size()>0){
            for (Message updatedMessage:updatedMessages){
                EventFramework.triggerUpdateMessageEvent(updatedMessage,channelName);
            }
        }
    }
}
