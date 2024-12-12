package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.enums.StatusSidang;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.repository.SidangRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

// Repository untuk mengelola data sidang menggunakan JDBC.
@Repository
public class JdbcSidangRepository implements SidangRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(JdbcSidangRepository.class);

    // Mengambil semua data sidang dari database.
    @Override
    public List<Sidang> findAll() {
        String sql = "SELECT s.*, ta.*, m.npm, m.nama as mahasiswa_nama " +
                    "FROM sidang s " +
                    "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
                    "JOIN mahasiswa m ON ta.npm = m.npm " +
                    "ORDER BY s.jadwal";
        return jdbcTemplate.query(sql, this::mapRowToSidang);
    }

    // Mengambil data sidang berdasarkan ID.
    @Override
    public Optional<Sidang> findById(Integer id) {
        try {
            String sql = "SELECT s.*, ta.*, m.npm, m.nama as mahasiswa_nama " +
                        "FROM sidang s " +
                        "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
                        "JOIN mahasiswa m ON ta.npm = m.npm " +
                        "WHERE s.sidang_id = ?";
            
            Sidang sidang = jdbcTemplate.queryForObject(sql, this::mapRowToSidang, id);
            return Optional.ofNullable(sidang);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Error finding Sidang by ID {}: {}", id, e.getMessage());
            return Optional.empty();
        }
    }

    // Mengambil data sidang berdasarkan ID Tugas Akhir.
    @Override
    public Optional<Sidang> findByTugasAkhirId(Integer taId) {
        try {
            String sql = "SELECT s.*, ta.*, m.npm, m.nama as mahasiswa_nama " +
                        "FROM sidang s " +
                        "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
                        "JOIN mahasiswa m ON ta.npm = m.npm " +
                        "WHERE s.ta_id = ?";
            
            Sidang sidang = jdbcTemplate.queryForObject(sql, this::mapRowToSidang, taId);
            return Optional.ofNullable(sidang);
        } catch (DataAccessException e) {
            logger.error("Error finding Sidang by TA ID {}: {}", taId, e.getMessage());
            return Optional.empty();
        }
    }

    // Mengambil data sidang berdasarkan ID dosen penguji.
    @Override
    public List<Sidang> findByDosenPengujiId(Integer dosenId) {
        String sql = "SELECT DISTINCT s.*, ta.*, m.npm, m.nama as mahasiswa_nama " +
                    "FROM sidang s " +
                    "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
                    "JOIN mahasiswa m ON ta.npm = m.npm " +
                    "JOIN penguji_sidang ps ON s.sidang_id = ps.sidang_id " +
                    "WHERE ps.dosen_id = ? " +
                    "ORDER BY s.jadwal";
        
        return jdbcTemplate.query(sql, this::mapRowToSidang, dosenId);
    }

    // Menyimpan atau memperbarui data sidang.
    @Override
    public Sidang save(Sidang sidang) {
        if (sidang.getSidangId() != null) {
            // Memperbarui data sidang yang sudah ada
            String sql = "UPDATE sidang SET " +
                        "ta_id = ?, jadwal = ?, ruangan = ?, status_sidang = ?::status_sidang, " +
                        "updated_at = ? " +
                        "WHERE sidang_id = ?";
            
            jdbcTemplate.update(sql,
                sidang.getTugasAkhir().getTaId(),
                sidang.getJadwal(),
                sidang.getRuangan(),
                sidang.getStatusSidang().name().toLowerCase(),
                LocalDateTime.now(),
                sidang.getSidangId()
            );
        } else {
            // Menyimpan data sidang baru
            String sql = "INSERT INTO sidang " +
                        "(ta_id, jadwal, ruangan, status_sidang, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?::status_sidang, ?, ?) RETURNING sidang_id";
            
            Long id = jdbcTemplate.queryForObject(sql, Long.class,
                sidang.getTugasAkhir().getTaId(),
                sidang.getJadwal(),
                sidang.getRuangan(),
                sidang.getStatusSidang().name().toLowerCase(),
                LocalDateTime.now(),
                LocalDateTime.now()
            );
            sidang.setSidangId(id);
        }
        return sidang;
    }

    // Menghapus data sidang berdasarkan ID.
    @Override
    public void deleteById(Integer id) {
        String sql = "DELETE FROM sidang WHERE sidang_id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Mengecek ketersediaan jadwal sidang.
    @Override
    public boolean isJadwalAvailable(LocalDateTime waktuMulai, LocalDateTime waktuSelesai, String ruangan) {
        String sql = "SELECT COUNT(*) FROM sidang " +
                    "WHERE ruangan = ? AND status_sidang != 'dibatalkan' AND " +
                    "((waktu_mulai <= ? AND waktu_selesai >= ?) OR " +
                    "(waktu_mulai <= ? AND waktu_selesai >= ?) OR " +
                    "(waktu_mulai >= ? AND waktu_selesai <= ?))";
        
        Integer overlappingCount = jdbcTemplate.queryForObject(sql, Integer.class,
            ruangan, 
            waktuMulai, waktuMulai,
            waktuSelesai, waktuSelesai,
            waktuMulai, waktuSelesai
        );
        
        return overlappingCount != null && overlappingCount == 0;
    }

    // Mengecek ketersediaan dosen pada jadwal sidang.
    @Override
    public boolean isDosenAvailable(Dosen dosen, LocalDateTime waktuMulai, LocalDateTime waktuSelesai) {
        if (dosen == null || dosen.getDosenId() == null) {
            return false;
        }

        String sql = "SELECT COUNT(*) FROM sidang s " +
                    "JOIN penguji_sidang ps ON s.sidang_id = ps.sidang_id " +
                    "WHERE ps.dosen_id = ? AND s.status_sidang != 'dibatalkan' AND " +
                    "((s.waktu_mulai <= ? AND s.waktu_selesai >= ?) OR " +
                    "(s.waktu_mulai <= ? AND s.waktu_selesai >= ?) OR " +
                    "(s.waktu_mulai >= ? AND s.waktu_selesai <= ?))";
        
        Integer overlappingCount = jdbcTemplate.queryForObject(sql, Integer.class,
            dosen.getDosenId(),
            waktuMulai, waktuMulai,
            waktuSelesai, waktuSelesai,
            waktuMulai, waktuSelesai
        );
        
        return overlappingCount != null && overlappingCount == 0;
    }

    // Mencari data sidang yang bentrok jadwalnya.
    @Override
    public List<Sidang> findOverlappingSidang(LocalDateTime waktuMulai, LocalDateTime waktuSelesai, String ruangan) {
        String sql = "SELECT s.*, ta.*, m.npm, m.nama as mahasiswa_nama " +
                    "FROM sidang s " +
                    "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
                    "JOIN mahasiswa m ON ta.npm = m.npm " +
                    "WHERE s.ruangan = ? AND s.status_sidang != 'dibatalkan' AND " +
                    "((s.waktu_mulai <= ? AND s.waktu_selesai >= ?) OR " +
                    "(s.waktu_mulai <= ? AND s.waktu_selesai >= ?) OR " +
                    "(s.waktu_mulai >= ? AND s.waktu_selesai <= ?)) " +
                    "ORDER BY s.jadwal";
        
        return jdbcTemplate.query(sql, this::mapRowToSidang,
            ruangan,
            waktuMulai, waktuMulai,
            waktuSelesai, waktuSelesai,
            waktuMulai, waktuSelesai
        );
    }

    // Mencari data sidang yang bentrok jadwalnya dengan dosen tertentu.
    @Override
    public List<Sidang> findDosenOverlappingSidang(Dosen dosen, LocalDateTime waktuMulai, LocalDateTime waktuSelesai) {
        if (dosen == null || dosen.getDosenId() == null) {
            return List.of();
        }

        String sql = "SELECT DISTINCT s.*, ta.*, m.npm, m.nama as mahasiswa_nama " +
                    "FROM sidang s " +
                    "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
                    "JOIN mahasiswa m ON ta.npm = m.npm " +
                    "JOIN penguji_sidang ps ON s.sidang_id = ps.sidang_id " +
                    "WHERE ps.dosen_id = ? AND s.status_sidang != 'dibatalkan' AND " +
                    "((s.waktu_mulai <= ? AND s.waktu_selesai >= ?) OR " +
                    "(s.waktu_mulai <= ? AND s.waktu_selesai >= ?) OR " +
                    "(s.waktu_mulai >= ? AND s.waktu_selesai <= ?)) " +
                    "ORDER BY s.jadwal";
        
        return jdbcTemplate.query(sql, this::mapRowToSidang,
            dosen.getDosenId(),
            waktuMulai, waktuMulai,
            waktuSelesai, waktuSelesai,
            waktuMulai, waktuSelesai
        );
    }

    // Memperbarui status sidang.
    @Override
    public boolean updateStatus(Long sidangId, StatusSidang newStatus) {
        // Validasi transisi status
        Optional<Sidang> existingSidang = findById(sidangId.intValue());
        if (existingSidang.isEmpty()) {
            return false;
        }

        StatusSidang currentStatus = existingSidang.get().getStatusSidang();
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new IllegalStateException(
                String.format("Invalid status transition from %s to %s", 
                currentStatus, newStatus)
            );
        }

        String sql = "UPDATE sidang SET status_sidang = ?::status_sidang, updated_at = ? WHERE sidang_id = ?";
        int updatedRows = jdbcTemplate.update(sql, 
            newStatus.name().toLowerCase(), 
            LocalDateTime.now(), 
            sidangId
        );
        return updatedRows > 0;
    }

    // Mengambil data sidang berdasarkan status.
    @Override
    public List<Sidang> findByStatus(StatusSidang status) {
        String sql = "SELECT s.*, ta.*, m.npm, m.nama as mahasiswa_nama " +
                    "FROM sidang s " +
                    "JOIN tugas_akhir ta ON s.ta_id = ta.ta_id " +
                    "JOIN mahasiswa m ON ta.npm = m.npm " +
                    "WHERE s.status_sidang = ?::status_sidang " +
                    "ORDER BY s.jadwal";
        
        return jdbcTemplate.query(sql, this::mapRowToSidang, 
            status.name().toLowerCase()
        );
    }

    // Memvalidasi transisi status sidang.
    private boolean isValidStatusTransition(StatusSidang currentStatus, StatusSidang newStatus) {
        if (currentStatus == null) {
            return newStatus == StatusSidang.terjadwal;
        }

        switch (currentStatus) {
            case terjadwal:
                return newStatus == StatusSidang.berlangsung || newStatus == StatusSidang.dibatalkan;
            case berlangsung:
                return newStatus == StatusSidang.selesai;
            case selesai:
                return false; // Status final
            case dibatalkan:
                return newStatus == StatusSidang.terjadwal; // Dapat dijadwal ulang
            default:
                return false;
        }
    }

    // Mengambil data dosen penguji untuk sidang tertentu.
    private List<Dosen> fetchPengujiForSidang(Long sidangId) {
        String sql = "SELECT d.* FROM dosen d " +
                    "JOIN penguji_sidang ps ON d.dosen_id = ps.dosen_id " +
                    "WHERE ps.sidang_id = ?";
        
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Dosen dosen = new Dosen();
            dosen.setDosenId(rs.getInt("dosen_id"));
            dosen.setNama(rs.getString("nama"));
            dosen.setEmail(rs.getString("email"));
            return dosen;
        }, sidangId);
    }

    // Mapping data ResultSet ke objek Sidang.
    private Sidang mapRowToSidang(ResultSet rs, int rowNum) throws SQLException {
        Sidang sidang = new Sidang();
        sidang.setSidangId(rs.getLong("sidang_id"));
        
        // Mapping data TugasAkhir
        TugasAkhir ta = new TugasAkhir();
        ta.setTaId(rs.getLong("ta_id"));
        ta.setJudul(rs.getString("judul"));
        
        // Mapping data Mahasiswa
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setNpm(rs.getString("npm"));
        mahasiswa.setNama(rs.getString("mahasiswa_nama"));
        ta.setMahasiswa(mahasiswa);
        
        sidang.setTugasAkhir(ta);
        
        // Mapping field lainnya
        sidang.setJadwal(rs.getTimestamp("jadwal").toLocalDateTime());
        sidang.setRuangan(rs.getString("ruangan"));
        sidang.setStatusSidang(StatusSidang.valueOf(rs.getString("status_sidang").toUpperCase()));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            sidang.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            sidang.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        // Mengambil dan menetapkan penguji
        List<Dosen> penguji = fetchPengujiForSidang(sidang.getSidangId());
        sidang.setPenguji(new HashSet<>(penguji));
        
        return sidang;
    }
}
