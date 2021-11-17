package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginHandler {
    List<User> userList = new ArrayList<>();
    PasswordUtils passwordUtils;

    public LoginHandler(PasswordUtils passwordUtils) {
        this.passwordUtils = passwordUtils;
    }

    //method for creating new User and adding user to List
    public User addUser(String userName, String password) {
        String salt = passwordUtils.generateSalt(512).get();
        String token = passwordUtils.generateToken().get();

        User user = new User(userName, passwordUtils.hashPassword(password, salt).get(), salt, token);
        userList.add(user);

        return user;
    }

    public String login(String uName, String password) throws MissingTokenException{
        String token = "";

        for (User user : userList){
            if(user.getUserName().equals(uName)){
                System.out.println("found user");
                if(verifyPassword(password, user.getPassword(), user.getSalt())){
                    user.setLoggedIn(true);
                    token = user.getToken();
                }
            }
            System.out.println("from loginhandler: " + user.getToken());
            if(token == ""){
                throw new MissingTokenException("Missing token");
            }
        }
        return token;
    }


    public static boolean verifyPassword (String password, String key, String salt) {
        PasswordUtils passwordEncrypter = new PasswordUtils();
        Optional<String> optEncrypted = passwordEncrypter.hashPassword(password, salt);
        if (!optEncrypted.isPresent()) return false;

        return optEncrypted.get().equals(key);
    }


}
