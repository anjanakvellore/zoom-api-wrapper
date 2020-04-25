package com.app.bots;

import com.app.bots.functions.MessageBySender;
import com.app.zoomapi.models.Message;
import org.ini4j.Wini;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Bot3 {
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
            List<Message> messages = new ArrayList<>();
            messages = client.getChat().history("test");
            for(Message message:messages){
                System.out.println(message.getSender()+"("+message.getDateTime()+"): "+message.getMessage());
            }

           /* MessageBySender func = new MessageBySender("diva@metaverseink.com");
            messages = (List<Message>)client.getChat().search("test",func);
            for(Message message:messages){
                System.out.println(message.getSender()+"("+message.getDateTime()+"): "+message.getMessage());
            }*/

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
