package com.rpl.project_sista.repository;

import com.rpl.project_sista.entity.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MahasiswaRepository extends JpaRepository<Mahasiswa, Long> {
    Optional<Mahasiswa> findByNpm(String npm);
    boolean existsByNpm(String npm);
}
