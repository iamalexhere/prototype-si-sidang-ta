package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.BAP;
import java.util.List;
import java.util.Optional;

public interface BAPRepository {
    List<BAP> findAll();
    Optional<BAP> findById(Long id);
    Optional<BAP> findBySidangId(Long sidangId);
    BAP save(BAP bap);
    void deleteById(Long id);
}
