package com.rpl.project_sista.kelolaTA;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcKelolaTARepository implements KelolaTARepository{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Peserta> findByNama(String nama) {
        String sql = "select nama from mahasiswa where nama ilike ?";
        return jdbcTemplate.query(sql, this::mapRowToPeserta, "%"+nama+"%");
    }

    public List<MahasiswaTA> findMahasiswa(String nama) {
        String sql = """
                select namaMhs, judul, namaPem, array_agg(namaPeng) as listPenguji
                from
                    (select npm, namaMhs, judul, namaPem, dosen.nama as namaPeng, peran
                    from
                        (select mahasiswa_id, npm, listSidang.nama as namaMhs, listSidang.ta_id, judul, listSidang.sidang_id, pembimbing_ta.dosen_id as idPembimbing, dosen.nama as namaPem, penguji_sidang.dosen_id as idPeng, penguji_sidang.peran_penguji as peran
                        from
                            (select mahasiswa_id, npm, nama, listTA.ta_id, judul, sidang_id
                            from
                                (select listMahasiswa.mahasiswa_id, npm, nama, ta_id, judul
                                from 
                                    (select mahasiswa_id, npm, nama
                                    from
                                        mahasiswa
                                    where
                                        nama ilike ?) as listMahasiswa
                                inner join tugas_akhir
                                on listMahasiswa.mahasiswa_id = tugas_akhir.mahasiswa_id) as listTA
                            inner join sidang
                            on listTA.ta_id = sidang.ta_id) as listSidang
                        inner join pembimbing_ta
                        on listSidang.ta_id = pembimbing_ta.ta_id
                        inner join dosen
                        on pembimbing_ta.dosen_id = dosen.dosen_id
                        inner join penguji_sidang
                        on listSidang.sidang_id = penguji_sidang.sidang_id) as listPenguji
                    inner join dosen
                    on listPenguji.idPeng = dosen.dosen_id
                    order by peran) as tabelFinal
                group by npm, namaMhs, judul, namaPem
                """;
        return jdbcTemplate.query(sql, this::mapRowToMahasiswaTA, "%"+nama+"%");
    }

    private Peserta mapRowToPeserta(ResultSet resultSet, int rowNum) throws SQLException {
        return new Peserta(resultSet.getString("nama"));
    }

    private MahasiswaTA mapRowToMahasiswaTA(ResultSet resultSet, int rowNum) throws SQLException {
        MahasiswaTA mhs = new MahasiswaTA();
        mhs.setNamaMhs(resultSet.getString("namamhs"));
        mhs.setJudul(resultSet.getString("judul"));
        mhs.setNamapem(resultSet.getString("namapem"));
        Array sqlArr = resultSet.getArray("listpenguji");
        mhs.setListpenguji((String[]) sqlArr.getArray());
        return mhs;
    }
}
