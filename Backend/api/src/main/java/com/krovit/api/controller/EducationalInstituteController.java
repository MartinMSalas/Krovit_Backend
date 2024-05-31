package com.krovit.api.controller;

import com.krovit.api.dto.request.EducationalInstituteRequestDto;
import com.krovit.api.dto.response.EducationalInstituteResponseDto;
import com.krovit.api.service.EducationalInstituteService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class EducationalInstituteController {

    public static final String EDUCATIONAL_INSTITUTE_PATH = "/api/v1/educationalInstitute";
    public static final String EDUCATIONAL_INSTITUTE_PATH_ID = EDUCATIONAL_INSTITUTE_PATH + "/{educationalInstituteId}";

    private final EducationalInstituteService educationalInstituteService;

    public EducationalInstituteController(EducationalInstituteService educationalInstituteService) {
        this.educationalInstituteService = educationalInstituteService;
    }

    @PostMapping(EDUCATIONAL_INSTITUTE_PATH)
    public ResponseEntity<EducationalInstituteResponseDto> createEducationalInstitute(@Validated @RequestBody EducationalInstituteRequestDto educationalInstitute) {
        Optional<EducationalInstituteResponseDto> createdEducationalInstitute = educationalInstituteService.createEducationalInstitute(educationalInstitute);
        if(createdEducationalInstitute.isPresent()) {
            return new ResponseEntity<>(createdEducationalInstitute.get(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(EDUCATIONAL_INSTITUTE_PATH)
    public Page<EducationalInstituteResponseDto> getAllEducationalInstitutes(@RequestParam(required = false) String name,
                                                                             @RequestParam(required = false) Integer pageNumber,
                                                                             @RequestParam(required = false) Integer pageSize) {
        return educationalInstituteService.getAllEducationalInstitutes(name, pageNumber, pageSize);
    }

    @GetMapping(EDUCATIONAL_INSTITUTE_PATH_ID)
    public ResponseEntity<EducationalInstituteResponseDto> getEducationalInstituteById(@PathVariable Long educationalInstituteId) {
        Optional<EducationalInstituteResponseDto> educationalInstitute = educationalInstituteService.getEducationalInstituteById(educationalInstituteId);
        if(educationalInstitute.isPresent()) {
            return new ResponseEntity<>(educationalInstitute.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(EDUCATIONAL_INSTITUTE_PATH_ID)
    public ResponseEntity<EducationalInstituteResponseDto> updateEducationalInstitute(@PathVariable Long educationalInstituteId, @RequestBody EducationalInstituteRequestDto educationalInstitute) {
        Optional<EducationalInstituteResponseDto> updatedEducationalInstitute = educationalInstituteService.updateEducationalInstitute(educationalInstituteId, educationalInstitute);
        if(updatedEducationalInstitute.isPresent()) {
            return new ResponseEntity<>(updatedEducationalInstitute.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(EDUCATIONAL_INSTITUTE_PATH_ID)
    public ResponseEntity<?> deleteEducationalInstitute(@PathVariable Long educationalInstituteId) {
        educationalInstituteService.deleteEducationalInstitute(educationalInstituteId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}