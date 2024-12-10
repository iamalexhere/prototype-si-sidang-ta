package com.rpl.project_sista.repository;

import java.util.List;
import java.util.Optional;

import com.rpl.project_sista.model.entity.Semester;

public interface SemesterRepository {
    List<Semester> findAll();
    Optional<Semester> findById(Long id);
    Semester save(Semester semester);
    void deleteById(Long id);
    Optional<Semester> findActiveSemester();
}
