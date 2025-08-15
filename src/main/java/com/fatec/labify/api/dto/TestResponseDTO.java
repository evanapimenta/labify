package com.fatec.labify.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fatec.labify.domain.Test;
import com.fatec.labify.domain.TestCategory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestResponseDTO {

    private Long id;

    private String name;

    private String description;

    private TestCategory testCategory;

    private boolean sexSpecific;

    private String sampleType;

    private String estimatedResultTime;

    private String preparationInstructions;

    private boolean isActive;

    private LocalDateTime createdAt;

    public TestResponseDTO(Test test) {
        this.id = test.getId();
        this.name = test.getName();
        this.description = test.getDescription();
        this.testCategory = test.getCategory();
        this.sexSpecific = test.isSexSpecific();
        this.sampleType = test.getSampleType();
        this.estimatedResultTime = test.getEstimatedResultTime();
        this.preparationInstructions = test.getPreparationInstructions();
        this.isActive = test.isActive();
        this.createdAt = test.getCreatedAt();
    }

}
