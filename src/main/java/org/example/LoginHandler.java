package org.example;

import java.util.*;

public class LoginHandler {
    List<User> userList = new ArrayList<>();
    PasswordUtils passwordUtils;
    Map<String, String> tokens = new HashMap<>(); //Key = userName, Value = token (List?)
    Map<String, List<User>> resourceAccessList = new HashMap<>(); //key = resourceName, Value=user
    //Map<String, List<String>> permissionsBasedOnResource = new HashMap<>(); //key = resourceName, Value = permissions

    public LoginHandler(PasswordUtils passwordUtils) {
        this.passwordUtils = passwordUtils;
    }

    //method for creating new User and adding user to List
    public User addUser(String userName, String password, List<String> permissions) {
        String salt = passwordUtils.generateSalt(512).get();

        //add permissions to user, and check resource before adding permissions?
        User user = new User(userName, passwordUtils.hashPassword(password, salt).get(), salt, permissions);
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

    public void addUserToResourceAccess(String resource, List<User>users){
        resourceAccessList.put(resource, users);
    }

    public List<String> getUserPermissions(String token, String resource) {
        List<String>listOfUserPermissions = new ArrayList<>();
        String userName = "";
        User thisUser = null;

        //find matching token in map tokens and get the key (username)
        for(Map.Entry<String, String> entry : tokens.entrySet()){
            if(Objects.equals(token, entry.getValue())){
                if(isTokenValid(token, entry.getKey())){
                    userName = entry.getKey();
                }
            }
        }

        //if username was found, get user from userlist
        if(userName  != "") {
            for (User user : userList) {
                if (user.getUserName().equals(userName)) {
                    thisUser = user;
                }
            }
        }

        // if user was found, get user from resourceAccessList
        if(resourceAccessList.get(resource).contains(thisUser)){
            //System.out.println("user was found in resource list");
            listOfUserPermissions = thisUser.getPermissions();
        }

        return listOfUserPermissions;
    }
}
