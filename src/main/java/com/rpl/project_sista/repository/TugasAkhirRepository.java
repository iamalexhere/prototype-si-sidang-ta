package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.TugasAkhir;
import java.util.List;
import java.util.Optional;

public interface TugasAkhirRepository {
    List<TugasAkhir> findAll();
    Optional<TugasAkhir> findById(Integer id);
    List<TugasAkhir> findByMahasiswaId(Integer mahasiswaId);
    TugasAkhir save(TugasAkhir tugasAkhir);
    void deleteById(Integer id);
}
