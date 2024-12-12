package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.BAP;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Users;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BAPRepository {
    List<BAP> findAll();
    Optional<BAP> findById(Long id);
    
    // Find BAP by sidang
    Optional<BAP> findBySidang(Sidang sidang);
    
    // Save or update BAP
    BAP save(BAP bap);
    
    // Delete BAP
    void deleteById(Long id);
    
    // Get all users who need to approve this BAP
    Set<Users> findRequiredApprovers(BAP bap);
    
    // Get all users who have approved this BAP
    Set<Users> findApprovers(BAP bap);
    
    // Check if BAP is fully approved
    boolean isFullyApproved(BAP bap);
    
    // Add approver to BAP
    void addApprover(BAP bap, Users user);
    
    // Remove approver from BAP
    void removeApprover(BAP bap, Users user);
}
