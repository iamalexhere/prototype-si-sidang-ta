package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.BAP;
import com.rpl.project_sista.model.entity.PersetujuanBAP;
import com.rpl.project_sista.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersetujuanBAPRepository extends JpaRepository<PersetujuanBAP, Long> {
    Optional<PersetujuanBAP> findByBapAndUser(BAP bap, Users user);
}
