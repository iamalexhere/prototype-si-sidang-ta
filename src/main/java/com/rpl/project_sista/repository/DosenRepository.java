package com.rpl.project_sista.repository;

import com.rpl.project_sista.entity.Dosen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DosenRepository extends JpaRepository<Dosen, Long> {
    // Find dosen by NIP
    Optional<Dosen> findByNip(String nip);
    
    // Find dosen by userId (will be useful for authentication later)
    Optional<Dosen> findByUserId(Long userId);
}
