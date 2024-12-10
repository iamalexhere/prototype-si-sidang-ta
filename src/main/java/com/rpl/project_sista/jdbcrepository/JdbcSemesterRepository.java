package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.Semester;
import com.rpl.project_sista.model.enums.Periode;
import com.rpl.project_sista.repository.SemesterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcSemesterRepository implements SemesterRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Semester> rowMapper = (rs, rowNum) -> {
        Semester semester = new Semester();
        semester.setSemesterId(rs.getLong("semester_id"));
        semester.setTahunAjaran(rs.getString("tahun_ajaran"));
        semester.setPeriode(Periode.valueOf(rs.getString("periode")));
        semester.setIsActive(rs.getBoolean("is_active"));
        semester.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return semester;
    };

    @Autowired
    public JdbcSemesterRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Semester> findAll() {
        String sql = "SELECT * FROM semester ORDER BY tahun_ajaran DESC, periode DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Semester> findById(Long id) {
        String sql = "SELECT * FROM semester WHERE semester_id = ?";
        try {
            Semester semester = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(semester);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Semester save(Semester semester) {
        if (semester.getSemesterId() == null) {
            return insert(semester);
        }
        return update(semester);
    }

    private Semester insert(Semester semester) {
        String sql = "INSERT INTO semester (tahun_ajaran, periode, is_active, created_at) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, semester.getTahunAjaran());
            ps.setString(2, semester.getPeriode().toString());
            ps.setBoolean(3, semester.getIsActive());
            ps.setTimestamp(4, Timestamp.valueOf(semester.getCreatedAt()));
            return ps;
        }, keyHolder);

        semester.setSemesterId(keyHolder.getKey().longValue());
        return semester;
    }

    private Semester update(Semester semester) {
        String sql = "UPDATE semester SET tahun_ajaran = ?, periode = ?, is_active = ? WHERE semester_id = ?";
        jdbcTemplate.update(sql,
                semester.getTahunAjaran(),
                semester.getPeriode().toString(),
                semester.getIsActive(),
                semester.getSemesterId());
        return semester;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM semester WHERE semester_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Optional<Semester> findActiveSemester() {
        String sql = "SELECT * FROM semester WHERE is_active = true";
        try {
            Semester semester = jdbcTemplate.queryForObject(sql, rowMapper);
            return Optional.ofNullable(semester);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
