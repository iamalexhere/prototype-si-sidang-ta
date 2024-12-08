package com.rpl.project_sista.jdbcrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.entity.Semester;
import com.rpl.project_sista.model.enums.JenisTA;
import com.rpl.project_sista.model.enums.Periode;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.repository.TugasAkhirRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTugasAkhirRepository implements TugasAkhirRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTugasAkhirRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<TugasAkhir> findAll() {
        String sql = "SELECT ta.*, m.npm, m.nama as mahasiswa_nama, s.tahun_ajaran, s.periode " +
                     "FROM tugas_akhir ta " +
                     "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                     "JOIN semester s ON ta.semester_id = s.semester_id";
        
        logger.info("Executing findAll query: {}", sql);
        return jdbcTemplate.query(sql, this::mapRowToTugasAkhir);
    }

    @Override
    public Optional<TugasAkhir> findById(Integer id) {
        String sql = "SELECT ta.*, m.npm, m.nama as mahasiswa_nama, s.tahun_ajaran, s.periode " +
                     "FROM tugas_akhir ta " +
                     "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                     "JOIN semester s ON ta.semester_id = s.semester_id " +
                     "WHERE ta.ta_id = ?";
        
        try {
            TugasAkhir tugasAkhir = jdbcTemplate.queryForObject(sql, this::mapRowToTugasAkhir, id);
            return Optional.ofNullable(tugasAkhir);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
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
        semester.setSemesterId(Long.valueOf(rs.getInt("semester_id")));
        semester.setTahunAjaran(rs.getString("tahun_ajaran"));
        
        String periodeStr = rs.getString("periode");
        logger.info("Mapping Tugas Akhir - Semester Periode: {}", periodeStr);
        semester.setPeriode(Periode.valueOf(periodeStr.toLowerCase()));
        
        ta.setSemester(semester);
        
        ta.setJudul(rs.getString("judul"));
        ta.setTopik(rs.getString("topik"));
        
        String jenisTaStr = rs.getString("jenis_ta");
        logger.info("Mapping Tugas Akhir - Jenis TA: {}", jenisTaStr);
        ta.setJenisTA(JenisTA.valueOf(jenisTaStr));
        
        String statusStr = rs.getString("status");
        logger.info("Mapping Tugas Akhir - Status: {}", statusStr);
        ta.setStatus(StatusTA.valueOf(statusStr.toLowerCase()));
        
        ta.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        return ta;
    }
}
