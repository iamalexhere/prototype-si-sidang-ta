package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.repository.MahasiswaDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MahasiswaDashboardService {

    @Autowired
    private MahasiswaDashboardRepository mahasiswaDashboardRepository;

    public TugasAkhir getTugasAkhir(Long mahasiswaId) {
        return mahasiswaDashboardRepository.findTugasAkhirByMahasiswaId(mahasiswaId);
    }

    public Sidang getSidang(Long taId) {
        return mahasiswaDashboardRepository.findSidangByTugasAkhirId(taId);
    }
}
