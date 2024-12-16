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

@Repository
public class JdbcDosenRepository implements DosenRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Dosen> findAll() {
        return jdbcTemplate.query(
            "SELECT d.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM dosen d JOIN users u ON d.user_id = u.user_id",
            this::mapRowToDosen
        );
    }

    @Override
    public Optional<Dosen> findById(Integer id) {
        List<Dosen> results = jdbcTemplate.query(
            "SELECT d.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM dosen d JOIN users u ON d.user_id = u.user_id WHERE u.user_id = ?",
            this::mapRowToDosen,
            id
        );
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Dosen save(Dosen dosen) {
        if (dosen.getUserId() != null) {
            // Update existing dosen
            jdbcTemplate.update(
                "UPDATE users SET username = ?, email = ?, password_hash = ?, role = ?::user_role, is_active = ? WHERE user_id = ?",
                dosen.getUsername(),
                dosen.getEmail(),
                dosen.getPasswordHash(),
                dosen.getRole().toString().toLowerCase(),
                dosen.getIsActive(),
                dosen.getUserId()
            );
            
            jdbcTemplate.update(
                "UPDATE dosen SET nip = ?, nama = ? WHERE user_id = ?",
                dosen.getNip(),
                dosen.getNama(),
                dosen.getUserId()
            );
        } else {
            // Insert new dosen
            jdbcTemplate.update(
                "INSERT INTO users (username, email, password_hash, role, created_at, is_active) VALUES (?, ?, ?, ?::user_role, ?, ?)",
                dosen.getUsername(),
                dosen.getEmail(),
                dosen.getPasswordHash(),
                dosen.getRole().toString().toLowerCase(),
                LocalDateTime.now(),
                dosen.getIsActive()
            );
            
            Integer userId = jdbcTemplate.queryForObject("SELECT currval('users_user_id_seq')", Integer.class);
            dosen.setUserId(userId);
            
            jdbcTemplate.update(
                "INSERT INTO dosen (user_id, nip, nama) VALUES (?, ?, ?)",
                userId,
                dosen.getNip(),
                dosen.getNama()
            );
            
            // Get the generated dosen_id
            Integer dosenId = jdbcTemplate.queryForObject(
                "SELECT dosen_id FROM dosen WHERE user_id = ?",
                Integer.class,
                userId
            );
            dosen.setDosenId(dosenId);
        }
        return dosen;
    }

    @Override
    public void deleteById(Integer id) {
        jdbcTemplate.update("DELETE FROM dosen WHERE user_id = ?", id);
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", id);
    }

    @Override
    public List<Dosen> findByName(String name) {
        return jdbcTemplate.query(
            "SELECT d.*, u.username, u.email, u.password_hash, u.role, u.created_at, u.is_active FROM dosen d JOIN users u ON d.user_id = u.user_id WHERE d.nama ILIKE ?",
            this::mapRowToDosen,
            "%" + name + "%"
        );
    }

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

    private Dosen mapRowToDosen(ResultSet rs, int rowNum) throws SQLException {
        Dosen dosen = new Dosen();
        dosen.setUserId(rs.getInt("user_id"));
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
