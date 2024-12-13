package com.rpl.project_sista;


public interface BapRepository{
    String findNama(String username);
    String findSemester(String nama);
    String findTahunAkademik(String nama);
    String findNpm(String nama);
    String findNamaz(String nama);
    String findTopik(String nama);
    String findPembimbingTunggal(String nama);
    String findKetuaPenguji(String nama);
    String findAnggotaPenguji(String nama);
    String findTanggal(String nama);
    double findBobotPembimbing(int semester);
    double findBobotPenguji(int semester);
    double findNilaiKetua(String name);
    double findNilaiPenguji(String name);
    double findNilaiPembimbing(String name);
}