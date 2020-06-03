package com.app.zoomapi.clients;

import com.app.zoomapi.components.ChatChannelsComponent;
import com.app.zoomapi.components.ChatMessagesComponent;
import com.app.zoomapi.components.UserComponent;
import com.app.zoomapi.componentwrapper.ChatChannelsComponentWrapper;
import com.app.zoomapi.componentwrapper.ChatMessagesComponentWrapper;
import com.app.zoomapi.componentwrapper.UserComponentWrapper;
import com.app.zoomapi.extended.Chat;
import com.app.zoomapi.extended.Members;
import com.app.zoomapi.models.Credentials;
import com.app.zoomapi.repo.cachehelpers.CredentialsHelper;
import com.app.zoomapi.utilities.TokenHandler;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple class to support OAuth implementation
 */
public class OAuthClient extends ZoomClient {
    private Map<String,String> config;
    private ChatChannelsComponent chatChannelsComponent;
    private ChatMessagesComponent chatMessagesComponent;
    private UserComponent userComponent;
    private TokenHandler tokenHandler;
    private Chat chat;
    private Members members;
    private ChatChannelsComponentWrapper chatChannelsComponentWrapper;
    private ChatMessagesComponentWrapper chatMessagesComponentWrapper;
    private UserComponentWrapper userComponentWrapper;
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
                       String dataType,Integer timeOut,String dbPath, boolean cache) throws Exception {

        super(clientId,clientSecret,dataType!=null ? dataType:"json",timeOut!=null ? timeOut : 15);
        config = new HashMap<>();
        config.put("clientId",clientId);
        config.put("clientSecret",clientSecret);
        config.put("port",String.valueOf(port));
        config.put("redirectUrl",redirectUrl);
        config.put("browserPath",browserPath);

        //TODO check
        if(!cache){
            tokenHandler = new TokenHandler(clientId,clientSecret,port,redirectUrl,browserPath);
            config.put("token",tokenHandler.getOauthToken());
        }else{
            CredentialsHelper credentialsHelper = new CredentialsHelper(dbPath);
            Credentials userCredentials = credentialsHelper.getCredentialsRecordByZoomClientId(clientId);
            config.put("token",userCredentials.getOAuthToken());
        }

        chatChannelsComponent = ChatChannelsComponent.getChatChannelsComponent(config);
        chatMessagesComponent = ChatMessagesComponent.getChatMessagesComponent(config);
        userComponent = UserComponent.getUserComponent(config);
        chat = Chat.getChatComponent(chatChannelsComponent,chatMessagesComponent,userComponent);
        members = Members.getMembersComponent(chatChannelsComponent,userComponent);
        chatChannelsComponentWrapper = ChatChannelsComponentWrapper.getChatChannelsComponentWrapper(chatChannelsComponent,dbPath);
        chatMessagesComponentWrapper = ChatMessagesComponentWrapper.getChatMessagesComponentWrapper(chatMessagesComponent,dbPath);
        userComponentWrapper = UserComponentWrapper.getUserComponentWrapper(userComponent,dbPath);
    }

    /**
     * refresh Zoom.us OAuth token
     * @return Zoom.us OAuth token
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     * @throws IOException
     */
    @Override
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

    /**
     * get the user component
     */
    public UserComponent getUserComponent(){
        return userComponent;
    }

    public Chat getChat(){return  chat;}

    public Members getMembers(){return members;}

    public ChatChannelsComponentWrapper getChatChannelsComponentWrapper(){return chatChannelsComponentWrapper;}

    public ChatMessagesComponentWrapper getChatMessagesComponentWrapper(){return chatMessagesComponentWrapper;}

    public UserComponentWrapper getUserComponentWrapper(){return userComponentWrapper;}

    public String getOAuthToken(){
        return config.get("token");
    }
}
