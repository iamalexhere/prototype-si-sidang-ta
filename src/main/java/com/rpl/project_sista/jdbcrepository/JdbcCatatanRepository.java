package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.CatatanRevisi;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.Sidang;
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

    @Override
    public List<CatatanRevisi> findAll() {
        String sql = "SELECT * FROM catatan_revisi";
        return jdbcTemplate.query(sql, new CatatanRowMapper());
    }

    @Override
    public List<CatatanRevisi> findBySidangId(Long sidangId) {
        String sql = "SELECT * FROM catatan_revisi WHERE sidang_id = ?";
        return jdbcTemplate.query(sql, new CatatanRowMapper(), sidangId);
    }

    @Override
    public List<CatatanRevisi> findByDosenId(Long dosenId) {
        String sql = "SELECT * FROM catatan_revisi WHERE dosen_id = ?";
        return jdbcTemplate.query(sql, new CatatanRowMapper(), dosenId);
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
}

