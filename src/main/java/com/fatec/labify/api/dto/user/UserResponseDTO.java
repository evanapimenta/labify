package com.fatec.labify.api.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fatec.labify.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponseDTO {

    private String id;

    private String email;

    private String name;

    private String imagePathUrl;

    private LocalDateTime createdAt;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.imagePathUrl = user.getImagePathUrl();
        this.createdAt = user.getCreatedAt();
    }

}
