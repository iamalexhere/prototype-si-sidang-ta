package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.enums.StatusSidang;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SidangRepository {
    List<Sidang> findAll();
    Optional<Sidang> findById(Integer id);
    Optional<Sidang> findByTugasAkhirId(Integer taId);
    List<Sidang> findByDosenPengujiId(Integer dosenId);
    Sidang save(Sidang sidang);
    void deleteById(Integer id);

    // New methods for schedule validation
    boolean isJadwalAvailable(LocalDateTime waktuMulai, LocalDateTime waktuSelesai, String ruangan);
    boolean isDosenAvailable(Dosen dosen, LocalDateTime waktuMulai, LocalDateTime waktuSelesai);
    List<Sidang> findOverlappingSidang(LocalDateTime waktuMulai, LocalDateTime waktuSelesai, String ruangan);
    List<Sidang> findDosenOverlappingSidang(Dosen dosen, LocalDateTime waktuMulai, LocalDateTime waktuSelesai);
    
    // Status management
    boolean updateStatus(Long sidangId, StatusSidang newStatus);
    List<Sidang> findByStatus(StatusSidang status);
}
