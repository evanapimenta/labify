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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/labs")
public class LaboratoryController {
    private final LaboratoryService laboratoryService;

    public LaboratoryController(LaboratoryService laboratoryService) {
        this.laboratoryService = laboratoryService;
    }

    @GetMapping
    public Page<LaboratoryResponseDTO> findAll(@ParameterObject Pageable pageable) {
        return laboratoryService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryResponseDTO> findById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(laboratoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CreateLaboratoryResponseDTO> create(@Valid @RequestBody CreateLaboratoryDTO createLaboratoryDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new CreateLaboratoryResponseDTO
                (laboratoryService.create(createLaboratoryDTO)));
    }

    @PutMapping("/active/{id}")
    public ResponseEntity<Void> changeActive(@PathVariable String id, boolean status) {
        laboratoryService.changeActive(id, status);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryResponseDTO> update(@Valid @RequestBody UpdateLaboratoryDTO updateLaboratoryDTO,
                                                        @PathVariable String id) {
        laboratoryService.update(id, updateLaboratoryDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new LaboratoryResponseDTO(laboratoryService.update(id, updateLaboratoryDTO)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        laboratoryService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
