package org.example;

import java.util.*;

public class LoginHandler {
    List<User> userList = new ArrayList<>();
    PasswordUtils passwordUtils;
    Map<String, String> tokens = new HashMap<>(); //Key = userName, Value = token (List?)

    public LoginHandler(PasswordUtils passwordUtils) {
        this.passwordUtils = passwordUtils;
    }

    //method for creating new User and adding user to List
    public User addUser(String userName, String password) {
        String salt = passwordUtils.generateSalt(512).get();

        User user = new User(userName, passwordUtils.hashPassword(password, salt).get(), salt);
        userList.add(user);

        return user;
    }

    public Map<String, String> login(String uName, String password) throws MissingTokenException{

        for (User user : userList){
            if(user.getUserName().equals(uName)){
                if(verifyPassword(password, user.getPassword(), user.getSalt())){
                    addToken(user);
                }
                break;
            }
        }
        if(tokens.isEmpty()){
            throw new MissingTokenException("Missing token");
        }
        return tokens;
    }


    public static boolean verifyPassword (String password, String key, String salt) {
        PasswordUtils passwordEncrypter = new PasswordUtils();
        Optional<String> optEncrypted = passwordEncrypter.hashPassword(password, salt);
        if (!optEncrypted.isPresent()) return false;

        return optEncrypted.get().equals(key);
    }

    public void addToken(User user){
        tokens.put(user.getUserName(), passwordUtils.generateToken().get());

    }

    public boolean isTokenValid(String token, String userName) {
        if(token == tokens.get(userName)){
            return true;
        }
        return false;
    }
}
