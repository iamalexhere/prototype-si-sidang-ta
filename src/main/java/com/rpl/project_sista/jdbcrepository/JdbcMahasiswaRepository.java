// src/main/java/com/rpl/project_sista/jdbcrepository/JdbcMahasiswaRepository.java
package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.model.enums.UserRole;
import com.rpl.project_sista.repository.MahasiswaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.Statement;

// Repository untuk mengelola data mahasiswa menggunakan JDBC.
@Repository
public class JdbcMahasiswaRepository implements MahasiswaRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Mengambil semua data mahasiswa dari database.
    @Override
    public List<Mahasiswa> findAll() {
        return jdbcTemplate.query(
            "SELECT m.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM mahasiswa m JOIN users u ON m.user_id = u.user_id",
            this::mapRowToMahasiswa
        );
    }

    // Mengambil data mahasiswa berdasarkan ID.
    @Override
    public Optional<Mahasiswa> findById(Integer id) {
        List<Mahasiswa> results = jdbcTemplate.query(
            "SELECT m.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active " +
            "FROM mahasiswa m " +
            "JOIN users u ON m.user_id = u.user_id " +
            "WHERE m.mahasiswa_id = ?",
            this::mapRowToMahasiswa,
            id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    // Menyimpan atau memperbarui data mahasiswa.
     @Override
    public Mahasiswa save(Mahasiswa mahasiswa) {
         if (mahasiswa.getMahasiswaId() == null) {
             // Menyimpan data mahasiswa baru
            return insert(mahasiswa);
         } else {
             // Memperbarui data mahasiswa yang sudah ada
             update(mahasiswa);
              return mahasiswa;
          }
    }

    private Mahasiswa insert(Mahasiswa mahasiswa) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
         jdbcTemplate.update(connection -> {
             PreparedStatement ps = connection.prepareStatement(
                  "INSERT INTO users (username, email, password_hash, role, created_at, is_active) VALUES (?, ?, ?, ?::user_role, ?, ?)",
                  Statement.RETURN_GENERATED_KEYS
             );
             ps.setString(1, mahasiswa.getUsername());
             ps.setString(2, mahasiswa.getEmail());
             ps.setString(3, mahasiswa.getPasswordHash());
             ps.setString(4, mahasiswa.getRole().toString());
             ps.setObject(5, LocalDateTime.now());
             ps.setBoolean(6, true);
             return ps;
         }, keyHolder);
        
         Integer userId = keyHolder.getKey().intValue();

         jdbcTemplate.update(
             "INSERT INTO mahasiswa (user_id, npm, nama, status_ta) VALUES (?, ?, ?, ?::status_ta)",
              userId,
              mahasiswa.getNpm(),
              mahasiswa.getNama(),
              mahasiswa.getStatusTa().toString()
        );

      
         mahasiswa.setMahasiswaId(userId);
         return mahasiswa;

    }

     private void update(Mahasiswa mahasiswa) {
       jdbcTemplate.update(
                 "UPDATE users SET username = ?, email = ?, password_hash = ?, role = ?::user_role, is_active = ? WHERE user_id = ?",
                 mahasiswa.getUsername(),
                 mahasiswa.getEmail(),
                 mahasiswa.getPasswordHash(),
                 mahasiswa.getRole().toString().toLowerCase(),
                 mahasiswa.getIsActive(),
                 mahasiswa.getMahasiswaId()
         );
             
         jdbcTemplate.update(
                 "UPDATE mahasiswa SET npm = ?, nama = ?, status_ta = ?::status_ta WHERE user_id = ?",
                 mahasiswa.getNpm(),
                 mahasiswa.getNama(),
                 mahasiswa.getStatusTa().toString(),
                 mahasiswa.getMahasiswaId()
        );
     }

    // Mencari mahasiswa berdasarkan nama.
    @Override
    public List<Mahasiswa> findByName(String name) {
        return jdbcTemplate.query(
            "SELECT m.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM mahasiswa m JOIN users u ON m.user_id = u.user_id WHERE m.nama ILIKE ?", 
            this::mapRowToMahasiswa, 
            "%" + name + "%"
        );
    }

    // Mencari mahasiswa secara terpaginasi.
    @Override
    public List<Mahasiswa> findPaginated(int page, int size, String filter) {
        int offset = (page - 1) * size;
        String query = filter.isEmpty() 
            ? "SELECT m.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM mahasiswa m JOIN users u ON m.user_id = u.user_id LIMIT ? OFFSET ?" 
            : "SELECT m.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM mahasiswa m JOIN users u ON m.user_id = u.user_id WHERE m.nama ILIKE ? LIMIT ? OFFSET ?";
        
        return jdbcTemplate.query(
            query, 
            this::mapRowToMahasiswa, 
            filter.isEmpty() ? new Object[]{size, offset} : new Object[]{"%" + filter + "%", size, offset}
        );
    }

    // Menghitung jumlah mahasiswa.
    @Override
    public int count(String filter) {
        String query = filter.isEmpty() 
            ? "SELECT COUNT(*) FROM mahasiswa" 
            : "SELECT COUNT(*) FROM mahasiswa WHERE nama ILIKE ?";
        
        return jdbcTemplate.queryForObject(
            query, 
            Integer.class, 
            filter.isEmpty() ? new Object[]{} : new Object[]{"%" + filter + "%"}
        );
    }

    // Menghapus data mahasiswa berdasarkan ID.
    @Override
    public void deleteById(Integer id) {
        // Hapus dari tabel mahasiswa terlebih dahulu
        jdbcTemplate.update("DELETE FROM mahasiswa WHERE user_id = ?", id);
        // Kemudian hapus dari tabel users
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
    }

    // Mengambil data mahasiswa berdasarkan user ID.
    @Override
    public Optional<Mahasiswa> findByUserId(Integer userId) {
        List<Mahasiswa> results = jdbcTemplate.query(
            "SELECT m.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active " +
            "FROM mahasiswa m " +
            "JOIN users u ON m.user_id = u.user_id " +
            "WHERE u.user_id = ?",
            this::mapRowToMahasiswa,
            userId
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    // Mapping data ResultSet ke objek Mahasiswa.
    private Mahasiswa mapRowToMahasiswa(ResultSet rs, int rowNum) throws SQLException {
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setMahasiswaId(rs.getInt("mahasiswa_id"));
        mahasiswa.setNpm(rs.getString("npm"));
        mahasiswa.setNama(rs.getString("nama"));
        mahasiswa.setStatusTa(StatusTA.valueOf(rs.getString("status_ta")));
        mahasiswa.setUsername(rs.getString("username"));
        mahasiswa.setEmail(rs.getString("email"));
        mahasiswa.setPasswordHash(rs.getString("password_hash"));
        mahasiswa.setRole(UserRole.valueOf(rs.getString("role")));
        mahasiswa.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        mahasiswa.setIsActive(rs.getBoolean("is_active"));
        return mahasiswa;
    }
}