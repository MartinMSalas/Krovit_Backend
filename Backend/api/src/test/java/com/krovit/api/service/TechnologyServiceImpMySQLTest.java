package com.krovit.api.service;
import com.krovit.api.dto.request.TechnologyRequestDto;
import com.krovit.api.dto.response.TechnologyResponseDto;
import com.krovit.api.entity.Technology;
import com.krovit.api.mapper.TechnologyMapper;
import com.krovit.api.repository.TechnologyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TechnologyServiceImpMySQLTest {

    @Mock
    private TechnologyRepository technologyRepository;

    @Mock
    private TechnologyMapper technologyMapper;

    @InjectMocks
    private TechnologyServiceImpMySQL technologyService;

    private TechnologyRequestDto requestDto;
    private Technology technology;
    private TechnologyResponseDto responseDto;

    @BeforeEach
    public void setUp() {

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


    }


    // createTechnology with valid input returns TechnologyResponseDto
    @Test
    public void test_createTechnology_validInput_returnsTechnologyResponseDto() {
        // Arrange
        when(technologyMapper.technologyRequestDtoToTechnology(requestDto)).thenReturn(technology);

        when(technologyMapper.technologyToTechnologyResponseDto(technology)).thenReturn(responseDto);
        when(technologyRepository.save(any(Technology.class))).thenReturn(technology);


        // Act
        Optional<TechnologyResponseDto> result = technologyService.createTechnology(requestDto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(responseDto, result.get());
    }

        // getTechnologyById with existing id returns Optional<TechnologyResponseDto>
    @Test
    public void test_getTechnologyById_existingId_returnsOptionalTechnologyResponseDto() {
        // Arrange
        Long id = 1L;
        //when(technologyMapper.technologyRequestDtoToTechnology(requestDto)).thenReturn(technology);

        when(technologyMapper.technologyToTechnologyResponseDto(technology)).thenReturn(responseDto);
        when(technologyRepository.findById(id)).thenReturn(Optional.of(technology));


        // Act
        Optional<TechnologyResponseDto> result = technologyService.getTechnologyById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(responseDto, result.get());
    }

    // updateTechnology with valid input and existing id returns Optional<TechnologyResponseDto>
    @Test
    public void test_updateTechnology_validInputAndExistingId_returnsOptionalTechnologyResponseDto() {
        // Arrange
        Long id = 1L;
        when(technologyMapper.technologyToTechnologyResponseDto(technology)).thenReturn(responseDto);
        when(technologyRepository.save(any(Technology.class))).thenReturn(technology);
        when(technologyRepository.findById(id)).thenReturn(Optional.of(technology));
        // Act
        Optional<TechnologyResponseDto> result = technologyService.updateTechnology(id, requestDto);
        // Assert
        assertTrue(result.isPresent());
        assertEquals(responseDto, result.get());
    }

    // updateTechnology with valid input and non-existing id returns Optional<TechnologyResponseDto>
    @Test
    public void test_updateTechnology_validInputAndNonExistingId_returnsOptionalTechnologyResponseDto() {
        // Arrange
        Long id = 1L;

        when(technologyMapper.technologyToTechnologyResponseDto(technology)).thenReturn(responseDto);
        when(technologyRepository.save(any(Technology.class))).thenReturn(technology);
        when(technologyRepository.findById(id)).thenReturn(Optional.of(technology));

        // Act
        Optional<TechnologyResponseDto> result = technologyService.updateTechnology(id, requestDto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(responseDto, result.get());
    }

    // deleteTechnology with existing id returns void
    @Test
    public void test_deleteTechnology_existingId_returnsVoid() {
        // Arrange
        Long id = 1L;


        // Act
        technologyService.deleteTechnology(id);

        // Assert
        // Verify that the delete method of the technologyRepository is called with the correct id
        verify(technologyRepository, times(1)).deleteById(id);
    }

    // createTechnology with null input throws IllegalArgumentException
    @Test
    public void test_createTechnology_nullInput_throwsIllegalArgumentException() {
        // Arrange
        TechnologyRequestDto technologyRequestDto = null;



        TechnologyServiceImpMySQL technologyService = new TechnologyServiceImpMySQL(technologyRepository, technologyMapper);

        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            technologyService.createTechnology(technologyRequestDto);
        });
    }

    // getTechnologyById with null id throws IllegalArgumentException
    @Test
    public void test_getTechnologyById_nullId_throwsIllegalArgumentException() {
        // Arrange
        Long id = null;


        // Act and Assert
        assertThrows(IllegalArgumentException.class, () -> {
            technologyService.getTechnologyById(id);
        });
    }

    @Test
    public void testGetAllTechnologies() {
        // Arrange
        Integer pageNumber = 1;
        Integer pageSize = 10;

        Page<Technology> technologyPage = new PageImpl<>(Collections.singletonList(technology), PageRequest.of(pageNumber, pageSize), 1);
        // when(technologyRepository.findAllByTechnologyNameIsLikeIgnoreCase(any(), any())).thenReturn(technologyPage);
        // when(technologyRepository.findAllByTag(any(), any())).thenReturn(technologyPage);
        when(technologyRepository.findAllByTechnologyNameIsLikeIgnoreCaseAndTag(any(), any(), any())).thenReturn(technologyPage);
        // when(technologyRepository.findAll((Pageable) any())).thenReturn(technologyPage);
        when(technologyMapper.technologyToTechnologyResponseDto(technology)).thenReturn(responseDto);

        // Act
        Page<TechnologyResponseDto> result = technologyService.getAllTechnologies(technology.getTechnologyName(), technology.getTag(), pageNumber, pageSize);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals(responseDto, result.getContent().getFirst());
    }

}
