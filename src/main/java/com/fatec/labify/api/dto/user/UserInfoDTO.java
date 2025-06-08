package com.fatec.labify.api.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fatec.labify.domain.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDTO {
    private String id;
    private String email;
    private String name;

    public UserInfoDTO() {}

    public UserInfoDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public String getId() {
        return id;
    }

    public UserInfoDTO setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserInfoDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public UserInfoDTO setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "UserInfoDTO{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
