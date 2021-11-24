package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
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

        anna = loginHandler.addUser("anna", "losen", List.of(Permission.READ.toString()));
        berit = loginHandler.addUser("berit", "123456", List.of(Permission.READ.toString(), Permission.WRITE.toString()));
        kalle = loginHandler.addUser("kalle", "password", List.of(Permission.EXECUTE.toString()));

        loginHandler.addUserToResourceAccess(Resource.ACCOUNT.toString(), List.of(anna, berit));
        loginHandler.addUserToResourceAccess(Resource.PROVISION_CALC.toString(), List.of(kalle));
    }

    @Test
    void test_login_return_token_success() throws MissingTokenException{
        String token = loginHandler.login(berit.getUserName(), "123456");
        int tokenID = loginHandler.getTokenId(token);

        assertEquals(token, loginHandler.tokens.get(tokenID).getToken());
    }


    @Test
    void test_login_return_token_fail(){
        MissingTokenException tokenException = assertThrows(MissingTokenException.class, () ->
                loginHandler.login(berit.getUserName(), "test"));

        assertEquals("Missing token", tokenException.getMessage());
    }

    @Test
    void test_return_valid_token_success() throws MissingTokenException{
        String token = loginHandler.login(kalle.getUserName(), "password");

        assertTrue(loginHandler.isTokenValid(token));
        assertFalse(loginHandler.isTokenValid("TTTTTT"));
    }

    @Test
    void test_return_user_permissions_success() throws MissingTokenException{
        //resurs är en tjänst som vissa users har tillgång till
        String token = loginHandler.login(anna.getUserName(), "losen");
        List<String> permissions = loginHandler.getUserPermissions(token, Resource.ACCOUNT.toString());

        assertEquals(List.of("READ"), permissions);
    }

}

