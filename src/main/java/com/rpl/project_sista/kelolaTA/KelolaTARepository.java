package com.rpl.project_sista.kelolaTA;

import java.util.List;

public interface KelolaTARepository {
    List<Peserta> findByNama(String nama);
    List<MahasiswaTA> findMahasiswa(String nama);
}
