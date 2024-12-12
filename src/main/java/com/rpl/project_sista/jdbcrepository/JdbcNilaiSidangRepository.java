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

// Repository untuk mengelola nilai sidang menggunakan JDBC.
@Repository
public class JdbcNilaiSidangRepository implements NilaiSidangRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Mengambil semua nilai sidang dari database.
    @Override
    public List<NilaiSidang> findAll() {
        String sql = "SELECT ns.*, s.*, kn.*, d.* FROM nilai_sidang ns " +
                    "JOIN sidang s ON ns.sidang_id = s.sidang_id " +
                    "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id " +
                    "JOIN dosen d ON ns.dosen_id = d.dosen_id";
        return jdbcTemplate.query(sql, this::mapRowToNilaiSidang);
    }

    // Mengambil nilai sidang berdasarkan ID.
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

    // Mengambil nilai sidang berdasarkan data Sidang.
    @Override
    public List<NilaiSidang> findBySidang(Sidang sidang) {
        String sql = "SELECT ns.*, s.*, kn.*, d.* FROM nilai_sidang ns " +
                    "JOIN sidang s ON ns.sidang_id = s.sidang_id " +
                    "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id " +
                    "JOIN dosen d ON ns.dosen_id = d.dosen_id " +
                    "WHERE ns.sidang_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToNilaiSidang, sidang.getSidangId());
    }

    // Mengambil nilai sidang berdasarkan data Sidang dan Dosen.
    @Override
    public List<NilaiSidang> findBySidangAndDosen(Sidang sidang, Dosen dosen) {
        String sql = "SELECT ns.*, s.*, kn.*, d.* FROM nilai_sidang ns " +
                    "JOIN sidang s ON ns.sidang_id = s.sidang_id " +
                    "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id " +
                    "JOIN dosen d ON ns.dosen_id = d.dosen_id " +
                    "WHERE ns.sidang_id = ? AND ns.dosen_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToNilaiSidang, sidang.getSidangId(), dosen.getDosenId());
    }

    // Menghitung rata-rata nilai sidang berdasarkan data Sidang.
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
            TipePenilai tipePenilai = TipePenilai.valueOf(((String) row.get("tipe_penilai")).toLowerCase());
            Float avgNilai = ((Double) row.get("avg_nilai")).floatValue();
            averages.put(tipePenilai, avgNilai);
        }
        
        return averages;
    }

    // Menghitung nilai akhir sidang berdasarkan data Sidang.
    @Override
    public Float getFinalNilaiBySidang(Sidang sidang) {
        Map<TipePenilai, Float> averages = getAverageNilaiBySidang(sidang);
        // Diasumsikan bobot yang sama antara penguji dan pembimbing (50-50)
        Float nilaiPenguji = averages.getOrDefault(TipePenilai.penguji, 0f);
        Float nilaiPembimbing = averages.getOrDefault(TipePenilai.pembimbing, 0f);
        return (nilaiPenguji + nilaiPembimbing) / 2;
    }

    // Menyimpan atau memperbarui nilai sidang.
    @Override
    public NilaiSidang save(NilaiSidang nilaiSidang) {
        if (nilaiSidang.getNilaiId() != null) {
            // Memperbarui nilai sidang yang sudah ada
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
            // Menyimpan nilai sidang baru
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
            
            // Mendapatkan ID yang dihasilkan
            Long generatedId = jdbcTemplate.queryForObject(
                "SELECT currval('nilai_sidang_nilai_id_seq')",
                Long.class
            );
            nilaiSidang.setNilaiId(generatedId);
        }
        return nilaiSidang;
    }

    // Menghapus nilai sidang berdasarkan ID.
    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM nilai_sidang WHERE nilai_id = ?", id);
    }

    // Mengecek apakah dosen sudah menyelesaikan penilaian.
    @Override
    public boolean hasDosenCompletedPenilaian(Sidang sidang, Dosen dosen) {
        // Mendapatkan total jumlah komponen untuk peran dosen
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

        // Mendapatkan jumlah nilai yang diberikan oleh dosen
        String sqlNilai = "SELECT COUNT(*) FROM nilai_sidang WHERE sidang_id = ? AND dosen_id = ?";
        Integer givenNilai = jdbcTemplate.queryForObject(sqlNilai, Integer.class,
            sidang.getSidangId(),
            dosen.getDosenId()
        );

        return totalKomponen != null && givenNilai != null && totalKomponen.equals(givenNilai);
    }

    // Mapping data ResultSet ke objek NilaiSidang.
    private NilaiSidang mapRowToNilaiSidang(ResultSet rs, int rowNum) throws SQLException {
        NilaiSidang nilaiSidang = new NilaiSidang();
        nilaiSidang.setNilaiId(rs.getLong("nilai_id"));
        nilaiSidang.setNilai(rs.getFloat("nilai"));
        nilaiSidang.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        nilaiSidang.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

        // Mapping data Sidang
        Sidang sidang = new Sidang();
        sidang.setSidangId(rs.getLong("sidang_id"));
        nilaiSidang.setSidang(sidang);

        // Mapping data KomponenNilai
        KomponenNilai komponenNilai = new KomponenNilai();
        komponenNilai.setKomponenId(rs.getLong("komponen_id"));
        nilaiSidang.setKomponenNilai(komponenNilai);

        // Mapping data Dosen
        Dosen dosen = new Dosen();
        dosen.setDosenId(rs.getInt("dosen_id"));
        nilaiSidang.setDosen(dosen);

        return nilaiSidang;
    }
}
