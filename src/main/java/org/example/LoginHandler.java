package org.example;

import java.util.*;

public class LoginHandler {
    List<User> userList = new ArrayList<>();
    PasswordUtils passwordUtils;
    //Map<String, String> tokens = new HashMap<>(); //Key = userName, Value = token (List?)
    Map<String, Token> tokens = new HashMap<>(); //Key = userName, Value = Object token (List?)
    Token userToken;
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
        //return tokens;
        return tokens.get(uName).getToken();
        //return userToken.getToken();
    }

    public static boolean verifyPassword (String password, String key, String salt) {
        PasswordUtils passwordEncrypter = new PasswordUtils();
        Optional<String> optEncrypted = passwordEncrypter.hashPassword(password, salt);
        if (!optEncrypted.isPresent()) return false;

        return optEncrypted.get().equals(key);
    }

    public void addToken(User user){
        //tokens.put(user.getUserName(), passwordUtils.generateToken().get());
        String testToken = passwordUtils.generateToken().get();
        userToken = new Token(testToken, user);
        tokens.put(user.getUserName(), userToken);

    }

    public void addUserToResourceAccess(String resource, List<User>users){
        resourceAccessList.put(resource, users);
    }

    public boolean isTokenValid(String token, String userName) {
        //if(token == tokens.get(userName)){
        if(token == userToken.getToken()){
            return true;
        }
        return false;
    }

    public List<String> getUserPermissions(String token, String resource) {
        List<String>listOfUserPermissions = new ArrayList<>();
        String userName = "";
        User thisUser = null;
        System.out.println("in getPermissions");

        //find matching token in map tokens and get the key (username)
        for(Map.Entry<String, Token> entry : tokens.entrySet()){
            if(Objects.equals(token, entry.getValue().getToken())){
                if(isTokenValid(token, entry.getKey())){
                    userName = entry.getKey();
                    System.out.println(entry.getKey());
                }
            }
        }

        //if username was found, get user from userList
        if(userName  != "") {
            for (User user : userList) {
                if (user.getUserName().equals(userName) && resourceAccessList.get(resource).contains(user)) {
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
        String token;
        User user;

        public Token(String token, User user) {
            this.token = token;
            this.user = user;
        }

        public String getToken() {
            return token;
        }

        public User getUser() {
            return user;
        }
    }

}


