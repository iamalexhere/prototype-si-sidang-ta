package com.rpl.project_sista.repository;

import com.rpl.project_sista.entity.PengujiSidang;
import com.rpl.project_sista.entity.PengujiSidangId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PengujiSidangRepository extends JpaRepository<PengujiSidang, PengujiSidangId> {
    List<PengujiSidang> findBySidang_SidangId(Long sidangId);
    List<PengujiSidang> findByDosen_DosenIdAndSidang_JadwalBetween(Long dosenId, LocalDateTime start, LocalDateTime end);
    boolean existsByDosen_DosenIdAndSidang_Jadwal(Long dosenId, LocalDateTime jadwal);
}
