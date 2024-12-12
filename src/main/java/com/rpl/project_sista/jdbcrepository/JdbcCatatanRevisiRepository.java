package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.CatatanRevisi;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.repository.CatatanRevisiRepository;
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

@Repository
public class JdbcCatatanRevisiRepository implements CatatanRevisiRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<CatatanRevisi> findAll() {
        String sql = "SELECT cr.*, s.*, d.* FROM catatan_revisi cr " +
                    "JOIN sidang s ON cr.sidang_id = s.sidang_id " +
                    "JOIN dosen d ON cr.dosen_id = d.dosen_id " +
                    "ORDER BY cr.created_at DESC";
        return jdbcTemplate.query(sql, this::mapRowToCatatanRevisi);
    }

    @Override
    public Optional<CatatanRevisi> findById(Long id) {
        try {
            String sql = "SELECT cr.*, s.*, d.* FROM catatan_revisi cr " +
                        "JOIN sidang s ON cr.sidang_id = s.sidang_id " +
                        "JOIN dosen d ON cr.dosen_id = d.dosen_id " +
                        "WHERE cr.catatan_id = ?";
            CatatanRevisi catatan = jdbcTemplate.queryForObject(sql, this::mapRowToCatatanRevisi, id);
            return Optional.ofNullable(catatan);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CatatanRevisi> findBySidang(Sidang sidang) {
        String sql = "SELECT cr.*, s.*, d.* FROM catatan_revisi cr " +
                    "JOIN sidang s ON cr.sidang_id = s.sidang_id " +
                    "JOIN dosen d ON cr.dosen_id = d.dosen_id " +
                    "WHERE cr.sidang_id = ? " +
                    "ORDER BY cr.created_at DESC";
        return jdbcTemplate.query(sql, this::mapRowToCatatanRevisi, sidang.getSidangId());
    }

    @Override
    public List<CatatanRevisi> findByDosen(Dosen dosen) {
        String sql = "SELECT cr.*, s.*, d.* FROM catatan_revisi cr " +
                    "JOIN sidang s ON cr.sidang_id = s.sidang_id " +
                    "JOIN dosen d ON cr.dosen_id = d.dosen_id " +
                    "WHERE cr.dosen_id = ? " +
                    "ORDER BY cr.created_at DESC";
        return jdbcTemplate.query(sql, this::mapRowToCatatanRevisi, dosen.getDosenId());
    }

    @Override
    public List<CatatanRevisi> findBySidangAndDosen(Sidang sidang, Dosen dosen) {
        String sql = "SELECT cr.*, s.*, d.* FROM catatan_revisi cr " +
                    "JOIN sidang s ON cr.sidang_id = s.sidang_id " +
                    "JOIN dosen d ON cr.dosen_id = d.dosen_id " +
                    "WHERE cr.sidang_id = ? AND cr.dosen_id = ? " +
                    "ORDER BY cr.created_at DESC";
        return jdbcTemplate.query(sql, this::mapRowToCatatanRevisi, 
                                sidang.getSidangId(), dosen.getDosenId());
    }

    @Override
    public CatatanRevisi save(CatatanRevisi catatanRevisi) {
        if (catatanRevisi.getCatatanId() != null) {
            // Update existing catatan
            String sql = "UPDATE catatan_revisi SET sidang_id = ?, dosen_id = ?, " +
                        "isi_catatan = ? WHERE catatan_id = ?";
            jdbcTemplate.update(sql,
                catatanRevisi.getSidang().getSidangId(),
                catatanRevisi.getDosen().getDosenId(),
                catatanRevisi.getIsiCatatan(),
                catatanRevisi.getCatatanId()
            );
        } else {
            // Insert new catatan
            String sql = "INSERT INTO catatan_revisi (sidang_id, dosen_id, isi_catatan, created_at) " +
                        "VALUES (?, ?, ?, ?)";
            LocalDateTime now = LocalDateTime.now();
            jdbcTemplate.update(sql,
                catatanRevisi.getSidang().getSidangId(),
                catatanRevisi.getDosen().getDosenId(),
                catatanRevisi.getIsiCatatan(),
                now
            );
            
            // Get the generated ID
            Long generatedId = jdbcTemplate.queryForObject(
                "SELECT currval('catatan_revisi_catatan_id_seq')",
                Long.class
            );
            catatanRevisi.setCatatanId(generatedId);
            catatanRevisi.setCreatedAt(now);
        }
        return catatanRevisi;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM catatan_revisi WHERE catatan_id = ?", id);
    }

    @Override
    public long countBySidang(Sidang sidang) {
        return jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM catatan_revisi WHERE sidang_id = ?",
            Long.class,
            sidang.getSidangId()
        );
    }

    private CatatanRevisi mapRowToCatatanRevisi(ResultSet rs, int rowNum) throws SQLException {
        CatatanRevisi catatan = new CatatanRevisi();
        catatan.setCatatanId(rs.getLong("catatan_id"));
        catatan.setIsiCatatan(rs.getString("isi_catatan"));
        catatan.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        // Map Sidang
        Sidang sidang = new Sidang();
        sidang.setSidangId(rs.getLong("sidang_id"));
        
        // Map TugasAkhir for Sidang if needed
        TugasAkhir ta = new TugasAkhir();
        ta.setTaId(rs.getLong("ta_id"));
        sidang.setTugasAkhir(ta);
        
        catatan.setSidang(sidang);

        // Map Dosen
        Dosen dosen = new Dosen();
        dosen.setDosenId(rs.getInt("dosen_id"));
        dosen.setNama(rs.getString("nama")); // Assuming dosen nama is selected
        catatan.setDosen(dosen);

        return catatan;
    }
}
