package com.rpl.project_sista.Mahasiswa;

public interface MahasiswaRepository {
    Iterable<Mahasiswa> getMahasiswa(String username);
}
