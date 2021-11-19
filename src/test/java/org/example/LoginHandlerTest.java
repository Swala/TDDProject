package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
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

    @Test
    void test_login_return_token_success() throws MissingTokenException{
        String token = loginHandler.login(berit.getUserName(), "123456").get(berit.getUserName());

        assertEquals(token,loginHandler.tokens.get(berit.getUserName()));
    }


    @Test
    void test_login_return_token_fail(){
        MissingTokenException tokenException = assertThrows(MissingTokenException.class, () ->
                loginHandler.login(berit.getUserName(), "test"));

        assertEquals("Missing token", tokenException.getMessage());
    }

    @Test
    void test_return_valid_token_success() throws MissingTokenException{
        Map <String, String> token = loginHandler.login(kalle.getUserName(), "password");

        assertTrue(loginHandler.isTokenValid(token.get(kalle.getUserName()), kalle.getUserName()));
        assertFalse(loginHandler.isTokenValid("TTTTTT", kalle.getUserName()));
    }

    /*
    @Test
    void test_return_user_permissions_success(){
        //anrop med token och resurs ska returnera lista med rättigheter


        assertEquals(List.of("READ"), anna.getPermissions());

    }*/

}

/*
 @Disabled
    @Test
    void test_login_success() throws MissingTokenException{
        loginHandler.login("berit", "123456");
        assertTrue(berit.getIsLoggedIn());
    }
     */
