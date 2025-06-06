package com.fatec.labify.exception;

public class UserAlreadyExistsException extends AlreadyExistsException {
    private static final String RESOURCE_NAME = "Usuário";

    public UserAlreadyExistsException(String propertyName,
                                      String identifier)
    { super(RESOURCE_NAME, propertyName, identifier); }
}
