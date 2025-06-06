package com.fatec.labify.api.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserDTO {
    @NotBlank(message = "O nome não pode estar em branco")
    private String name;

    @Email(message = "Email inválido")
    @NotBlank(message = "O email não pode estar em branco")
    private String email;

    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$",
            message = "Senha deve ter letras maiúsculas, minúsculas e números"
    )
    @NotBlank(message = "A senha não pode estar em branco") @Size(min = 8)
    private String password;

    public String getName() {
        return name;
    }

    public CreateUserDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public CreateUserDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public CreateUserDTO setPassword(String password) {
        this.password = password;
        return this;
    }
 }

