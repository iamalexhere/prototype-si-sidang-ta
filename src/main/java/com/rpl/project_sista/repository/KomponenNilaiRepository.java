package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.model.entity.Semester;
import com.rpl.project_sista.model.enums.TipePenilai;

import java.util.List;
import java.util.Optional;

public interface KomponenNilaiRepository {
    List<KomponenNilai> findAll();
    Optional<KomponenNilai> findById(Long id);
    List<KomponenNilai> findBySemester(Semester semester);
    List<KomponenNilai> findBySemesterAndTipePenilai(Semester semester, TipePenilai tipePenilai);
    List<KomponenNilai> findKomponenByTipePenilai(TipePenilai tipePenilai);
    KomponenNilai save(KomponenNilai komponenNilai);
    void deleteById(Long id);
    long count();
}
