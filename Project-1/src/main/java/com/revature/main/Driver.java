package com.revature.main;

import com.revature.controller.*;
import io.javalin.Javalin;

public class Driver {


    public static void main(String[] args) {
        Javalin app = Javalin.create((config) -> {
            config.enableCorsForAllOrigins();
        });

        map(app, new AuthenticationController(), new ExceptionController(), new ReimbursementController(), new UserController());


        app.start(8080);
    }

    public static void map (Javalin app, Controller... controllers){
        for (Controller c : controllers){
            c.mapEndpoints(app);
        }
    }
}
