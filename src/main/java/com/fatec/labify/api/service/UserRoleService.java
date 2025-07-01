package com.fatec.labify.api.service;

import com.fatec.labify.domain.Branch;
import com.fatec.labify.domain.Laboratory;
import com.fatec.labify.domain.Role;
import com.fatec.labify.domain.User;
import com.fatec.labify.exception.*;
import com.fatec.labify.repository.BranchRepository;
import com.fatec.labify.repository.LaboratoryRepository;
import com.fatec.labify.repository.UserRepository;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Objects;

@Service
public class UserRoleService {
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final LaboratoryRepository laboratoryRepository;

    public UserRoleService(UserRepository userRepository,
                           BranchRepository branchRepository,
                           LaboratoryRepository laboratoryRepository) {
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.laboratoryRepository = laboratoryRepository;
    }

    @Transactional
    public void assignSuperAdmin(String userId, String labId) {
        if (!isUserInRole("ROLE_SYSTEM")) {
            throw new ForbiddenOperationException("Apenas SYSTEM pode atribuir SUPER_ADMIN");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Laboratory laboratory = laboratoryRepository.findById(labId).orElseThrow(() -> new LaboratoryNotFoundException(labId));

        if (laboratory.getSuperAdmin() != null) {
            User prevSuperAdmin = userRepository.findById(laboratory.getSuperAdmin().getId()).orElseThrow(() -> new UserNotFoundException(laboratory.getSuperAdmin().getId()));
            prevSuperAdmin.setRole(null);

            userRepository.save(prevSuperAdmin);
        }

        user.setRole(Role.SUPER_ADMIN);
        userRepository.save(user);

        laboratory.setSuperAdmin(user);
        laboratoryRepository.save(laboratory);
    }

    @Transactional
    public void assignAdmin(String userId, String branchId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!hasBranchAccess(username, null)) {
            throw new ForbiddenOperationException("Apenas SYSTEM ou SUPER_ADMIN podem atribuir ADMIN");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new BranchNotFoundException(branchId));

        if (branch.getAdmin() != null) {
            User prevAdmin = userRepository.findById(branch.getAdmin().getId()).orElseThrow(() -> new UserNotFoundException(branch.getAdmin().getId()));
            prevAdmin.setRole(null);

            userRepository.save(prevAdmin);
        }

        user.setRole(Role.ADMIN);
        userRepository.save(user);

        branch.setAdmin(user);
        branchRepository.save(branch);
    }

    public User validateUserAccess(String id, String username) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (!user.getVerified()) {
            throw new UserNotVerifiedException("Usuário não verificado");
        }

        if (!Objects.equals(user.getUsername(), username)) {
            throw new ForbiddenOperationException("Acesso não autorizado");
        }

        return user;
    }
    
    public boolean hasBranchAccess(String username, @Nullable String branchId) {
        if (isUserInRole("ROLE_SYSTEM")) {
            return true;
        }

        if (isUserInRole("ROLE_SUPER_ADMIN") && branchId != null) {
            User user = userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new UserNotFoundException(username));
            return branchRepository.isSuperAdmin(user.getId(), branchId);
        }

        return false;
    }

    private boolean isUserInRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals(role));
    }

    private boolean canRevokeRole(String targetRole) {
        if (isUserInRole("ROLE_SYSTEM")) {
            return true;
        }

        if (isUserInRole("ROLE_SUPER_ADMIN") && "ROLE_ADMIN".equals(targetRole)) {
            return true;
        }

        return false;
    }

    public void revokeRole(String userId, String roleToRevoke) throws RoleNotFoundException {
        if (!canRevokeRole(roleToRevoke)) {
            throw new ForbiddenOperationException("Você não tem permissão para realizar esta operação");
        }

        User revokeUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        Role userRole = revokeUser.getRole();

        if (userRole == null || !userRole.name().equals(roleToRevoke)) {
            throw new RoleNotFoundException(roleToRevoke);
        }

        if (userRole == Role.ADMIN) {
            revokeAdminRole(revokeUser);
        } else if (userRole == Role.SUPER_ADMIN) {
            revokeSuperAdminRole(revokeUser);
        }

        userRepository.save(revokeUser);
    }

    @Transactional
    private void revokeSuperAdminRole(User revokeUser) {
        Laboratory laboratory = laboratoryRepository.findBySuperAdminId(revokeUser.getId()).orElseThrow(() -> new UserNotInRoleException("Usuário" + revokeUser.getId() + "não tem o papel."));
        laboratory.setSuperAdmin(null);
        laboratoryRepository.save(laboratory);

        revokeUser.setRole(null);
        userRepository.save(revokeUser);
    }

    @Transactional
    private void revokeAdminRole(User revokeUser) {
        Branch branch = branchRepository.findByAdminId(revokeUser.getId()).orElseThrow(() -> new UserNotInRoleException("Usuário" + revokeUser.getId() + "não tem o papel."));
        branch.setAdmin(null);
        branchRepository.save(branch);

        revokeUser.setRole(null);
        userRepository.save(revokeUser);
    }

}