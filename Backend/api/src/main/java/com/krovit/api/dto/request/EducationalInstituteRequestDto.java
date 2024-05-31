package com.krovit.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EducationalInstituteRequestDto {

    private Long educationalInstituteId;

    @NotNull(message = "Educational Institute name cannot be null")
    @NotBlank(message = "Educational Institute name cannot be empty")
    @Size(max=50, message = "Educational Institute name must be less than or equal to 50 characters")
    private String educationalInstituteName;

    @NotBlank(message = "Education level cannot be empty")
    @NotNull(message = "Education level cannot be null")
    private String educationLevel;

    @Size(max=255 , message = "Description must be less than or equal to 255 characters")
    private String description;

    private String websiteLink;
    private int educationalInstituteVersion;


}
