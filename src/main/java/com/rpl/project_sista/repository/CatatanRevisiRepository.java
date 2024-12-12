package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.CatatanRevisi;
import java.util.List;
import java.util.Optional;

public interface CatatanRevisiRepository {
    List<CatatanRevisi> findAll();
    Optional<CatatanRevisi> findById(Long id);
    List<CatatanRevisi> findBySidangId(Long sidangId);
    List<CatatanRevisi> findByDosenId(Long dosenId);
    CatatanRevisi save(CatatanRevisi catatanRevisi);
    void deleteById(Long id);
}
