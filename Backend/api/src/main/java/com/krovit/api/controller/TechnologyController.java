package com.krovit.api.controller;

import com.krovit.api.dto.request.TechnologyRequestDto;
import com.krovit.api.dto.response.TechnologyResponseDto;
import com.krovit.api.entity.Technology;
import com.krovit.api.service.TechnologyService;
import com.krovit.api.service.TechnologyServiceImpMySQL;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class TechnologyController {

    public static final String TECHNOLOGY_PATH = "/api/v1/technology";
    public static final String TECHNOLOGY_PATH_ID = TECHNOLOGY_PATH + "/{technologyId}";

    private final TechnologyService technologyService;

    public TechnologyController(TechnologyService technologyService) {
        this.technologyService = technologyService;
    }

    @PostMapping(TECHNOLOGY_PATH)
    public ResponseEntity<TechnologyResponseDto> createTechnology(@Validated @RequestBody TechnologyRequestDto technology) {
        // check if technology is has valid fields


        Optional<TechnologyResponseDto> createdTechnology = technologyService.createTechnology(technology);
        // check if technology is null
        if(createdTechnology.isPresent()) {
            return new ResponseEntity<>(createdTechnology.get(), HttpStatus.CREATED);
        }else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping(TECHNOLOGY_PATH)
    public Page<TechnologyResponseDto> getAllTechnologies(@RequestParam(required = false) String technologyName,
                                                          @RequestParam(required = false) String tag,
                                                          @RequestParam(required = false) Integer pageNumber,
                                                          @RequestParam(required = false) Integer pageSize) {

            Page<TechnologyResponseDto> technologyDtoPage = technologyService.getAllTechnologies(technologyName, tag, pageNumber, pageSize);
            return technologyDtoPage;




//            List<TechnologyResponseDto> technologies = technologyService.getAllTechnologies();
//        return new ResponseEntity<>(technologies, HttpStatus.OK);
    }

    @GetMapping(TECHNOLOGY_PATH_ID)
    public ResponseEntity<TechnologyResponseDto> getTechnologyById(@PathVariable Long technologyId) {
        Optional<TechnologyResponseDto> technology = technologyService.getTechnologyById(technologyId);
        if(technology.isPresent()) {
            return new ResponseEntity<>(technology.get(), HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping(TECHNOLOGY_PATH_ID)
    public ResponseEntity<TechnologyResponseDto> updateTechnology(@PathVariable Long technologyId, @RequestBody TechnologyRequestDto technology) {
        Optional<TechnologyResponseDto> updatedTechnology = technologyService.updateTechnology(technologyId, technology);

        if(updatedTechnology.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(updatedTechnology.get(), HttpStatus.OK);
        }

    }

    @DeleteMapping(TECHNOLOGY_PATH_ID)
    public ResponseEntity<?> deleteTechnology(@PathVariable Long technologyId) {
        technologyService.deleteTechnology(technologyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
