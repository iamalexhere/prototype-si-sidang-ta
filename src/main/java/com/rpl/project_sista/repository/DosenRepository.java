package com.rpl.project_sista.repository;

import com.rpl.project_sista.entity.Dosen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DosenRepository extends JpaRepository<Dosen, Long> {
    Optional<Dosen> findByNip(String nip);
    Optional<Dosen> findByUser_UserId(Long userId);
    boolean existsByNip(String nip);
}
