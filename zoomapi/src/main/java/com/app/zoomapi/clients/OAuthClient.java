package com.app.zoomapi.clients;

import com.app.zoomapi.components.ChatChannelsComponent;
import com.app.zoomapi.components.ChatMessagesComponent;
import com.app.zoomapi.utilities.TokenHandler;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OAuthClient extends ZoomClient {
    private Map<String,String> config;
    private ChatChannelsComponent chatChannelsComponent;
    private ChatMessagesComponent chatMessagesComponent;
    private TokenHandler tokenHandler;

    /**
     * Set up new OAuthClient
     * @param clientId : The Zooom.us client id for this OAuth bot
     * @param clientSecret : The Zoom.us client secret for this OAuth bot
     * @param port : The port that has tuneling enabled
     * @param redirectUrl : Zoom.us OAuth redirect Url
     * @param browserPath : The browser path to open authorization Url
     * @param dataType : The expected return data type. Either 'json' or 'xml'
     * @param timeOut : The time out to use for API requests
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     * @throws IOException
     */
    public OAuthClient(String clientId, String clientSecret, int port, String redirectUrl, String browserPath,
                       String dataType,Integer timeOut) throws OAuthSystemException, OAuthProblemException, IOException {

        super(clientId,clientSecret,dataType!=null ? dataType:"json",timeOut!=null ? timeOut : 15);
        config = new HashMap<>();
        config.put("clientId",clientId);
        config.put("clientSecret",clientSecret);
        config.put("port",String.valueOf(port));
        config.put("redirectUrl",redirectUrl);
        config.put("browserPath",browserPath);

        tokenHandler = new TokenHandler(clientId,clientSecret,port,redirectUrl,browserPath);
        config.put("token",tokenHandler.getOauthToken());

        //ToDo: should we create a hashmap like in Python?
        chatChannelsComponent = ChatChannelsComponent.getChatChannelsComponent(config);
        chatMessagesComponent = ChatMessagesComponent.getChatMessagesComponent(config);
    }

    /**
     * refresh Zoom.us OAuth token
     * @return Zoom.us OAuth token
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     * @throws IOException
     */
    @Override //TODO check abstract method? do I leave as override
    public String refreshToken() throws OAuthProblemException, OAuthSystemException,IOException {
        tokenHandler = new TokenHandler(config.get("clientId"),config.get("clientSecret"),
                Integer.valueOf(config.get("port")),config.get("redirectUrl"),config.get("browserPath"));
        config.put("token",tokenHandler.getOauthToken());
        return (config.get("token"));
    }

    /**
     * get the redirect Url set by NGROK
     * @return Zoom.us OAuth redirect Url
     */
    public String getRedirectUrl(){
        return config.get("redirectUrl");
    }

    /**
     * set Zoom.us OAuth redirect url
     * @param redirectUrl redirect Url as a string
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     * @throws IOException
     */
    private void setRedirectUrl(String redirectUrl) throws OAuthSystemException, OAuthProblemException, IOException {
        config.put("redirectUrl",redirectUrl);
        refreshToken();
    }

    /**
     * get the chat messages component
     */
    public ChatMessagesComponent getChatMessagesComponent(){
        return chatMessagesComponent;
    }

    /**
     * get the chat channels component
     */
    public ChatChannelsComponent getChatChannelsComponent(){
        return chatChannelsComponent;
    }
}
