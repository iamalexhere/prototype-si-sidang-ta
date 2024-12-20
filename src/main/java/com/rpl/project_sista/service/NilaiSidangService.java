package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.NilaiSidang;
import com.rpl.project_sista.dto.KomponenNilaiDTO;
import com.rpl.project_sista.jdbcrepository.JdbcNilaiSidangRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NilaiSidangService {

    private final JdbcNilaiSidangRepository nilaiSidangJdbcRepository;

    public NilaiSidangService(JdbcNilaiSidangRepository nilaiSidangJdbcRepository) {
        this.nilaiSidangJdbcRepository = nilaiSidangJdbcRepository;
    }

    public List<NilaiSidang> getAllNilaiSidang() {
        return nilaiSidangJdbcRepository.findAll();
    }

    public NilaiSidang getNilaiSidangById(Long id) {
        return nilaiSidangJdbcRepository.findById(id);
    }

    public void saveNilaiSidang(int idSidang, int komponenId, int dosenId, float nilai) {
        nilaiSidangJdbcRepository.saveNilaiSidang(idSidang, komponenId, dosenId, nilai);
    }

    public List<KomponenNilaiDTO> findAllNilaiByIdSidang(int idSidang) {
        return nilaiSidangJdbcRepository.findAllNilaiByIdSidang(idSidang);
    }
}
