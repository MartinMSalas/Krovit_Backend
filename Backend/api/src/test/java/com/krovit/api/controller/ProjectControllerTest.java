package com.krovit.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krovit.api.dto.request.ProjectRequestDto;
import com.krovit.api.dto.request.TechnologyRequestDto;
import com.krovit.api.dto.response.ProjectResponseDto;
import com.krovit.api.dto.response.TechnologyResponseDto;
import com.krovit.api.entity.Technology;
import com.krovit.api.service.ProjectService;
import com.krovit.api.service.TechnologyService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private TechnologyService technologyService;

    private ProjectRequestDto requestDto;
    private ProjectResponseDto responseDto;

    private TechnologyRequestDto technologyRequestDto;
    private TechnologyResponseDto technologyResponseDto;


    public static final String PROJECT_PATH = "/api/v1/project";
    public static final String PROJECT_PATH_ID = PROJECT_PATH + "/{projectId}";

    private Validator validator;

    @BeforeEach
    public void setup() {
        // setup methods before each test
        technologyRequestDto = TechnologyRequestDto.builder()
                .technologyName("Java")
                .description("Java is a programming language")
                .tag("tag")
                .build();
        technologyResponseDto = TechnologyResponseDto.builder()
                .technologyId(1L)
                .technologyName("Java")
                .description("Java is a programming language")
                .tag("tag")
                .build();
        requestDto = ProjectRequestDto.builder()
                .projectName("Project")
                .projectDescription("Description")
                .certifiedTechnologiesDto(new HashSet<>(Collections.singletonList(technologyRequestDto)))
                .build();
        responseDto = ProjectResponseDto.builder()
                .projectId(1L)
                .projectName("Project")
                .projectDescription("Description")
                .certifiedTechnologiesDto(new HashSet<>(Collections.singletonList(technologyResponseDto)))
                .build();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void test_createProject_validInput_returnsCreatedResponseWithCreatedProject() throws Exception {
        // Arrange
        when(projectService.createProject(any(ProjectRequestDto.class))).thenReturn(Optional.of(responseDto));
        // Act Assert

        mockMvc.perform(post(PROJECT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(jsonPath("$.projectName", is("Project")))
                .andExpect(jsonPath("$.projectDescription", is("Description")))
                .andExpect(jsonPath("$.certifiedTechnologiesDto.size()", is(1)))
                .andExpect(jsonPath("$.certifiedTechnologiesDto[0].technologyName", is("Java")))
                .andExpect(jsonPath("$.certifiedTechnologiesDto[0].tag", is("tag")))
                .andExpect(status().isCreated());

    }

    @Test
    public void test_getAllProjects_returnsListOfAllProjectsWithOkResponse() throws Exception {

        // Arrange
        List<ProjectResponseDto> allProjectResponseDto = Arrays.asList(responseDto, responseDto, responseDto);
        Page<ProjectResponseDto> projectResponseDtoPage = new PageImpl<>(allProjectResponseDto);
        when(projectService.getAllProjects(any(), any(), any())).thenReturn(projectResponseDtoPage);
        // Act

        mockMvc.perform(get(PROJECT_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(3)));
        // Assert
    }

    @Test
    public void test_getProjectById_existingId_returnsProjectWithOkResponse() throws Exception {
        when(projectService.getProjectById(anyLong())).thenReturn(Optional.of(responseDto));
        mockMvc.perform(get(PROJECT_PATH_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.projectName", is("Project")))
                .andExpect(jsonPath("$.projectDescription", is("Description")))
                .andExpect(jsonPath("$.certifiedTechnologiesDto.size()", is(1)))
                .andExpect(jsonPath("$.certifiedTechnologiesDto[0].technologyName", is("Java")))
                .andExpect(jsonPath("$.certifiedTechnologiesDto[0].tag", is("tag")))
                .andExpect(status().isOk());
    }

    @Test
    public void test_updateProject_validInput_returnsUpdatedProjectWithOkResponse() throws Exception {
        when(projectService.updateProject(anyLong(), any(ProjectRequestDto.class))).thenReturn(Optional.of(responseDto));
        mockMvc.perform(put(PROJECT_PATH_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(jsonPath("$.projectName", is("Project")))
                .andExpect(jsonPath("$.projectDescription", is("Description")))
                .andExpect(jsonPath("$.certifiedTechnologiesDto.size()", is(1)))
                .andExpect(jsonPath("$.certifiedTechnologiesDto[0].technologyName", is("Java")))
                .andExpect(jsonPath("$.certifiedTechnologiesDto[0].tag", is("tag")))
                .andExpect(status().isOk());
    }

    @Test
    public void test_deleteProject_existingId_returnsNoContentResponse() throws Exception {
        mockMvc.perform(delete(PROJECT_PATH_ID, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void test_addTechnologyToProject_validInput_returnsCreatedResponseWithCreatedTechnology() throws Exception {
        when(technologyService.createTechnology(any(TechnologyRequestDto.class))).thenReturn(Optional.of(technologyResponseDto));
        String technologyRequestDtoJson = objectMapper.writeValueAsString(technologyRequestDto);
        mockMvc.perform(post("/api/v1/project/1/technology")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(technologyRequestDtoJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void test_getTechnologiesForProject_existingId_returnsListOfTechnologiesWithOkResponse() throws Exception {
//ResponseEntity<Set<TechnologyResponseDto>>
        when(projectService.getTechnologiesForProject(anyLong())).thenReturn(new HashSet<>(Collections.singletonList(technologyResponseDto)));
        //when(projectService.getProjectById(anyLong())).thenReturn(Optional.of(responseDto));
        mockMvc.perform(get("/api/v1/project/1/technology")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].technologyId", is(1)))
                .andExpect(jsonPath("$[0].technologyName", is("Java")))
                .andExpect(jsonPath("$[0].tag", is("tag")))
                .andExpect(status().isOk());
    }

    @Test
    public void test_removeTechnologyFromProject_existingId_returnsNoContentResponse() throws Exception {
        mockMvc.perform(delete("/api/v1/project/1/technology/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    @Test
    public void test_createProject_invalidInput_returnsBadRequestResponse() throws Exception {
    //    when(projectService.createProject(any(ProjectRequestDto.class))).thenReturn(Optional.empty());
        mockMvc.perform(post(PROJECT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getProjectById_nonExistingId_returnsNotFoundResponse() throws Exception {
        when(projectService.getProjectById(anyLong())).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/project/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_updateProject_nonExistingId_returnsNotFoundResponse() throws Exception {
        when(projectService.updateProject(anyLong(), any(ProjectRequestDto.class))).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/v1/project/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_addTechnologyToProject_invalidInput_returnsBadRequestResponse() throws Exception {
        //when(technologyService.createTechnology(any(TechnologyRequestDto.class))).thenReturn(Optional.empty());
        mockMvc.perform(post("/api/v1/project/1/technology")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getTechnologiesForProject_nonExistingId_returnsEmptySet() throws Exception {
        when(projectService.getTechnologiesForProject(anyLong())).thenReturn(Collections.emptySet());
        mockMvc.perform(get("/api/v1/project/1/technology")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(0)))
                .andExpect(status().isOk());
    }

    @Test
    public void test_removeTechnologyFromProject_nonExistingId_returnsNotFoundResponse() throws Exception {
        doThrow(new NoSuchElementException("Technology with id 1 not found in project with id 1")).when(projectService).removeTechnologyFromProject(anyLong(), anyLong());
        mockMvc.perform(delete("/api/v1/project/1/technology/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", is("Technology with id 1 not found in project with id 1")))
                .andExpect(status().isNotFound());

                //.andExpect(result -> assertTrue(result.getResolvedException() instanceof NoSuchElementException));
                //.andExpect(result -> assertEquals("Technology with id 1 not found in project with id 1", Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    public void test_createProject_invalidProjectNameLength_returnsBadRequestResponse() throws Exception {
        // Arrange
        // Set invalid projectName for projectRequestDto
        ProjectRequestDto invalidRequestDto = ProjectRequestDto.builder()
                .projectName("This is a very long project name that is definitely more than fifty characters long")
                .projectDescription("Valid project description")
                .build();

        // Act
        // Validate the invalidRequestDto
        Set<ConstraintViolation<ProjectRequestDto>> violations = validator.validate(invalidRequestDto);
        assertFalse(violations.isEmpty(), "Project name must be less than or equal to 50 characters Not Validated");
        mockMvc.perform(post(PROJECT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_createProject_invalidProjectDescriptionNameLength_returnsBadRequestResponse() throws Exception {
        // Arrange
        when(projectService.createProject(any(ProjectRequestDto.class))).thenReturn(Optional.of(responseDto));
        // Set invalid projectDescription for projectRequestDto
        ProjectRequestDto invalidRequestDto = ProjectRequestDto.builder()
                .projectName("Valid project name")
                .projectDescription("This is a very long project description that is definitely more than two hundred and fifty five characters long. This is a very long project description that is definitely more than two hundred and fifty five characters long. This is a very long project description that is definitely more than two hundred and fifty five characters long. This is a very long project description that is definitely more than two hundred and fifty five characters long. This is a very long project description that is definitely more than two hundred and fifty five characters long. This is a very long project description that is definitely more than two hundred and fifty five characters long.")
                //.projectDescription("valid project description")
                .build();

        // Act
        // Validate the invalidRequestDto
        Set<ConstraintViolation<ProjectRequestDto>> violations = validator.validate(invalidRequestDto);
        assertFalse(violations.isEmpty(), "Project description must be less than or equal to 255 characters Not Validated");
        mockMvc.perform(post(PROJECT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());
        System.out.println(mockMvc);
    }
}