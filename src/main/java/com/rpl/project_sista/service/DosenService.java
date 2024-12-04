package com.rpl.project_sista.service;

import com.rpl.project_sista.entity.Dosen;
import com.rpl.project_sista.entity.Mahasiswa;
import java.util.List;
import java.util.Optional;

public interface DosenService {
    // Basic CRUD operations
    List<Dosen> getAllDosen();
    Optional<Dosen> getDosenById(Long id);
    Optional<Dosen> getDosenByNip(String nip);
    Optional<Dosen> getDosenByUserId(Long userId);
    
    // Get supervised students
    List<Mahasiswa> getMahasiswaBimbingan(Long dosenId);
}
