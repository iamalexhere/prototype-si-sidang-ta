package com.rpl.project_sista.jdbcrepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.enums.UserRole;
import com.rpl.project_sista.repository.DosenRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// Repository untuk mengelola data dosen menggunakan JDBC.
@Repository
public class JdbcDosenRepository implements DosenRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Mengambil semua data dosen dari database.
    @Override
    public List<Dosen> findAll() {
        return jdbcTemplate.query(
            "SELECT d.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM dosen d JOIN users u ON d.user_id = u.user_id",
            this::mapRowToDosen
        );
    }

    // Mengambil data dosen berdasarkan ID.
    @Override
    public Optional<Dosen> findById(Integer id) {
        List<Dosen> results = jdbcTemplate.query(
            "SELECT d.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM dosen d JOIN users u ON d.user_id = u.user_id WHERE d.dosen_id = ?",
            this::mapRowToDosen,
            id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    // Menyimpan atau memperbarui data dosen.
    @Override
    public Dosen save(Dosen dosen) {
        if (dosen.getDosenId() != null) {
            // Memperbarui data dosen yang sudah ada
            jdbcTemplate.update(
                "UPDATE users SET username = ?, email = ?, password_hash = ?, role = ?::user_role, is_active = ? WHERE user_id = ?",
                dosen.getUsername(),
                dosen.getEmail(),
                dosen.getPasswordHash(),
                dosen.getRole().toString().toLowerCase(),
                dosen.getIsActive(),
                dosen.getDosenId()
            );
            
            jdbcTemplate.update(
                "UPDATE dosen SET nip = ?, nama = ? WHERE user_id = ?",
                dosen.getNip(),
                dosen.getNama(),
                dosen.getDosenId()
            );
        } else {
            // Menyimpan data dosen baru
            jdbcTemplate.update(
                "INSERT INTO users (username, email, password_hash, role, created_at, is_active) VALUES (?, ?, ?, ?::user_role, ?, ?)",
                dosen.getUsername(),
                dosen.getEmail(),
                dosen.getPasswordHash(),
                dosen.getRole().toString().toLowerCase(),
                LocalDateTime.now(),
                true
            );
            
            Integer userId = jdbcTemplate.queryForObject(
                "SELECT user_id FROM users WHERE username = ?",
                Integer.class,
                dosen.getUsername()
            );
            
            jdbcTemplate.update(
                "INSERT INTO dosen (user_id, nip, nama) VALUES (?, ?, ?)",
                userId,
                dosen.getNip(),
                dosen.getNama()
            );
            
            dosen.setDosenId(userId);
        }
        return dosen;
    }

    // Menghapus data dosen berdasarkan ID.
    @Override
    public void deleteById(Integer id) {
        // Hapus dari tabel dosen terlebih dahulu
        jdbcTemplate.update("DELETE FROM dosen WHERE user_id = ?", id);
        // Kemudian hapus dari tabel users
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
    }

    // Mencari dosen berdasarkan nama.
    @Override
    public List<Dosen> findByName(String name) {
        return jdbcTemplate.query(
            "SELECT d.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM dosen d JOIN users u ON d.user_id = u.user_id WHERE d.nama ILIKE ?",
            this::mapRowToDosen,
            "%" + name + "%"
        );
    }

    // Mencari dosen secara terpaginasi.
    @Override
    public List<Dosen> findPaginated(int page, int size, String filter) {
        int offset = (page - 1) * size;
        String query = filter.isEmpty()
            ? "SELECT d.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM dosen d JOIN users u ON d.user_id = u.user_id LIMIT ? OFFSET ?"
            : "SELECT d.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM dosen d JOIN users u ON d.user_id = u.user_id WHERE d.nama ILIKE ? LIMIT ? OFFSET ?";
        
        return jdbcTemplate.query(
            query,
            this::mapRowToDosen,
            filter.isEmpty() ? new Object[]{size, offset} : new Object[]{"%" + filter + "%", size, offset}
        );
    }

    // Menghitung jumlah dosen.
    @Override
    public int count(String filter) {
        String query = filter.isEmpty()
            ? "SELECT COUNT(*) FROM dosen"
            : "SELECT COUNT(*) FROM dosen WHERE nama ILIKE ?";
        
        return jdbcTemplate.queryForObject(
            query,
            Integer.class,
            filter.isEmpty() ? new Object[]{} : new Object[]{"%" + filter + "%"}
        );
    }

    // Mapping data ResultSet ke objek Dosen.
    private Dosen mapRowToDosen(ResultSet rs, int rowNum) throws SQLException {
        Dosen dosen = new Dosen();
        dosen.setDosenId(rs.getInt("dosen_id"));
        dosen.setNip(rs.getString("nip"));
        dosen.setNama(rs.getString("nama"));
        dosen.setUsername(rs.getString("username"));
        dosen.setEmail(rs.getString("email"));
        dosen.setPasswordHash(rs.getString("password_hash"));
        dosen.setRole(UserRole.valueOf(rs.getString("role")));
        dosen.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        dosen.setIsActive(rs.getBoolean("is_active"));
        return dosen;
    }
}
