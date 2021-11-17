package org.example;

import java.util.Objects;

public class User {
    private String userName;
    private String password;
    private String salt;
    private String token;
    private boolean isLoggedIn;

    public User(String userName, String password, String salt, String token) {
        this.userName = userName;
        this.password = password;
        this.isLoggedIn = false;
        this.salt = salt;
        this.token = token;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName == user.userName && Objects.equals(password, user.password) && Objects.equals(token, user.token);
    }
}
