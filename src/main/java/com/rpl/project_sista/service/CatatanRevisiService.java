package com.rpl.project_sista.service;


import com.rpl.project_sista.model.entity.CatatanRevisi;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.repository.CatatanRevisiRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CatatanRevisiService {

    @Autowired
    private CatatanRevisiRepository catatanRevisiRepository;

    public List < CatatanRevisi > findAllCatatanRevisi() {
        return catatanRevisiRepository.findAll();
    }

    public Optional < CatatanRevisi > findCatatanRevisiById(Long id) {
        return catatanRevisiRepository.findById(id);
    }

    public List < CatatanRevisi > findBySidang(Sidang sidang) {
        return catatanRevisiRepository.findBySidang(sidang);
    }

    public List < CatatanRevisi > findByDosen(Dosen dosen) {
        return catatanRevisiRepository.findByDosen(dosen);
    }
    public List < CatatanRevisi > findBySidangAndDosen(Sidang sidang, Dosen dosen) {
        return catatanRevisiRepository.findBySidangAndDosen(sidang, dosen);
    }
    @Transactional
    public CatatanRevisi saveCatatanRevisi(CatatanRevisi catatanRevisi) {
        return catatanRevisiRepository.save(catatanRevisi);
    }

    @Transactional
    public void deleteCatatanRevisi(Long id) {
        catatanRevisiRepository.deleteById(id);
    }
    public long countBySidang(Sidang sidang) {
        return catatanRevisiRepository.countBySidang(sidang);
    }

}