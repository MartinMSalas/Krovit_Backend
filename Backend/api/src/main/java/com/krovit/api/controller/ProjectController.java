package com.krovit.api.controller;

import com.krovit.api.dto.request.ProjectRequestDto;
import com.krovit.api.dto.request.TechnologyRequestDto;
import com.krovit.api.dto.response.ProjectResponseDto;
import com.krovit.api.dto.response.TechnologyResponseDto;
import com.krovit.api.mapper.TechnologyMapperImpl;
import com.krovit.api.service.ProjectService;
import com.krovit.api.service.TechnologyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@RestController
public class ProjectController {

    public static final String PROJECT_PATH = "/api/v1/project";
    public static final String PROJECT_PATH_ID = PROJECT_PATH + "/{projectId}";

    private final ProjectService projectService;
    private final TechnologyService technologyService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectController.class);
    private final TechnologyMapperImpl technologyMapperImpl;

    public ProjectController(ProjectService projectService, TechnologyService technologyService, TechnologyMapperImpl technologyMapperImpl) {
        this.projectService = projectService;
        this.technologyService = technologyService;
        this.technologyMapperImpl = technologyMapperImpl;
    }

    @PostMapping(PROJECT_PATH)
    public ResponseEntity<ProjectResponseDto> createProject(@Validated @RequestBody ProjectRequestDto project) {
        LOGGER.info("Creating project: {}", project);
        Optional<ProjectResponseDto> createdProject = projectService.createProject(project);
        if(createdProject.isPresent()) {
            LOGGER.info("Project created successfully: {}", createdProject.get());
            return new ResponseEntity<>(createdProject.get(), HttpStatus.CREATED);
        } else {
            LOGGER.error("Failed to create project: {}", project);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(PROJECT_PATH)
    public Page<ProjectResponseDto> getAllProjects(@RequestParam(required = false) String projectName,
                                                   @RequestParam(required = false) Integer pageNumber,
                                                   @RequestParam(required = false) Integer pageSize) {
        return projectService.getAllProjects(projectName, pageNumber, pageSize);
    }

    @GetMapping(PROJECT_PATH_ID)
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable Long projectId) {
        Optional<ProjectResponseDto> project = projectService.getProjectById(projectId);
        if(project.isPresent()) {
            return new ResponseEntity<>(project.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(PROJECT_PATH_ID)
    public ResponseEntity<ProjectResponseDto> updateProject(@PathVariable Long projectId, @RequestBody ProjectRequestDto project) {
        Optional<ProjectResponseDto> updatedProject = projectService.updateProject(projectId, project);
        if(updatedProject.isPresent()) {
            return new ResponseEntity<>(updatedProject.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(PROJECT_PATH_ID)
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId) {
        projectService.deleteProject(projectId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Technology endpoints
    // save new technology to project

    @PostMapping(PROJECT_PATH + "/{projectId}/technology")
    public ResponseEntity<TechnologyResponseDto> addNewTechnologyToProject(@PathVariable Long projectId, @Validated @RequestBody TechnologyRequestDto technology) {

        Optional<TechnologyResponseDto> createdTechnology = technologyService.createTechnology(technology);
        if(createdTechnology.isPresent()) {
            projectService.addTechnologyToProject(projectId, technologyMapperImpl.technologyResponseDtoToTechnologyRequestDto(createdTechnology.get()));

            return new ResponseEntity<>(createdTechnology.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    //
    @PostMapping(PROJECT_PATH + "/{projectId}/technology/{technologyId}")
    public ResponseEntity<TechnologyResponseDto> addTechnologyToProject(@PathVariable Long projectId, @Validated @PathVariable Long technologyId) {

        Optional<TechnologyResponseDto> createdTechnology = technologyService.getTechnologyById(technologyId);

        if(createdTechnology.isPresent()) {

            projectService.addTechnologyToProject(projectId, technologyMapperImpl.technologyResponseDtoToTechnologyRequestDto(createdTechnology.get()));

            return new ResponseEntity<>(createdTechnology.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(PROJECT_PATH + "/{projectId}/technology")
    public ResponseEntity<Set<TechnologyResponseDto>> getTechnologiesForProject(@PathVariable Long projectId) {
        Set<TechnologyResponseDto> technologies = projectService.getTechnologiesForProject(projectId);

        return new ResponseEntity<>(technologies, HttpStatus.OK);
    }

    @DeleteMapping(PROJECT_PATH + "/{projectId}/technology/{technologyId}")
    public ResponseEntity<?> removeTechnologyFromProject(@PathVariable Long projectId, @PathVariable Long technologyId) {

        try {
            projectService.removeTechnologyFromProject(projectId, technologyId);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
