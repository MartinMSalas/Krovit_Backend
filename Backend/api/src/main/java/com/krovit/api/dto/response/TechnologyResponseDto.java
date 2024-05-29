package com.krovit.api.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TechnologyResponseDto {

    private Long technologyId;
    @NotBlank
    @NotNull(message = "Technology name cannot be null")
    @Size(max=50, message = "Technology name must be less than or equal to 50 characters")
    private String technologyName;
    private String logo;
    @NotNull(message = "Description cannot be null")
    @Size(max=255 , message = "Description must be less than or equal to 255 characters")
    private String description;
    private String tag;
}
