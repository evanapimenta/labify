package com.fatec.labify.api.controller;

import com.fatec.labify.api.dto.user.UserRoleDTO;
import com.fatec.labify.api.service.UserRoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class UserRoleController {
    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @GetMapping
    public ResponseEntity<Page<UserRoleDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(userRoleService.findAll(pageable));
    }

    @PostMapping("/super-admin")
    public ResponseEntity<Void> assignSuperAdmin(@AuthenticationPrincipal UserDetails userDetails,
                                                 @RequestParam String userId,
                                                 @RequestParam String labId) {
        userRoleService.assignSuperAdmin(userDetails.getUsername(), userId, labId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin")
    public ResponseEntity<Void> assignAdmin(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestParam String userId,
                                            @RequestParam String branchId) {
        userRoleService.assignAdmin(userDetails.getUsername(), userId, branchId);
        return ResponseEntity.ok().build();
    }
}
