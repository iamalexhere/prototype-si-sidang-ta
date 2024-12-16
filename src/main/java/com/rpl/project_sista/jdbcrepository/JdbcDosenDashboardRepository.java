package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.*;
import com.rpl.project_sista.model.enums.*;
import com.rpl.project_sista.repository.DosenDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcDosenDashboardRepository implements DosenDashboardRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<TugasAkhir> findTugasAkhirByPembimbingId(Integer dosenId) {
        String sql = """
            SELECT DISTINCT ta.*, m.*, d.*, s.*
            FROM tugas_akhir ta
            JOIN pembimbing_ta pt ON ta.ta_id = pt.ta_id
            JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id
            JOIN semester s ON ta.semester_id = s.semester_id
            LEFT JOIN dosen d ON pt.dosen_id = d.dosen_id
            WHERE pt.dosen_id = ?
        """;

        return jdbcTemplate.query(sql, this::mapRowToTugasAkhir, dosenId);
    }

    @Override
    public List<Sidang> findSidangByPengujiId(Integer dosenId) {
        String sql = """
            SELECT DISTINCT s.*, ta.*, m.*, d.*, sem.*
            FROM sidang s
            JOIN penguji_sidang ps ON s.sidang_id = ps.sidang_id
            JOIN tugas_akhir ta ON s.ta_id = ta.ta_id
            JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id
            JOIN semester sem ON ta.semester_id = sem.semester_id
            LEFT JOIN dosen d ON ps.dosen_id = d.dosen_id
            WHERE ps.dosen_id = ?
        """;

        return jdbcTemplate.query(sql, this::mapRowToSidang, dosenId);
    }

    private TugasAkhir mapRowToTugasAkhir(ResultSet rs, int rowNum) throws SQLException {
        TugasAkhir tugasAkhir = new TugasAkhir();
        tugasAkhir.setTaId(rs.getLong("ta_id"));
        tugasAkhir.setJudul(rs.getString("judul"));
        tugasAkhir.setJenisTA(JenisTA.valueOf(rs.getString("jenis_ta")));
        tugasAkhir.setStatus(StatusTA.valueOf(rs.getString("status")));
        tugasAkhir.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        // Set Mahasiswa
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setMahasiswaId(rs.getInt("mahasiswa_id"));
        mahasiswa.setNpm(rs.getString("npm"));
        mahasiswa.setNama(rs.getString("nama"));
        tugasAkhir.setMahasiswa(mahasiswa);

        // Set Semester
        Semester semester = new Semester();
        semester.setSemesterId(rs.getLong("semester_id"));
        semester.setTahunAjaran(rs.getString("tahun_ajaran"));
        semester.setPeriode(Periode.valueOf(rs.getString("periode")));
        tugasAkhir.setSemester(semester);

        return tugasAkhir;
    }

    private Sidang mapRowToSidang(ResultSet rs, int rowNum) throws SQLException {
        Sidang sidang = new Sidang();
        sidang.setSidangId(rs.getLong("sidang_id"));
        sidang.setJadwal(rs.getTimestamp("jadwal").toLocalDateTime());
        sidang.setRuangan(rs.getString("ruangan"));
        sidang.setStatusSidang(StatusSidang.valueOf(rs.getString("status_sidang")));

        // Set Tugas Akhir with Mahasiswa
        TugasAkhir tugasAkhir = mapRowToTugasAkhir(rs, rowNum);
        sidang.setTugasAkhir(tugasAkhir);

        return sidang;
    }

    @SuppressWarnings("deprecation")
    public Sidang findSidangByTugasAkhirId() {
        String sql = "SELECT * FROM sidang WHERE ta_id = 1";

        return jdbcTemplate.queryForObject(sql, new Object[]{}, (rs, rowNum) -> {
            Sidang sidang = new Sidang();
            sidang.setSidangId(rs.getLong("sidang_id"));
            sidang.setJadwal(rs.getTimestamp("jadwal").toLocalDateTime());
            sidang.setRuangan(rs.getString("ruangan"));
            StatusSidang.valueOf(rs.getString("status_sidang"));
            sidang.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            sidang.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return sidang;
        });
    }    
}
