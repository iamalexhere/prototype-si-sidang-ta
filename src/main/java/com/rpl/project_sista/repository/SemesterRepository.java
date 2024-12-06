package com.rpl.project_sista.repository;

import com.rpl.project_sista.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Optional<Semester> findByIsActiveTrue();
    boolean existsByTahunAjaranAndPeriode(String tahunAjaran, String periode);
}
