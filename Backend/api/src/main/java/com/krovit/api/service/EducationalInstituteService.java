package com.krovit.api.service;

import com.krovit.api.dto.request.EducationalInstituteRequestDto;
import com.krovit.api.dto.response.EducationalInstituteResponseDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface EducationalInstituteService {

    Optional<EducationalInstituteResponseDto> createEducationalInstitute(EducationalInstituteRequestDto educationalInstitute);

    Page<EducationalInstituteResponseDto> getAllEducationalInstitutes(String name, Integer pageNumber, Integer pageSize);

    Optional<EducationalInstituteResponseDto> getEducationalInstituteById(Long educationalInstituteId);

    Optional<EducationalInstituteResponseDto> updateEducationalInstitute(Long educationalInstituteId, EducationalInstituteRequestDto educationalInstitute);

    void deleteEducationalInstitute(Long educationalInstituteId);
}
