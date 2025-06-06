package com.fatec.labify.exception;

public class UserNotFoundException extends NotFoundException {
    private static final String RESOURCE_NAME = "Usuário";

    public UserNotFoundException(String identifier) {
        super(String.format("%s %s", RESOURCE_NAME, identifier));
    }
}
