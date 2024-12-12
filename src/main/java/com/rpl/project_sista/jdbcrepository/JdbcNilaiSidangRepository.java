package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.NilaiSidang;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.KomponenNilai;
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
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;

// Repository untuk mengelola data nilai sidang menggunakan JDBC.
@Repository
public class JdbcNilaiSidangRepository implements NilaiSidangRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Mengambil semua data nilai sidang dari database.
    @Override
    public List<NilaiSidang> findAll() {
         String sql = "SELECT ns.*, s.*, d.*, kn.* FROM nilai_sidang ns " +
                         "JOIN sidang s ON ns.sidang_id = s.sidang_id "+
                         "JOIN dosen d ON ns.dosen_id = d.dosen_id "+
                         "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id";

        return jdbcTemplate.query(sql, this::mapRowToNilaiSidang);
    }

    // Mengambil data nilai sidang berdasarkan ID.
    @Override
    public Optional<NilaiSidang> findById(Long id) {
        String sql = "SELECT ns.*, s.*, d.*, kn.* FROM nilai_sidang ns " +
                 "JOIN sidang s ON ns.sidang_id = s.sidang_id "+
                 "JOIN dosen d ON ns.dosen_id = d.dosen_id "+
                "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id "+
                "WHERE ns.nilai_id = ?";
        try {
            NilaiSidang nilaiSidang = jdbcTemplate.queryForObject(sql, this::mapRowToNilaiSidang, id);
            return Optional.ofNullable(nilaiSidang);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    // Find nilai by sidang
    @Override
     public List<NilaiSidang> findBySidang(Sidang sidang) {
        String sql = "SELECT ns.*, s.*, d.*, kn.* FROM nilai_sidang ns " +
                 "JOIN sidang s ON ns.sidang_id = s.sidang_id "+
                 "JOIN dosen d ON ns.dosen_id = d.dosen_id "+
                "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id "+
                "WHERE ns.sidang_id = ?";
           return jdbcTemplate.query(sql, this::mapRowToNilaiSidang, sidang.getSidangId());
     }


    // Find nilai given by a specific dosen for a sidang
    @Override
    public List<NilaiSidang> findBySidangAndDosen(Sidang sidang, Dosen dosen) {
        String sql = "SELECT ns.*, s.*, d.*, kn.* FROM nilai_sidang ns " +
                 "JOIN sidang s ON ns.sidang_id = s.sidang_id "+
                 "JOIN dosen d ON ns.dosen_id = d.dosen_id "+
                "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id "+
                "WHERE ns.sidang_id = ? AND ns.dosen_id = ?";
        return jdbcTemplate.query(sql, this::mapRowToNilaiSidang, sidang.getSidangId(), dosen.getDosenId());
    }
    @Override
    public Map<TipePenilai, Float> getAverageNilaiBySidang(Sidang sidang){
        String sql = "SELECT kn.tipe_penilai, AVG(ns.nilai) as avg_nilai FROM nilai_sidang ns " +
                    "JOIN komponen_nilai kn ON ns.komponen_id = kn.komponen_id " +
                     "WHERE ns.sidang_id = ? "+
                    "GROUP BY kn.tipe_penilai";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, sidang.getSidangId());

        return rows.stream().collect(Collectors.toMap(
                row -> TipePenilai.valueOf(((String) row.get("tipe_penilai")).toUpperCase()),
                row -> ((Number) row.get("avg_nilai")).floatValue()
             ));
    }
     // Get final nilai for a sidang (weighted average of all components)
     @Override
     public Float getFinalNilaiBySidang(Sidang sidang){
         Map<TipePenilai, Float> averages = getAverageNilaiBySidang(sidang);
         if (averages.isEmpty()) {
             return 0f;
         }
         
         float totalScore = 0f;
         int count = 0;
         
         if (averages.containsKey(TipePenilai.PENGUJI)) {
             totalScore += averages.get(TipePenilai.PENGUJI);
             count++;
         }
         if (averages.containsKey(TipePenilai.PEMBIMBING)) {
             totalScore += averages.get(TipePenilai.PEMBIMBING);
             count++;
         }
         
         return count > 0 ? totalScore / count : 0f;
     }
    // Menyimpan atau memperbarui data nilai sidang.
      @Override
     public NilaiSidang save(NilaiSidang nilaiSidang) {
        if (nilaiSidang.getNilaiId() == null) {
          return insert(nilaiSidang);
         } else {
            update(nilaiSidang);
             return nilaiSidang;
         }
     }

    private NilaiSidang insert(NilaiSidang nilaiSidang) {
        String sql = "INSERT INTO nilai_sidang (sidang_id, komponen_id, dosen_id, nilai, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

         KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, nilaiSidang.getSidang().getSidangId());
            ps.setObject(2, nilaiSidang.getKomponenNilai().getKomponenId());
             ps.setObject(3, nilaiSidang.getDosen().getDosenId());
            ps.setObject(4, nilaiSidang.getNilai());
            ps.setObject(5, LocalDateTime.now());
           ps.setObject(6, LocalDateTime.now());
            return ps;
         }, keyHolder);

         nilaiSidang.setNilaiId(keyHolder.getKey().longValue());
          return nilaiSidang;

     }

      private void update(NilaiSidang nilaiSidang) {
         String sql = "UPDATE nilai_sidang SET sidang_id = ?, komponen_id = ?, dosen_id = ?, nilai = ?, updated_at = ? WHERE nilai_id = ?";
         jdbcTemplate.update(sql,
                 nilaiSidang.getSidang().getSidangId(),
                 nilaiSidang.getKomponenNilai().getKomponenId(),
                 nilaiSidang.getDosen().getDosenId(),
                nilaiSidang.getNilai(),
                 LocalDateTime.now(),
                nilaiSidang.getNilaiId()
        );
      }

    // Menghapus data nilai sidang berdasarkan ID.
     @Override
      public void deleteById(Long id) {
           jdbcTemplate.update("DELETE FROM nilai_sidang WHERE nilai_id = ?", id);
      }
      
     // Check if dosen has completed all nilai components for a sidang
     @Override
     public boolean hasDosenCompletedPenilaian(Sidang sidang, Dosen dosen) {
      // First get the dosen's role (penguji or pembimbing)
      String roleQuery = "SELECT UPPER(role::TEXT) as role FROM users WHERE user_id = ?";
      String role = jdbcTemplate.queryForObject(roleQuery, String.class, dosen.getDosenId());
      
      if (role == null) {
          return false;
      }

      String sql = "SELECT COUNT(DISTINCT kn.komponen_id) " +
                "FROM komponen_nilai kn " +
                "WHERE kn.semester_id = (SELECT ta.semester_id FROM tugas_akhir ta JOIN sidang s ON s.ta_id = ta.ta_id WHERE s.sidang_id = ?) " +
                "AND UPPER(kn.tipe_penilai::TEXT) = ?";
                
      Integer totalComponents = jdbcTemplate.queryForObject(sql, Integer.class, sidang.getSidangId(), role);

      String sql2 = "SELECT COUNT(*) FROM nilai_sidang ns " +
                "WHERE ns.sidang_id = ? AND ns.dosen_id = ?";

      Integer ratedComponents = jdbcTemplate.queryForObject(sql2, Integer.class, sidang.getSidangId(), dosen.getDosenId());
      
        return totalComponents != null && ratedComponents != null && totalComponents.equals(ratedComponents);
    }


    private NilaiSidang mapRowToNilaiSidang(ResultSet rs, int rowNum) throws SQLException{
        NilaiSidang nilaiSidang = new NilaiSidang();
          nilaiSidang.setNilaiId(rs.getLong("nilai_id"));
          nilaiSidang.setNilai(rs.getFloat("nilai"));
           nilaiSidang.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
          nilaiSidang.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

          // Mapping Sidang
           Sidang sidang = new Sidang();
            sidang.setSidangId(rs.getLong("sidang_id"));
           nilaiSidang.setSidang(sidang);
        // Mapping Dosen
            Dosen dosen = new Dosen();
           dosen.setDosenId(rs.getInt("dosen_id"));
           dosen.setNama(rs.getString("nama"));
            nilaiSidang.setDosen(dosen);
           // Mapping Komponen Nilai
           KomponenNilai komponenNilai = new KomponenNilai();
              komponenNilai.setKomponenId(rs.getLong("komponen_id"));
              komponenNilai.setNamaKomponen(rs.getString("nama_komponen"));
              komponenNilai.setBobot(rs.getFloat("bobot"));
              komponenNilai.setTipePenilai(TipePenilai.valueOf(rs.getString("tipe_penilai")));
              nilaiSidang.setKomponenNilai(komponenNilai);

             return nilaiSidang;

      }
}