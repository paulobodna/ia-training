package com.bodna.logic_hub.exceptions;

public class ConflictedEmailException extends RuntimeException {
    public ConflictedEmailException(String message) {
        super(message);
    }
}
