package com.app.bots;

import com.app.zoomapi.extended.EventFramework;
import com.app.zoomapi.models.Event;
import com.app.zoomapi.models.Message;
import org.ini4j.Wini;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class Bot4 {
    public static void main(String[] args) {
        try {
            /**
             * to read credentials for bot.ini
             */
            File file = new File(
                    OAuthBot.class.getClassLoader().getResource("bot.ini").getFile()
            );
            Wini ini = new Wini(file);
            String clientId = ini.get("OAuth", "client_id");
            String clientSecret = ini.get("OAuth", "client_secret");
            String portStr = ini.get("OAuth", "port");

            int port = 4001;
            if (portStr != null) {
                port = Integer.parseInt(portStr);
            }
            String browserPath = ini.get("OAuth", "browser_path");

            /**
             * Creating ngrok tunnel which is needed to enable tunneling through firewalls
             * Run "ngrok start --none" on terminal before running the bot
             */

            NgrokTunnel tunnel = new NgrokTunnel(port);
            String url = tunnel.url();
            System.out.println("Redirect url:" + url);

            com.app.zoomapi.clients.OAuthClient client = new com.app.zoomapi.clients.OAuthClient
                    (clientId, clientSecret, port, url, browserPath, null, null);
            EventFramework eventFramework = new EventFramework();
            eventFramework.registerForNewMessageEvent(new Event(EventHandler.getNewMessages, "history"), client);

            while(true) {
                //see what happens
                //System.out.println("Waiting for the event....");
            }


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}

class EventHandler{
    static Consumer<Message> getNewMessages = (message)->{
        System.out.println(message.getSender() + "(" + message.getDateTime() + "): " + message.getMessage());

    };

    static Consumer<List<Message>> getUpdatedMessages = (messages)->{
        System.out.println(messages);
    };
}
