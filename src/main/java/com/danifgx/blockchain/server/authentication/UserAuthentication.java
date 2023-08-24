package com.danifgx.blockchain.server.authentication;

import java.util.HashMap;
import java.util.Map;

public class UserAuthentication {
    private static final Map<String, String> USERS = new HashMap<>();

    static {
        USERS.put("user1", "password1");
        USERS.put("user2", "password2");
    }

    public static boolean authenticate(String username, String password) {
        return password.equals(USERS.get(username));
    }
}
