package com.rpl.project_sista;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcRepositoryBap implements BapRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("deprecation")
    @Override
    public String findNama(String username) {
        String sql = "SELECT m.nama FROM mahasiswa m JOIN users u ON m.user_id = u.user_id WHERE u.email = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, String.class);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String findSemester(String nama) {
        String sql = "SELECT s.periode FROM tugas_akhir ta JOIN mahasiswa m ON m.mahasiswa_id = ta.mahasiswa_id JOIN semester s ON s.semester_id = ta.semester_id WHERE m.nama = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{nama}, String.class);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String findTahunAkademik(String nama) {
        String sql = "SELECT s.tahun_ajaran FROM tugas_akhir ta JOIN mahasiswa m ON m.mahasiswa_id = ta.mahasiswa_id JOIN semester s ON s.semester_id = ta.semester_id WHERE m.nama = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{nama}, String.class);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String findNpm(String nama) {
        String sql = "SELECT m.npm FROM tugas_akhir ta JOIN mahasiswa m ON m.mahasiswa_id = ta.mahasiswa_id WHERE m.nama = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{nama}, String.class);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String findNamaz(String nama) {
        String sql = "SELECT m.nama FROM tugas_akhir ta JOIN mahasiswa m ON m.mahasiswa_id = ta.mahasiswa_id WHERE m.nama = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{nama}, String.class);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String findTopik(String nama) {
        String sql = "SELECT ta.topik FROM tugas_akhir ta JOIN mahasiswa m ON m.mahasiswa_id = ta.mahasiswa_id WHERE m.nama = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{nama}, String.class);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String findPembimbingTunggal(String nama) {
        String sql = "SELECT d.nama FROM tugas_akhir ta JOIN mahasiswa m ON m.mahasiswa_id = ta.mahasiswa_id JOIN pembimbing_ta pt ON pt.ta_id = ta.ta_id JOIN dosen d ON d.dosen_id = pt.dosen_id WHERE m.nama = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{nama}, String.class);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String findKetuaPenguji(String nama) {
        String sql = "SELECT d.nama FROM tugas_akhir ta JOIN mahasiswa m ON m.mahasiswa_id = ta.mahasiswa_id JOIN sidang s ON s.ta_id = ta.ta_id JOIN penguji_sidang ps ON ps.sidang_id = s.sidang_id JOIN dosen d ON d.dosen_id = ps.dosen_id WHERE m.nama = ? AND ps.peran_penguji = 'penguji1'";
        return jdbcTemplate.queryForObject(sql, new Object[]{nama}, String.class);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String findAnggotaPenguji(String nama) {
        String sql = "SELECT d.nama FROM tugas_akhir ta JOIN mahasiswa m ON m.mahasiswa_id = ta.mahasiswa_id JOIN sidang s ON s.ta_id = ta.ta_id JOIN penguji_sidang ps ON ps.sidang_id = s.sidang_id JOIN dosen d ON d.dosen_id = ps.dosen_id WHERE m.nama = ? AND ps.peran_penguji = 'penguji2'";
        return jdbcTemplate.queryForObject(sql, new Object[]{nama}, String.class);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String findTanggal(String nama) {
        String sql = "SELECT s.jadwal::DATE AS tanggal_sidang FROM sidang s JOIN tugas_akhir ta ON ta.ta_id = s.ta_id JOIN mahasiswa m ON m.mahasiswa_id = ta.mahasiswa_id WHERE m.nama = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{nama}, String.class);
    }
}
