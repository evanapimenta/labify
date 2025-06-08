package com.fatec.labify.exception;


public class PatientNotFoundException extends NotFoundException {
    private static final String RESOURCE_NAME = "Paciente";

    public PatientNotFoundException(String identifier) {
        super(String.format("%s %s", RESOURCE_NAME, identifier));
    }
}
