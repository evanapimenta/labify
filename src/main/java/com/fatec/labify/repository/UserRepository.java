package com.fatec.labify.repository;

import com.fatec.labify.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailIgnoreCase(String username);
    Optional<User> findByToken(String code);
}
