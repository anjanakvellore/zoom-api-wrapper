package com.app.zoomapi.utilities;

import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthResourceResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple class to get access token with Zoom.us OAuth
 */
public class TokenHandler {
    private Map<String,String> config;
    private String accessToken;

    /**
     * set up new token
     * @param clientId : The Zooom.us client id for this OAuth bot
     * @param clientSecret : The Zoom.us client secret for this OAuth bot
     * @param port : The port that has tuneling enabled
     * @param redirectUrl : Zoom.us OAuth redirect Url
     * @param browserPath : The browser path to open authorization Url
     * @throws OAuthProblemException
     * @throws OAuthSystemException
     * @throws IOException
     */
    public TokenHandler(String clientId, String clientSecret, int port, String redirectUrl, String browserPath)
            throws OAuthProblemException, OAuthSystemException, IOException {
        config = new HashMap<>();
        config.put("clientId",clientId);
        config.put("clientSecret",clientSecret);
        config.put("port",String.valueOf(port));
        config.put("redirectUrl",redirectUrl);
        config.put("browserPath",browserPath);
        setOauthToken();
    }

    /**
     * sets OAuth token
     * @throws OAuthSystemException
     * @throws OAuthProblemException
     * @throws IOException
     */
    private void setOauthToken() throws OAuthSystemException, OAuthProblemException, IOException {
        String authorizationUrl;
        OAuthClientRequest request=OAuthClientRequest
                .authorizationLocation("https://zoom.us/oauth/authorize")
                .setClientId(config.get("clientId"))
                .setRedirectURI(config.get("redirectUrl"))
                .setResponseType("code")
                .buildQueryMessage();
        authorizationUrl=request.getLocationUri();
        System.out.printf("Going to %s to authorize access.\n",authorizationUrl);

        Runtime runtime = Runtime.getRuntime();
        runtime.exec(new String[]{config.get("browserPath"),authorizationUrl});

        String code = httpReciever();
        String encodedBytes = Base64.getEncoder().encodeToString(
                (config.get("clientId")+":"+config.get("clientSecret")).getBytes());

        OAuthClientRequest request2=OAuthClientRequest
                .tokenLocation("https://zoom.us/oauth/token")
                .setGrantType(GrantType.AUTHORIZATION_CODE)
                .setRedirectURI(config.get("redirectUrl"))
                .setCode(code)
                .buildQueryMessage();
        request2.setHeader("Authorization","Basic"+encodedBytes);
        OAuthClient oAuthClient=new OAuthClient(new URLConnectionClient());
        OAuthResourceResponse resourceResponse=oAuthClient.resource(request2, OAuth.HttpMethod.POST,
                OAuthResourceResponse.class);
        JSONObject jsonObj=new JSONObject(resourceResponse.getBody());
        accessToken=jsonObj.get("access_token").toString();
    }

    /**
     * Helper function for accepting server request at the given port
     * @throws IOException
     */
    public String httpReciever() throws IOException{

        final ServerSocket serverSocket=new ServerSocket(Integer.valueOf(config.get("port")));
        System.out.println("Serving at port "+ config.get("port"));
        Socket clientSocket=serverSocket.accept();

        InputStreamReader isr=new InputStreamReader(clientSocket.getInputStream());
        BufferedReader reader=new BufferedReader(isr);
        String readerStr = reader.readLine();
        String code = readerStr.split("/")[1].split("=")[1].split(" ")[0];
        System.out.println("End of http receiver");

        return code;
    }

    /**
     * get the Zooom.us OAuth access token
     */
    public String getOauthToken(){
        return accessToken;
    }
}