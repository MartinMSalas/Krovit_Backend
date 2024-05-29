package com.krovit.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.krovit.api.dto.request.TechnologyRequestDto;
import com.krovit.api.dto.response.TechnologyResponseDto;
import com.krovit.api.service.TechnologyService;
import com.krovit.api.service.TechnologyServiceImpMySQL;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static java.util.Optional.empty;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TechnologyController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TechnologyControllerTest {

    public static final String TECHNOLOGY_PATH = "/api/v1/technology";
    public static final String TECHNOLOGY_PATH_ID = TECHNOLOGY_PATH + "/{technologyId}";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TechnologyController technologyController;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private TechnologyService technologyService;

    private TechnologyRequestDto requestDto;
    private TechnologyResponseDto responseDto;

    @BeforeEach
    public void setup() {

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

    }

    // creating a new technology with valid input returns a 201 CREATED response with the created technology
    @Test
    public void test_createTechnology_validInput_returnsCreatedResponseWithCreatedTechnology() throws Exception {
        // Arrange

            // Set valid input values for technologyRequestDto


            // Set expected values for createdTechnologyResponseDto


            when(technologyService.createTechnology(requestDto)).thenReturn(Optional.of(responseDto));

        // Act
        mockMvc.perform(post(TECHNOLOGY_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.technologyId", is(1)))
                .andExpect(jsonPath("$.technologyName", is("Java")))
                .andExpect(jsonPath("$.tag", is("tag")))
                .andExpect(jsonPath("$.logo", is("logo")))
                .andExpect(jsonPath("$.description", is("Java is a programming language")));



        // Assert

    }

    // getting all technologies returns a list of all technologies with a 200 OK response

    @Test
    public void test_getAllTechnologies_returnsListOfAllTechnologiesWithOkResponse() throws Exception {
        // Arrange


        List<TechnologyResponseDto> allTechnologiesResponseDto =  Arrays.asList(responseDto, responseDto, responseDto);

        Page<TechnologyResponseDto> technologyResponseDtoPage = new PageImpl<>(allTechnologiesResponseDto);
        // Set expected values for allTechnologiesResponseDto


        when(technologyService.getAllTechnologies(any(), any(), any(), any())).thenReturn(technologyResponseDtoPage);

        // Act
        mockMvc.perform(get(TECHNOLOGY_PATH)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.size()", is(3)));

        // Assert

    }

    // getting a technology by ID that exists returns the technology with a 200 OK response
    @Test
    public void test_getTechnologyById_existingId_returnsTechnologyWithOkResponse() throws Exception {
        // Arrange
        Long id = 1L;

        when(technologyService.getTechnologyById(id)).thenReturn(Optional.of(responseDto));

        // Act
        mockMvc.perform(get(TECHNOLOGY_PATH_ID, id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Assert: The status is OK
                .andExpect(jsonPath("$.technologyId", is(responseDto.getTechnologyId().intValue()))) // Assert: The technologyId matches
                .andExpect(jsonPath("$.technologyName", is(responseDto.getTechnologyName()))) // Assert: The technologyName matches
                .andExpect(jsonPath("$.tag", is(responseDto.getTag()))) // Assert: The tag matches
                .andExpect(jsonPath("$.logo", is(responseDto.getLogo()))) // Assert: The logo matches
                .andExpect(jsonPath("$.description", is(responseDto.getDescription()))); // Assert: The description matches
    }
    // updating a technology with valid input returns the updated technology with a 200 OK response
    @Test
    public void test_updateTechnology_validInput_returnsUpdatedTechnologyWithOkResponse() throws Exception {
        // Arrange
        Long technologyId = 1L;

        // Set valid input values for technologyRequestDto


        // Set expected values for updatedTechnologyResponseDto

//        TechnologyServiceImpMySQL technologyService = mock(TechnologyServiceImpMySQL.class);

        when(technologyService.updateTechnology(technologyId, requestDto)).thenReturn(Optional.of(responseDto));

        // Act
        mockMvc.perform(put(TECHNOLOGY_PATH_ID, technologyId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.technologyId", is(1)))
                .andExpect(jsonPath("$.technologyName", is("Java")))
                .andExpect(jsonPath("$.tag", is("tag")))
                .andExpect(jsonPath("$.logo", is("logo")))
                .andExpect(jsonPath("$.description", is("Java is a programming language")));


        // Assert

    }

    // deleting a technology that exists returns a 204 NO CONTENT response
    @Test
    public void test_deleteTechnology_existingId_returnsNoContentResponse() throws Exception {
        // Arrange
        Long id = 1L;


        // Act
        mockMvc.perform(delete(TECHNOLOGY_PATH_ID, id))
                .andExpect(status().isNoContent());

        // Assert

    }

    // creating a new technology with invalid input returns a 400 BAD REQUEST response
    @Test
    public void test_createTechnology_invalidInput_returnsBadRequestResponse() throws Exception {
        // Arrange

        // Set invalid input values for technologyRequestDto
        TechnologyRequestDto invalidRequestDto = TechnologyRequestDto.builder()
        //      .technologyName("Java")
                .tag("tag")
                .logo("logo")
        //        .description("Java is a programming language")
                .build();

        //when(technologyService.createTechnology(invalidRequestDto)).thenReturn(Optional.of(responseDto));
        mockMvc.perform(post(TECHNOLOGY_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequestDto)))
                .andExpect(status().isBadRequest());

        // Act


        // Assert


    }

    // getting a technology by ID that does not exist returns a 404 NOT FOUND response
    @Test
    public void test_getTechnologyById_nonExistingId_returnsNotFoundResponse() throws Exception {
        // Arrange
        Long id = 1L;

        when(technologyService.getTechnologyById(id)).thenReturn(empty());




        // Act
        mockMvc.perform(get(TECHNOLOGY_PATH_ID, id))
                .andExpect(status().isNotFound());

        // Assert

    }


    // deleting a technology with an ID that does not exist returns a 204 NO CONTENT response
    @Test
    public void test_deleteTechnology_nonExistingId_returnsNoContentResponse() throws Exception {
        // Arrange
        Long id = -1L;



        // Act
        mockMvc.perform(delete(TECHNOLOGY_PATH_ID, id))
                .andExpect(status().isNoContent());
        // Assert

    }



}