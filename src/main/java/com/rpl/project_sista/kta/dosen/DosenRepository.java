package com.rpl.project_sista.kta.dosen;

import java.util.List;
import java.util.Optional;

public interface DosenRepository {
    List<Dosen> findAll();
    Optional<Dosen> findById(Integer id);
    Dosen save(Dosen dosen);
    List<Dosen> findByName(String name);
    List<Dosen> findPaginated(int page, int size, String filter);
    int count(String filter);
    void deleteById(Integer id);
}
