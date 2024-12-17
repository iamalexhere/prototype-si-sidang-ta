package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.dto.KomponenNilaiDTO;
import com.rpl.project_sista.model.entity.NilaiSidang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcNilaiSidangRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcNilaiSidangRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<NilaiSidang> nilaiSidangRowMapper = (rs, rowNum) -> {
        NilaiSidang nilaiSidang = new NilaiSidang();
        nilaiSidang.setNilaiId(rs.getLong("nilai_id"));
        nilaiSidang.setNilai(rs.getFloat("nilai"));
        return nilaiSidang;
    };

    public List<NilaiSidang> findAll() {
        String sql = "SELECT * FROM nilai_sidang";
        return jdbcTemplate.query(sql, nilaiSidangRowMapper);
    }

    public List<KomponenNilaiDTO> findAllNilaiByIdSidang(int sidangId) {
        String sql = "SELECT n.komponen_id, n.nilai, k.nama_komponen, d.nama as nama_dosen, k.tipe_penilai " +
                    "FROM nilai_sidang n " +
                    "JOIN komponen_nilai k ON n.komponen_id = k.komponen_id " +
                    "JOIN penguji_sidang p ON n.dosen_id = p.dosen_id AND n.sidang_id = p.sidang_id " +
                    "JOIN dosen d ON p.dosen_id = d.dosen_id " +
                    "WHERE n.sidang_id = ?";

        return jdbcTemplate.query(sql, new Object[]{sidangId}, (rs, rowNum) ->
            new KomponenNilaiDTO(
                rs.getInt("komponen_id"),
                rs.getFloat("nilai"),
                rs.getString("nama_komponen"),
                rs.getString("nama_dosen"),
                rs.getString("tipe_penilai")
            )
        );
    }    

    public NilaiSidang findById(Long id) {
        String sql = "SELECT * FROM nilai_sidang WHERE nilai_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, nilaiSidangRowMapper);
    }

    public void saveNilaiSidang(int idSidang, int komponenId, int dosenId, float nilai) {
        String sql = "INSERT INTO nilai_sidang (sidang_id, komponen_id, dosen_id, nilai) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, idSidang, komponenId, dosenId, nilai);
    }
}
