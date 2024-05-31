package com.krovit.api.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EducationalInstituteResponseDto {

    private Long educationalInstituteId;

    @NotBlank
    @NotNull(message = "Educational Institute name cannot be null")
    @Size(max=50, message = "Educational Institute name must be less than or equal to 50 characters")
    private String educationalInstituteName;

    @NotBlank(message = "Education level cannot be empty")
    @NotNull(message = "Education level cannot be null")
    private String educationLevel;

    private String description;
    private String websiteLink;

    private int educationalInstituteVersion;

    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
