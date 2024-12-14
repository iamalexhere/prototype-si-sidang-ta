package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.CatatanRevisi;

import java.util.List;
import java.util.Optional;

public interface CatatanRepository {
    List<CatatanRevisi> findCatatanByMahasiswaId(Long mahasiswaId);
    CatatanRevisi save(CatatanRevisi catatan);
    Optional<CatatanRevisi> findById(Long id);
    void delete(CatatanRevisi catatan);
}