package com.app.bots;

import com.app.zoomapi.models.Message;
import com.app.zoomapi.models.Result;
import org.ini4j.Wini;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.io.File;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

/*
bot program for milestone 3
 */
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
                    (clientId, clientSecret, port, url, browserPath, null, null,null,false);

            String channelName = "";
            String messageToSend = "";
            int date, month,year;
            LocalDate fromDate, toDate;
            Scanner in = new Scanner(System.in);

            /**
             * send a chat message to the given channel
             * function call example : client.chat.sendMessage("test", "Hello world!")
             */
            System.out.println("Send chat messages to a given channel...");
            System.out.println("Enter Channel Name:");
            channelName=in.nextLine();
            System.out.println("Enter the message to send: ");
            messageToSend = in.nextLine();
            //HttpResponse<Object> response = client.getChat().sendMessage(channelName,messageToSend);
            Result response = client.getChat().sendMessage(channelName,messageToSend);
            int statusCodeResponse = response.getStatus();
            //Object bodyResponse = response.body();
            //checking the status code: 200 means all OK
            if(statusCodeResponse==201) {

                System.out.println("Message successfully sent to "+channelName);
            }
            else {
                String bodyResponse = response.getErrorMessage();
                System.out.println(bodyResponse);
            }
            System.out.println("-------------------------------------------------------------------------------------");

            /*
            retrieving the entire chat history of a given channel
            function call example: client.chat.history("test")
             */
            System.out.println("Retrieve the chat history of a given channel between two given dates...");

            List<Message> messages = new ArrayList<>();
            System.out.println("Enter Channel Name: ");
            channelName = in.nextLine();

            System.out.println("Enter 'from' date");
            System.out.println("Enter date: ");
            date = Integer.parseInt(in.nextLine());
            System.out.println("Enter month (1-12): ");
            month = Integer.parseInt(in.nextLine());
            System.out.println("Enter year: ");
            year = Integer.parseInt(in.nextLine());
            fromDate = LocalDate.of(year,month,date);


            System.out.println("Enter 'to' date");
            System.out.println("Enter date: ");
            date = Integer.parseInt(in.nextLine());
            System.out.println("Enter month (1-12): ");
            month = Integer.parseInt(in.nextLine());
            System.out.println("Enter year: ");
            year = Integer.parseInt(in.nextLine());
            toDate = LocalDate.of(year,month,date);


            //HttpResponse<Object> messageResponse = client.getChat().history(channelName, fromDate,toDate);
            Result messageResponse = client.getChat().history(channelName, fromDate,toDate);
            int statusCode = messageResponse.getStatus();
            //checking the status code: 200 means all OK
            if(statusCode==200) {
                Object body = messageResponse.getData();
                messages = (List<Message>)body;
                for (Message message : messages) {
                    System.out.println(message.getSender() + "(" + message.getDateTime() + "): " + message.getMessage());
                }
            }
            else{
                String body = messageResponse.getErrorMessage();
                System.out.println(body);
            }

            System.out.println("-------------------------------------------------------------------------------------");
            /**
             * searching for specific events related to chat between two given dates on a given channel
             * search based on sender name: client.chat.search("test",  lambda message : message.sender.contains("diva"))
             */

            System.out.println("Search for messages sent by a given sender in a given channel between two dates...");

            System.out.println("Enter channel name: ");
            channelName = in.nextLine();

            System.out.println("Enter sender name: ");
            String senderName = in.nextLine();

            System.out.println("Enter 'from' date");
            System.out.println("Enter date: ");
            date = Integer.parseInt(in.nextLine());
            System.out.println("Enter month (1-12): ");
            month = Integer.parseInt(in.nextLine());
            System.out.println("Enter year: ");
            year = Integer.parseInt(in.nextLine());
            fromDate = LocalDate.of(year,month,date);


            System.out.println("Enter 'to' date");
            System.out.println("Enter date: ");
            date = Integer.parseInt(in.nextLine());
            System.out.println("Enter month (1-12): ");
            month = Integer.parseInt(in.nextLine());
            System.out.println("Enter year: ");
            year = Integer.parseInt(in.nextLine());
            toDate = LocalDate.of(year,month,date);

            //search based on sender name
            Predicate<Message> predicate = message -> {
                return message.getSender().contains(senderName);
            };

            messages = new ArrayList<>();
            messageResponse = client.getChat().search(channelName,fromDate,toDate,predicate);
            statusCode = messageResponse.getStatus();
            //checking the status code: 200 means all OK
            if(statusCode == 200){
                Object body = messageResponse.getData();
                messages = (List<Message>)body;
                for(Message message:messages){
                    System.out.println(message.getSender()+"("+message.getDateTime()+"): "+message.getMessage());
                }
            }
            else{
                String body = messageResponse.getErrorMessage();
                System.out.println(body);
            }

            System.out.println("-------------------------------------------------------------------------------------");

            /**
             * searching for specific events related to chat between two given dates on a given channel
             * search based on chat message: client.chat.search("test",  lambda message: message.message.contains("hello"))
             */
            System.out.println("Search for messages containing a given word in a given channel between given two dates...");

            System.out.println("Enter channel name: ");
            channelName = in.nextLine();

            System.out.println("Enter word to search: ");
            String word = in.nextLine();

            System.out.println("Enter 'from' date");
            System.out.println("Enter date: ");
            date = Integer.parseInt(in.nextLine());
            System.out.println("Enter month (1-12): ");
            month = Integer.parseInt(in.nextLine());
            System.out.println("Enter year: ");
            year = Integer.parseInt(in.nextLine());
            fromDate = LocalDate.of(year,month,date);


            System.out.println("Enter 'to' date");
            System.out.println("Enter date: ");
            date = Integer.parseInt(in.nextLine());
            System.out.println("Enter month (1-12): ");
            month = Integer.parseInt(in.nextLine());
            System.out.println("Enter year: ");
            year = Integer.parseInt(in.nextLine());
            toDate = LocalDate.of(year,month,date);

            predicate = message -> {
                return message.getMessage().contains(word);
            };
            messageResponse = client.getChat().search(channelName,fromDate,toDate,predicate);
            statusCode = messageResponse.getStatus();

            //checking the status code: 200 means all OK
            if(statusCode == 200){
                Object body = messageResponse.getData();
                messages = (List<Message>)body;
                for(Message message:messages){
                    System.out.println(message.getSender()+"("+message.getDateTime()+"): "+message.getMessage());
                }
            }
            else{
                String body = messageResponse.getErrorMessage();
                System.out.println(body);
            }

            System.out.println("-------------------------------------------------------------------------------------");


        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
