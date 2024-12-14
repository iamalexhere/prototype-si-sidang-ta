package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.entity.Sidang;

public interface MahasiswaDashboardRepository {
    TugasAkhir findTugasAkhirByMahasiswaId(Long mahasiswaId);
    Sidang findSidangByTugasAkhirId(Long taId);
}