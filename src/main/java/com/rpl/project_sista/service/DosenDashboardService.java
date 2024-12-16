package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.enums.TipePenilai;
import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.repository.DosenDashboardRepository;
import com.rpl.project_sista.repository.KomponenNilaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DosenDashboardService {

    @Autowired
    private DosenDashboardRepository dosenDashboardRepository;

    @Autowired
    private KomponenNilaiRepository komponenNilaiRepository;

    public List<TugasAkhir> getTugasAkhirBimbingan(Integer dosenId) {
        return dosenDashboardRepository.findTugasAkhirByPembimbingId(dosenId);
    }

    public List<Sidang> getSidangPenguji(Integer dosenId) {
        return dosenDashboardRepository.findSidangByPengujiId(dosenId);
    }

    @Transactional
    public void beriNilaiBimbingan(Integer taId, Double nilai, String catatan) {
        KomponenNilai komponenNilai = new KomponenNilai();
        komponenNilai.setTugasAkhirId(Long.valueOf(taId));
        komponenNilai.setNilai(nilai);
        komponenNilai.setCatatan(catatan);
        komponenNilai.setTipePenilai(TipePenilai.pembimbing);
        komponenNilaiRepository.save(komponenNilai);
    }

    @Transactional
    public void beriNilaiPenguji(Integer taId, Double nilai) {
        KomponenNilai komponenNilai = new KomponenNilai();
        komponenNilai.setTugasAkhirId(Long.valueOf(taId));
        komponenNilai.setNilai(nilai);
        komponenNilai.setTipePenilai(TipePenilai.penguji);
        komponenNilaiRepository.save(komponenNilai);
    }
}
