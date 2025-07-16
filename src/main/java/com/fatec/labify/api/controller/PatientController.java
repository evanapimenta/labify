package com.fatec.labify.api.controller;


import com.fatec.labify.api.dto.patient.CreatePatientDTO;
import com.fatec.labify.api.dto.patient.CreatePatientResponseDTO;
import com.fatec.labify.api.dto.patient.PatientResponseDTO;
import com.fatec.labify.api.dto.patient.UpdatePatientDTO;
import com.fatec.labify.api.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<Page<PatientResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok().body(patientService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> findById(@PathVariable String id,
                                                       @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(patientService.findById(id, userDetails.getUsername()));
    }

    @PostMapping("/{id}")
    public ResponseEntity<CreatePatientResponseDTO> create(@PathVariable String id,
                                                           @Valid @RequestBody CreatePatientDTO createPatientDTO) {
        CreatePatientResponseDTO patientDTO = patientService.create(id, createPatientDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(patientDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(patientDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable String id,
                                       @Valid @RequestBody UpdatePatientDTO updatePatientDTO) {
        patientService.update(id, updatePatientDTO, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails,
                                       @PathVariable String id) {
        patientService.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}