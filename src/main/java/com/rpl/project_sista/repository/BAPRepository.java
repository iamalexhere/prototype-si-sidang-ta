package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.BAP;
import com.rpl.project_sista.model.entity.Sidang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BAPRepository extends JpaRepository<BAP, Long> {
    Optional<BAP> findBySidang(Sidang sidang);
}
