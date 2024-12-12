package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.*;
import com.rpl.project_sista.repository.BAPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

// Repository untuk mengelola data BAP (Berita Acara Sidang) menggunakan JDBC.
@Repository
public class JdbcBAPRepository implements BAPRepository {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Mengambil semua data BAP dari database.
    @Override
    public List<BAP> findAll() {
        String sql = "SELECT b.*, s.*, pb.user_id, pb.is_approved, pb.approved_at " +
                    "FROM bap b " +
                    "JOIN sidang s ON b.sidang_id = s.sidang_id " +
                    "LEFT JOIN persetujuan_bap pb ON b.bap_id = pb.bap_id";
        
        Map<Long, BAP> bapMap = new HashMap<>();
        
        jdbcTemplate.query(
            sql,
            (RowMapper<BAP>) (rs, rowNum) -> {
                Long bapId = rs.getLong("bap_id");
                BAP bap = bapMap.computeIfAbsent(bapId, k -> {
                    try {
                        return mapRowToBAP(rs, rowNum);
                    } catch (SQLException e) {
                        throw new RuntimeException("Error mapping BAP row", e);
                    }
                });
                
                // Menambahkan persetujuan jika ada
                Integer userId = rs.getInt("user_id");
                if (!rs.wasNull()) {
                    Users user = new Users();
                    user.setUserId(userId);
                    bap.getPersetujuan().add(user);
                }
                
                return bap;
            }
        );
        
        return new ArrayList<>(bapMap.values());
    }

    // Mengambil data BAP berdasarkan ID.
    @Override
    public Optional<BAP> findById(Long id) {
        try {
            String sql = "SELECT b.*, s.* FROM bap b " +
                        "JOIN sidang s ON b.sidang_id = s.sidang_id " +
                        "WHERE b.bap_id = ?";
            
            BAP bap = jdbcTemplate.queryForObject(sql, this::mapRowToBAP, id);
            
            if (bap != null) {
                // Memuat data persetujuan
                String persetujuanSql = "SELECT u.* FROM persetujuan_bap pb " +
                                      "JOIN users u ON pb.user_id = u.user_id " +
                                      "WHERE pb.bap_id = ?";
                List<Users> persetujuan = jdbcTemplate.query(persetujuanSql, this::mapRowToUsers, id);
                bap.setPersetujuan(new HashSet<>(persetujuan));
            }
            
            return Optional.ofNullable(bap);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Mengambil data BAP berdasarkan data Sidang.
    @Override
    public Optional<BAP> findBySidang(Sidang sidang) {
        try {
            String sql = "SELECT b.*, s.* FROM bap b " +
                        "JOIN sidang s ON b.sidang_id = s.sidang_id " +
                        "WHERE b.sidang_id = ?";
            
            BAP bap = jdbcTemplate.queryForObject(sql, this::mapRowToBAP, sidang.getSidangId());
            
            if (bap != null) {
                // Memuat data persetujuan
                String persetujuanSql = "SELECT u.* FROM persetujuan_bap pb " +
                                      "JOIN users u ON pb.user_id = u.user_id " +
                                      "WHERE pb.bap_id = ?";
                List<Users> persetujuan = jdbcTemplate.query(persetujuanSql, this::mapRowToUsers, bap.getBapId());
                bap.setPersetujuan(new HashSet<>(persetujuan));
            }
            
            return Optional.ofNullable(bap);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Menyimpan atau memperbarui data BAP.
    @Override
    @Transactional
    public BAP save(BAP bap) {
        if (bap.getBapId() != null) {
            // Memperbarui data BAP yang sudah ada
            jdbcTemplate.update(
                "UPDATE bap SET sidang_id = ?, catatan_tambahan = ? WHERE bap_id = ?",
                bap.getSidang().getSidangId(),
                bap.getCatatanTambahan(),
                bap.getBapId()
            );
        } else {
            // Menyimpan data BAP baru
            jdbcTemplate.update(
                "INSERT INTO bap (sidang_id, catatan_tambahan, created_at) VALUES (?, ?, ?)",
                bap.getSidang().getSidangId(),
                bap.getCatatanTambahan(),
                LocalDateTime.now()
            );
            
            Long generatedId = jdbcTemplate.queryForObject(
                "SELECT currval('bap_bap_id_seq')",
                Long.class
            );
            bap.setBapId(generatedId);
        }
        
        // Memperbarui data persetujuan jika ada
        if (bap.getPersetujuan() != null && !bap.getPersetujuan().isEmpty()) {
            // Hapus data persetujuan yang sudah ada
            jdbcTemplate.update("DELETE FROM persetujuan_bap WHERE bap_id = ?", bap.getBapId());
            
            // Simpan data persetujuan baru
            for (Users user : bap.getPersetujuan()) {
                jdbcTemplate.update(
                    "INSERT INTO persetujuan_bap (bap_id, user_id, is_approved, approved_at) VALUES (?, ?, ?, ?)",
                    bap.getBapId(),
                    user.getUserId(),
                    false,
                    null
                );
            }
        }
        
        return bap;
    }

    // Menghapus data BAP berdasarkan ID.
    @Override
    @Transactional
    public void deleteById(Long id) {
        // Hapus data persetujuan terlebih dahulu
        jdbcTemplate.update("DELETE FROM persetujuan_bap WHERE bap_id = ?", id);
        // Kemudian hapus data BAP
        jdbcTemplate.update("DELETE FROM bap WHERE bap_id = ?", id);
    }

    // Mencari pengguna yang perlu melakukan persetujuan.
    @Override
    public Set<Users> findRequiredApprovers(BAP bap) {
        // Mendapatkan semua pengguna yang perlu menyetujui: koordinator, penguji, pembimbing, dan mahasiswa
        String sql = "SELECT DISTINCT u.* FROM users u " +
                    "WHERE u.user_id IN ( " +
                    "    SELECT user_id FROM users WHERE role = 'admin' " + // Koordinator
                    "    UNION " +
                    "    SELECT d.user_id FROM penguji_sidang ps " +
                    "    JOIN dosen d ON ps.dosen_id = d.dosen_id " +
                    "    WHERE ps.sidang_id = ? " + // Penguji
                    "    UNION " +
                    "    SELECT d.user_id FROM pembimbing_ta pt " +
                    "    JOIN dosen d ON pt.dosen_id = d.dosen_id " +
                    "    JOIN sidang s ON s.ta_id = pt.ta_id " +
                    "    WHERE s.sidang_id = ? " + // Pembimbing
                    "    UNION " +
                    "    SELECT m.user_id FROM mahasiswa m " +
                    "    JOIN tugas_akhir ta ON ta.mahasiswa_id = m.mahasiswa_id " +
                    "    JOIN sidang s ON s.ta_id = ta.ta_id " +
                    "    WHERE s.sidang_id = ? " + // Mahasiswa
                    ")";
        
        List<Users> users = jdbcTemplate.query(sql, this::mapRowToUsers,
            bap.getSidang().getSidangId(),
            bap.getSidang().getSidangId(),
            bap.getSidang().getSidangId()
        );
        
        return new HashSet<>(users);
    }

    // Mencari pengguna yang sudah melakukan persetujuan.
    @Override
    public Set<Users> findApprovers(BAP bap) {
        String sql = "SELECT u.* FROM users u " +
                    "JOIN persetujuan_bap pb ON u.user_id = pb.user_id " +
                    "WHERE pb.bap_id = ? AND pb.is_approved = true";
        
        List<Users> approvers = jdbcTemplate.query(sql, this::mapRowToUsers, bap.getBapId());
        return new HashSet<>(approvers);
    }

    // Mengecek apakah semua persetujuan sudah lengkap.
    @Override
    public boolean isFullyApproved(BAP bap) {
        Set<Users> requiredApprovers = findRequiredApprovers(bap);
        Set<Users> actualApprovers = findApprovers(bap);
        return requiredApprovers.size() == actualApprovers.size();
    }

    // Menambahkan atau memperbarui persetujuan pengguna.
    @Override
    @Transactional
    public void addApprover(BAP bap, Users user) {
        // Periksa apakah persetujuan sudah ada
        String checkSql = "SELECT COUNT(*) FROM persetujuan_bap WHERE bap_id = ? AND user_id = ?";
        int count = jdbcTemplate.queryForObject(checkSql, Integer.class, bap.getBapId(), user.getUserId());
        
        if (count == 0) {
            // Tambahkan persetujuan baru
            jdbcTemplate.update(
                "INSERT INTO persetujuan_bap (bap_id, user_id, is_approved, approved_at) VALUES (?, ?, true, ?)",
                bap.getBapId(),
                user.getUserId(),
                LocalDateTime.now()
            );
        } else {
            // Perbarui persetujuan yang sudah ada
            jdbcTemplate.update(
                "UPDATE persetujuan_bap SET is_approved = true, approved_at = ? WHERE bap_id = ? AND user_id = ?",
                LocalDateTime.now(),
                bap.getBapId(),
                user.getUserId()
            );
        }
    }

    // Menghapus persetujuan pengguna.
    @Override
    @Transactional
    public void removeApprover(BAP bap, Users user) {
        jdbcTemplate.update(
            "UPDATE persetujuan_bap SET is_approved = false, approved_at = null WHERE bap_id = ? AND user_id = ?",
            bap.getBapId(),
            user.getUserId()
        );
    }

    // Mapping data ResultSet ke objek BAP.
    private BAP mapRowToBAP(ResultSet rs, int rowNum) throws SQLException {
        BAP bap = new BAP();
        bap.setBapId(rs.getLong("bap_id"));
        bap.setCatatanTambahan(rs.getString("catatan_tambahan"));
        bap.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        
        // Mapping data Sidang
        Sidang sidang = new Sidang();
        sidang.setSidangId(rs.getLong("sidang_id"));
        bap.setSidang(sidang);
        
        bap.setPersetujuan(new HashSet<>());
        
        return bap;
    }

    // Mapping data ResultSet ke objek Users.
    private Users mapRowToUsers(ResultSet rs, int rowNum) throws SQLException {
        Users user = new Users();
        user.setUserId(rs.getInt("user_id")); 
        return user;
    }
}
