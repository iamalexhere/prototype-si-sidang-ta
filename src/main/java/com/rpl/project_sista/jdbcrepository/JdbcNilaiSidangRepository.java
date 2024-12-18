package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.dto.KomponenNilaiDTO;
import com.rpl.project_sista.model.entity.NilaiSidang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
                    "LEFT JOIN dosen d ON n.dosen_id = d.dosen_id " +
                    "WHERE n.sidang_id = ? " +
                    "ORDER BY k.tipe_penilai, k.komponen_id";

        System.out.println("Executing SQL for sidangId: " + sidangId);
        try {
            List<KomponenNilaiDTO> results = jdbcTemplate.query(sql, new Object[]{sidangId}, (rs, rowNum) -> {
                BigDecimal bd = new BigDecimal(rs.getFloat("nilai"));
                bd = bd.setScale(2, RoundingMode.HALF_UP);
                float roundedNilai = bd.floatValue();
                KomponenNilaiDTO dto = new KomponenNilaiDTO(
                    rs.getLong("komponen_id"),
                    roundedNilai,
                    rs.getString("nama_komponen"),
                    rs.getString("nama_dosen") != null ? rs.getString("nama_dosen") : "Unknown",
                    rs.getString("tipe_penilai")
                );
                System.out.println("Found nilai: " + dto.getTipePenilai() + " - " + dto.getNamaKomponen() + " - " + dto.getNilai());
                return dto;
            });
            System.out.println("Total results found: " + results.size());
            return results;
        } catch (Exception e) {
            System.err.println("Error executing query: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }    

    public NilaiSidang findById(Long id) {
        String sql = "SELECT * FROM nilai_sidang WHERE nilai_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, nilaiSidangRowMapper);
    }

    public void saveNilaiSidang(int idSidang, int komponenId, int dosenId, float nilai) {
        // Round the nilai to two decimal places
        BigDecimal bd = new BigDecimal(Float.toString(nilai));
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        float roundedNilai = bd.floatValue();

        String sql = "INSERT INTO nilai_sidang (sidang_id, komponen_id, dosen_id, nilai) " +
                     "VALUES (?, ?, ?, ?) " +
                     "ON CONFLICT (sidang_id, komponen_id, dosen_id) " +
                     "DO UPDATE SET nilai = ?";
        jdbcTemplate.update(sql, idSidang, komponenId, dosenId, roundedNilai, roundedNilai);
    }
}
