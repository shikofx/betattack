package com.pkt.betattack.app.api.config;

import com.pkt.betattack.app.api.client.controllers.UserApiController;
import com.pkt.betattack.app.api.pojo.account.Account;
import com.pkt.betattack.app.api.pojo.user.User;
import okhttp3.Response;
import io.socket.client.Socket;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthConfig {


    public static final String DATA_BASE_URL = "http://185.213.209.34:3000";
//    public static final String SOCKET_SERVER = "http://localhost:3001";
//    public static final String SOCKET_SERVER = "https://betattack-socket-server.herokuapp.com";
    public static final String SOCKET_SERVER = "http://185.213.209.34:3001";
    public static Socket MM_SOCKET;
    public static final String BETATTACK_TOKEN_FILE = "batt-data-file.dat";
    private AuthConfig() {
    }

    public static String currentToken = "";
    public static String currentTokenString = "";
    public static User currentUser = new User();
    public static List<User> userList = new ArrayList<>();
    public static List<Account> attackAccounts = new ArrayList<>();
    public static Response responceError = null;
    public static String logErrorMessage = "";

    public static void saveToken() throws IOException {
        FileOutputStream tokenFile = new FileOutputStream(BETATTACK_TOKEN_FILE);
        tokenFile.write(currentTokenString.getBytes());
        tokenFile.close();
    }

    public static boolean getCurrentToken() {
        String token = "";
        try {
            FileInputStream tokenFile = new FileInputStream(BETATTACK_TOKEN_FILE);
            int i=-1;
            while((i=tokenFile.read())!=-1)
                token = String.format("%s%s", token, (char) i);
        } catch (IOException e) {
            return false;
        }
        if(!token.equals("")) {
            currentTokenString = token;
            currentToken = currentTokenString.replace("Bearer ", "");
            currentUser = new UserApiController().getUser();
            return true;
        }
        return false;
    }

    public static void deleteToken() {
        File tokenFile = new File(BETATTACK_TOKEN_FILE);
        tokenFile.delete();
    }
}
