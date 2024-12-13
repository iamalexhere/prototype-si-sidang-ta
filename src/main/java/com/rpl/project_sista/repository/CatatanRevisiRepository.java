package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.CatatanRevisi;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Dosen;
import java.util.List;
import java.util.Optional;

public interface CatatanRevisiRepository {
    List<CatatanRevisi> findAll();
    Optional<CatatanRevisi> findById(Long id);
    
    // Find catatan by sidang
    List<CatatanRevisi> findBySidang(Sidang sidang);
    
    // Find catatan by dosen (pembimbing)
    List<CatatanRevisi> findByDosen(Dosen dosen);
    
    // Find catatan by sidang and dosen
    List<CatatanRevisi> findBySidangAndDosen(Sidang sidang, Dosen dosen);
    
    // Save or update catatan
    CatatanRevisi save(CatatanRevisi catatanRevisi);
    
    // Delete catatan
    void deleteById(Long id);
    
    // Count catatan for a sidang
    long countBySidang(Sidang sidang);
}
