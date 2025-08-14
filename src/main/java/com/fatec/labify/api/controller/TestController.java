package com.fatec.labify.api.controller;


import com.fatec.labify.api.dto.CreateTestDTO;
import com.fatec.labify.api.dto.TestResponseDTO;
import com.fatec.labify.api.dto.UpdateTestDTO;
import com.fatec.labify.api.dto.user.UserResponseDTO;
import com.fatec.labify.api.service.TestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/tests")
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping
    public ResponseEntity<Page<TestResponseDTO>> findAll(Pageable pageable) {
        return ResponseEntity.ok(testService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TestResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(testService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TestResponseDTO> create(@RequestBody CreateTestDTO dto) {
        TestResponseDTO testDTO = testService.create(dto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(testDTO.getId())
                .toUri();

        return ResponseEntity.created(location).body(testDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TestResponseDTO> update(@PathVariable Long id, @RequestBody UpdateTestDTO dto) {
        return ResponseEntity.ok(testService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        testService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
