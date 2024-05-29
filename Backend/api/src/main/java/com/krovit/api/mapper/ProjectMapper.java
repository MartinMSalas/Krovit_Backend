package com.krovit.api.mapper;
import com.krovit.api.dto.request.ProjectRequestDto;
import com.krovit.api.dto.response.ProjectResponseDto;
import com.krovit.api.dto.response.TechnologyResponseDto;
import com.krovit.api.entity.Project;
import com.krovit.api.entity.Technology;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(uses = TechnologyMapper.class)
public interface ProjectMapper {
    @Mapping(source = "certifiedTechnologies", target = "certifiedTechnologiesDto")
    ProjectResponseDto projectToProjectResponseDto(Project project);

    @Mapping(source = "certifiedTechnologiesDto", target = "certifiedTechnologies")
    Project projectRequestDtoToProject(ProjectRequestDto projectRequestDto);

    Set<TechnologyResponseDto> technologySetToTechnologyResponseDtoSet(Set<Technology> technologies);
    Set<Technology> technologyResponseDtoSetToTechnologySet(Set<TechnologyResponseDto> technologyResponseDtos);

}
