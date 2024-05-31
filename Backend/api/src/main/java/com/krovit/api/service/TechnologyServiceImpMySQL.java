package com.krovit.api.service;

import com.krovit.api.dto.request.TechnologyRequestDto;
import com.krovit.api.dto.response.TechnologyResponseDto;
import com.krovit.api.mapper.TechnologyMapper;
import com.krovit.api.repository.TechnologyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.krovit.api.entity.Technology;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TechnologyServiceImpMySQL implements TechnologyService{

    private final TechnologyRepository technologyRepository;
    private final TechnologyMapper technologyMapper;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    @Autowired
    public TechnologyServiceImpMySQL(TechnologyRepository technologyRepository, TechnologyMapper technologyMapper) {
        this.technologyRepository = technologyRepository;
        this.technologyMapper = technologyMapper;
    }

    @Override
    public Optional<TechnologyResponseDto> createTechnology(TechnologyRequestDto technologyRequestDto) {
        if (technologyRequestDto == null) {
            throw new IllegalArgumentException("TechnologyRequestDto cannot be null");
        }
        Technology technology = technologyMapper.technologyRequestDtoToTechnology(technologyRequestDto);
        TechnologyResponseDto technologyResponseDto = technologyMapper.technologyToTechnologyResponseDto(technologyRepository.save(technology));
        return Optional.of(technologyResponseDto);
    }

    @Override
    public Page<TechnologyResponseDto> getAllTechnologies(String technologyName, String tag, Integer pageNumber, Integer pageSize) {

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);
        Page<Technology> technologyPage;

        if(StringUtils.hasText(technologyName) && !StringUtils.hasText(tag)){
            technologyPage = listTechnologyByName(technologyName, pageRequest);
        }else if(!StringUtils.hasText(technologyName) && StringUtils.hasText(tag) ){
            technologyPage = listTechnologyByTag(tag, pageRequest);
        }else if(StringUtils.hasText(technologyName) && StringUtils.hasText(tag)){
            technologyPage = listTechnologyByNameAndTag(technologyName, tag, pageRequest);
        }else{
            technologyPage = technologyRepository.findAll(pageRequest);
        }
        return technologyPage.map(technologyMapper::technologyToTechnologyResponseDto);
    }

    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize){
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0){
            queryPageNumber = pageNumber -1;
        }else{
            queryPageNumber = DEFAULT_PAGE;
        }
        if (pageSize == null || pageSize < 1) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        }else{
            if(pageSize > 1000){
                queryPageSize = 1000;
            }else {
                queryPageSize = pageSize;
            }
        }

        Sort sort = Sort.by(Sort.Order.asc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }

    private Page<Technology> listTechnologyByName(String technologyName, Pageable pageable){
        return technologyRepository.findAllByTechnologyNameIsLikeIgnoreCase("%" + technologyName + "%", pageable);
    }

    private Page<Technology> listTechnologyByTag(String tag, Pageable pageable){
        return technologyRepository.findAllByTag(tag, pageable);
    }

    private Page<Technology> listTechnologyByNameAndTag(String technologyName, String tag, Pageable pageable){
        return technologyRepository.findAllByTechnologyNameIsLikeIgnoreCaseAndTag("%" + technologyName + "%", tag, pageable);
    }

    @Override
    public Optional<TechnologyResponseDto> getTechnologyById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        Optional<Technology> technology = technologyRepository.findById(id);
        if (technology.isPresent()) {
            return Optional.of(technologyMapper.technologyToTechnologyResponseDto(technology.get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<TechnologyResponseDto> updateTechnology(Long id, TechnologyRequestDto technologyRequestDto) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (technologyRequestDto == null) {
            throw new IllegalArgumentException("TechnologyRequestDto cannot be null");
        }
        Optional<Technology> technology = technologyRepository.findById(id);
        if (technology.isPresent()) {
            if (technologyRequestDto.getTechnologyName() != null) {
                technology.get().setTechnologyName(technologyRequestDto.getTechnologyName());
            }
            if (technologyRequestDto.getDescription() != null) {
                technology.get().setDescription(technologyRequestDto.getDescription());
            }
            if (technologyRequestDto.getLogo() != null) {
                technology.get().setLogo(technologyRequestDto.getLogo());
            }
            if (technologyRequestDto.getTag() != null) {
                technology.get().setTag(technologyRequestDto.getTag());
            }

            technology.get().setTechnologyId(id);
            Technology updatedTechnology = technologyRepository.save(technology.get());

            TechnologyResponseDto technologyResponseDto = technologyMapper.technologyToTechnologyResponseDto(technologyRepository.save(updatedTechnology));
            return Optional.of(technologyResponseDto);
        }else {
            TechnologyResponseDto technologyResponseDto = technologyMapper.technologyToTechnologyResponseDto(technologyRepository.save(technologyMapper.technologyRequestDtoToTechnology(technologyRequestDto)));
            return Optional.of(technologyResponseDto);
        }

    }

    @Override
    public void deleteTechnology(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        technologyRepository.deleteById(id);
    }
}
