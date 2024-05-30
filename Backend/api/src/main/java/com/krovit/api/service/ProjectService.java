package com.krovit.api.service;


import com.krovit.api.dto.request.ProjectRequestDto;
import com.krovit.api.dto.request.TechnologyRequestDto;
import com.krovit.api.dto.response.ProjectResponseDto;
import com.krovit.api.dto.response.TechnologyResponseDto;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.Set;

public interface ProjectService {

        Optional<ProjectResponseDto> createProject(ProjectRequestDto projectRequestDto);

        Page<ProjectResponseDto> getAllProjects(String projectName, Integer pageNumber, Integer pageSize);

        Optional<ProjectResponseDto> getProjectById(Long id);

        Optional<ProjectResponseDto> updateProject(Long id, ProjectRequestDto projectRequestDto);

        void deleteProject(Long id);

        Optional<ProjectResponseDto> addTechnologyToProject(Long projectId, TechnologyRequestDto technologyRequestDto);

        Optional<ProjectResponseDto> removeTechnologyFromProject(Long projectId, Long technologyId);

        Set<TechnologyResponseDto> getTechnologiesForProject(Long projectId);
}
