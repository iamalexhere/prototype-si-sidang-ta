package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.Sidang;
import java.util.List;
import java.util.Optional;

public interface SidangRepository {
    List<Sidang> findAll();
    Optional<Sidang> findById(Integer id);
    Optional<Sidang> findByTugasAkhirId(Integer taId);
    List<Sidang> findByDosenPengujiId(Integer dosenId);
    Sidang save(Sidang sidang);
    void deleteById(Integer id);
}
