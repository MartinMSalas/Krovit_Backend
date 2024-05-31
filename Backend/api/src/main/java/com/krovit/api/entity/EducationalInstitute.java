package com.krovit.api.entity;

import com.krovit.api.entity.enums.EducationLevel;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class EducationalInstitute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long educationId;
    private String educationalInstituteName;
    private EducationLevel educationLevel;
}
