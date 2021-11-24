package org.example;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginHandler {
    List<User> userList = new ArrayList<>();
    PasswordUtils passwordUtils;
    Map<Integer, Token> tokens = new HashMap<>(); //Key = tokenId, Value = Object Token
    Map<String, List<User>> resourceAccessList = new HashMap<>(); //Key = resourceName, Value = user

    public LoginHandler(PasswordUtils passwordUtils) {
        this.passwordUtils = passwordUtils;
    }

    //method for creating new User and adding user to List
    public User addUser(String userName, String password, List<String> permissions) {
        String salt = PasswordUtils.generateSalt(512).get();

        User user = new User(userName, PasswordUtils.hashPassword(password, salt).get(), salt, permissions);
        userList.add(user);

        return user;
    }

    //success login should return a token
    public String login(String uName, String password) throws MissingTokenException{
        int tokenId = 0;
        for (User user : userList){
            if(user.getUserName().equals(uName)){
                if(verifyPassword(password, user.getPassword(), user.getSalt())){
                    tokenId = addToken(user);
                }
                break;
            }
        }
        if(tokens.isEmpty()){
            throw new MissingTokenException("Missing token");
        }
        return tokens.get(tokenId).getToken();
    }

    public static boolean verifyPassword (String password, String key, String salt) {
        Optional<String> optEncrypted = PasswordUtils.hashPassword(password, salt);
        return optEncrypted.map(s -> s.equals(key)).orElse(false);

    }

    public int addToken(User user){
        Token userToken = new Token(PasswordUtils.generateToken().get(), user);
        tokens.put(userToken.getTokenId(), userToken);

        return userToken.getTokenId();
    }

    public void addUserToResourceAccess(String resource, List<User>users){
        resourceAccessList.put(resource, users);
    }

    public boolean isTokenValid(String token) {
        for (Map.Entry <Integer, Token> entry : tokens.entrySet()){
            if(entry.getValue().getToken().equals(token)){
                return true;
            }
        }
        return false;
    }

    public int getTokenId(String token){
        for (Map.Entry <Integer, Token> entry : tokens.entrySet()){
            if(entry.getValue().getToken().equals(token)){
                return entry.getKey();
            }
        }
        return 0;
    }

    public List<String> getUserPermissions(String token, String resource) {
        List<String>listOfUserPermissions = new ArrayList<>();
        User thisUser = null;
        int tokenID = getTokenId(token);

        if(tokenID != 0){
            thisUser = tokens.get(tokenID).getUser();
        }

        //if user was found and user is found in resourceAccessList -> get permissions
        if(thisUser != null) {
            if(userList.contains(thisUser) && resourceAccessList.get(resource).contains(thisUser)){
                listOfUserPermissions = thisUser.getPermissions();
            }
        }
        return listOfUserPermissions;
    }

    public static class Token{
        int tokenId;
        String token;
        User user;
        AtomicInteger tokenIDCounter = new AtomicInteger(0);

        public Token(String token, User user) {
            this.tokenId = tokenIDCounter.incrementAndGet();
            this.token = token;
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public User getUser() {
            return user;
        }

        public int getTokenId() {
            return tokenId;
        }
    }

}


