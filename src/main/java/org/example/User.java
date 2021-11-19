package org.example;

import java.util.List;

public class User {
    private String userName;
    private String password;
    private String salt;
    //private List<String> permissions;

    public User(String userName, String password, String salt) {
        this.userName = userName;
        this.password = password;
        this.salt = salt;
        //this.permissions = permissions;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

}
