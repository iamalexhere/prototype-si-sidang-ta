package com.rpl.project_sista.login;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcLoginRepository implements LoginRepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<User> findByEmail(String email) {
        String sql = "select email, password_hash from users where email = ?";
        return jdbcTemplate.query(sql, this::mapRowToUser, email);
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return new User(resultSet.getString("email"), resultSet.getString("password_hash"));
    }
}
