package com.rpl.project_sista.repository;

import com.rpl.project_sista.entity.TugasAkhir;
import com.rpl.project_sista.entity.Mahasiswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface TugasAkhirRepository extends JpaRepository<TugasAkhir, Long> {
    Optional<TugasAkhir> findByMahasiswa(Mahasiswa mahasiswa);
    List<TugasAkhir> findByMahasiswa_MahasiswaId(Long mahasiswaId);
    List<TugasAkhir> findBySemester_SemesterId(Long semesterId);
    boolean existsByMahasiswaAndSemester_SemesterId(Mahasiswa mahasiswa, Long semesterId);
}
