package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.entity.Sidang;
import java.util.List;

public interface DosenDashboardRepository {
    List<TugasAkhir> findTugasAkhirByPembimbingId(Integer dosenId);
    List<Sidang> findSidangByPengujiId(Integer dosenId);
}
