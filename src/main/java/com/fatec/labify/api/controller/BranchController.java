package com.fatec.labify.api.controller;

import com.fatec.labify.api.dto.branch.CreateBranchDTO;
import com.fatec.labify.api.dto.branch.CreateBranchResponseDTO;
import com.fatec.labify.api.dto.branch.UpdateBranchDTO;
import com.fatec.labify.api.dto.branch.BranchResponseDTO;
import com.fatec.labify.api.service.BranchService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/{labId}")
    public ResponseEntity<Page<BranchResponseDTO>> findAll(@AuthenticationPrincipal UserDetails userDetails,
                                                           @PathVariable String labId,
                                                           @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(branchService.findByLabId(userDetails.getUsername(), labId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> findById(@AuthenticationPrincipal UserDetails userDetails,
                                                      @PathVariable String id) {
        return ResponseEntity.ok(branchService.findById(userDetails.getUsername(), id));
    }

    @GetMapping("/by-area")
    public ResponseEntity<Page<BranchResponseDTO>> findByArea(@AuthenticationPrincipal UserDetails userDetails,
                                                              @RequestParam double lat,
                                                              @RequestParam double lon,
                                                              @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(branchService.getClosestBranches(userDetails.getUsername(), lat, lon, limit));
    }

    @PostMapping
    public ResponseEntity<CreateBranchResponseDTO> create(@RequestBody CreateBranchDTO createBranchDTO) {
        return ResponseEntity.ok(branchService.create(createBranchDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BranchResponseDTO> update(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable String id,
                                       @RequestBody UpdateBranchDTO updateBranchDTO) {
        return ResponseEntity.ok(branchService.update(userDetails.getUsername(), id, updateBranchDTO));
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@AuthenticationPrincipal UserDetails userDetails,
                                         @PathVariable String id) {
        branchService.activate(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                                           @PathVariable String id) {
        branchService.delete(userDetails.getUsername(), id);
        return ResponseEntity.noContent().build();
    }
}
