package com.krovit.api.repository;

import com.krovit.api.entity.Technology;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TechnologyRepositoryTest {

    @Autowired
    private TechnologyRepository technologyRepository;

    private Technology technology;
    // ...

    @BeforeEach
    public void setUp() {
        technology = Technology.builder()
                .technologyId(1L)
                .technologyName("Java")
                .tag("tag")
                .logo("logo")
                .description("Java is a programming language")
                .build();

    }

    @Test
    public void testFindAllByTechnologyNameIsLikeIgnoreCase() {
        // Arrange


        technologyRepository.save(technology);

        // Act
        Page<Technology> technologies = technologyRepository.findAllByTechnologyNameIsLikeIgnoreCase("%" + technology.getTechnologyName() + "%", PageRequest.of(0, 10));

        // Assert
        assertEquals(1, technologies.getTotalElements());
    }

    @Test
    public void testFindAllByTag() {
        // Arrange

        technologyRepository.save(technology);

        // Act
        Page<Technology> technologies = technologyRepository.findAllByTag(technology.getTag(), PageRequest.of(0, 10));

        // Assert
        assertEquals(1, technologies.getTotalElements());
    }

    @Test
    public void testFindAllByTechnologyNameIsLikeIgnoreCaseAndTag() {
        // Arrange

        technologyRepository.save(technology);

        // Act
        Page<Technology> technologies = technologyRepository.findAllByTechnologyNameIsLikeIgnoreCaseAndTag("%" + technology.getTechnologyName() + "%", technology.getTag(), PageRequest.of(0, 10));

        // Assert
        assertEquals(1, technologies.getTotalElements());
    }
}