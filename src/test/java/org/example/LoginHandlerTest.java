package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Disabled
    @Test
    void test_login_success() throws MissingTokenException{
        loginHandler.login("berit", "123456");
        assertTrue(berit.getIsLoggedIn());
    }

    @Test
    void test_login_return_token_success() throws MissingTokenException{
        String token = berit.getToken();
        System.out.println("from test: " + token);
        assertEquals(token, loginHandler.login("berit", "123456"));
    }


    @Test
    void test_login_return_token_fail(){
        MissingTokenException tokenException = assertThrows(MissingTokenException.class, () ->
                loginHandler.login(berit.getUserName(), "test"));

        assertEquals("Missing token", tokenException.getMessage());
    }


}
