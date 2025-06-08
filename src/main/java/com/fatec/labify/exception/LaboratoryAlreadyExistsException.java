package com.fatec.labify.exception;

public class LaboratoryAlreadyExistsException extends AlreadyExistsException {
    private static final String RESOURCE_NAME = "Laboratório";

    public LaboratoryAlreadyExistsException(String propertyName,
                                            String identifier)
    { super(RESOURCE_NAME, propertyName, identifier); }
}
