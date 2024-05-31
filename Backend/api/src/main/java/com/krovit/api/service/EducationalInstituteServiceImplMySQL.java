package com.krovit.api.service;

import com.krovit.api.dto.request.EducationalInstituteRequestDto;
import com.krovit.api.dto.response.EducationalInstituteResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EducationalInstituteServiceImplMySQL implements EducationalInstituteService {
    @Override
    public Optional<EducationalInstituteResponseDto> createEducationalInstitute(EducationalInstituteRequestDto educationalInstitute) {
        return Optional.empty();
    }

    @Override
    public Page<EducationalInstituteResponseDto> getAllEducationalInstitutes(String name, Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public Optional<EducationalInstituteResponseDto> getEducationalInstituteById(Long educationalInstituteId) {
        return Optional.empty();
    }

    @Override
    public Optional<EducationalInstituteResponseDto> updateEducationalInstitute(Long educationalInstituteId, EducationalInstituteRequestDto educationalInstitute) {
        return Optional.empty();
    }

    @Override
    public void deleteEducationalInstitute(Long educationalInstituteId) {

    }
}
