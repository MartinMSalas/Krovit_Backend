package com.krovit.api.service;

import com.krovit.api.dto.request.TechnologyRequestDto;

import com.krovit.api.dto.response.TechnologyResponseDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TechnologyService {

    Optional<TechnologyResponseDto> createTechnology(TechnologyRequestDto technologyRequestDto);

    //public List<TechnologyResponseDto> getAllTechnologies();
    Page<TechnologyResponseDto> getAllTechnologies(String technologyName, String tag, Integer pageNumber, Integer pageSize);
    Optional<TechnologyResponseDto> getTechnologyById(Long id);

    Optional<TechnologyResponseDto> updateTechnology(Long id, TechnologyRequestDto technologyRequestDto);

    void deleteTechnology(Long id);


}
