package com.app.zoomapi.utilities;

import com.app.bots.OAuthBot;
import com.app.zoomapi.clients.OAuthClient;
import com.app.zoomapi.models.Credentials;
import com.app.zoomapi.repo.cachehelpers.CredentialsHelper;
import org.ini4j.Wini;
import xyz.dmanchon.ngrok.client.NgrokTunnel;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Handles essential credentials given by the User.
 */
public class CredentialsHandler {
    private String clientId,clientSecret,portStr,browserPath,dbPath;
    private int port;
    private String url;
    private OAuthClient oAuthClient;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private CredentialsHelper credentialsHelper = null;

    public CredentialsHandler(String iniFileName) throws Exception {

        /**
         * to read credentials for bot.ini
         */
        File file = new File(
                OAuthBot.class.getClassLoader().getResource(iniFileName).getFile()
        );
        Wini ini = new Wini(file);
        this.clientId = ini.get("OAuth", "client_id");
        this.clientSecret = ini.get("OAuth", "client_secret");
        this.portStr = ini.get("OAuth", "port");

        this.port = 4001;
        if (portStr != null) {
            this.port = Integer.parseInt(portStr);
        }
        this.browserPath = ini.get("OAuth", "browser_path");
        this.dbPath = ini.get("cache","cache_path");

        NgrokTunnel tunnel = null;

        try {
            this.credentialsHelper = new CredentialsHelper(dbPath);
            Credentials userCredentials = credentialsHelper.getCredentialsRecordByZoomClientId(clientId);
            LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
            if (userCredentials == null || LocalDateTime.parse(userCredentials.getLoginTime(), formatter).until(now, ChronoUnit.MINUTES) > 59) {
                credentialsHelper.deleteCredentialsRecordByZoomClientId(clientId);
                tunnel = new NgrokTunnel(port);
                this.url = tunnel.url();
                System.out.println("Redirect url:" + url);
                this.oAuthClient = new OAuthClient(clientId, clientSecret, port, url, browserPath, null, null, dbPath, false);
                /**
                 * Successful signup
                 */
                if (oAuthClient.getOAuthToken() != null) {
                    LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("UTC"));
                    credentialsHelper.insertCredentialsRecord(new Credentials(clientId, oAuthClient.getOAuthToken(), dateTime.format(formatter)));
                }
            } else {
                this.oAuthClient = new OAuthClient(clientId, clientSecret, port, url, browserPath, null, null, dbPath, true);
            }
        }
        catch (SQLException | IllegalAccessException | NoSuchFieldException ex){
            if(this.oAuthClient == null) {
                if(tunnel == null) {
                    tunnel = new NgrokTunnel(port);
                    this.url = tunnel.url();
                    System.out.println("Redirect url:" + url);
                }
                this.oAuthClient = new OAuthClient(clientId, clientSecret, port, url, browserPath, null, null, dbPath, false);
            }
            /**
             * Successful signup
             */
            if (oAuthClient.getOAuthToken() != null) {
                LocalDateTime dateTime = LocalDateTime.now(ZoneId.of("UTC"));
                if(this.credentialsHelper!=null) {
                    try {
                        credentialsHelper.insertCredentialsRecord(new Credentials(clientId, oAuthClient.getOAuthToken(), dateTime.format(formatter)));
                    }
                    catch (SQLException | IllegalAccessException e){
                        //we don't need to do anything. app should work as is
                    }
                }
            }
        }
    }

    /**
     * Returns an OAuthClient that was initialized with credentials given by the user.
     */
    public OAuthClient getOAuthClient(){
        return oAuthClient;
    }

    public String getClientId(){return clientId;}

    public String getClientSecret(){return clientSecret;}

    public String getPortStr() {return portStr;}

    public int getPort(){return port;}

    public String getBrowserPath(){return browserPath;}

    public String getDbPath(){return dbPath;}

}