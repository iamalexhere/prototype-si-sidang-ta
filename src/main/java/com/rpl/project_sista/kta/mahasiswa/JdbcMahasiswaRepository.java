package com.rpl.project_sista.kta.mahasiswa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMahasiswaRepository implements MahasiswaRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Mahasiswa> findAll() {
        return jdbcTemplate.query("SELECT * FROM mahasiswa", this::mapRowToMahasiswa);
    }

    @Override
    public Optional<Mahasiswa> findById(Integer id) {
        List<Mahasiswa> results = jdbcTemplate.query("SELECT * FROM mahasiswa WHERE mahasiswa_id = ?", this::mapRowToMahasiswa, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Mahasiswa save(Mahasiswa mahasiswa) {
        if (mahasiswa.getMahasiswaId() != null) {
            jdbcTemplate.update("UPDATE mahasiswa SET npm = ?, nama = ? WHERE mahasiswa_id = ?", mahasiswa.getNpm(), mahasiswa.getNama(), mahasiswa.getMahasiswaId());
        } else {
            jdbcTemplate.update("INSERT INTO mahasiswa (npm, nama, user_id) VALUES (?, ?, ?)", mahasiswa.getNpm(), mahasiswa.getNama(), mahasiswa.getUserId());
        }
        return mahasiswa;
    }

    @Override
    public List<Mahasiswa> findByName(String name) {
        return jdbcTemplate.query("SELECT * FROM mahasiswa WHERE nama ILIKE ?", this::mapRowToMahasiswa, "%" + name + "%");
    }

    @Override
    public List<Mahasiswa> findPaginated(int page, int size, String filter) {
        int offset = (page - 1) * size;
        String query = filter.isEmpty() ? "SELECT * FROM mahasiswa LIMIT ? OFFSET ?" : "SELECT * FROM mahasiswa WHERE nama ILIKE ? LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, this::mapRowToMahasiswa, filter.isEmpty() ? new Object[]{size, offset} : new Object[]{"%" + filter + "%", size, offset});
    }

    @Override
    public int count(String filter) {
        String query = filter.isEmpty() ? "SELECT COUNT(*) FROM mahasiswa" : "SELECT COUNT(*) FROM mahasiswa WHERE nama ILIKE ?";
        return jdbcTemplate.queryForObject(query, Integer.class, filter.isEmpty() ? new Object[]{} : new Object[]{"%" + filter + "%"});
    }

    private Mahasiswa mapRowToMahasiswa(ResultSet rs, int rowNum) throws SQLException {
        return new Mahasiswa(rs.getInt("mahasiswa_id"), rs.getString("npm"), rs.getString("nama"), rs.getInt("user_id"), rs.getString("status_ta"));
    }
}
