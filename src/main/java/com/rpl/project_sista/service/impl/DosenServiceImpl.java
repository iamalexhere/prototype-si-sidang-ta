package com.rpl.project_sista.service.impl;

import com.rpl.project_sista.entity.Dosen;
import com.rpl.project_sista.entity.Mahasiswa;
import com.rpl.project_sista.entity.TugasAkhir;
import com.rpl.project_sista.repository.DosenRepository;
import com.rpl.project_sista.service.DosenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DosenServiceImpl implements DosenService {
    
    private final DosenRepository dosenRepository;

    public DosenServiceImpl(DosenRepository dosenRepository) {
        this.dosenRepository = dosenRepository;
    }

    @Override
    public List<Dosen> getAllDosen() {
        return dosenRepository.findAll();
    }

    @Override
    public Optional<Dosen> getDosenById(Long id) {
        return dosenRepository.findById(id);
    }

    @Override
    public Optional<Dosen> getDosenByNip(String nip) {
        return dosenRepository.findByNip(nip);
    }

    @Override
    public Optional<Dosen> getDosenByUserId(Long userId) {
        return dosenRepository.findByUserId(userId);
    }

    @Override
    public List<Mahasiswa> getMahasiswaBimbingan(Long dosenId) {
        return dosenRepository.findById(dosenId)
            .map(dosen -> dosen.getTugasAkhirBimbingan().stream()
                .map(TugasAkhir::getMahasiswa)
                .collect(Collectors.toList()))
            .orElse(List.of());
    }
}
