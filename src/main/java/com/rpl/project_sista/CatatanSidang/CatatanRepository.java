package com.rpl.project_sista.CatatanSidang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class CatatanRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CatatanRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Fetch all catatan
    public List<Catatan> findAll() {
        String sql = "SELECT * FROM catatan_revisi";
        return jdbcTemplate.query(sql, new CatatanRowMapper());
    }

    // Fetch catatan by sidangId
    public List<Catatan> findBySidangId(Integer sidangId) {
        String sql = "SELECT * FROM catatan_revisi WHERE sidang_id = ?";
        return jdbcTemplate.query(sql, new CatatanRowMapper(), sidangId);
    }

    // Fetch catatan by dosenId
    public List<Catatan> findByDosenId(Integer dosenId) {
        String sql = "SELECT * FROM catatan_revisi WHERE dosen_id = ?";
        return jdbcTemplate.query(sql, new CatatanRowMapper(), dosenId);
    }

    // Fetch catatan by ID
    public Optional<Catatan> findById(Long id) {
        String sql = "SELECT * FROM catatan_revisi WHERE catatan_id = ?";
        List<Catatan> results = jdbcTemplate.query(sql, new CatatanRowMapper(), id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    // Save a new catatan
    public Catatan save(Catatan catatan) {
        String sql = "INSERT INTO catatan_revisi (sidang_id, dosen_id, isi_catatan, created_at) VALUES (?, ?, ?, ?) RETURNING catatan_id";
        Integer catatanId = jdbcTemplate.queryForObject(sql, Integer.class,
                catatan.getSidangId(),
                catatan.getDosenId(),
                catatan.getIsiCatatan(),
                catatan.getCreatedAt());
        catatan.setCatatanId(catatanId);
        return catatan;
    }

    // Delete a catatan by ID
    public void delete(Catatan catatan) {
        String sql = "DELETE FROM catatan_revisi WHERE catatan_id = ?";
        jdbcTemplate.update(sql, catatan.getCatatanId());
    }

    // RowMapper for mapping ResultSet to Catatan object
    private static class CatatanRowMapper implements RowMapper<Catatan> {
        @Override
        public Catatan mapRow(ResultSet rs, int rowNum) throws SQLException {
            Catatan catatan = new Catatan();
            catatan.setCatatanId(rs.getInt("catatan_id"));
            catatan.setSidangId(rs.getInt("sidang_id"));
            catatan.setDosenId(rs.getInt("dosen_id"));
            catatan.setIsiCatatan(rs.getString("isi_catatan"));
            catatan.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return catatan;
        }
    }
}
