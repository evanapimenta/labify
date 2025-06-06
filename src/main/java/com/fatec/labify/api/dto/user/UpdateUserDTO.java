package com.fatec.labify.api.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateUserDTO {
    private String name;
    private String email;

    public String getName() {
        return name;
    }

    public UpdateUserDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UpdateUserDTO setEmail(String email) {
        this.email = email;
        return this;
    }
}
