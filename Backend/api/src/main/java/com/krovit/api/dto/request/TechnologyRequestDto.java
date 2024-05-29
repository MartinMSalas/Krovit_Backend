package com.krovit.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TechnologyRequestDto {

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
    private LocalDateTime createdDate;
    private LocalDateTime updateDate;
}
