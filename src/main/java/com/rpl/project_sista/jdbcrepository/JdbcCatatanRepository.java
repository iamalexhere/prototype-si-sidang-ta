package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.CatatanRevisi;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.enums.StatusSidang;
import com.rpl.project_sista.repository.CatatanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcCatatanRepository implements CatatanRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JdbcCatatanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<CatatanRevisi> findById(Long id) {
        String sql = "SELECT * FROM catatan_revisi WHERE catatan_id = ?";
        List<CatatanRevisi> results = jdbcTemplate.query(sql, new CatatanRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public CatatanRevisi save(CatatanRevisi catatan) {
        String sql = "INSERT INTO catatan_revisi (sidang_id, dosen_id, isi_catatan, created_at) VALUES (?, ?, ?, ?) RETURNING catatan_id";
        Long catatanId = jdbcTemplate.queryForObject(sql, Long.class,
                catatan.getSidang().getSidangId(),
                catatan.getDosen().getDosenId(),
                catatan.getIsiCatatan(),
                LocalDateTime.now());
        catatan.setCatatanId(catatanId);
        return catatan;
    }

    @Override
    public void delete(CatatanRevisi catatan) {
        String sql = "DELETE FROM catatan_revisi WHERE catatan_id = ?";
        jdbcTemplate.update(sql, catatan.getCatatanId());
    }

    // RowMapper for mapping ResultSet to CatatanRevisi object
    private static class CatatanRowMapper implements RowMapper<CatatanRevisi> {
        @Override
        public CatatanRevisi mapRow(ResultSet rs, int rowNum) throws SQLException {
            CatatanRevisi catatan = new CatatanRevisi();
            catatan.setCatatanId(rs.getLong("catatan_id"));

            // Map Sidang entity
            Sidang sidang = new Sidang();
            sidang.setSidangId(rs.getLong("sidang_id"));
            catatan.setSidang(sidang);

            // Map Dosen entity
            Dosen dosen = new Dosen();
            dosen.setDosenId(rs.getInt("dosen_id"));
            catatan.setDosen(dosen);

            catatan.setIsiCatatan(rs.getString("isi_catatan"));
            catatan.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return catatan;
        }
    }
    
    @SuppressWarnings("deprecation")
    public List<CatatanRevisi> findCatatanByMahasiswaId(Long mahasiswaId) {
        String sql = "SELECT cr.catatan_id, cr.sidang_id, cr.dosen_id, cr.isi_catatan, cr.created_at " +
                     "FROM catatan_revisi cr " +
                     "JOIN sidang s ON cr.sidang_id = s.sidang_id " +
                     "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
                     "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                     "WHERE m.mahasiswa_id = ?";
        
        return jdbcTemplate.query(sql, new Object[]{mahasiswaId}, (rs, rowNum) -> {
            CatatanRevisi catatanRevisi = new CatatanRevisi();
            catatanRevisi.setCatatanId(rs.getLong("catatan_id"));
            catatanRevisi.setSidang(findSidangById(rs.getLong("sidang_id")));
            catatanRevisi.setDosen(findDosenById(rs.getLong("dosen_id")));
            catatanRevisi.setIsiCatatan(rs.getString("isi_catatan"));
            catatanRevisi.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return catatanRevisi;
        });
    }

    @SuppressWarnings("deprecation")
    public Sidang findSidangById(Long sidangId) {
        String sql = "SELECT * FROM sidang WHERE sidang_id = ?";

        return jdbcTemplate.queryForObject(sql, new Object[]{sidangId}, (rs, rowNum) -> {
            Sidang sidang = new Sidang();
            sidang.setSidangId(rs.getLong("sidang_id"));
            sidang.setJadwal(rs.getTimestamp("jadwal").toLocalDateTime());
            sidang.setRuangan(rs.getString("ruangan"));
            StatusSidang.valueOf(rs.getString("status_sidang"));
            sidang.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            sidang.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return sidang;
        });
    }

    @SuppressWarnings("deprecation")
    public Dosen findDosenById(Long dosenId) {
        String sql = "SELECT * FROM dosen WHERE dosen_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{dosenId}, (rs, rowNum) -> {
            Dosen dosen = new Dosen();
            dosen.setDosenId(rs.getInt("dosen_id"));
            dosen.setNip(rs.getString("nip"));
            dosen.setNama(rs.getString("nama"));
            dosen.setUserId(rs.getInt("user_id"));
            dosen.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return dosen;
        });
    }
}