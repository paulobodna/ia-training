package com.bodna.logic_hub.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bodna.logic_hub.dtos.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSudokuSolutionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSudokuSolution(
        InvalidSudokuSolutionException e
    ) {
        return ResponseEntity
            .badRequest()
            .body(new ErrorResponse("invalid_solution", e.getMessage()));
    }

    @ExceptionHandler(InvalidSudokuException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSudoku(
        InvalidSudokuException e
    ) {
        return ResponseEntity
            .internalServerError()
            .body(new ErrorResponse("invalid_board", e.getMessage()));
    }

    @ExceptionHandler(ConflictedEmailException.class)
    public ResponseEntity<ErrorResponse> handleConflictedEmail(
        ConflictedEmailException e
    ) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse("conflicted_email", e.getMessage()));
    }

    @ExceptionHandler(ConflictedUsernameException.class)
    public ResponseEntity<ErrorResponse> handleConflictedUsername(
        ConflictedUsernameException e
    ) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ErrorResponse("conflicted_username", e.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFound(
        UsernameNotFoundException e
    ) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("user_not_found", e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(
        UserNotFoundException e
    ) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("user_not_found", e.getMessage()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(
        InvalidCredentialsException e
    ) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse("invalid_credentials", e.getMessage()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadFormattedCredentials(
        BadCredentialsException e
    ) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse("invalid_credentials", "Invalid email or password"));
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidToken(
        JWTVerificationException e
    ) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(new ErrorResponse("invalid_token", e.getMessage()));
    }
}