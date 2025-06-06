package com.fatec.labify.api.dto.authentication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdatePasswordDTO {

    @NotBlank(message = "A senha não pode estar em branco")
    private String oldPassword;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
            message = "Senha deve ter letras maiúsculas, minúsculas e números"
    )
    @NotBlank(message = "A senha não pode estar em branco") @Size(min = 8)
    private String password;

    public String getOldPassword() {
        return oldPassword;
    }

    public UpdatePasswordDTO setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UpdatePasswordDTO setPassword(String password) {
        this.password = password;
        return this;
    }

}
