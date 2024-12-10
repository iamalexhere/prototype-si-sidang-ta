package com.rpl.project_sista.repository;

import java.util.List;
import java.util.Optional;

import com.rpl.project_sista.model.entity.Mahasiswa;

public interface MahasiswaRepository {
    List<Mahasiswa> findAll();
    Optional<Mahasiswa> findById(Integer id);
    Optional<Mahasiswa> findByUserId(Integer userId);
    Mahasiswa save(Mahasiswa mahasiswa);
    List<Mahasiswa> findByName(String name);
    List<Mahasiswa> findPaginated(int page, int size, String filter);
    int count(String filter);
    void deleteById(Integer id);
}
