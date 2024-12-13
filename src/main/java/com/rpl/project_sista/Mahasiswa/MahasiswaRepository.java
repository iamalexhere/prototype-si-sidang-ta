package com.rpl.project_sista.Mahasiswa;

import java.util.List;

public interface MahasiswaRepository {
    List<Mahasiswa> getMahasiswa(String username);
}
