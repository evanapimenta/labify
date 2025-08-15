package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.user.UserRoleDTO;
import com.fatec.labify.domain.Branch;
import com.fatec.labify.domain.Laboratory;
import com.fatec.labify.domain.Role;
import com.fatec.labify.domain.User;
import com.fatec.labify.exception.*;
import com.fatec.labify.repository.BranchRepository;
import com.fatec.labify.repository.LaboratoryRepository;
import com.fatec.labify.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class UserRoleService {

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final LaboratoryRepository laboratoryRepository;
    private final AccessControlService accessControlService;

    public UserRoleService(UserRepository userRepository,
                           BranchRepository branchRepository,
                           LaboratoryRepository laboratoryRepository, AccessControlService accessControlService) {
        this.userRepository = userRepository;
        this.branchRepository = branchRepository;
        this.laboratoryRepository = laboratoryRepository;
        this.accessControlService = accessControlService;
    }

    public Page<UserRoleDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserRoleDTO::new);
    }

    @Transactional
    public void assignSuperAdmin(String username, String userId, String labId) {
        User authenticated = userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        validateCanRevokeRole(authenticated, Role.SUPER_ADMIN);

        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Usuário", userId));
        Laboratory laboratory = laboratoryRepository.findById(labId).orElseThrow(() -> new NotFoundException("Laboratório", labId));

        transferRole(
                user,
                Role.SUPER_ADMIN,
                () -> Optional.ofNullable(laboratory.getSuperAdmin()),
                prevUser -> laboratory.setSuperAdmin(null),
                () -> laboratoryRepository.save(laboratory)
        );

        laboratory.setSuperAdmin(user);
        laboratoryRepository.save(laboratory);
    }

    @Transactional
    public void assignAdmin(String username, String userId, String branchId) {
        User authenticated = userRepository.findByEmailIgnoreCase(username).orElseThrow(() -> new NotFoundException("Usuário", username));
        Branch branch = branchRepository.findById(branchId).orElseThrow(() -> new NotFoundException("Filial", branchId));

        accessControlService.validateCanManageLab(authenticated, branch.getLaboratory());
        validateCanRevokeRole(authenticated, Role.ADMIN);
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Usuário", userId));

        transferRole(
                user,
                Role.ADMIN,
                () -> Optional.ofNullable(branch.getAdmin()),
                prevUser -> branch.setAdmin(null),
                () -> branchRepository.save(branch)
        );

        branch.setAdmin(user);
        branchRepository.save(branch);
    }

    @Transactional
    public void transferRole(User newUser, Role newRole,
                             Supplier<Optional<User>> findPreviousHolder,
                             Consumer<User> clearPreviousHolderAssociation,
                             Runnable saveParentEntity) {

        findPreviousHolder.get().ifPresent(prevUser -> {
            clearPreviousHolderAssociation.accept(prevUser);
            prevUser.setRole(Role.PATIENT);
            userRepository.save(prevUser);
        });

        newUser.setRole(newRole);
        userRepository.save(newUser);
        saveParentEntity.run();
    }

    public boolean isUserInRole(User user, Role role) {
        return user.getRole() == role;
    }

    private void validateCanRevokeRole(User user, Role targetRole) {
        if (!(isUserInRole(user, Role.SYSTEM) || (isUserInRole(user, Role.SUPER_ADMIN) && targetRole == Role.ADMIN))) {
            throw new ForbiddenOperationException();
        }
    }

}