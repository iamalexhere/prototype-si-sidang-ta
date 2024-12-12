package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.model.entity.Semester;
import com.rpl.project_sista.model.enums.Periode;
import com.rpl.project_sista.model.enums.TipePenilai;
import com.rpl.project_sista.repository.KomponenNilaiRepository;
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
public class JdbcKomponenNilaiRepository implements KomponenNilaiRepository {
    
    private final JdbcTemplate jdbcTemplate;
    private final SemesterRowMapper semesterRowMapper;

    @Autowired
    public JdbcKomponenNilaiRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.semesterRowMapper = new SemesterRowMapper();
    }

    @Override
    public List<KomponenNilai> findAll() {
        String sql = "SELECT kn.*, s.* FROM komponen_nilai kn " +
                    "JOIN semester s ON kn.semester_id = s.semester_id";
        return jdbcTemplate.query(sql, new KomponenNilaiRowMapper());
    }

    @Override
    public Optional<KomponenNilai> findById(Long id) {
        String sql = "SELECT kn.*, s.* FROM komponen_nilai kn " +
                    "JOIN semester s ON kn.semester_id = s.semester_id " +
                    "WHERE kn.komponen_id = ?";
        try {
            KomponenNilai komponenNilai = jdbcTemplate.queryForObject(
                sql, new KomponenNilaiRowMapper(), id);
            return Optional.ofNullable(komponenNilai);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<KomponenNilai> findBySemester(Semester semester) {
        String sql = "SELECT kn.*, s.* FROM komponen_nilai kn " +
                    "JOIN semester s ON kn.semester_id = s.semester_id " +
                    "WHERE kn.semester_id = ?";
        return jdbcTemplate.query(sql, new KomponenNilaiRowMapper(), semester.getSemesterId());
    }

    @Override
    public List<KomponenNilai> findBySemesterAndTipePenilai(Semester semester, TipePenilai tipePenilai) {
        String sql = "SELECT kn.*, s.* FROM komponen_nilai kn " +
                    "JOIN semester s ON kn.semester_id = s.semester_id " +
                    "WHERE kn.semester_id = ? AND kn.tipe_penilai = ?::tipe_penilai";
        return jdbcTemplate.query(sql, new KomponenNilaiRowMapper(), 
                                semester.getSemesterId(), tipePenilai.toString().toLowerCase());
    }

    @Override
    public KomponenNilai save(KomponenNilai komponenNilai) {
        if (komponenNilai.getKomponenId() != null) {
            // Update
            String sql = "UPDATE komponen_nilai SET semester_id = ?, nama_komponen = ?, " +
                        "bobot = ?, tipe_penilai = ?::tipe_penilai, deskripsi = ? " +
                        "WHERE komponen_id = ?";
            jdbcTemplate.update(sql,
                komponenNilai.getSemester().getSemesterId(),
                komponenNilai.getNamaKomponen(),
                komponenNilai.getBobot(),
                komponenNilai.getTipePenilai().toString().toLowerCase(),
                komponenNilai.getDeskripsi(),
                komponenNilai.getKomponenId()
            );
        } else {
            // Insert with RETURNING clause
            String sql = "INSERT INTO komponen_nilai (semester_id, nama_komponen, bobot, " +
                        "tipe_penilai, deskripsi, created_at) " +
                        "VALUES (?, ?, ?, ?::tipe_penilai, ?, ?) RETURNING komponen_id";
            
            Long generatedId = jdbcTemplate.queryForObject(sql,
                Long.class,
                komponenNilai.getSemester().getSemesterId(),
                komponenNilai.getNamaKomponen(),
                komponenNilai.getBobot(),
                komponenNilai.getTipePenilai().toString().toLowerCase(),
                komponenNilai.getDeskripsi(),
                LocalDateTime.now()
            );
            
            komponenNilai.setKomponenId(generatedId);
        }
        return komponenNilai;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM komponen_nilai WHERE komponen_id = ?", id);
    }

    @Override
    public long count() {
        return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM komponen_nilai", Long.class);
    }

    private class KomponenNilaiRowMapper implements RowMapper<KomponenNilai> {
        @Override
        public KomponenNilai mapRow(ResultSet rs, int rowNum) throws SQLException {
            KomponenNilai komponenNilai = new KomponenNilai();
            komponenNilai.setKomponenId(rs.getLong("komponen_id"));
            komponenNilai.setNamaKomponen(rs.getString("nama_komponen"));
            komponenNilai.setBobot(rs.getFloat("bobot"));
            komponenNilai.setTipePenilai(TipePenilai.valueOf(rs.getString("tipe_penilai")));
            komponenNilai.setDeskripsi(rs.getString("deskripsi"));
            komponenNilai.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            
            // Map the semester
            Semester semester = semesterRowMapper.mapRow(rs, rowNum);
            komponenNilai.setSemester(semester);
            
            return komponenNilai;
        }
    }

    private static class SemesterRowMapper implements RowMapper<Semester> {
        @Override
        public Semester mapRow(ResultSet rs, int rowNum) throws SQLException {
            Semester semester = new Semester();
            semester.setSemesterId(rs.getLong("semester_id"));
            semester.setTahunAjaran(rs.getString("tahun_ajaran"));
            semester.setPeriode(Periode.valueOf(rs.getString("periode").toLowerCase()));
            semester.setIsActive(rs.getBoolean("is_active"));
            semester.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return semester;
        }
    }
}
