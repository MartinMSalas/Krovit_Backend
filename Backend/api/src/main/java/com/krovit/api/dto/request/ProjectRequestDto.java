package com.krovit.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class ProjectRequestDto {

    private Long projectId;

    @NotBlank
    @NotNull(message = "Project name cannot be null")
    @Size(max=50, message = "Project name must be less than or equal to 50 characters")
    private String projectName;
    @NotNull(message = "Project description cannot be null")
    @Size(max=255 , message = "Project description must be less than or equal to 255 characters")
    private String projectDescription;

    private String websiteLink;
    private String sourceCodeLink;

    private Set<TechnologyRequestDto> certifiedTechnologiesDto;

}
