package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.model.enums.TipePenilai;
import com.rpl.project_sista.repository.KomponenNilaiRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KomponenNilaiService {

    @Autowired
    private KomponenNilaiRepository komponenNilaiRepository;

    public List<KomponenNilai> getAllKomponenNilai(){
        return this.komponenNilaiRepository.findAll();
    }

    public void save(KomponenNilai komponenNilai){
        komponenNilaiRepository.save(komponenNilai);
    }

    public List<KomponenNilai> getKomponenNilaiByTipePenilai(TipePenilai penilai){
        return this.komponenNilaiRepository.findKomponenByTipePenilai(penilai);
    }
}
