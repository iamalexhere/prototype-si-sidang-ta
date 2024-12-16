package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.BAP;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.repository.BAPRepository;
import com.rpl.project_sista.repository.SidangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BAPService {

    @Autowired
    private BAPRepository bapRepository;

    @Autowired
    private SidangRepository sidangRepository;

    public BAP getBAPBySidangId(Integer sidangId) {
        Optional<Sidang> sidang = sidangRepository.findById(sidangId);
        if (sidang.isPresent()) {
            return bapRepository.findBySidang(sidang.get())
                    .orElseGet(() -> createNewBAP(sidang.get()));
        }
        throw new RuntimeException("Sidang not found");
    }

    private BAP createNewBAP(Sidang sidang) {
        BAP bap = new BAP();
        bap.setSidang(sidang);
        bap.setCreatedAt(LocalDateTime.now());
        return bapRepository.save(bap);
    }

    public void uploadSignedBAP(Integer sidangId, MultipartFile file, String userType) {
        BAP bap = getBAPBySidangId(sidangId);
        try {
            // Store the file
            // TODO: Implement actual file storage logic here
            // For now, just update the approval status
            bap.getApprovedBy().add(userType);
            bapRepository.save(bap);
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload BAP document", e);
        }
    }

    public void approveBAP(Integer sidangId, String userType) {
        BAP bap = getBAPBySidangId(sidangId);
        bap.getApprovedBy().add(userType);
        bapRepository.save(bap);
    }

    public BAP approveBap(Long bapId, Long dosenId) {
        Optional<BAP> bapOpt = bapRepository.findById(bapId);
        if (!bapOpt.isPresent()) {
            throw new RuntimeException("BAP not found");
        }

        BAP bap = bapOpt.get();
        bap.getApprovedBy().add(dosenId.toString());
        return bapRepository.save(bap);
    }

    public ResponseEntity<Resource> downloadBap(Long bapId) {
        Optional<BAP> bapOpt = bapRepository.findById(bapId);
        if (!bapOpt.isPresent()) {
            throw new RuntimeException("BAP not found");
        }

        // TODO: Implement actual file download logic
        // For now, return a placeholder response
        return ResponseEntity.notFound().build();
    }
}
