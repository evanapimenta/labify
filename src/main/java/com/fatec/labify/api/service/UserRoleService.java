package com.fatec.labify.api.service;

import com.fatec.labify.domain.User;
import com.fatec.labify.exception.ForbiddenOperationException;
import com.fatec.labify.exception.UserNotFoundException;
import com.fatec.labify.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    public UserRoleService(UserRepository userRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
    }

    public User validateUserCredentials(String id, String username) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        authenticationService.validateVerified(user);

        if (!user.getUsername().equals(username)) {
            throw new ForbiddenOperationException("Acesso não autorizado");
        }

        return user;
    }

    public void validateSystemCredentials() {
        boolean isSystem = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SYSTEM"));

        if (!isSystem) {
            throw new ForbiddenOperationException("Acesso não autorizado");
        }
    }

    public User validateAdminCredentials(String id, String username) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        authenticationService.validateVerified(user);

        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!user.getUsername().equals(username) && !isAdmin) {
            throw new ForbiddenOperationException("Acesso não autorizado");
        }

        return user;
    }

}