package com.rpl.project_sista.jdbcrepository;

import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.entity.Semester;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.enums.JenisTA;
import com.rpl.project_sista.model.enums.Periode;
import com.rpl.project_sista.model.enums.StatusSidang;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.repository.MahasiswaDashboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class JdbcMahasiswaDashboardRepository implements MahasiswaDashboardRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public TugasAkhir findTugasAkhirByMahasiswaId(int mahasiswaId) {
        String sql = "SELECT DISTINCT ta.*, m.*, s.* FROM tugas_akhir ta " +
                    "JOIN mahasiswa m ON ta.mahasiswa_id = m.mahasiswa_id " +
                    "JOIN semester s ON ta.semester_id = s.semester_id " +
                    "WHERE m.mahasiswa_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                TugasAkhir tugasAkhir = new TugasAkhir();
                tugasAkhir.setTaId(rs.getLong("ta_id"));
                tugasAkhir.setJudul(rs.getString("judul"));
                tugasAkhir.setTopik(rs.getString("topik"));
                tugasAkhir.setJenisTA(JenisTA.valueOf(rs.getString("jenis_ta")));
                tugasAkhir.setStatus(StatusTA.valueOf(rs.getString("status")));
                tugasAkhir.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                // Set Mahasiswa
                Mahasiswa mahasiswa = new Mahasiswa();
                mahasiswa.setMahasiswaId(rs.getInt("mahasiswa_id"));
                mahasiswa.setNpm(rs.getString("npm"));
                mahasiswa.setNama(rs.getString("nama"));
                mahasiswa.setStatusTa(StatusTA.valueOf(rs.getString("status_ta")));
                mahasiswa.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                tugasAkhir.setMahasiswa(mahasiswa);

                // Set Semester
                Semester semester = new Semester();
                semester.setSemesterId(rs.getLong("semester_id"));
                semester.setTahunAjaran(rs.getString("tahun_ajaran"));
                semester.setPeriode(Periode.valueOf(rs.getString("periode")));
                semester.setIsActive(rs.getBoolean("is_active"));
                semester.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                tugasAkhir.setSemester(semester);

                // Get pembimbing in a separate query with proper joins
                String pembimbingSQL = "SELECT d.* FROM dosen d " +
                                     "JOIN pembimbing_ta pt ON d.dosen_id = pt.dosen_id " +
                                     "WHERE pt.ta_id = ?";
                
                List<Dosen> pembimbingList = jdbcTemplate.query(pembimbingSQL, 
                    (pembimbingRs, pembimbingRowNum) -> {
                        Dosen pembimbing = new Dosen();
                        pembimbing.setDosenId(pembimbingRs.getInt("dosen_id"));
                        pembimbing.setNip(pembimbingRs.getString("nip"));
                        pembimbing.setNama(pembimbingRs.getString("nama"));
                        return pembimbing;
                    }, 
                    tugasAkhir.getTaId());
                
                Set<Dosen> pembimbingSet = new HashSet<>(pembimbingList);
                tugasAkhir.setPembimbing(pembimbingSet);
                
                return tugasAkhir;
            }, mahasiswaId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Sidang findSidangByTugasAkhirId(Long taId) {
        String sql = "SELECT s.* FROM sidang s WHERE s.ta_id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Sidang sidang = new Sidang();
                sidang.setSidangId(rs.getLong("sidang_id"));
                sidang.setJadwal(rs.getTimestamp("jadwal").toLocalDateTime());
                sidang.setRuangan(rs.getString("ruangan"));
                sidang.setStatusSidang(StatusSidang.valueOf(rs.getString("status_sidang")));
                sidang.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                sidang.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

                // Get penguji in a separate query with their roles
                String pengujiSql = "SELECT d.*, ps.peran_penguji FROM dosen d " +
                                  "JOIN penguji_sidang ps ON d.dosen_id = ps.dosen_id " +
                                  "WHERE ps.sidang_id = ?";
                
                Set<Dosen> pengujiSet = new HashSet<>();
                jdbcTemplate.query(pengujiSql, 
                    (pengujiRs, pengujiRowNum) -> {
                        Dosen penguji = new Dosen();
                        penguji.setDosenId(pengujiRs.getInt("dosen_id"));
                        penguji.setNip(pengujiRs.getString("nip"));
                        penguji.setNama(pengujiRs.getString("nama"));
                        String peranPenguji = pengujiRs.getString("peran_penguji");
                        
                        // Store dosen ID in penguji1 or penguji2 based on role
                        if ("penguji1".equals(peranPenguji)) {
                            sidang.setPenguji1(penguji.getDosenId());
                        } else if ("penguji2".equals(peranPenguji)) {
                            sidang.setPenguji2(penguji.getDosenId());
                        }
                        pengujiSet.add(penguji);
                        return penguji;
                    }, 
                    sidang.getSidangId());
                
                sidang.setPenguji(pengujiSet);
                return sidang;
            }, taId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
