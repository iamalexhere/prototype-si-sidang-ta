package com.rpl.project_sista.jdbcrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.entity.Semester;
import com.rpl.project_sista.model.enums.JenisTA;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.repository.TugasAkhirRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTugasAkhirRepository implements TugasAkhirRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<TugasAkhir> findAll() {
        return jdbcTemplate.query(
            "SELECT ta.*, m.npm, m.nama as mahasiswa_nama, s.tahun_ajaran, s.periode " +
            "FROM tugas_akhir ta " +
            "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
            "JOIN semester s ON ta.semester_id = s.semester_id",
            this::mapRowToTugasAkhir
        );
    }

    @Override
    public Optional<TugasAkhir> findById(Integer id) {
        List<TugasAkhir> results = jdbcTemplate.query(
            "SELECT ta.*, m.npm, m.nama as mahasiswa_nama, s.tahun_ajaran, s.periode " +
            "FROM tugas_akhir ta " +
            "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
            "JOIN semester s ON ta.semester_id = s.semester_id " +
            "WHERE ta.ta_id = ?",
            this::mapRowToTugasAkhir,
            id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<TugasAkhir> findByMahasiswaId(Integer mahasiswaId) {
        return jdbcTemplate.query(
            "SELECT ta.*, m.npm, m.nama as mahasiswa_nama, s.tahun_ajaran, s.periode " +
            "FROM tugas_akhir ta " +
            "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
            "JOIN semester s ON ta.semester_id = s.semester_id " +
            "WHERE ta.mahasiswa_id = ?",
            this::mapRowToTugasAkhir,
            mahasiswaId
        );
    }

    @Override
    public TugasAkhir save(TugasAkhir tugasAkhir) {
        if (tugasAkhir.getTaId() != null) {
            // Update existing tugas akhir
            jdbcTemplate.update(
                "UPDATE tugas_akhir SET mahasiswa_id = ?, semester_id = ?, judul = ?, " +
                "topik = ?, jenis_ta = ?::jenis_ta, status = ?::status_ta " +
                "WHERE ta_id = ?",
                tugasAkhir.getMahasiswa() != null ? tugasAkhir.getMahasiswa().getMahasiswaId() : null,
                tugasAkhir.getSemester() != null ? tugasAkhir.getSemester().getSemesterId() : null,
                tugasAkhir.getJudul(),
                tugasAkhir.getTopik(),
                tugasAkhir.getJenisTA().toString().toLowerCase(),
                tugasAkhir.getStatus().toString().toLowerCase(),
                tugasAkhir.getTaId()
            );
        } else {
            // Insert new tugas akhir
            jdbcTemplate.update(
                "INSERT INTO tugas_akhir (mahasiswa_id, semester_id, judul, topik, " +
                "jenis_ta, status, created_at) VALUES (?, ?, ?, ?, ?::jenis_ta, ?::status_ta, ?)",
                tugasAkhir.getMahasiswa() != null ? tugasAkhir.getMahasiswa().getMahasiswaId() : null,
                tugasAkhir.getSemester() != null ? tugasAkhir.getSemester().getSemesterId() : null,
                tugasAkhir.getJudul(),
                tugasAkhir.getTopik(),
                tugasAkhir.getJenisTA().toString().toLowerCase(),
                tugasAkhir.getStatus().toString().toLowerCase(),
                LocalDateTime.now()
            );
            
            Integer taId = jdbcTemplate.queryForObject(
                "SELECT ta_id FROM tugas_akhir WHERE mahasiswa_id = ? AND judul = ? AND created_at = ?",
                Integer.class,
                tugasAkhir.getMahasiswa() != null ? tugasAkhir.getMahasiswa().getMahasiswaId() : null,
                tugasAkhir.getJudul(),
                tugasAkhir.getCreatedAt()
            );
            
            tugasAkhir.setTaId(taId != null ? Long.valueOf(taId) : null);
        }
        return tugasAkhir;
    }

    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update("DELETE FROM tugas_akhir WHERE ta_id = ?", id);
    }

    private TugasAkhir mapRowToTugasAkhir(ResultSet rs, int rowNum) throws SQLException {
        TugasAkhir ta = new TugasAkhir();
        ta.setTaId(rs.getLong("ta_id"));
        
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setMahasiswaId(rs.getObject("mahasiswa_id", Integer.class));
        mahasiswa.setNpm(rs.getString("npm"));
        mahasiswa.setNama(rs.getString("mahasiswa_nama"));
        ta.setMahasiswa(mahasiswa);
        
        Semester semester = new Semester();
        semester.setSemesterId(rs.getObject("semester_id", Long.class));
        semester.setTahunAjaran(rs.getString("tahun_ajaran"));
        ta.setSemester(semester);
        
        ta.setJudul(rs.getString("judul"));
        ta.setTopik(rs.getString("topik"));
        ta.setJenisTA(JenisTA.valueOf(rs.getString("jenis_ta").toUpperCase()));
        ta.setStatus(StatusTA.valueOf(rs.getString("status").toUpperCase()));
        ta.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        return ta;
    }
}
