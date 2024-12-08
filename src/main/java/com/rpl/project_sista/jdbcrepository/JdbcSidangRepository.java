package com.rpl.project_sista.jdbcrepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.enums.StatusSidang;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.repository.SidangRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcSidangRepository implements SidangRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(JdbcSidangRepository.class);

    @Override
    public List<Sidang> findAll() {
        String sql = "SELECT s.sidang_id, s.ta_id, ta.judul, ta.topik, ta.status, " +
                     "m.nama AS mahasiswa_nama, m.npm, " +
                     "s.jadwal, s.ruangan, s.status_sidang, s.created_at, s.updated_at " +
                     "FROM sidang s " +
                     "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
                     "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                     "ORDER BY s.jadwal DESC";
        
        List<Sidang> sidangList = jdbcTemplate.query(sql, this::mapRowToSidang);
        
        // Fetch pembimbing for each Sidang
        for (Sidang sidang : sidangList) {
            if (sidang.getTugasAkhir() != null) {
                List<Dosen> pembimbing = fetchPembimbingForTugasAkhir(sidang.getTugasAkhir().getTaId());
                sidang.getTugasAkhir().setPembimbing(new HashSet<>(pembimbing));
            }
        }
        
        return sidangList;
    }

    @Override
    public Optional<Sidang> findById(Integer id) {
        String sql = "SELECT s.sidang_id, s.ta_id, ta.judul, ta.topik, ta.status, " +
                     "m.nama AS mahasiswa_nama, m.npm, " +
                     "s.jadwal, s.ruangan, s.status_sidang, s.created_at, s.updated_at " +
                     "FROM sidang s " +
                     "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
                     "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                     "WHERE s.sidang_id = ?";
        
        try {
            Sidang sidang = jdbcTemplate.queryForObject(sql, this::mapRowToSidang, id);
            
            // Fetch pembimbing
            if (sidang != null && sidang.getTugasAkhir() != null) {
                List<Dosen> pembimbing = fetchPembimbingForTugasAkhir(sidang.getTugasAkhir().getTaId());
                sidang.getTugasAkhir().setPembimbing(new HashSet<>(pembimbing));
                
                logger.info("Sidang details: {}", sidang);
                logger.info("Tugas Akhir details: {}", sidang.getTugasAkhir());
            }
            
            return Optional.ofNullable(sidang);
        } catch (DataAccessException e) {
            logger.error("Error finding Sidang by ID {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Sidang> findByTugasAkhirId(Integer taId) {
        List<Sidang> results = jdbcTemplate.query(
            "SELECT s.*, ta.judul, ta.topik, ta.status, ta.mahasiswa_id, m.npm, m.nama as mahasiswa_nama " +
            "FROM sidang s " +
            "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
            "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
            "WHERE s.ta_id = ?",
            this::mapRowToSidang,
            taId
        );
        
        Optional<Sidang> sidangOptional = results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        
        // Fetch pembimbing if sidang exists
        sidangOptional.ifPresent(sidang -> {
            if (sidang.getTugasAkhir() != null) {
                List<Dosen> pembimbing = fetchPembimbingForTugasAkhir(sidang.getTugasAkhir().getTaId());
                sidang.getTugasAkhir().setPembimbing(new HashSet<>(pembimbing));
            }
        });
        
        return sidangOptional;
    }

    @Override
    public List<Sidang> findByDosenPengujiId(Integer dosenId) {
        List<Sidang> sidangList = jdbcTemplate.query(
            "SELECT DISTINCT s.*, ta.judul, ta.topik, ta.status, ta.mahasiswa_id, m.npm, m.nama as mahasiswa_nama " +
            "FROM sidang s " +
            "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
            "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
            "JOIN penguji_sidang ps ON s.sidang_id = ps.sidang_id " +
            "WHERE ps.dosen_id = ?",
            this::mapRowToSidang,
            dosenId
        );
        
        // Fetch pembimbing for each Sidang
        for (Sidang sidang : sidangList) {
            if (sidang.getTugasAkhir() != null) {
                List<Dosen> pembimbing = fetchPembimbingForTugasAkhir(sidang.getTugasAkhir().getTaId());
                sidang.getTugasAkhir().setPembimbing(new HashSet<>(pembimbing));
            }
        }
        
        return sidangList;
    }

    @Override
    public Sidang save(Sidang sidang) {
        if (sidang.getSidangId() != null) {
            // Update existing sidang
            jdbcTemplate.update(
                "UPDATE sidang SET ta_id = ?, jadwal = ?, ruangan = ?, " +
                "status_sidang = ?::status_sidang, updated_at = ? " +
                "WHERE sidang_id = ?",
                sidang.getTugasAkhir().getTaId(),
                sidang.getJadwal(),
                sidang.getRuangan(),
                sidang.getStatusSidang().toString().toLowerCase(),
                LocalDateTime.now(),
                sidang.getSidangId()
            );
        } else {
            // Insert new sidang
            jdbcTemplate.update(
                "INSERT INTO sidang (ta_id, jadwal, ruangan, status_sidang, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?::status_sidang, ?, ?)",
                sidang.getTugasAkhir().getTaId(),
                sidang.getJadwal(),
                sidang.getRuangan(),
                sidang.getStatusSidang().toString().toLowerCase(),
                LocalDateTime.now(),
                LocalDateTime.now()
            );
            
            Integer sidangId = jdbcTemplate.queryForObject(
                "SELECT sidang_id FROM sidang WHERE ta_id = ? AND jadwal = ?",
                Integer.class,
                sidang.getTugasAkhir().getTaId(),
                sidang.getJadwal()
            );
            
            sidang.setSidangId(Long.valueOf(sidangId));
        }
        return sidang;
    }

    @Override
    public void deleteById(Integer id) {
        // First delete related records in penguji_sidang
        jdbcTemplate.update("DELETE FROM penguji_sidang WHERE sidang_id = ?", id);
        // Then delete the sidang
        jdbcTemplate.update("DELETE FROM sidang WHERE sidang_id = ?", id);
    }

    private List<Dosen> fetchPembimbingForTugasAkhir(Long taId) {
        String sql = "SELECT d.dosen_id, d.nama, d.nip " +
                     "FROM pembimbing_ta pt " +
                     "JOIN dosen d ON pt.dosen_id = d.dosen_id " +
                     "WHERE pt.ta_id = ?";
        
        List<Dosen> pembimbing = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Dosen dosen = new Dosen();
            dosen.setDosenId(rs.getInt("dosen_id"));
            dosen.setNama(rs.getString("nama"));
            dosen.setNip(rs.getString("nip"));
            return dosen;
        }, taId);

        logger.info("Fetched pembimbing for TA {}: {}", taId, pembimbing);
        return pembimbing;
    }

    private Sidang mapRowToSidang(ResultSet rs, int rowNum) throws SQLException {
        Sidang sidang = new Sidang();
        sidang.setSidangId(rs.getLong("sidang_id"));
        
        TugasAkhir ta = new TugasAkhir();
        ta.setTaId(rs.getLong("ta_id"));
        ta.setJudul(rs.getString("judul"));
        ta.setTopik(rs.getString("topik"));
        ta.setStatus(StatusTA.valueOf(rs.getString("status")));
        
        // Fetch Mahasiswa details
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setNama(rs.getString("mahasiswa_nama"));
        mahasiswa.setNpm(rs.getString("npm"));
        
        ta.setMahasiswa(mahasiswa);
        sidang.setTugasAkhir(ta);
        
        sidang.setJadwal(rs.getTimestamp("jadwal").toLocalDateTime());
        sidang.setRuangan(rs.getString("ruangan"));
        sidang.setStatusSidang(StatusSidang.valueOf(rs.getString("status_sidang")));
        sidang.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        sidang.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        
        return sidang;
    }
}
