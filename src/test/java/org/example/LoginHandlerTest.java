package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginHandlerTest {
    LoginHandler loginHandler;
    PasswordUtils passwordUtils;

    private User anna;
    private User berit;
    private User kalle;

    @BeforeEach
    void setUp(){

        passwordUtils = new PasswordUtils();
        loginHandler = new LoginHandler(passwordUtils);

        anna = loginHandler.addUser("anna", "losen");
        berit = loginHandler.addUser("berit", "123456");
        kalle = loginHandler.addUser("kalle", "password");

    }


    @Test
    void test_login_success(){
        loginHandler.login("berit", "123456");
        assertTrue(berit.getIsLoggedIn());
    }


}
