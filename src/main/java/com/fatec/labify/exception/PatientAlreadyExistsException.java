package com.fatec.labify.exception;

public class PatientAlreadyExistsException extends AlreadyExistsException {
    private static final String RESOURCE_NAME = "Paciente";

    public PatientAlreadyExistsException(String propertyName,
                                      String identifier)
    { super(RESOURCE_NAME, propertyName, identifier); }

}
