package com.rpl.project_sista.Dosen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.rpl.project_sista.ListMahasiswa;

@Repository
public class JdbcDosenRepository implements DosenRepository {
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Iterable<ListMahasiswa> findAllPembimbing(String username, String nama_mahasiswa, String npm_mahasiswa, String periode, String tahun_ajaran) {
        String query =           
        "SELECT mahasiswa.npm, mahasiswa.nama, periode, tahun_ajaran\n" + //
        "FROM tugas_akhir \n" + //
        "JOIN sidang ON tugas_akhir.ta_id = sidang.ta_id\n" + //
        "JOIN pembimbing_ta ON pembimbing_ta.ta_id = tugas_akhir.ta_id\n" + //
        "JOIN mahasiswa ON mahasiswa.mahasiswa_id = tugas_akhir.mahasiswa_id\n" + //
        "JOIN dosen ON dosen.dosen_id = pembimbing_ta.dosen_id\n" + //
        "JOIN users ON users.user_id = dosen.user_id\n" + //
        "JOIN semester ON semester.semester_id = tugas_akhir.semester_id\n" + //
        "WHERE users.username = ?";

        if (nama_mahasiswa != null && !nama_mahasiswa.isEmpty()) {
            query += " AND mahasiswa.nama ILIKE '%" + nama_mahasiswa+"%'";
        }
        if (npm_mahasiswa != null && !npm_mahasiswa.isEmpty()) {
            query += " AND mahasiswa.npm ILIKE '%" + npm_mahasiswa+"%'";
        }
        if (periode != null && !periode.isEmpty()) {
            query += " AND periode = '"+periode+"'";
        }
        if (tahun_ajaran != null && !tahun_ajaran.isEmpty()) {
            query += " AND tahun_ajaran ILIKE '"+tahun_ajaran+"%'";
        }

        return jdbc.query(query, this::mapRowToMahasiswa, username);              
    }

    @Override
    public Iterable<ListMahasiswa> findAllPenguji(String username, String nama_mahasiswa, String npm_mahasiswa, String periode, String tahun_ajaran) {
        String query = 
        "SELECT mahasiswa.npm, mahasiswa.nama, periode, tahun_ajaran\n" + //
        "FROM tugas_akhir \n" + //
        "JOIN sidang ON tugas_akhir.ta_id = sidang.ta_id\n" + //
        "JOIN penguji_sidang ON penguji_sidang.sidang_id = sidang.sidang_id\n" + //
        "JOIN mahasiswa ON mahasiswa.mahasiswa_id = tugas_akhir.mahasiswa_id\n" + //
        "JOIN dosen ON dosen.dosen_id = penguji_sidang.dosen_id\n" + //
        "JOIN users ON users.user_id = dosen.user_id\n" + //
        "JOIN semester ON semester.semester_id = tugas_akhir.semester_id\n" + //
        "WHERE users.username = ?";
                                
        if (nama_mahasiswa != null && !nama_mahasiswa.isEmpty()) {
            query += " AND mahasiswa.nama ILIKE '%" + nama_mahasiswa+"%'";
        }
        if (npm_mahasiswa != null && !npm_mahasiswa.isEmpty()) {
            query += " AND mahasiswa.npm ILIKE '" + npm_mahasiswa+"%'";
        }
        if (periode != null && !periode.isEmpty()) {
            query += " AND periode = '"+periode+"'";
        }
        if (tahun_ajaran != null && !tahun_ajaran.isEmpty()) {
            query += " AND tahun_ajaran ILIKE '"+tahun_ajaran+"%'";
        }

        return jdbc.query(query, this::mapRowToMahasiswa, username);  
    }

    private ListMahasiswa mapRowToMahasiswa(ResultSet resultSet, int rowNum) throws SQLException{
        String dummy = resultSet.getString("periode");
        dummy = dummy.substring(0,1).toUpperCase() + dummy.substring(1);
        
    return new ListMahasiswa(
        resultSet.getString("npm"),
        resultSet.getString("nama"),
        dummy,
        resultSet.getString("tahun_ajaran")
    );
}
}
