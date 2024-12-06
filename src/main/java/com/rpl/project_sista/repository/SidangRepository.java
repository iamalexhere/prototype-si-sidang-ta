package com.rpl.project_sista.repository;

import com.rpl.project_sista.entity.Sidang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SidangRepository extends JpaRepository<Sidang, Long> {
    List<Sidang> findByJadwalBetween(LocalDateTime start, LocalDateTime end);
    List<Sidang> findByTugasAkhir_Mahasiswa_MahasiswaId(Long mahasiswaId);
    boolean existsByRuanganAndJadwal(String ruangan, LocalDateTime jadwal);
}
