package com.revature.controller;

import com.revature.dto.LoginDTO;
import com.revature.model.User;
import com.revature.service.JWTService;
import com.revature.service.UserService;
import io.javalin.Javalin;
import io.javalin.http.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AuthenticationController implements Controller {

    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    private UserService userService;
    private JWTService jwtService;

    public AuthenticationController() {
        this.userService = new UserService();
        this.jwtService = new JWTService();
    }

    private Handler login = (ctx) -> {
        logger.info("Login endpoint invoked.");

        LoginDTO loginInfo = ctx.bodyAsClass(LoginDTO.class);

        User user = userService.login(loginInfo);

        System.out.println(user);

        String jwt = jwtService.createJWT(user);

        ctx.header("Access-Control-Expose-Headers", "*");
        ctx.header("Token", jwt);
        ctx.json(user);
    };



    @Override
    public void mapEndpoints(Javalin app) {
        app.post("/login", login);
    }
}