package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.BAP;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.repository.BAPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcBAPRepository implements BAPRepository {
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcBAPRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<BAP> findAll() {
        String sql = "SELECT b.*, s.* FROM bap b " +
                    "JOIN sidang s ON b.sidang_id = s.sidang_id";
        return jdbcTemplate.query(sql, new BAPRowMapper());
    }

    @Override
    public Optional<BAP> findById(Long id) {
        String sql = "SELECT b.*, s.* FROM bap b " +
                    "JOIN sidang s ON b.sidang_id = s.sidang_id " +
                    "WHERE b.bap_id = ?";
        try {
            BAP bap = jdbcTemplate.queryForObject(sql, new BAPRowMapper(), id);
            return Optional.ofNullable(bap);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<BAP> findBySidangId(Long sidangId) {
        String sql = "SELECT b.*, s.* FROM bap b " +
                    "JOIN sidang s ON b.sidang_id = s.sidang_id " +
                    "WHERE b.sidang_id = ?";
        try {
            BAP bap = jdbcTemplate.queryForObject(sql, new BAPRowMapper(), sidangId);
            return Optional.ofNullable(bap);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public BAP save(BAP bap) {
        if (bap.getBapId() != null) {
            // Update
            String sql = "UPDATE bap SET sidang_id = ?, catatan_tambahan = ? " +
                        "WHERE bap_id = ?";
            jdbcTemplate.update(sql,
                bap.getSidang().getSidangId(),
                bap.getCatatanTambahan(),
                bap.getBapId()
            );
        } else {
            // Insert
            String sql = "INSERT INTO bap (sidang_id, catatan_tambahan, created_at) " +
                        "VALUES (?, ?, ?) RETURNING bap_id";
            Long generatedId = jdbcTemplate.queryForObject(sql, Long.class,
                bap.getSidang().getSidangId(),
                bap.getCatatanTambahan(),
                LocalDateTime.now()
            );
            bap.setBapId(generatedId);
        }
        return bap;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM bap WHERE bap_id = ?";
        jdbcTemplate.update(sql, id);
    }

    private class BAPRowMapper implements RowMapper<BAP> {
        @Override
        public BAP mapRow(ResultSet rs, int rowNum) throws SQLException {
            BAP bap = new BAP();
            bap.setBapId(rs.getLong("bap_id"));
            bap.setCatatanTambahan(rs.getString("catatan_tambahan"));
            bap.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

            // Map Sidang
            Sidang sidang = new Sidang();
            sidang.setSidangId(rs.getLong("sidang_id"));
            bap.setSidang(sidang);

            return bap;
        }
    }
}
