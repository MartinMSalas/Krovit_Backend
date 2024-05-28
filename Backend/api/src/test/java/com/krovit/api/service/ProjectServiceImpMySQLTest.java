package com.krovit.api.service;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.krovit.api.dto.request.ProjectRequestDto;
import com.krovit.api.dto.request.TechnologyRequestDto;
import com.krovit.api.dto.response.ProjectResponseDto;
import com.krovit.api.dto.response.TechnologyResponseDto;
import com.krovit.api.entity.Project;
import com.krovit.api.entity.Technology;
import com.krovit.api.mapper.ProjectMapper;
import com.krovit.api.mapper.TechnologyMapper;
import com.krovit.api.repository.ProjectRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

//import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImpMySQLTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;
    @Mock
    private TechnologyMapper technologyMapper;

    @InjectMocks
    private ProjectServiceImpMySQL projectService;

    private TechnologyRequestDto requestDto;
    private Technology technology;
    private TechnologyResponseDto responseDto;
    private ProjectRequestDto projectRequestDto;
    private ProjectResponseDto  projectResponseDto;
    private Project project;

    @BeforeEach
    void setUp() {
        technology = Technology.builder()
                .technologyId(1L)
                .technologyName("Java")
                .tag("tag")
                .logo("logo")
                .description("Java is a programming language")
                .build();
        requestDto = TechnologyRequestDto.builder()
                .technologyName("Java")
                .tag("tag")
                .logo("logo")
                .description("Java is a programming language")
                .build();
        responseDto = TechnologyResponseDto.builder()
                .technologyId(1L)
                .technologyName("Java")
                .tag("tag")
                .logo("logo")
                .description("Java is a programming language")
                .build();
        project = Project.builder()
                .projectId(1L)
                .projectName("Project")
                .certifiedTechnology(technology)
                .projectDescription("Project Description")
                .build();
        projectRequestDto = ProjectRequestDto.builder()
                .projectName("Project")
                .projectDescription("Project Description")
                .build();
        projectResponseDto = ProjectResponseDto.builder()
                .projectId(1L)
                .projectName("Project")
                .projectDescription("Project Description")
                .build();

    }

    @Test
    void test_createProject_validInput_returnsProjectResponseDto() {
        // Arrange
        when(projectMapper.projectRequestDtoToProject(projectRequestDto)).thenReturn(project);
        when(projectMapper.projectToProjectResponseDto(project)).thenReturn(projectResponseDto);
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Optional<ProjectResponseDto> result = projectService.createProject(projectRequestDto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(projectResponseDto, result.get());
    }

    @Test
    void test_getAllProjects_validInput_returnsPage() {
        // Arrange
        Integer pageNumber = 1;
        Integer pageSize = 10;
        when(projectRepository.findAll()).thenReturn(null);

    }

    @Test
    void test_getProjectById_existingId_returnsOptionalProjectResponseDto() {
        // Arrange
        Long id = 1L;
        when(projectMapper.projectToProjectResponseDto(project)).thenReturn(projectResponseDto);
        when(projectRepository.findById(id)).thenReturn(Optional.of(project));

        // Act
        Optional<ProjectResponseDto> result = projectService.getProjectById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(projectResponseDto, result.get());
    }

    @Test
    void test_updateProject_existingId_returnsOptionalProjectResponseDto() {
        // Arrange
        Long id = 1L;
        when(projectMapper.projectToProjectResponseDto(project)).thenReturn(projectResponseDto);
        when(projectRepository.findById(id)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Optional<ProjectResponseDto> result = projectService.updateProject(id, projectRequestDto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(projectResponseDto, result.get());
    }

    @Test
    void test_deleteProject_existingId_returnsVoid() {
        // Arrange
        Long id = 1L;

        // Act
        projectService.deleteProject(id);

        // Assert
    }

    @Test
    void test_addTechnologyToProject_validInput_returnsProjectResponseDto() {
        // Arrange
        Long projectId = 1L;
        when(projectMapper.projectToProjectResponseDto(project)).thenReturn(projectResponseDto);
        when(technologyMapper.technologyRequestDtoToTechnology(requestDto)).thenReturn(technology);
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(project);

        // Act
        Optional<ProjectResponseDto> projectResponseDtoCall = projectService.addTechnologyToProject(projectId, requestDto);


        // Assert
        assertTrue(projectResponseDtoCall.isPresent());
        assertEquals(projectResponseDto, projectResponseDtoCall.get());
    }

    @Test
    void removeTechnologyFromProject() {
    }

    @Test
    void getTechnologiesForProject() {
    }
}