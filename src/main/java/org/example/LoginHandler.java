package org.example;

import java.util.ArrayList;
import java.util.List;

public class LoginHandler {
    List<User> userList = new ArrayList<>();
    PasswordUtils passwordUtils;
    String salt;

    public LoginHandler(PasswordUtils passwordUtils) {
        this.passwordUtils = passwordUtils;
        this.salt = passwordUtils.generateSalt(512).get();
    }

    //method for creating new User and adding user to List
    public User addUser(String userName, String password) {
        User user = new User(userName, password);
        userList.add(user);
        return user;
    }

    public boolean login(String uName, String password){
        boolean loginSuccess = false;

            for (User user : userList){
                if(user.getUserName().equals(uName)){
                    if(verifyUser(user, password) == true){
                        user.setLoggedIn(true);
                        loginSuccess = user.getIsLoggedIn();
                    }
                }
            }
            return loginSuccess;
        }


    private boolean verifyUser(User user, String password) {
        String inputPassword = password;
        String key = passwordUtils.hashPassword(user.getPassword(), salt).get();

        return passwordUtils.verifyPassword(inputPassword, key, salt);
    }


}
