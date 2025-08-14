package com.fatec.labify.api.controller;

import com.fatec.labify.api.dto.laboratory.CreateLaboratoryDTO;
import com.fatec.labify.api.dto.laboratory.CreateLaboratoryResponseDTO;
import com.fatec.labify.api.dto.laboratory.LaboratoryResponseDTO;
import com.fatec.labify.api.dto.laboratory.UpdateLaboratoryDTO;
import com.fatec.labify.api.service.LaboratoryService;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/labs")
public class LaboratoryController {
    private final LaboratoryService laboratoryService;

    public LaboratoryController(LaboratoryService laboratoryService) {
        this.laboratoryService = laboratoryService;
    }

    @GetMapping
    public ResponseEntity<Page<LaboratoryResponseDTO>> findAll(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(laboratoryService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryResponseDTO> findById(@AuthenticationPrincipal UserDetails userDetails,
                                                          @PathVariable String id) {
        return ResponseEntity.ok(laboratoryService.findById(id, userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<CreateLaboratoryResponseDTO> create(@RequestBody @Valid CreateLaboratoryDTO dto) {
        CreateLaboratoryResponseDTO laboratoryDTO = laboratoryService.create(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(laboratoryDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(laboratoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryResponseDTO> update(@AuthenticationPrincipal UserDetails userDetails,
                                                        @PathVariable String id,
                                                        @Valid @RequestBody UpdateLaboratoryDTO updateLaboratoryDTO) {
        return ResponseEntity.ok(laboratoryService.update(id, updateLaboratoryDTO, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable String id) {
        laboratoryService.deactivate(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable String id,
                                             @AuthenticationPrincipal UserDetails userDetails) {
        laboratoryService.activate(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
