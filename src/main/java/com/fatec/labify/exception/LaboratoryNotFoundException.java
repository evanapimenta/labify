package com.fatec.labify.exception;

public class LaboratoryNotFoundException extends NotFoundException {
    private static final String RESOURCE_NAME = "Laboratório";

    public LaboratoryNotFoundException(String identifier) {
        super(String.format("%s %s", RESOURCE_NAME, identifier));
    }
}
