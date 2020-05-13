package com.app.bots;

import com.app.zoomapi.models.Member;
import com.app.zoomapi.models.Message;

import java.util.function.Consumer;

/**
 * Call back functions
 */
class EventHandler{
    static Consumer<Message> getNewMessages = (message)->{
        System.out.println("New message: ");
        System.out.println(message.getSender() + "(" + message.getDateTime() + "): " + message.getMessage());

    };

    static Consumer<Message> getUpdatedMessages = (message)->{
        System.out.println("Updated message: ");
        System.out.println(message.getSender() + "(" + message.getDateTime() + "): " + message.getMessage());
    };

    static Consumer<Member> getNewMembers = (member)->{
        System.out.println(member.getFirstName() + " " + member.getLastName() + "(" + member.getEmail() + ") added as "+
                member.getRole()+ " in " +member.getChannel());
    };
}
