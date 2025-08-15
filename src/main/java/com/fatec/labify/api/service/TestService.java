package com.fatec.labify.api.service;

import com.fatec.labify.api.dto.CreateTestDTO;
import com.fatec.labify.api.dto.TestResponseDTO;
import com.fatec.labify.api.dto.UpdateTestDTO;
import com.fatec.labify.domain.Test;
import com.fatec.labify.exception.AlreadyExistsException;
import com.fatec.labify.exception.NotFoundException;
import com.fatec.labify.repository.TestRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TestService {

    private final TestRepository testRepository;

    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    public Page<TestResponseDTO> findAll(Pageable pageable) {
        return testRepository.findAll(pageable).map(TestResponseDTO::new);
    }

    public TestResponseDTO findById(Long id) {
        return new TestResponseDTO(testRepository.findById(id).orElseThrow(() -> new NotFoundException("Teste")));
    }

    @Transactional
    public TestResponseDTO create(CreateTestDTO dto) {
        validateName(dto.getName());
        Test test = new Test(dto.getName(), dto.getDescription(), dto.getCategory(), dto.isSexSpecific(), dto.getSampleType(), dto.getEstimatedResultTime(), dto.getPreparationInstructions());
        testRepository.save(test);
        return new TestResponseDTO(test);
    }

    @Transactional
    public TestResponseDTO update(Long id, UpdateTestDTO dto) {
        Test test = testRepository.findById(id).orElseThrow(() -> new NotFoundException("Test"));

        Optional.ofNullable(dto.getName()).ifPresent(test::setName);
        Optional.ofNullable(dto.getDescription()).ifPresent(test::setDescription);
        Optional.ofNullable(dto.getEstimatedResultTime()).ifPresent(test::setEstimatedResultTime);
        Optional.ofNullable(dto.getPreparationInstructions()).ifPresent(test::setPreparationInstructions);

        testRepository.save(test);
        return new TestResponseDTO(test);
    }

    @Transactional
    public void delete(Long id) {
        Test test = testRepository.findById(id).orElseThrow(() -> new NotFoundException("Test"));
        test.deactivate();
        testRepository.save(test);
    }

    private void validateName(String name) {
        if (testRepository.existsByName(name)) throw new AlreadyExistsException("Exame", "nome", name);
    }

}
