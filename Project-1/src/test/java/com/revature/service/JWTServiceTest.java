package com.revature.service;

import com.revature.model.User;
import com.revature.model.UserRole;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class JWTServiceTest {

    private JWTService jwtService = new JWTService();
    private UserRole userAdmin = new UserRole(1, "admin");
    private UserRole userManager = new UserRole(2, "Finance-Manager");

    @Test
    public void testCreateValidJwt() {
        String jwt = jwtService.createJWT(new User(1, "admin", "Jane", "Doe", "test@email.com", userAdmin));

        Assertions.assertEquals(145, jwt.length());
    }

    @Test
    public void testCreatesDifferentJwtsForDifferentUsers(){

        String jwt1 = jwtService.createJWT(new User(1, "admin", "Jane", "Doe", "test@email.com", userAdmin));
        String jwt2 = jwtService.createJWT(new User(2, "user", "John", "Doe", "test2@email.com", userManager));


        Assertions.assertNotEquals(jwt1, jwt2);
    }

    @Test
    public void testParseValidJWT() {
        User user = new User(1, "admin", "Jane", "Doe", "test@email.com", userAdmin);
        String jwt = jwtService.createJWT(user);

        Jws<Claims> token = jwtService.parseJwt(jwt);

        String username = token.getBody().getSubject();
        Object id = token.getBody().get("user_id");
        Object role = token.getBody().get("user_role_id");

        Assertions.assertEquals(user.getId(), id);
        Assertions.assertEquals(user.getUsername(), username);
        Assertions.assertEquals(user.getUserRole().getId(), role);
    }

    @Test
    public void testParseInvalidJWT() {
        String jwt = jwtService.createJWT(new User(1, "administrator", "Sneak", "Jones", "fake@email.com", userAdmin));


        String invalidJwt = jwt.substring(0, jwt.length() -2) + "@";

        Assertions.assertThrows(UnauthorizedResponse.class, () ->{
            jwtService.parseJwt(invalidJwt);
        });
    }
}