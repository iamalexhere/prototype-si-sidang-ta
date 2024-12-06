package com.rpl.project_sista.kta.mahasiswa;

import java.util.List;
import java.util.Optional;

public interface MahasiswaRepository {
    List<Mahasiswa> findAll();
    Optional<Mahasiswa> findById(Integer id);
    Mahasiswa save(Mahasiswa mahasiswa);
    List<Mahasiswa> findByName(String name);
    List<Mahasiswa> findPaginated(int page, int size, String filter);
    int count(String filter);
}
