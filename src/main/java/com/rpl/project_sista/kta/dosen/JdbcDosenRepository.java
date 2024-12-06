package com.rpl.project_sista.kta.dosen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcDosenRepository implements DosenRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Dosen> findAll() {
        return jdbcTemplate.query("SELECT * FROM dosen", this::mapRowToDosen);
    }

    @Override
    public Optional<Dosen> findById(Integer id) {
        List<Dosen> results = jdbcTemplate.query("SELECT * FROM dosen WHERE dosen_id = ?", this::mapRowToDosen, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Dosen save(Dosen dosen) {
        if (dosen.getDosenId() != null) {
            jdbcTemplate.update("UPDATE dosen SET nip = ?, nama = ? WHERE dosen_id = ?", dosen.getNip(), dosen.getNama(), dosen.getDosenId());
        } else {
            jdbcTemplate.update("INSERT INTO dosen (nip, nama) VALUES (?, ?)", dosen.getNip(), dosen.getNama());
        }
        return dosen;
    }

    @Override
    public List<Dosen> findByName(String name) {
        return jdbcTemplate.query("SELECT * FROM dosen WHERE nama ILIKE ?", this::mapRowToDosen, "%" + name + "%");
    }

    @Override
    public List<Dosen> findPaginated(int page, int size, String filter) {
        int offset = (page - 1) * size;
        String query = filter.isEmpty() ? "SELECT * FROM dosen LIMIT ? OFFSET ?" : "SELECT * FROM dosen WHERE nama ILIKE ? LIMIT ? OFFSET ?";
        return jdbcTemplate.query(query, this::mapRowToDosen, filter.isEmpty() ? new Object[]{size, offset} : new Object[]{"%" + filter + "%", size, offset});
    }

    @Override
    public int count(String filter) {
        String query = filter.isEmpty() ? "SELECT COUNT(*) FROM dosen" : "SELECT COUNT(*) FROM dosen WHERE nama ILIKE ?";
        return jdbcTemplate.queryForObject(query, Integer.class, filter.isEmpty() ? new Object[]{} : new Object[]{"%" + filter + "%"});
    }

    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update("DELETE FROM dosen WHERE dosen_id = ?", id);
    }

    private Dosen mapRowToDosen(ResultSet rs, int rowNum) throws SQLException {
        return new Dosen(rs.getInt("dosen_id"), rs.getString("nip"), rs.getString("nama"), rs.getInt("user_id")); 
    }
}
