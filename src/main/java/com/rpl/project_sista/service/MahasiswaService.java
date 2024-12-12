// src/main/java/com/rpl/project_sista/service/MahasiswaService.java
package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.repository.MahasiswaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MahasiswaService {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    public List<Mahasiswa> findAllMahasiswa() {
        return mahasiswaRepository.findAll();
    }
    public Optional<Mahasiswa> findMahasiswaById(Integer id) {
        return mahasiswaRepository.findById(id);
    }

    @Transactional
    public Mahasiswa saveMahasiswa(Mahasiswa mahasiswa) {
        return mahasiswaRepository.save(mahasiswa);
    }

    @Transactional
    public void deleteMahasiswa(Integer id) {
        mahasiswaRepository.deleteById(id);
    }
    
}