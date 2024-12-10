package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.model.entity.Semester;
import com.rpl.project_sista.model.enums.TipePenilai;
import com.rpl.project_sista.repository.KomponenNilaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcKomponenNilaiRepository implements KomponenNilaiRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<KomponenNilai> rowMapper = (rs, rowNum) -> {
        KomponenNilai komponenNilai = new KomponenNilai();
        komponenNilai.setKomponenId(rs.getLong("komponen_id"));
        
        Semester semester = new Semester();
        semester.setSemesterId(rs.getLong("semester_id"));
        komponenNilai.setSemester(semester);
        
        komponenNilai.setNamaKomponen(rs.getString("nama_komponen"));
        komponenNilai.setBobot(rs.getFloat("bobot"));
        komponenNilai.setTipePenilai(TipePenilai.valueOf(rs.getString("tipe_penilai")));
        komponenNilai.setDeskripsi(rs.getString("deskripsi"));
        komponenNilai.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return komponenNilai;
    };

    @Override
    public List<KomponenNilai> findAll() {
        String sql = "SELECT * FROM komponen_nilai ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<KomponenNilai> findBySemester(Semester semester) {
        String sql = "SELECT * FROM komponen_nilai WHERE semester_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, rowMapper, semester.getSemesterId());
    }

    @Override
    public Optional<KomponenNilai> findById(Long id) {
        String sql = "SELECT * FROM komponen_nilai WHERE komponen_id = ?";
        List<KomponenNilai> results = jdbcTemplate.query(sql, rowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public KomponenNilai save(KomponenNilai komponenNilai) {
        if (komponenNilai.getKomponenId() == null) {
            return insert(komponenNilai);
        }
        return update(komponenNilai);
    }

    private KomponenNilai insert(KomponenNilai komponenNilai) {
        String sql = "INSERT INTO komponen_nilai (semester_id, nama_komponen, bobot, tipe_penilai, deskripsi, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, komponenNilai.getSemester().getSemesterId());
            ps.setString(2, komponenNilai.getNamaKomponen());
            ps.setFloat(3, komponenNilai.getBobot());
            ps.setString(4, komponenNilai.getTipePenilai().name());
            ps.setString(5, komponenNilai.getDeskripsi());
            ps.setTimestamp(6, Timestamp.valueOf(komponenNilai.getCreatedAt()));
            return ps;
        }, keyHolder);

        komponenNilai.setKomponenId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return komponenNilai;
    }

    private KomponenNilai update(KomponenNilai komponenNilai) {
        String sql = "UPDATE komponen_nilai SET semester_id = ?, nama_komponen = ?, bobot = ?, " +
                    "tipe_penilai = ?, deskripsi = ? WHERE komponen_id = ?";
        
        jdbcTemplate.update(sql,
            komponenNilai.getSemester().getSemesterId(),
            komponenNilai.getNamaKomponen(),
            komponenNilai.getBobot(),
            komponenNilai.getTipePenilai().name(),
            komponenNilai.getDeskripsi(),
            komponenNilai.getKomponenId()
        );
        
        return komponenNilai;
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM komponen_nilai WHERE komponen_id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM komponen_nilai";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
