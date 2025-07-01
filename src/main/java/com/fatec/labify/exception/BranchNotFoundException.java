package com.fatec.labify.exception;

public class BranchNotFoundException extends NotFoundException {
    private static final String RESOURCE_NAME = "Filial";

    public BranchNotFoundException(String identifier) {
        super(String.format("%s %s", RESOURCE_NAME, identifier));
    }
}
