package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.dto.KomponenNilaiDTO;
import com.rpl.project_sista.model.entity.NilaiSidang;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcNilaiSidangRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNilaiSidangRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<NilaiSidang> nilaiSidangRowMapper = (rs, rowNum) -> {
        NilaiSidang nilaiSidang = new NilaiSidang();
        nilaiSidang.setNilaiId(rs.getLong("nilai_id"));
        return nilaiSidang;
    };

    public List<NilaiSidang> findAll() {
        String sql = "SELECT * FROM nilai_sidang";
        return jdbcTemplate.query(sql, nilaiSidangRowMapper);
    }

    @SuppressWarnings("deprecation")    
    public List<KomponenNilaiDTO> findAllNilaiByIdSidang(int idSidang) {
        String sql = "
            SELECT kn.komponen_id, kn.nama_komponen, kn.bobot, ns.nilai 
            FROM komponen_nilai kn 
            LEFT JOIN nilai_sidang ns ON ns.komponen_id = kn.komponen_id 
            WHERE ns.sidang_id = ?"

        return jdbcTemplate.query(sql, new Object[]{idSidang}, (rs, rowNum) ->
            new KomponenNilaiDTO(
                rs.getInt("komponen_id"),
                rs.getDouble("nilai")
            )
        );
    }    

    @SuppressWarnings("deprecation")
    public NilaiSidang findById(Long id) {
        String sql = "SELECT * FROM nilai_sidang WHERE nilai_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, nilaiSidangRowMapper);
    }

    public int saveNilaiSidang(int idSidang, int komponenId, int dosenId, double nilai) {
        String sql = "INSERT INTO nilai_sidang (sidang_id, komponen_id, dosen_id, nilai, created_at, updated_at) " +
                 "VALUES (?, ?, ?, ?, now(), now()) " +
                 "ON CONFLICT (sidang_id, komponen_id, dosen_id) " +
                 "DO UPDATE SET nilai = EXCLUDED.nilai, updated_at = now()";

    return jdbcTemplate.update(sql, idSidang, komponenId, dosenId, nilai);
    }
}
