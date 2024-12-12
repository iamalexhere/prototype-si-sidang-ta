package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.NilaiSidang;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.enums.TipePenilai;
import com.rpl.project_sista.repository.NilaiSidangRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NilaiSidangService {

    @Autowired
    private NilaiSidangRepository nilaiSidangRepository;

    public List < NilaiSidang > findAllNilaiSidang() {
        return nilaiSidangRepository.findAll();
    }

    public Optional < NilaiSidang > findNilaiSidangById(Long id) {
        return nilaiSidangRepository.findById(id);
    }
    public List < NilaiSidang > findBySidang(Sidang sidang) {
        return nilaiSidangRepository.findBySidang(sidang);
    }
    public List < NilaiSidang > findBySidangAndDosen(Sidang sidang, Dosen dosen) {
        return nilaiSidangRepository.findBySidangAndDosen(sidang, dosen);
    }
    public Map < TipePenilai, Float > getAverageNilaiBySidang(Sidang sidang) {
        return nilaiSidangRepository.getAverageNilaiBySidang(sidang);
    }
    public Float getFinalNilaiBySidang(Sidang sidang) {
        return nilaiSidangRepository.getFinalNilaiBySidang(sidang);
    }
    @Transactional
    public NilaiSidang saveNilaiSidang(NilaiSidang nilaiSidang) {
        return nilaiSidangRepository.save(nilaiSidang);
    }

    @Transactional
    public void deleteNilaiSidang(Long id) {
        nilaiSidangRepository.deleteById(id);
    }

    public boolean hasDosenCompletedPenilaian(Sidang sidang, Dosen dosen) {
        return nilaiSidangRepository.hasDosenCompletedPenilaian(sidang, dosen);
    }
}