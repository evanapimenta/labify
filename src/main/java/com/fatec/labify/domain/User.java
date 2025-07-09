package com.fatec.labify.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fatec.labify.exception.InvalidTokenException;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    private String id;

    private String name;

    private String email;

    private String password;

    private boolean active;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private String token;

    private boolean verified;

    private LocalDateTime tokenExpiresIn;

    private LocalDateTime lastLoginAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Patient patient;

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
        this.active = false;
        this.verified = false;
        this.createdAt = LocalDateTime.now();
        this.token = UUID.randomUUID().toString();
        this.tokenExpiresIn = LocalDateTime.now().plusMinutes(30);
    }

    public User() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return Collections.emptyList();
        }

        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public Boolean getVerified() {
        return verified;
    }

    public LocalDateTime getTokenExpiresIn() {
        return tokenExpiresIn;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void verify() {
        if (tokenExpiresIn.isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Link de verificação expirado!");
        }
        this.verified = true;
        this.active = true;
        this.token = null;
        this.tokenExpiresIn = null;
    }

}