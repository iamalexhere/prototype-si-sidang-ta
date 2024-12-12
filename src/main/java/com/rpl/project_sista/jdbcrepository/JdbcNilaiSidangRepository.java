package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.*;
import com.rpl.project_sista.model.enums.TipePenilai;
import com.rpl.project_sista.repository.NilaiSidangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class JdbcNilaiSidangRepository implements NilaiSidangRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<NilaiSidang> findAll() {
        String sql = "SELECT ns.*, s.*, kn.*, d.* FROM nilai_sidang ns " +
                    "JOIN sidang s ON ns.sidang_id = s.sidang_id " +
                    "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id " +
                    "JOIN dosen d ON ns.dosen_id = d.dosen_id";
        return jdbcTemplate.query(sql, this::mapRowToNilaiSidang);
    }

    @Override
    public Optional<NilaiSidang> findById(Long id) {
        try {
            String sql = "SELECT ns.*, s.*, kn.*, d.* FROM nilai_sidang ns " +
                        "JOIN sidang s ON ns.sidang_id = s.sidang_id " +
                        "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id " +
                        "JOIN dosen d ON ns.dosen_id = d.dosen_id " +
                        "WHERE ns.nilai_id = ?";
            NilaiSidang nilaiSidang = jdbcTemplate.queryForObject(sql, this::mapRowToNilaiSidang, id);
            return Optional.ofNullable(nilaiSidang);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<NilaiSidang> findBySidang(Sidang sidang) {
        String sql = "SELECT ns.*, s.*, kn.*, d.* FROM nilai_sidang ns " +
                    "JOIN sidang s ON ns.sidang_id = s.sidang_id " +
                    "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id " +
                    "JOIN dosen d ON ns.dosen_id = d.dosen_id " +
                    "WHERE ns.sidang_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToNilaiSidang, sidang.getSidangId());
    }

    @Override
    public List<NilaiSidang> findBySidangAndDosen(Sidang sidang, Dosen dosen) {
        String sql = "SELECT ns.*, s.*, kn.*, d.* FROM nilai_sidang ns " +
                    "JOIN sidang s ON ns.sidang_id = s.sidang_id " +
                    "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id " +
                    "JOIN dosen d ON ns.dosen_id = d.dosen_id " +
                    "WHERE ns.sidang_id = ? AND ns.dosen_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToNilaiSidang, sidang.getSidangId(), dosen.getDosenId());
    }

    @Override
    public Map<TipePenilai, Float> getAverageNilaiBySidang(Sidang sidang) {
        String sql = "SELECT kn.tipe_penilai, AVG(ns.nilai * kn.bobot / 100) as avg_nilai " +
                    "FROM nilai_sidang ns " +
                    "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id " +
                    "WHERE ns.sidang_id = ? " +
                    "GROUP BY kn.tipe_penilai";
        
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, sidang.getSidangId());
        Map<TipePenilai, Float> averages = new HashMap<>();
        
        for (Map<String, Object> row : results) {
            TipePenilai tipePenilai = TipePenilai.valueOf(((String) row.get("tipe_penilai")).toUpperCase());
            Float avgNilai = ((Double) row.get("avg_nilai")).floatValue();
            averages.put(tipePenilai, avgNilai);
        }
        
        return averages;
    }

    @Override
    public Float getFinalNilaiBySidang(Sidang sidang) {
        Map<TipePenilai, Float> averages = getAverageNilaiBySidang(sidang);
        // Assuming equal weight between penguji and pembimbing (50-50)
        Float nilaiPenguji = averages.getOrDefault(TipePenilai.penguji, 0f);
        Float nilaiPembimbing = averages.getOrDefault(TipePenilai.pembimbing, 0f);
        return (nilaiPenguji + nilaiPembimbing) / 2;
    }

    @Override
    public NilaiSidang save(NilaiSidang nilaiSidang) {
        if (nilaiSidang.getNilaiId() != null) {
            // Update
            String sql = "UPDATE nilai_sidang SET sidang_id = ?, komponen_id = ?, " +
                        "dosen_id = ?, nilai = ?, updated_at = ? WHERE nilai_id = ?";
            jdbcTemplate.update(sql,
                nilaiSidang.getSidang().getSidangId(),
                nilaiSidang.getKomponenNilai().getKomponenId(),
                nilaiSidang.getDosen().getDosenId(),
                nilaiSidang.getNilai(),
                LocalDateTime.now(),
                nilaiSidang.getNilaiId()
            );
        } else {
            // Insert
            String sql = "INSERT INTO nilai_sidang (sidang_id, komponen_id, dosen_id, " +
                        "nilai, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            jdbcTemplate.update(sql,
                nilaiSidang.getSidang().getSidangId(),
                nilaiSidang.getKomponenNilai().getKomponenId(),
                nilaiSidang.getDosen().getDosenId(),
                nilaiSidang.getNilai(),
                now,
                now
            );
            
            // Get the generated ID
            Long generatedId = jdbcTemplate.queryForObject(
                "SELECT currval('nilai_sidang_nilai_id_seq')",
                Long.class
            );
            nilaiSidang.setNilaiId(generatedId);
        }
        return nilaiSidang;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM nilai_sidang WHERE nilai_id = ?", id);
    }

    @Override
    public boolean hasDosenCompletedPenilaian(Sidang sidang, Dosen dosen) {
        // Get total number of komponen for the dosen's role
        String sqlKomponen = "SELECT COUNT(*) FROM komponen_nilai kn " +
                           "WHERE kn.semester_id = ? AND kn.tipe_penilai = " +
                           "(SELECT CASE " +
                           "  WHEN EXISTS (SELECT 1 FROM pembimbing_ta pt WHERE pt.ta_id = ? AND pt.dosen_id = ?) " +
                           "  THEN 'pembimbing'::tipe_penilai " +
                           "  ELSE 'penguji'::tipe_penilai " +
                           "END)";
        
        Integer totalKomponen = jdbcTemplate.queryForObject(sqlKomponen, Integer.class,
            sidang.getTugasAkhir().getSemester().getSemesterId(),
            sidang.getTugasAkhir().getTaId(),
            dosen.getDosenId()
        );

        // Get number of nilai given by the dosen
        String sqlNilai = "SELECT COUNT(*) FROM nilai_sidang WHERE sidang_id = ? AND dosen_id = ?";
        Integer givenNilai = jdbcTemplate.queryForObject(sqlNilai, Integer.class,
            sidang.getSidangId(),
            dosen.getDosenId()
        );

        return totalKomponen != null && givenNilai != null && totalKomponen.equals(givenNilai);
    }

    private NilaiSidang mapRowToNilaiSidang(ResultSet rs, int rowNum) throws SQLException {
        NilaiSidang nilaiSidang = new NilaiSidang();
        nilaiSidang.setNilaiId(rs.getLong("nilai_id"));
        nilaiSidang.setNilai(rs.getFloat("nilai"));
        nilaiSidang.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        nilaiSidang.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        // Map Sidang
        Sidang sidang = new Sidang();
        sidang.setSidangId(rs.getLong("sidang_id"));
        nilaiSidang.setSidang(sidang);

        // Map KomponenNilai
        KomponenNilai komponenNilai = new KomponenNilai();
        komponenNilai.setKomponenId(rs.getLong("komponen_id"));
        nilaiSidang.setKomponenNilai(komponenNilai);

        // Map Dosen
        Dosen dosen = new Dosen();
        dosen.setDosenId(rs.getInt("dosen_id"));
        nilaiSidang.setDosen(dosen);

        return nilaiSidang;
    }
}
