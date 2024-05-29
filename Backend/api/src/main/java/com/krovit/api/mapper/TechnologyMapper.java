package com.krovit.api.mapper;

import com.krovit.api.dto.request.TechnologyRequestDto;
import com.krovit.api.dto.response.TechnologyResponseDto;
import com.krovit.api.entity.Technology;
import org.mapstruct.Mapper;

@Mapper
public interface TechnologyMapper {

    TechnologyResponseDto technologyToTechnologyResponseDto(Technology technology);

    Technology technologyRequestDtoToTechnology(TechnologyRequestDto technologyRequestDto);

    TechnologyRequestDto technologyResponseDtoToTechnologyRequestDto(TechnologyResponseDto technologyResponseDto);
}
