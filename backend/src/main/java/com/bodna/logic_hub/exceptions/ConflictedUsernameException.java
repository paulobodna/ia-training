package com.bodna.logic_hub.exceptions;

public class ConflictedUsernameException extends RuntimeException {
    public ConflictedUsernameException(String message) {
        super(message);
    }
}
