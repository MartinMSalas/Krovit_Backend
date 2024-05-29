package com.krovit.api.repository;

import com.krovit.api.dto.response.TechnologyResponseDto;
import com.krovit.api.entity.Technology;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TechnologyRepository extends JpaRepository<Technology, Long> {

    Page<Technology> findAllByTechnologyNameIsLikeIgnoreCase(String technologyName, Pageable pageable);

    Page<Technology> findAllByTag(String tag, Pageable pageable);

    Page<Technology> findAllByTechnologyNameIsLikeIgnoreCaseAndTag(String technologyName, String tag, Pageable pageable);
}
