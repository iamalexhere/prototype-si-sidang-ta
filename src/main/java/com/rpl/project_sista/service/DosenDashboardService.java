package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.repository.DosenDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DosenDashboardService {

    @Autowired
    private DosenDashboardRepository dosenDashboardRepository;

    public List<TugasAkhir> getTugasAkhirBimbingan(Integer dosenId) {
        return dosenDashboardRepository.findTugasAkhirByPembimbingId(dosenId);
    }

    public List<Sidang> getSidangPenguji(Integer dosenId) {
        return dosenDashboardRepository.findSidangByPengujiId(dosenId);
    }
}
