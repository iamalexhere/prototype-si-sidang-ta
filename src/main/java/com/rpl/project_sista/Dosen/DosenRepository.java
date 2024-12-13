package com.rpl.project_sista.Dosen;

public interface DosenRepository {
    Iterable<ListMahasiswa> findAllPembimbing(String username, String nama_mahasiswa, String npm_mahasiswa, String periode, String tahun_ajaran);
    Iterable<ListMahasiswa> findAllPenguji(String username, String nama_mahasiswa, String npm_mahasiswa, String periode, String tahun_ajaran);
    String getName(String username);
}
