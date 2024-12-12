package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.NilaiSidang;
import java.util.List;
import java.util.Optional;

public interface NilaiSidangRepository {
    List<NilaiSidang> findAll();
    Optional<NilaiSidang> findById(Long id);
    List<NilaiSidang> findBySidangId(Long sidangId);
    List<NilaiSidang> findByDosenId(Long dosenId);
    NilaiSidang save(NilaiSidang nilaiSidang);
    void deleteById(Long id);
}
