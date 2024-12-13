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
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.enums.JenisTA;
import com.rpl.project_sista.model.enums.Periode;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.repository.TugasAkhirRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

// Repository untuk mengelola data Tugas Akhir menggunakan JDBC.
@Repository
public class JdbcTugasAkhirRepository implements TugasAkhirRepository {
    private static final Logger logger = LoggerFactory.getLogger(JdbcTugasAkhirRepository.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Mengambil semua data Tugas Akhir dari database.
    @Override
    public List<TugasAkhir> findAll() {
        String sql = "SELECT DISTINCT ta.*, m.npm, m.nama as mahasiswa_nama, " +
                    "s.tahun_ajaran, s.periode, " +
                    "d.dosen_id, d.nip as dosen_nip, d.nama as dosen_nama " +
                    "FROM tugas_akhir ta " +
                    "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                    "JOIN semester s ON ta.semester_id = s.semester_id " +
                    "LEFT JOIN pembimbing_ta pt ON ta.ta_id = pt.ta_id " +
                    "LEFT JOIN dosen d ON pt.dosen_id = d.dosen_id " +
                    "ORDER BY ta.ta_id";
        
        logger.info("Executing findAll query: {}", sql);
        
        List<TugasAkhir> tugasAkhirList = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Long currentTaId = rs.getLong("ta_id");
            TugasAkhir ta = mapBasicTugasAkhir(rs);
            ta.setPembimbing(findPembimbingByTaId(currentTaId));
            return ta;
        });

        return tugasAkhirList;
    }

    // Mengambil data Tugas Akhir berdasarkan ID.
    @Override
    public Optional<TugasAkhir> findById(Integer id) {
        String sql = "SELECT ta.*, m.npm, m.nama as mahasiswa_nama, " +
                    "s.tahun_ajaran, s.periode " +
                    "FROM tugas_akhir ta " +
                    "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                    "JOIN semester s ON ta.semester_id = s.semester_id " +
                    "WHERE ta.ta_id = ?";
        
        try {
            TugasAkhir tugasAkhir = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                TugasAkhir ta = mapBasicTugasAkhir(rs);
                ta.setPembimbing(findPembimbingByTaId(ta.getTaId()));
                return ta;
            }, id);
            return Optional.ofNullable(tugasAkhir);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    // Mengambil data Tugas Akhir berdasarkan ID mahasiswa.
    @Override
    public List<TugasAkhir> findByMahasiswaId(Integer mahasiswaId) {
        String sql = "SELECT ta.*, m.npm, m.nama as mahasiswa_nama, " +
                    "s.tahun_ajaran, s.periode " +
                    "FROM tugas_akhir ta " +
                    "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                    "JOIN semester s ON ta.semester_id = s.semester_id " +
                    "WHERE ta.mahasiswa_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TugasAkhir ta = mapBasicTugasAkhir(rs);
            ta.setPembimbing(findPembimbingByTaId(ta.getTaId()));
            return ta;
        }, mahasiswaId);
    }

    // Memvalidasi transisi status Tugas Akhir.
    private boolean isValidStatusTransition(StatusTA currentStatus, StatusTA newStatus) {
        if (currentStatus == null && newStatus == StatusTA.draft) {
            return true; // Tugas Akhir baru dapat dibuat sebagai draft
        }

        // Mendefinisikan transisi yang valid
        switch (currentStatus) {
            case draft:
                return newStatus == StatusTA.diajukan;
            case diajukan:
                return newStatus == StatusTA.diterima || newStatus == StatusTA.ditolak;
            case ditolak:
                return newStatus == StatusTA.draft; // Dapat dikirim ulang setelah penolakan
            case diterima:
                return newStatus == StatusTA.dalam_pengerjaan;
            case dalam_pengerjaan:
                return newStatus == StatusTA.selesai;
            case selesai:
                return false; // Status final, tidak ada transisi selanjutnya
            default:
                return false;
        }
    }

    // Menyimpan atau memperbarui data Tugas Akhir.
    @Override
    public TugasAkhir save(TugasAkhir tugasAkhir) {
        if (tugasAkhir.getTaId() != null) {
            // Untuk Tugas Akhir yang sudah ada, validasi transisi status
            Optional<TugasAkhir> existingTA = findById(tugasAkhir.getTaId().intValue());
            if (existingTA.isPresent()) {
                StatusTA currentStatus = existingTA.get().getStatus();
                if (!isValidStatusTransition(currentStatus, tugasAkhir.getStatus())) {
                    throw new IllegalStateException(
                        String.format("Invalid status transition from %s to %s", 
                        currentStatus, tugasAkhir.getStatus())
                    );
                }
            }

            // Memperbarui Tugas Akhir yang sudah ada
            jdbcTemplate.update(
                "UPDATE tugas_akhir SET mahasiswa_id = ?, semester_id = ?, judul = ?, " +
                "topik = ?, jenis_ta = ?::jenis_ta, status = ?::status_ta, " +
                "updated_at = ? " +
                "WHERE ta_id = ?",
                tugasAkhir.getMahasiswa().getMahasiswaId(),
                tugasAkhir.getSemester().getSemesterId(),
                tugasAkhir.getJudul(),
                tugasAkhir.getTopik(),
                tugasAkhir.getJenisTA().toString().toLowerCase(),
                tugasAkhir.getStatus().toString().toLowerCase(),
                LocalDateTime.now(),
                tugasAkhir.getTaId()
            );

            // Memperbarui data pembimbing
            updatePembimbing(tugasAkhir);
        } else {
            // Untuk Tugas Akhir baru, hanya status draft yang diizinkan
            if (tugasAkhir.getStatus() != StatusTA.draft) {
                throw new IllegalStateException("New Tugas Akhir must be created with draft status");
            }

            // Menyimpan Tugas Akhir baru
            tugasAkhir.setCreatedAt(LocalDateTime.now());
            jdbcTemplate.update(
                "INSERT INTO tugas_akhir (mahasiswa_id, semester_id, judul, topik, " +
                "jenis_ta, status, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?::jenis_ta, ?::status_ta, ?, ?)",
                tugasAkhir.getMahasiswa().getMahasiswaId(),
                tugasAkhir.getSemester().getSemesterId(),
                tugasAkhir.getJudul(),
                tugasAkhir.getTopik(),
                tugasAkhir.getJenisTA().toString().toLowerCase(),
                tugasAkhir.getStatus().toString().toLowerCase(),
                tugasAkhir.getCreatedAt(),
                tugasAkhir.getCreatedAt()
            );
            
            Integer taId = jdbcTemplate.queryForObject(
                "SELECT ta_id FROM tugas_akhir WHERE mahasiswa_id = ? AND judul = ? AND created_at = ?",
                Integer.class,
                tugasAkhir.getMahasiswa().getMahasiswaId(),
                tugasAkhir.getJudul(),
                tugasAkhir.getCreatedAt()
            );
            
            tugasAkhir.setTaId(taId != null ? Long.valueOf(taId) : null);

            // Menyimpan data pembimbing untuk Tugas Akhir baru
            if (tugasAkhir.getTaId() != null) {
                updatePembimbing(tugasAkhir);
            }
        }
        return tugasAkhir;
    }

    // Memperbarui data pembimbing Tugas Akhir.
    private void updatePembimbing(TugasAkhir tugasAkhir) {
        // Hapus data pembimbing yang sudah ada
        jdbcTemplate.update("DELETE FROM pembimbing_ta WHERE ta_id = ?", tugasAkhir.getTaId());

        // Simpan data pembimbing baru
        if (tugasAkhir.getPembimbing() != null && !tugasAkhir.getPembimbing().isEmpty()) {
            for (Dosen pembimbing : tugasAkhir.getPembimbing()) {
                jdbcTemplate.update(
                    "INSERT INTO pembimbing_ta (ta_id, dosen_id, assigned_at) VALUES (?, ?, ?)",
                    tugasAkhir.getTaId(),
                    pembimbing.getDosenId(),
                    LocalDateTime.now()
                );
            }
        }
    }

    // Mengambil data pembimbing berdasarkan ID Tugas Akhir.
    private Set<Dosen> findPembimbingByTaId(Long taId) {
        String sql = "SELECT d.* FROM dosen d " +
                    "JOIN pembimbing_ta pt ON d.dosen_id = pt.dosen_id " +
                    "WHERE pt.ta_id = ?";
        
        List<Dosen> pembimbingList = jdbcTemplate.query(sql, (rs, rowNum) -> {
            Dosen dosen = new Dosen();
            dosen.setDosenId(rs.getInt("dosen_id"));
            dosen.setNip(rs.getString("nip"));
            dosen.setNama(rs.getString("nama"));
            return dosen;
        }, taId);

        return new HashSet<>(pembimbingList);
    }

    // Menghapus data Tugas Akhir berdasarkan ID.
    @Override
    public void deleteById(Integer id) {
        try {
            // Hapus relasi di tabel pembimbing_ta terlebih dahulu
            jdbcTemplate.update("DELETE FROM pembimbing_ta WHERE ta_id = ?", id);
            
            // Kemudian hapus data Tugas Akhir
            int deletedRows = jdbcTemplate.update("DELETE FROM tugas_akhir WHERE ta_id = ?", id);
            
            if (deletedRows == 0) {
                logger.warn("No tugas akhir found with id: {}", id);
                throw new DataAccessException("No tugas akhir found with id: " + id) {};
            }
            
            logger.info("Successfully deleted tugas akhir with id: {}", id);
        } catch (DataAccessException e) {
            logger.error("Error deleting tugas akhir with id: {}", id, e);
            throw e;
        }
    }

    // Memperbarui status Tugas Akhir.
    @Override
    public boolean updateStatus(Long taId, StatusTA newStatus) {
        try {
            int updatedRows = jdbcTemplate.update(
                "UPDATE tugas_akhir SET status = ?::status_ta WHERE ta_id = ?",
                newStatus.toString().toLowerCase(),
                taId
            );
            
            if (updatedRows == 0) {
                logger.warn("No tugas akhir found with id: {} for status update", taId);
                return false;
            }
            
            logger.info("Successfully updated status to {} for tugas akhir with id: {}", 
                       newStatus, taId);
            return true;
        } catch (DataAccessException e) {
            logger.error("Error updating status for tugas akhir with id: {}", taId, e);
            throw e;
        }
    }

    // Mengambil data Tugas Akhir berdasarkan status.
    @Override
    public List<TugasAkhir> findByStatus(StatusTA status) {
        String sql = "SELECT ta.*, m.npm, m.nama as mahasiswa_nama, " +
                    "s.tahun_ajaran, s.periode " +
                    "FROM tugas_akhir ta " +
                    "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                    "JOIN semester s ON ta.semester_id = s.semester_id " +
                    "WHERE ta.status = ?::status_ta " +
                    "ORDER BY ta.created_at DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TugasAkhir ta = mapBasicTugasAkhir(rs);
            ta.setPembimbing(findPembimbingByTaId(ta.getTaId()));
            return ta;
        }, status.toString().toLowerCase());
    }

    // Mengambil data Tugas Akhir berdasarkan ID mahasiswa dan status.
    @Override
    public List<TugasAkhir> findByMahasiswaIdAndStatus(Integer mahasiswaId, StatusTA status) {
        String sql = "SELECT ta.*, m.npm, m.nama as mahasiswa_nama, " +
                    "s.tahun_ajaran, s.periode " +
                    "FROM tugas_akhir ta " +
                    "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                    "JOIN semester s ON ta.semester_id = s.semester_id " +
                    "WHERE ta.mahasiswa_id = ? AND ta.status = ?::status_ta " +
                    "ORDER BY ta.created_at DESC";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TugasAkhir ta = mapBasicTugasAkhir(rs);
            ta.setPembimbing(findPembimbingByTaId(ta.getTaId()));
            return ta;
        }, mahasiswaId, status.toString().toLowerCase());
    }

    // Mapping data ResultSet ke objek TugasAkhir (data dasar).
    private TugasAkhir mapBasicTugasAkhir(ResultSet rs) throws SQLException {
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
        semester.setPeriode(Periode.valueOf(rs.getString("periode").toLowerCase()));
        ta.setSemester(semester);
        
        ta.setJudul(rs.getString("judul"));
        ta.setTopik(rs.getString("topik"));
        ta.setJenisTA(JenisTA.valueOf(rs.getString("jenis_ta")));
        ta.setStatus(StatusTA.valueOf(rs.getString("status").toLowerCase()));
        ta.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        return ta;
    }
}
