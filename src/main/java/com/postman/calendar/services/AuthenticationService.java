package com.postman.calendar.services;


import java.io.IOException;
import java.util.Base64;
import java.util.StringTokenizer;

public class AuthenticationService {


    public boolean authenticate(String authCredentials, UserService userService) {

        if (null == authCredentials)
            return false;

        final String encodedUserPassword = authCredentials.replaceFirst("Basic"
                + " ", "");
        String usernameAndPassword = null;
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(
                    encodedUserPassword);
            usernameAndPassword = new String(decodedBytes, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final StringTokenizer tokenizer = new StringTokenizer(
                usernameAndPassword, ":");
        final String username = tokenizer.nextToken();
        final String password = tokenizer.nextToken();

        boolean authenticationStatus = userService.authenticateUser(username, password);
        return authenticationStatus;
    }

}
