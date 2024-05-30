package com.krovit.api.service;

import com.krovit.api.dto.request.ProjectRequestDto;
import com.krovit.api.dto.request.TechnologyRequestDto;
import com.krovit.api.dto.response.ProjectResponseDto;
import com.krovit.api.dto.response.TechnologyResponseDto;
import com.krovit.api.entity.Project;
import com.krovit.api.entity.Technology;
import com.krovit.api.mapper.ProjectMapper;
import com.krovit.api.mapper.TechnologyMapper;
import com.krovit.api.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpMySQL implements ProjectService{
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final TechnologyMapper technologyMapper;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;

    public ProjectServiceImpMySQL(ProjectRepository projectRepository, ProjectMapper projectMapper, TechnologyMapper technologyMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.technologyMapper = technologyMapper;
    }
    @Override
    public Optional<ProjectResponseDto> createProject(ProjectRequestDto projectRequestDto) {
        if (projectRequestDto == null) {
            throw new IllegalArgumentException("ProjectRequestDto cannot be null");
        }
        ProjectResponseDto projectResponseDto = projectMapper.projectToProjectResponseDto(projectRepository.save(projectMapper.projectRequestDtoToProject(projectRequestDto)));

        return Optional.of(projectResponseDto);
    }

    @Override
    public Page<ProjectResponseDto> getAllProjects(String projectName, Integer pageNumber, Integer pageSize) {
        return null;
    }

    @Override
    public Optional<ProjectResponseDto> getProjectById(Long id) {
        if( id == null){
            throw new IllegalArgumentException("Id cannot be null");
        }
        Optional<Project> projectResponseDto = projectRepository.findById(id);
        if(projectResponseDto.isPresent()){
            return Optional.of(projectMapper.projectToProjectResponseDto(projectResponseDto.get()));
        }

        return Optional.empty();
    }

    @Override
    public Optional<ProjectResponseDto> updateProject(Long id, ProjectRequestDto projectRequestDto) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        if (projectRequestDto == null) {
            throw new IllegalArgumentException("ProjectRequestDto cannot be null");
        }
        Optional<Project> project = projectRepository.findById(id);
        Project projectSaved = new Project();
        if (project.isPresent()) {
            if(projectRequestDto.getProjectDescription() != null){
                project.get().setProjectDescription(projectRequestDto.getProjectDescription());
            }
            if(projectRequestDto.getProjectName() != null){
                project.get().setProjectName(projectRequestDto.getProjectName());
            }
            if(projectRequestDto.getCertifiedTechnologiesDto() != null){
//                project.get().setCertifiedTechnologies(technologyMapper.technologyRequestDtoToTechnology(projectRequestDto.getCertifiedTechnologiesDto()).stream()..
                Set<TechnologyRequestDto> technologyRequestDtos = projectRequestDto.getCertifiedTechnologiesDto();
                Set<Technology> technologies = technologyRequestDtos.stream().map(technologyMapper::technologyRequestDtoToTechnology).collect(Collectors.toSet());
                project.get().setCertifiedTechnologies(technologies);

            }
            if(projectRequestDto.getWebsiteLink() != null){
                project.get().setWebsiteLink(projectRequestDto.getWebsiteLink());
            }
            if(projectRequestDto.getSourceCodeLink() != null){
                project.get().setSourceCodeLink(projectRequestDto.getSourceCodeLink());
            }
            projectSaved = projectRepository.save(project.get());

        }else {
            projectSaved = projectRepository.save(projectMapper.projectRequestDtoToProject(projectRequestDto));
        }
        return Optional.of(projectMapper.projectToProjectResponseDto(projectSaved));



    }

    @Override
    public void deleteProject(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        projectRepository.deleteById(id);

    }

    @Override
    public Optional<ProjectResponseDto> addTechnologyToProject(Long projectId, TechnologyRequestDto technologyRequestDto) {
        if (projectId == null) {
            throw new IllegalArgumentException("ProjectId cannot be null");
        }
        if (technologyRequestDto == null) {
            throw new IllegalArgumentException("TechnologyRequestDto cannot be null");
        }
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
            Set<Technology> certifiedTechnologies = new HashSet<>(project.get().getCertifiedTechnologies());
            certifiedTechnologies.add(technologyMapper.technologyRequestDtoToTechnology(technologyRequestDto));
            project.get().setCertifiedTechnologies(certifiedTechnologies);


            projectRepository.save(project.get());
            return Optional.of(projectMapper.projectToProjectResponseDto(project.get()));
        } else {
            throw new IllegalArgumentException("Project not found");
        }


    }

    @Override
    public Optional<ProjectResponseDto> removeTechnologyFromProject(Long projectId, Long technologyId) {
        if (projectId == null) {
            throw new IllegalArgumentException("ProjectId cannot be null");
        }
        if (technologyId == null) {
            throw new IllegalArgumentException("TechnologyId cannot be null");
        }
        Optional<Project> project = projectRepository.findById(projectId);
        if (project.isPresent()) {
//            Set<Technology> certifiedTechnologies = new HashSet<>(project.get().getCertifiedTechnologies());
//            certifiedTechnologies.removeIf(technology -> technology.getTechnologyId().equals(technologyId));
//            project.get().setCertifiedTechnologies(certifiedTechnologies);


            // another way to do it
            Set<Technology> certifiedTechnologiesStream = project.get().getCertifiedTechnologies().stream().filter(technology -> !technology.getTechnologyId().equals(technologyId)).collect(Collectors.toSet());
            project.get().setCertifiedTechnologies(certifiedTechnologiesStream);

            Project projectSaved = projectRepository.save(project.get());
            ProjectResponseDto projectResponseDtoSaved = projectMapper.projectToProjectResponseDto(projectSaved);
            return Optional.of(projectResponseDtoSaved);

        } else {
            throw new IllegalArgumentException("Project not found");
        }



    }

    @Override
    public Set<TechnologyResponseDto> getTechnologiesForProject(Long projectId) {
        return Set.of();
    }
}
