package com.revature.controller;

import com.revature.dto.UserDTO;
import com.revature.service.JWTService;
import com.revature.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserController implements Controller{


    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private JWTService jwtService;
    private UserService userService;

    public UserController() {
        this.jwtService = new JWTService();
        this.userService = new UserService();
    }

    private Handler getUser = (ctx) -> {
        if (ctx.header("Authorization") == null) {
            throw new UnauthorizedResponse("You must be logged in to access this endpoint.");
        }
        String jwt = ctx.header("Authorization").split(" ")[1];

        this.jwtService.parseJwt(jwt);
        String userId = ctx.pathParam("user_id");
        UserDTO userInfo = userService.getUserInfo(userId);

        logger.info("User requested information on user " + userId + ".");
        ctx.status(200);
        ctx.json(userInfo);
    };

    private Handler getAllUsers = (ctx) -> {

        List<UserDTO> users = userService.getAllUsers();
        logger.info("User requested information on all users.");
        ctx.status(200);
        ctx.json(users);
    };

    @Override
    public void mapEndpoints(Javalin app) {

        app.get("/users/{user_id}", getUser);
        app.get("/users", getAllUsers);

    }
}