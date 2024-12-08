package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.model.entity.Semester;
import java.util.List;
import java.util.Optional;

public interface KomponenNilaiRepository {
    List<KomponenNilai> findAll();
    List<KomponenNilai> findBySemester(Semester semester);
    Optional<KomponenNilai> findById(Long id);
    KomponenNilai save(KomponenNilai komponenNilai);
    void deleteById(Long id);
    int count();
}
