package com.shinhan.user;

public class LoginSession {
    public static UserDTO loggedInUser = null;

    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }
}