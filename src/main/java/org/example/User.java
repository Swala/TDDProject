package org.example;

import java.sql.PreparedStatement;
import java.util.List;

public class User {
    private String userName;
    private String password;
    private String salt;
    private String resource;
    private List<String> permissions;

    public User(String userName, String password, String salt, String resource) {
        this.userName = userName;
        this.password = password;
        this.salt = salt;
        this.resource = resource;
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

    public List<String> getPermissions() {
        return permissions;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
