package com.rpl.project_sista.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rpl.project_sista.jdbcrepository.JdbcDosenRepository;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.repository.DosenRepository;

@Service
public class DosenService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DosenRepository dosenRepository;

    @Transactional
    public void deleteDosen(Integer dosenId) {
        // Check if dosen exists
        Dosen dosen = dosenRepository.findById(dosenId)
                .orElseThrow(() -> new RuntimeException("Dosen tidak ditemukan"));

        // Check if dosen is a penguji
        int pengujiCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM penguji_sidang WHERE dosen_id = ?",
                Integer.class,
                dosenId);
        if (pengujiCount > 0) {
            throw new DataIntegrityViolationException(
                    "Tidak dapat menghapus dosen karena masih terdaftar sebagai penguji sidang");
        }

        // Check if dosen is a pembimbing
        int pembimbingCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM pembimbing_ta WHERE dosen_id = ?",
                Integer.class,
                dosenId);
        if (pembimbingCount > 0) {
            throw new DataIntegrityViolationException(
                    "Tidak dapat menghapus dosen karena masih terdaftar sebagai pembimbing tugas akhir");
        }

        // If all checks pass, delete the dosen
        dosenRepository.deleteById(dosenId);
    }
}
