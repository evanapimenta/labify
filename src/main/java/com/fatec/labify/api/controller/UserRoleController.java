package com.fatec.labify.api.controller;

import com.fatec.labify.api.service.UserRoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;

@RestController
@RequestMapping("/roles")
public class UserRoleController {
    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping("/super-admin")
    public ResponseEntity<Void> assignSuperAdmin(@RequestParam String userId,
                                                 @RequestParam String labId) {
        userRoleService.assignSuperAdmin(userId, labId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/admin")
    public ResponseEntity<Void> assignAdmin(@RequestParam String userId,
                                            @RequestParam String branchId) {
        userRoleService.assignAdmin(userId, branchId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/{userId}/revoke-role")
    public ResponseEntity<Void> revokeRole(@PathVariable String userId, @RequestParam String roleToRevoke) throws RoleNotFoundException {
        userRoleService.revokeRole(userId, roleToRevoke);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
