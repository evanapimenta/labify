package com.fatec.labify.api.controller;

import com.fatec.labify.api.dto.authentication.UpdatePasswordDTO;
import com.fatec.labify.api.dto.user.CreateUserDTO;
import com.fatec.labify.api.dto.user.UpdateUserDTO;
import com.fatec.labify.api.dto.user.UserResponseDTO;
import com.fatec.labify.api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(pageable)).getBody();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable String id,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id, userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> create(@Valid @RequestBody CreateUserDTO createUserDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserResponseDTO(userService.create(createUserDTO)));
    }

    @GetMapping("/verify-account")
    public ResponseEntity<String> verifyAccount(@RequestParam String code) {
        userService.verifyAccount(code);
        return ResponseEntity.ok("Conta verificada com sucesso!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable String id,
                                       @RequestBody UpdateUserDTO updateUserDTO) {
        userService.update(id, updateUserDTO, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable String id,
                                               @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        userService.changePassword(id, userDetails.getUsername(), updatePasswordDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable String id) {
        userService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

}
