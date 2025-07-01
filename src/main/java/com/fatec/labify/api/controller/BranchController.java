package com.fatec.labify.api.controller;

import com.fatec.labify.api.dto.CreateBranchDTO;
import com.fatec.labify.api.dto.UpdateBranchDTO;
import com.fatec.labify.api.dto.branch.BranchDTO;
import com.fatec.labify.api.dto.laboratory.CreateLaboratoryResponseDTO;
import com.fatec.labify.api.service.BranchService;
import com.fatec.labify.domain.Branch;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/branches")
public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping
    public Page<BranchDTO> findAll(@AuthenticationPrincipal UserDetails userDetails,
                                    @ParameterObject Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(branchService.findAll(userDetails.getUsername(), pageable)).getBody();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDTO> findById(@AuthenticationPrincipal UserDetails userDetails,
                                              @PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(branchService.findById(userDetails.getUsername(), id));
    }

    @PostMapping
    public ResponseEntity<BranchDTO> create(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody CreateBranchDTO createBranchDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new BranchDTO(branchService.create(userDetails.getUsername(), createBranchDTO)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable String id,
                                       @RequestBody UpdateBranchDTO updateBranchDTO) {
        branchService.update(userDetails.getUsername(), id, updateBranchDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable String id) {
        branchService.delete(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
