package com.rpl.project_sista.repository;

import com.rpl.project_sista.model.entity.NilaiSidang;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.enums.TipePenilai;

import java.util.List;
import java.util.Optional;
import java.util.Map;

public interface NilaiSidangRepository {
    List<NilaiSidang> findAll();
    Optional<NilaiSidang> findById(Long id);
    
    // Find nilai by sidang
    List<NilaiSidang> findBySidang(Sidang sidang);
    
    // Find nilai given by a specific dosen for a sidang
    List<NilaiSidang> findBySidangAndDosen(Sidang sidang, Dosen dosen);
    
    // Get average nilai for a sidang by tipe penilai
    Map<TipePenilai, Float> getAverageNilaiBySidang(Sidang sidang);
    
    // Get final nilai for a sidang (weighted average of all components)
    Float getFinalNilaiBySidang(Sidang sidang);
    
    // Save or update nilai
    NilaiSidang save(NilaiSidang nilaiSidang);
    
    // Delete nilai
    void deleteById(Long id);
    
    // Check if dosen has completed all nilai components for a sidang
    boolean hasDosenCompletedPenilaian(Sidang sidang, Dosen dosen);
}
