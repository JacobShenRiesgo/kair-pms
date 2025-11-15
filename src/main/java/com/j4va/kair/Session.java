package com.j4va.kair;

/**
 * Small in-memory session holder. Set after successful login.
 */
public class Session {
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    public static User getCurrentUser() {
        return currentUser;
    }
}