package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.CatatanRevisi;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Dosen;
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
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcCatatanRevisiRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CatatanRevisi> findAll() {
        String sql = "SELECT cr.*, s.*, d.* FROM catatan_revisi cr " +
                    "JOIN sidang s ON cr.sidang_id = s.sidang_id " +
                    "JOIN dosen d ON cr.dosen_id = d.dosen_id";
        return jdbcTemplate.query(sql, new CatatanRevisiRowMapper());
    }

    @Override
    public Optional<CatatanRevisi> findById(Long id) {
        String sql = "SELECT cr.*, s.*, d.* FROM catatan_revisi cr " +
                    "JOIN sidang s ON cr.sidang_id = s.sidang_id " +
                    "JOIN dosen d ON cr.dosen_id = d.dosen_id " +
                    "WHERE cr.catatan_id = ?";
        try {
            CatatanRevisi catatan = jdbcTemplate.queryForObject(sql, new CatatanRevisiRowMapper(), id);
            return Optional.ofNullable(catatan);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<CatatanRevisi> findBySidangId(Long sidangId) {
        String sql = "SELECT cr.*, s.*, d.* FROM catatan_revisi cr " +
                    "JOIN sidang s ON cr.sidang_id = s.sidang_id " +
                    "JOIN dosen d ON cr.dosen_id = d.dosen_id " +
                    "WHERE cr.sidang_id = ?";
        return jdbcTemplate.query(sql, new CatatanRevisiRowMapper(), sidangId);
    }

    @Override
    public List<CatatanRevisi> findByDosenId(Long dosenId) {
        String sql = "SELECT cr.*, s.*, d.* FROM catatan_revisi cr " +
                    "JOIN sidang s ON cr.sidang_id = s.sidang_id " +
                    "JOIN dosen d ON cr.dosen_id = d.dosen_id " +
                    "WHERE cr.dosen_id = ?";
        return jdbcTemplate.query(sql, new CatatanRevisiRowMapper(), dosenId);
    }

    @Override
    public CatatanRevisi save(CatatanRevisi catatanRevisi) {
        if (catatanRevisi.getCatatanId() != null) {
            // Update
            String sql = "UPDATE catatan_revisi SET sidang_id = ?, dosen_id = ?, " +
                        "isi_catatan = ? WHERE catatan_id = ?";
            jdbcTemplate.update(sql,
                catatanRevisi.getSidang().getSidangId(),
                catatanRevisi.getDosen().getDosenId(),
                catatanRevisi.getIsiCatatan(),
                catatanRevisi.getCatatanId()
            );
        } else {
            // Insert
            String sql = "INSERT INTO catatan_revisi (sidang_id, dosen_id, isi_catatan, created_at) " +
                        "VALUES (?, ?, ?, ?) RETURNING catatan_id";
            Long generatedId = jdbcTemplate.queryForObject(sql, Long.class,
                catatanRevisi.getSidang().getSidangId(),
                catatanRevisi.getDosen().getDosenId(),
                catatanRevisi.getIsiCatatan(),
                LocalDateTime.now()
            );
            catatanRevisi.setCatatanId(generatedId);
        }
        return catatanRevisi;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM catatan_revisi WHERE catatan_id = ?";
        jdbcTemplate.update(sql, id);
    }

    private class CatatanRevisiRowMapper implements RowMapper<CatatanRevisi> {
        @Override
        public CatatanRevisi mapRow(ResultSet rs, int rowNum) throws SQLException {
            CatatanRevisi catatan = new CatatanRevisi();
            catatan.setCatatanId(rs.getLong("catatan_id"));
            catatan.setIsiCatatan(rs.getString("isi_catatan"));
            catatan.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

            // Map Sidang
            Sidang sidang = new Sidang();
            sidang.setSidangId(rs.getLong("sidang_id"));
            catatan.setSidang(sidang);

            // Map Dosen
            Dosen dosen = new Dosen();
            dosen.setDosenId(rs.getLong("dosen_id"));
            dosen.setNip(rs.getString("nip"));
            dosen.setNama(rs.getString("nama"));
            catatan.setDosen(dosen);

            return catatan;
        }
    }
}
