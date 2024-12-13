package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.enums.StatusTA;
import java.util.List;
import java.util.Optional;

public interface TugasAkhirRepository {
    List<TugasAkhir> findAll();
    Optional<TugasAkhir> findById(Integer id);
    List<TugasAkhir> findByMahasiswaId(Integer mahasiswaId);
    TugasAkhir save(TugasAkhir tugasAkhir);
    void deleteById(Integer id);
    
    // New methods for managing TA status
    boolean updateStatus(Long taId, StatusTA newStatus);
    List<TugasAkhir> findByStatus(StatusTA status);
    List<TugasAkhir> findByMahasiswaIdAndStatus(Integer mahasiswaId, StatusTA status);
}
