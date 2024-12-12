// src/main/java/com/rpl/project_sista/service/DosenService.java
package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.repository.DosenRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DosenService {

    @Autowired
    private DosenRepository dosenRepository;

    public List<Dosen> findAllDosen() {
        return dosenRepository.findAll();
    }

   public Optional<Dosen> findDosenById(Integer id) {
        return dosenRepository.findById(id);
    }

    @Transactional
    public Dosen saveDosen(Dosen dosen) {
        return dosenRepository.save(dosen);
    }

    @Transactional
    public void deleteDosen(Integer id) {
        dosenRepository.deleteById(id);
    }

}