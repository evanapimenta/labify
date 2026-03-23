package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.authentication.TokenData;
import com.fatec.labify.api.dto.authentication.UpdatePasswordDTO;
import com.fatec.labify.api.dto.user.CreateUserDTO;
import com.fatec.labify.api.dto.user.UpdateUserDTO;
import com.fatec.labify.api.dto.user.UserResponseDTO;
import com.fatec.labify.domain.User;
import com.fatec.labify.domain.UserToken;
import com.fatec.labify.exception.*;
import com.fatec.labify.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }

    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponseDTO::new);
    }

    public UserResponseDTO findById(String id, String username) {
        return new UserResponseDTO(validateUser(id, username));
    }

    @Transactional
    public UserResponseDTO create(CreateUserDTO dto) {
        validateEmail(dto.getEmail());
        User user = new User(dto.getName(), dto.getEmail().toLowerCase(), passwordEncoder.encode(dto.getPassword()));
        emailService.sendVerificationEmail(user);
        userRepository.save(user);
        return new UserResponseDTO(user);
    }

    @Transactional
    public TokenData verifyAccount(String token) {
        User user = userRepository.findByToken(token).orElseThrow();
        user.verify();
        return new TokenData(tokenService.generateToken(user), tokenService.generateRefreshToken(user));
    }

    @Transactional
    public UserResponseDTO update(String id, UpdateUserDTO updateUserDTO, String username) {
        User user = validateUser(id, username);
        Optional.ofNullable(updateUserDTO.getName()).ifPresent(user::setName);

        if (updateUserDTO.getEmail() != null) {
            user = updateEmail(id, updateUserDTO.getEmail());
        }

        userRepository.save(user);
        return new UserResponseDTO(user);
    }

    @Transactional
    public String updateImage(String id, MultipartFile file, String username) throws IOException {
        User user = validateUser(id, username);
        Path path = Paths.get("uploads/profile-images/");

        if (user.getImagePathUrl() != null) {
            Path oldFile = path.resolve(
                    Paths.get(user.getImagePathUrl()).getFileName());
            Files.deleteIfExists(oldFile);
        }

        String originalName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        String fileName = UUID.randomUUID() + "-" + originalName;
        Path uploadPath = path;
        Files.createDirectories(uploadPath);

        Path filePath = uploadPath.resolve(fileName);
        file.transferTo(filePath);

        String urlPath = "/images/profile/" + fileName;
        user.setImagePathUrl(urlPath);
        userRepository.save(user);

        return urlPath;
    }

    private User updateEmail(String id, String newEmail) {
        validateEmail(newEmail);
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário", id));
        user.setEmail(newEmail);
        return user;
    }


    @Transactional
    public void changePassword(String id, String username, UpdatePasswordDTO updatePasswordDTO) {
        User user = validateUser(id, username);
        user.setPassword(passwordEncoder.encode(updatePasswordDTO.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void delete(String id, String username) {
        User user = validateUser(id, username);
        userRepository.delete(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
    }

    public User validateUser(String id, String username) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("Usuário", id));
        if (!user.isVerified()) {
            throw new UserNotVerifiedException();
        }
        if (!Objects.equals(username, user.getUsername())) {
            throw new ForbiddenOperationException();
        }
        return user;
    }

    private void validateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistsException("Usuário", "email", email);
        }
    }
}

