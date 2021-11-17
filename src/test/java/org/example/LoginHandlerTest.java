package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginHandlerTest {
    LoginHandler loginHandler;

    private User anna;
    private User berit;
    private User kalle;

    @BeforeEach
    void setUp(){
        loginHandler = new LoginHandler();
        anna = loginHandler.addUser("anna", "losen");
        berit = loginHandler.addUser("berit", "123456");
        kalle = loginHandler.addUser("kalle", "password");
    }


    @Test
    void test_login_success(){
        loginHandler.login(anna.getUserName(), anna.getPassword());

        assertTrue(anna.getIsLoggedIn());
    }


}
