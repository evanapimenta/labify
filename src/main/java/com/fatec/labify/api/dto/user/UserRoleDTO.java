package com.fatec.labify.api.dto.user;

import com.fatec.labify.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserRoleDTO {

    private String id;

    private String name;

    private String email;

    private String role;

    private LocalDateTime createdAt;

    public UserRoleDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.createdAt = user.getCreatedAt();
    }
}
