package com.revature.controller;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.exception.*;
import io.javalin.Javalin;
import io.javalin.http.ExceptionHandler;

import io.javalin.http.UnauthorizedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.SizeLimitExceededException;
import javax.security.auth.login.FailedLoginException;

public class ExceptionController implements Controller{

    Logger logger = LoggerFactory.getLogger(ExceptionController.class);

    private ExceptionHandler<IllegalArgumentException> invalidIntegerParam = ((exception, ctx) -> {
        logger.warn(exception.getLocalizedMessage());
        ctx.status(400);
        ctx.json(exception.getMessage());
    });

    private ExceptionHandler<MismatchedInputException> invalidOrIncompleteBodySent = ((exception, ctx) -> {
        logger.warn(exception.getLocalizedMessage());
        ctx.status(418);
        ctx.json(exception.getMessage());
    });

    private ExceptionHandler<FailedLoginException> failedLogin = ((exception, ctx) -> {
        logger.warn(exception.getLocalizedMessage());
        ctx.status(400);
        ctx.json(exception.getMessage());
    });
    private ExceptionHandler<UnauthorizedResponse> unauthorizedUser = ((exception, ctx) -> {
        logger.warn(exception.getLocalizedMessage());
        ctx.status(401);
        ctx.json(exception.getMessage());
    });

    private ExceptionHandler<SizeLimitExceededException> fileSizeTooLarge = ((exception, ctx) -> {
        logger.warn(exception.getMessage());
        ctx.status(413);
        ctx.json(exception.getMessage());
    });

    private ExceptionHandler<InvalidFileTypeException> invalidFileType = ((exception, ctx) -> {
        logger.warn(exception.getLocalizedMessage());
        ctx.status(400);
        ctx.json(exception.getLocalizedMessage());
    });

    private ExceptionHandler<IndexOutOfBoundsException> noFileUploaded = (((exception, ctx) -> {
        logger.warn("User did not upload an image file.");
        ctx.status(400);
        ctx.json("You must upload an image of your receipt.");
    }));
    private ExceptionHandler<ReimbursementDoesNotExist> reimbursementNotFound = (((exception, ctx) -> {
        logger.warn("User attempted to access a reimbursement that does not exist. Message: " + exception.getMessage());
        ctx.status(404);
        ctx.json(exception.getMessage());
    }));

    private ExceptionHandler<InvalidJsonBodyProvided> invalidJsonBody = (((exception, ctx) -> {
        logger.warn("User provided invalid JSON body. Message: " + exception.getMessage());
        ctx.status(400);
        ctx.json(exception.getMessage());
    }));

    private ExceptionHandler<UserDoesNotExist> userNotFound = (((exception, ctx) -> {
        logger.warn("User attempted to access a user that does not exist. Message: " + exception.getMessage());
        ctx.status(404);
        ctx.json(exception.getMessage());
    }));

    private ExceptionHandler<ReimbursementAlreadyResolved> reimbursementResolved = (((exception, ctx) -> {
        logger.warn("User attempted to edit a reimbursement that has already been resolved. Message: " + exception.getMessage());
        ctx.status(403);
        ctx.json(exception.getMessage());
    }));

    private ExceptionHandler<InvalidQueryParamProvided> invalidQueryParam = (((exception, ctx) -> {
        logger.warn("User attempted to provide an invalid value to a Query Parameter. Message: " + exception.getMessage());
        ctx.status(400);
        ctx.json(exception.getMessage());
    }));

    @Override
    public void mapEndpoints(Javalin app) {
        app.exception(FailedLoginException.class, failedLogin);
        app.exception(IllegalArgumentException.class, invalidIntegerParam);
        app.exception(UnauthorizedResponse.class, unauthorizedUser);
        app.exception(MismatchedInputException.class, invalidOrIncompleteBodySent);
        app.exception(SizeLimitExceededException.class, fileSizeTooLarge);
        app.exception(InvalidFileTypeException.class, invalidFileType);
        app.exception(IndexOutOfBoundsException.class, noFileUploaded);
        app.exception(ReimbursementDoesNotExist.class, reimbursementNotFound);
        app.exception(UserDoesNotExist.class, userNotFound);
        app.exception(ReimbursementAlreadyResolved.class, reimbursementResolved);
        app.exception(InvalidQueryParamProvided.class, invalidQueryParam);
        app.exception(InvalidJsonBodyProvided.class, invalidJsonBody);
    }
}