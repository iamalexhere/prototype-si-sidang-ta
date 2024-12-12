package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.NilaiSidang;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.repository.NilaiSidangRepository;
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
public class JdbcNilaiSidangRepository implements NilaiSidangRepository {
    
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcNilaiSidangRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<NilaiSidang> findAll() {
        String sql = "SELECT ns.*, s.*, k.*, d.* FROM nilai_sidang ns " +
                    "JOIN sidang s ON ns.sidang_id = s.sidang_id " +
                    "JOIN komponen_nilai k ON ns.komponen_id = k.komponen_id " +
                    "JOIN dosen d ON ns.dosen_id = d.dosen_id";
        return jdbcTemplate.query(sql, new NilaiSidangRowMapper());
    }

    @Override
    public Optional<NilaiSidang> findById(Long id) {
        String sql = "SELECT ns.*, s.*, k.*, d.* FROM nilai_sidang ns " +
                    "JOIN sidang s ON ns.sidang_id = s.sidang_id " +
                    "JOIN komponen_nilai k ON ns.komponen_id = k.komponen_id " +
                    "JOIN dosen d ON ns.dosen_id = d.dosen_id " +
                    "WHERE ns.nilai_id = ?";
        try {
            NilaiSidang nilaiSidang = jdbcTemplate.queryForObject(sql, new NilaiSidangRowMapper(), id);
            return Optional.ofNullable(nilaiSidang);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<NilaiSidang> findBySidangId(Long sidangId) {
        String sql = "SELECT ns.*, s.*, k.*, d.* FROM nilai_sidang ns " +
                    "JOIN sidang s ON ns.sidang_id = s.sidang_id " +
                    "JOIN komponen_nilai k ON ns.komponen_id = k.komponen_id " +
                    "JOIN dosen d ON ns.dosen_id = d.dosen_id " +
                    "WHERE ns.sidang_id = ?";
        return jdbcTemplate.query(sql, new NilaiSidangRowMapper(), sidangId);
    }

    @Override
    public List<NilaiSidang> findByDosenId(Long dosenId) {
        String sql = "SELECT ns.*, s.*, k.*, d.* FROM nilai_sidang ns " +
                    "JOIN sidang s ON ns.sidang_id = s.sidang_id " +
                    "JOIN komponen_nilai k ON ns.komponen_id = k.komponen_id " +
                    "JOIN dosen d ON ns.dosen_id = d.dosen_id " +
                    "WHERE ns.dosen_id = ?";
        return jdbcTemplate.query(sql, new NilaiSidangRowMapper(), dosenId);
    }

    @Override
    public NilaiSidang save(NilaiSidang nilaiSidang) {
        if (nilaiSidang.getNilaiId() != null) {
            // Update
            String sql = "UPDATE nilai_sidang SET sidang_id = ?, komponen_id = ?, " +
                        "dosen_id = ?, nilai = ?, updated_at = ? WHERE nilai_id = ?";
            jdbcTemplate.update(sql,
                nilaiSidang.getSidang().getSidangId(),
                nilaiSidang.getKomponen().getKomponenId(),
                nilaiSidang.getDosen().getDosenId(),
                nilaiSidang.getNilai(),
                LocalDateTime.now(),
                nilaiSidang.getNilaiId()
            );
        } else {
            // Insert
            String sql = "INSERT INTO nilai_sidang (sidang_id, komponen_id, dosen_id, " +
                        "nilai, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?) " +
                        "RETURNING nilai_id";
            Long generatedId = jdbcTemplate.queryForObject(sql, Long.class,
                nilaiSidang.getSidang().getSidangId(),
                nilaiSidang.getKomponen().getKomponenId(),
                nilaiSidang.getDosen().getDosenId(),
                nilaiSidang.getNilai(),
                LocalDateTime.now(),
                LocalDateTime.now()
            );
            nilaiSidang.setNilaiId(generatedId);
        }
        return nilaiSidang;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM nilai_sidang WHERE nilai_id = ?";
        jdbcTemplate.update(sql, id);
    }

    private class NilaiSidangRowMapper implements RowMapper<NilaiSidang> {
        @Override
        public NilaiSidang mapRow(ResultSet rs, int rowNum) throws SQLException {
            NilaiSidang nilaiSidang = new NilaiSidang();
            nilaiSidang.setNilaiId(rs.getLong("nilai_id"));
            nilaiSidang.setNilai(rs.getFloat("nilai"));
            nilaiSidang.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            nilaiSidang.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

            // Map Sidang
            Sidang sidang = new Sidang();
            sidang.setSidangId(rs.getLong("sidang_id"));
            nilaiSidang.setSidang(sidang);

            // Map KomponenNilai
            KomponenNilai komponen = new KomponenNilai();
            komponen.setKomponenId(rs.getLong("komponen_id"));
            nilaiSidang.setKomponen(komponen);

            // Map Dosen
            Dosen dosen = new Dosen();
            dosen.setDosenId(rs.getLong("dosen_id"));
            nilaiSidang.setDosen(dosen);

            return nilaiSidang;
        }
    }
}
