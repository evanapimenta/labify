package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.authentication.UpdatePasswordDTO;
import com.fatec.labify.api.dto.user.CreateUserDTO;
import com.fatec.labify.api.dto.user.UpdateUserDTO;
import com.fatec.labify.api.dto.user.UserResponseDTO;
import com.fatec.labify.domain.Role;
import com.fatec.labify.domain.User;
import com.fatec.labify.exception.UserAlreadyExistsException;
import com.fatec.labify.exception.UserNotFoundException;
import com.fatec.labify.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserRoleService userRoleService;

    public UserService(UserRepository userRepository, AuthenticationService authenticationService, PasswordEncoder passwordEncoder, EmailService emailService, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userRoleService = userRoleService;
    }

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponseDTO::new);
    }

    public UserResponseDTO findById(String id, String username) {
        userRoleService.validateUserCredentials(id, username);
        return userRepository.findById(id).map(UserResponseDTO::new).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Transactional
    public User create(CreateUserDTO createUserDTO) {
        User user = validateCreateUser(createUserDTO);
        emailService.sendVerificationEmail(user);
        return userRepository.save(user);
    }

    @Transactional
    public void verifyAccount(String code) {
        User user = userRepository.findByToken(code).orElseThrow();
        user.verify();
    }

    @Transactional
    public void update(String id, UpdateUserDTO updateUserDTO, String username) {
        User user = userRoleService.validateUserCredentials(id, username);
        if (updateUserDTO.getName() != null) user.setName(updateUserDTO.getName());

        if (updateUserDTO.getEmail() != null) updateEmail(id, updateUserDTO.getEmail());
        userRepository.save(user);
    }

    @Transactional
    public void delete(String id, String username) {
        User user = userRoleService.validateUserCredentials(id, username);
        userRepository.delete(user);
    }

    @Transactional
    public void changePassword(String id, String username, UpdatePasswordDTO updatePasswordDTO) {
        User user = userRoleService.validateUserCredentials(id, username);
        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getPassword()));
        user.updateLastLogin();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
    }

    private User validateCreateUser(CreateUserDTO createUserDTO) {
        Optional<User> existingByEmail = userRepository.findByEmailIgnoreCase(createUserDTO.getEmail());

        if (existingByEmail.isPresent()) {
            throw new UserAlreadyExistsException("email", createUserDTO.getEmail());
        }

        return createUser(createUserDTO);
    }

    private User createUser(CreateUserDTO createUserDTO) {
        return new User()
                .setId(UUID.randomUUID().toString())
                .setName(createUserDTO.getName())
                .setEmail(createUserDTO.getEmail().toLowerCase())
                .setPassword(passwordEncoder.encode(createUserDTO.getPassword()))
                .setRole(Role.PATIENT)
                .setActive(false)
                .setVerified(false)
                .setToken(UUID.randomUUID().toString())
                .setTokenExpiresIn(LocalDateTime.now().plusMinutes(30));
    }

    private void updateEmail(String id, String newEmail) {
        if (userRepository.findByEmailIgnoreCase(newEmail).isPresent()) {
            throw new UserAlreadyExistsException("email", newEmail);
        }

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        user.setEmail(newEmail);
        userRepository.save(user);
    }

    private UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO()
                .setId(user.getId())
                .setName(user.getName())
                .setEmail(user.getEmail())
                .setCreatedAt(user.getCreatedAt());
    }
}

