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

@Repository
public class JdbcMahasiswaRepository implements MahasiswaRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Mahasiswa> findAll() {
        return jdbcTemplate.query(
            "SELECT m.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM mahasiswa m JOIN users u ON m.user_id = u.user_id",
            this::mapRowToMahasiswa
        );
    }

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

    @Override
    public Mahasiswa save(Mahasiswa mahasiswa) {
        if (mahasiswa.getMahasiswaId() != null) {
            // Update existing mahasiswa
            jdbcTemplate.update(
                "UPDATE users SET username = ?, email = ?, password_hash = ?, role = ?::user_role, is_active = ? WHERE user_id = ?",
                mahasiswa.getUsername(),
                mahasiswa.getEmail(),
                mahasiswa.getPasswordHash(),
                mahasiswa.getRole().toString().toLowerCase(),
                mahasiswa.getIsActive(),
                mahasiswa.getUserId()  // Use user_id for users table
            );
            
            jdbcTemplate.update(
                "UPDATE mahasiswa SET npm = ?, nama = ?, status_ta = ?::status_ta WHERE mahasiswa_id = ?",
                mahasiswa.getNpm(),
                mahasiswa.getNama(),
                mahasiswa.getStatusTa().toString().toLowerCase(),
                mahasiswa.getMahasiswaId()  // Use mahasiswa_id for mahasiswa table
            );
        } else {
            // Insert new mahasiswa
            jdbcTemplate.update(
                "INSERT INTO users (username, email, password_hash, role, created_at, is_active) VALUES (?, ?, ?, ?::user_role, ?, ?)",
                mahasiswa.getUsername(),
                mahasiswa.getEmail(),
                mahasiswa.getPasswordHash(),
                mahasiswa.getRole().toString().toLowerCase(),
                LocalDateTime.now(),
                true
            );
            
            Integer userId = jdbcTemplate.queryForObject(
                "SELECT user_id FROM users WHERE username = ?",
                Integer.class,
                mahasiswa.getUsername()
            );
            
            jdbcTemplate.update(
                "INSERT INTO mahasiswa (user_id, npm, nama, status_ta) VALUES (?, ?, ?, ?::status_ta)",
                userId,
                mahasiswa.getNpm(),
                mahasiswa.getNama(),
                mahasiswa.getStatusTa().toString().toLowerCase()
            );
            
            // Get the generated mahasiswa_id
            Integer mahasiswaId = jdbcTemplate.queryForObject(
                "SELECT mahasiswa_id FROM mahasiswa WHERE user_id = ?",
                Integer.class,
                userId
            );
            
            mahasiswa.setUserId(userId);
            mahasiswa.setMahasiswaId(mahasiswaId);
        }
        return mahasiswa;
    }

    @Override
    public List<Mahasiswa> findByName(String name) {
        return jdbcTemplate.query(
            "SELECT m.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM mahasiswa m JOIN users u ON m.user_id = u.user_id WHERE m.nama ILIKE ?", 
            this::mapRowToMahasiswa, 
            "%" + name + "%"
        );
    }

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

    @Override
    public void deleteById(Integer id) {
        // First delete from mahasiswa table
        jdbcTemplate.update("DELETE FROM mahasiswa WHERE user_id = ?", id);
        // Then delete from users table
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
    }

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
