package com.revature.controller;

import com.revature.dto.ResponseReimbursementDTO;
import com.revature.dto.UpdateReimbursementDTO;
import com.revature.dto.AddReimbursementDTO;
import com.revature.dto.UpdateReimbursementStatusDTO;
import com.revature.model.Reimbursement;

import com.revature.service.JWTService;
import com.revature.service.ReimbursementService;
import com.revature.service.UserService;

import io.javalin.Javalin;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.List;

public class ReimbursementController implements Controller{

    private JWTService jwtService;
    private ReimbursementService reimbursementService;
    private UserService userService;

    public ReimbursementController() {
        this.jwtService = new JWTService();
        this.reimbursementService = new ReimbursementService();
        this.userService = new UserService();
    }


    private Handler addReimbursement = (ctx) -> {
        if (ctx.header("Authorization") == null) {
            throw new UnauthorizedResponse("You must be logged in to access this endpoint.");
        }
        String jwt = ctx.header("Authorization").split(" ")[1];

        Jws<Claims> token = this.jwtService.parseJwt(jwt);


        String userId = ctx.pathParam("user_id");
        if(!(""+token.getBody().get("user_id")).equals(userId)) {
            throw new UnauthorizedResponse("You cannot add a reimbursement request for anyone but yourself.");
        }

        AddReimbursementDTO dto = new AddReimbursementDTO();
        dto.setReimbAmount(Double.parseDouble(ctx.formParam("amount")));
        dto.setReimbAuthor((Integer)token.getBody().get("user_id"));
        dto.setReimbDescription(ctx.formParam("description"));
        dto.setReimbType(Integer.parseInt(ctx.formParam("type")));
        dto.setReimbReceiptImage(ctx.uploadedFiles().get(0));
        ResponseReimbursementDTO newReimbursement = reimbursementService.addReimbursement(dto);
        ctx.status(201);
        ctx.json(newReimbursement);

    };

    private Handler getAllReimbursements = (ctx) -> {
        if (ctx.header("Authorization") == null) {
            throw new UnauthorizedResponse("You must be logged in to access this endpoint.");
        }
        String jwt = ctx.header("Authorization").split(" ")[1];

        Jws<Claims> token = this.jwtService.parseJwt(jwt);

        if (((Integer) token.getBody().get("user_role_id")) == 2) {
            throw new UnauthorizedResponse("You must be a Finance-Manager to access this endpoint.");
        }

        String status = ctx.queryParam("status");

        List<ResponseReimbursementDTO> reimbursements;

        if (status != null) {
            reimbursements = reimbursementService.getAllReimbursementsByStatus(status);
        } else {
            reimbursements = reimbursementService.getAllReimbursements();
        }
        ctx.status(200);
        ctx.json(reimbursements);
    };


    private Handler getReimbursementById = (ctx) -> {
        if (ctx.header("Authorization") == null) {
            throw new UnauthorizedResponse("You must be logged in to access this endpoint.");
        }
        String jwt = ctx.header("Authorization").split(" ")[1];

        Jws<Claims> token = this.jwtService.parseJwt(jwt);

        String userId = ctx.pathParam("user_id");

        if((Integer) (token.getBody().get("user_role_id")) == 2) {
            if (!(""+token.getBody().get("user_id")).equals(userId)){
                throw new UnauthorizedResponse("You cannot access a reimbursement that does not belong to you.");
            }
        }
        String reimbId = ctx.pathParam("reimb_id");
        Reimbursement reimbursement = reimbursementService.getReimbursementById(reimbId);
        ctx.status(200);
        ctx.json(reimbursement);

    };

    private Handler getAllReimbursementsForUser = (ctx) -> {
        if (ctx.header("Authorization") == null) {
            throw new UnauthorizedResponse("You must be logged in to access this endpoint.");
        }
        String jwt = ctx.header("Authorization").split(" ")[1];

        Jws<Claims> token = this.jwtService.parseJwt(jwt);

        String userId = ctx.pathParam("user_id");
        if((Integer) (token.getBody().get("user_role_id")) == 2) {
            if (!("" + token.getBody().get("user_id")).equals(userId)) {
                throw new UnauthorizedResponse("You cannot access reimbursements that do not belong to you.");
            }
        }
        String status = ctx.queryParam("status");
        List<ResponseReimbursementDTO> reimbursements;
        if (status != null) {
            reimbursements = reimbursementService.getReimbursementsByUserAndStatus(userId, status);
        } else {
            reimbursements = reimbursementService.getReimbursementsByUser(userId);
        }
        ctx.status(200);
        ctx.json(reimbursements);
    };

    private Handler editUnresolvedReimbursement = (ctx) -> {
        if (ctx.header("Authorization") == null) {
            throw new UnauthorizedResponse("You must be logged in to access this endpoint.");
        }
        String jwt = ctx.header("Authorization").split(" ")[1];

        Jws<Claims> token = this.jwtService.parseJwt(jwt);

        String userId = ctx.pathParam("user_id");
        if (!(""+token.getBody().get("user_id")).equals(userId)) {
            throw new UnauthorizedResponse("You cannot edit a reimbursement that does not belong to you.");
        }
        String reimbId = ctx.pathParam("reimb_id");
        if (!(reimbursementService.getReimbursementById(reimbId).getEmail().equals(userService.getUserInfo(userId).getEmail()))) {
            throw new UnauthorizedResponse("You cannot edit a reimbursement that does not belong to you.");
        }

        UpdateReimbursementDTO reimbursementDetails = ctx.bodyAsClass(UpdateReimbursementDTO.class);

        Reimbursement reimbursement = reimbursementService.editUnresolvedReimbursement(reimbId, reimbursementDetails);
        ctx.status(201);
        ctx.json(reimbursement);
    };

    private Handler updateReimbursementStatus = (ctx) -> {
        if (ctx.header("Authorization") == null) {
            throw new UnauthorizedResponse("You must be logged in to access this endpoint.");
        }
        String jwt = ctx.header("Authorization").split(" ")[1];

        Jws<Claims> token = this.jwtService.parseJwt(jwt);
        int userRoleId = (Integer) token.getBody().get("user_role_id");
        if(!(userRoleId == 1)) {
            throw new UnauthorizedResponse("You must be a finance manager to approve or deny reimbursements.");
        }
        String reimbId = ctx.pathParam("reimb_id");
        if (reimbursementService.getReimbursementById(reimbId).getEmail().equals(userService.getUserInfo(""+token.getBody().get("user_id")).getEmail())) {
            throw new UnauthorizedResponse("You cannot approve or deny your own reimbursement ticket.");
        }
        UpdateReimbursementStatusDTO dto = ctx.bodyAsClass(UpdateReimbursementStatusDTO.class);
        dto.setResolverId((Integer) token.getBody().get("user_id"));
        boolean success = reimbursementService.updateReimbursementStatus(dto, reimbId);

        ctx.status(201);
        ctx.json(success);
    };

    private Handler deleteUnresolvedReimbursement = (ctx) -> {
        if (ctx.header("Authorization") == null) {
            throw new UnauthorizedResponse("You must be logged in to access this endpoint.");
        }
        String jwt = ctx.header("Authorization").split(" ")[1];

        Jws<Claims> token = this.jwtService.parseJwt(jwt);

        String userId = ctx.pathParam("user_id");
        if (!(""+token.getBody().get("user_id")).equals(userId)) {
            throw new UnauthorizedResponse("You cannot edit a reimbursement that does not belong to you.");
        }
        String reimbId = ctx.pathParam("reimb_id");
        if (!(reimbursementService.getReimbursementById(reimbId).getEmail().equals(userService.getUserInfo(userId).getEmail()))) {
            throw new UnauthorizedResponse("You cannot edit a reimbursement that does not belong to you.");
        }
        boolean success = reimbursementService.deleteUnresolvedReimbursement(reimbId);
        ctx.status(200);
        ctx.json(success);
    };



    @Override
    public void mapEndpoints(Javalin app) {

        app.post("/users/{user_id}/reimbursements", addReimbursement);

        app.get("/users/{user_id}/reimbursements", getAllReimbursementsForUser);
        app.get("/reimbursements", getAllReimbursements);
        app.get("/users/{user_id}/reimbursements/{reimb_id}", getReimbursementById);

        app.put("/users/{user_id}/reimbursements/{reimb_id}", editUnresolvedReimbursement);
        app.patch("/reimbursements/{reimb_id}", updateReimbursementStatus);
        app.delete("/users/{user_id}/reimbursements/{reimb_id}", deleteUnresolvedReimbursement);
    }
}