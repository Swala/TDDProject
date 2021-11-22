package org.example;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LoginHandler {
    List<User> userList = new ArrayList<>();
    PasswordUtils passwordUtils;
    //Map<String, String> tokens = new HashMap<>(); //Key = userName, Value = token (List?)
    Map<Integer, Token> tokens = new HashMap<>(); //Key = tokenId, Value = Object token
    Map<String, List<User>> resourceAccessList = new HashMap<>(); //key = resourceName, Value=user

    public LoginHandler(PasswordUtils passwordUtils) {
        this.passwordUtils = passwordUtils;
    }

    //method for creating new User and adding user to List
    public User addUser(String userName, String password, List<String> permissions) {
        String salt = passwordUtils.generateSalt(512).get();

        User user = new User(userName, passwordUtils.hashPassword(password, salt).get(), salt, permissions);
        userList.add(user);

        return user;
    }

    //return string token, redo token object
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
        //return userToken.getToken();
    }

    public static boolean verifyPassword (String password, String key, String salt) {
        PasswordUtils passwordEncrypter = new PasswordUtils();
        Optional<String> optEncrypted = passwordEncrypter.hashPassword(password, salt);
        if (!optEncrypted.isPresent()) return false;

        return optEncrypted.get().equals(key);
    }

    public int addToken(User user){
        //tokens.put(user.getUserName(), passwordUtils.generateToken().get());

        Token userToken = new Token(passwordUtils.generateToken().get(), user);
        tokens.put(userToken.getTokenId(), userToken);

        return userToken.getTokenId();

    }

    public void addUserToResourceAccess(String resource, List<User>users){
        resourceAccessList.put(resource, users);
    }

    public boolean isTokenValid(String token) { // String userName
        /*if(token == tokens.get(userName)){
            return true;
        }*/

        for (Map.Entry <Integer, Token> entry : tokens.entrySet()){
            if(entry.getValue().getToken().equals(token)){
                return true;
            }
        }
        return false;
    }

    public int getTokenId(String token){
        int tokenID = 0;

        for (Map.Entry <Integer, Token> entry : tokens.entrySet()){
            if(entry.getValue().getToken().equals(token)){
                tokenID = entry.getKey();
            }
        }

        return tokenID;
    }

    public List<String> getUserPermissions(String token, String resource) {
        List<String>listOfUserPermissions = new ArrayList<>();
        String userName = "";
        User thisUser = null;

        //find matching token in map tokens and get the user)
        for(Map.Entry<Integer, Token> entry : tokens.entrySet()){
            if(Objects.equals(token, entry.getValue().getToken())){
                if(isTokenValid(token)){ //entry.getKey()
                    //userName = entry.getKey();
                    thisUser = entry.getValue().getUser();
                }
            }
        }

        //if username was found, get user from userList
        if(thisUser != null) {
            for (User user : userList) {
                if (user == thisUser && resourceAccessList.get(resource).contains(user)) {
                    //thisUser = user;
                    listOfUserPermissions = user.getPermissions();
                }
            }
        }

        // if user was found, get user from resourceAccessList
        /*if(resourceAccessList.get(resource).contains(thisUser)){
            listOfUserPermissions = thisUser.getPermissions();
        }*/

        return listOfUserPermissions;
    }

    public class Token{
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


