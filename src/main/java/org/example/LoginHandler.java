package org.example;

import java.util.ArrayList;
import java.util.List;

public class LoginHandler {

    //list of users
    //boolean loginSuccess

    List<User> userList = new ArrayList<>();

    //method for creating new User and adding user to List
    public User addUser(String userName, String password) {
        User user = new User(userName, password);
        userList.add(user);
        return user;
    }

    public boolean login(String uName, String password){
        boolean loginSuccess = false;

        for (User user : userList){
            if(user.getUserName().equals(uName) && user.getPassword().equals(password)){
               user.setLoggedIn(true);
               loginSuccess = user.getIsLoggedIn();
            }
        }
        return loginSuccess;
    }


}
