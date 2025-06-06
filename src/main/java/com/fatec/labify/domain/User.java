package com.fatec.labify.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fatec.labify.exception.InvalidTokenException;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

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
    @Column(nullable = false)
    private Role role;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    private String token;

    private boolean verified;

    private LocalDateTime tokenExpiresIn;

    private LocalDateTime lastLoginAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Patient patient;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
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

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public boolean getActive() {
        return active;
    }

    public User setActive(boolean active) {
        this.active = active;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public User setRole(Role role) {
        this.role = role;
        return this;
    }

    public String getToken() {
        return token;
    }

    public User setToken(String token) {
        this.token = token;
        return this;
    }

    public Boolean getVerified() {
        return verified;
    }

    public User setVerified(Boolean verified) {
        this.verified = verified;
        return this;
    }

    public LocalDateTime getTokenExpiresIn() {
        return tokenExpiresIn;
    }

    public User setTokenExpiresIn(LocalDateTime tokenExpiresIn) {
        this.tokenExpiresIn = tokenExpiresIn;
        return this;
    }

    public Patient getPatient() {
        return patient;
    }

    public User setPatient(Patient patient) {
        this.patient = patient;
        return this;
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