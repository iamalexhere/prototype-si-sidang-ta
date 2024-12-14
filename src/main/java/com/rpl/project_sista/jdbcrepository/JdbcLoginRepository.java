package com.rpl.project_sista.jdbcrepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rpl.project_sista.model.entity.Users;
import com.rpl.project_sista.model.enums.UserRole;
import com.rpl.project_sista.repository.LoginRepository;

import javax.sql.DataSource;

@Repository
public class JdbcLoginRepository implements LoginRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    public List<Users> findUserByEmail(String email) {
        String sql = "select * from users where email = ?";
        return jdbcTemplate.query(sql, this::mapRowToUsers, email);
    }

    public int findMhsId(int userId) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int mhsId = -1;
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement("select mahasiswa_id from mahasiswa where user_id=?");
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            mhsId = rs.getInt("mahasiswa_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mhsId;
    }

    public int findDosenId(int userId) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int dosenId = -1;
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement("select dosen_id from dosen where user_id=?");
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            dosenId = rs.getInt("dosen_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dosenId;
    }

    private Users mapRowToUsers(ResultSet rs, int rowNum) throws SQLException {
        return new Users(
            rs.getInt("user_id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password_hash"),
            UserRole.valueOf(rs.getString("role")),
            rs.getObject("created_at", LocalDateTime.class),
            rs.getBoolean("is_active")
        );
    }
}
