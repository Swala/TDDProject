package org.example;

import java.util.Optional;

public class User {
    private String userName;
    private String password;
    private boolean isLoggedIn;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.isLoggedIn = false;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

}
