package com.rpl.project_sista.Mahasiswa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcMahasiswaRepository implements MahasiswaRepository {
    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public Iterable<Mahasiswa> getMahasiswa(String username) {
        return jdbc.query(
            "SELECT \n" + //
                                "userMahasiswa.username,\n" + //
                                "mahasiswa.nama AS mahasiswa_nama,\n" + //
                                "ruangan,\n" + //
                                "jadwal::DATE AS tanggal,\n" + //
                                "jadwal::TIME AS jam,\n" + //
                                "array_agg(dosen.nama ORDER BY peran_penguji) AS penguji_nama,\n" + //
                                "array_agg(users.email ORDER BY peran_penguji) AS penguji_email,\n" + //
                                "array_agg(peran_penguji ORDER BY peran_penguji) AS penguji_peran\n" + //
                                "FROM users AS userMahasiswa\n" + //
                                "JOIN Mahasiswa ON userMahasiswa.user_id = mahasiswa.user_id\n" + //
                                "JOIN tugas_akhir ON mahasiswa.mahasiswa_id = tugas_akhir.mahasiswa_id\n" + //
                                "JOIN sidang ON sidang.ta_id = tugas_akhir.ta_id\n" + //
                                "JOIN penguji_sidang ON penguji_sidang.sidang_id = sidang.sidang_id\n" + //
                                "JOIN dosen ON dosen.dosen_id = penguji_sidang.dosen_id\n" + //
                                "JOIN users ON users.user_id = dosen.user_id\n" + //
                                "WHERE userMahasiswa.username = ?\n" + //
                                "GROUP BY userMahasiswa.username, mahasiswa.nama, ruangan, jadwal", this::mapRowToMahasiswa, username);
    }

    private Mahasiswa mapRowToMahasiswa(ResultSet resultSet, int rowNum) throws SQLException{
        String penguji = resultSet.getString("penguji_email");
        List<String> pengujiEmails = Arrays.asList(
            penguji.replace("{", "").replace("}", "").split(","));
            
        String penguji_nama = resultSet.getString("penguji_nama");
        List<String> listPenguji = Arrays.asList(
            penguji_nama.replace("{", "").replace("}", "").split(","));

        return new Mahasiswa(
            resultSet.getString("mahasiswa_nama"),
            resultSet.getString("ruangan"),
            resultSet.getDate("tanggal").toLocalDate(),
            resultSet.getTime("jam"),
            listPenguji.get(0).substring(1, listPenguji.get(0).length()-1),
            pengujiEmails.get(0),
            listPenguji.get(1).substring(1, listPenguji.get(1).length()-1),
            pengujiEmails.get(1)
        );
    }
}
    