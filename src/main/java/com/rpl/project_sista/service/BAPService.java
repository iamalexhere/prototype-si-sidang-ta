package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.*;
import com.rpl.project_sista.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BAPService {

    @Autowired
    private BAPRepository bapRepository;

    @Autowired
    private SidangRepository sidangRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PersetujuanBAPRepository persetujuanBAPRepository;

    @Value("${app.upload.dir:${user.home}/bap-uploads}")
    private String uploadDir;

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
            // Create the upload directory if it doesn't exist
            File directory = new File(uploadDir + "/" + sidangId);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Save the file with a unique name based on userType
            String fileName = userType.toLowerCase() + "_signed_bap" + getFileExtension(file.getOriginalFilename());
            Path targetPath = Paths.get(directory.getAbsolutePath(), fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            // Get or create PersetujuanBAP
            Users user = getCurrentUser(); 
            PersetujuanBAP persetujuan = persetujuanBAPRepository.findByBapAndUser(bap, user)
                    .orElseGet(() -> {
                        PersetujuanBAP newPersetujuan = new PersetujuanBAP();
                        newPersetujuan.setBap(bap);
                        newPersetujuan.setUser(user);
                        return newPersetujuan;
                    });

            // Update approval status
            persetujuan.setIsApproved(true);
            persetujuan.setApprovedAt(LocalDateTime.now());
            persetujuanBAPRepository.save(persetujuan);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload BAP document", e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) {
            return ".pdf";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public File getSignedBAP(Integer sidangId, String userType) {
        String fileName = userType.toLowerCase() + "_signed_bap.pdf";
        File file = new File(uploadDir + "/" + sidangId + "/" + fileName);
        if (!file.exists()) {
            throw new RuntimeException("BAP file not found");
        }
        return file;
    }

    public void approveBAP(Integer sidangId, String userType) {
        BAP bap = getBAPBySidangId(sidangId);
        Users user = getCurrentUser(); 
        
        PersetujuanBAP persetujuan = persetujuanBAPRepository.findByBapAndUser(bap, user)
                .orElseGet(() -> {
                    PersetujuanBAP newPersetujuan = new PersetujuanBAP();
                    newPersetujuan.setBap(bap);
                    newPersetujuan.setUser(user);
                    return newPersetujuan;
                });

        persetujuan.setIsApproved(true);
        persetujuan.setApprovedAt(LocalDateTime.now());
        persetujuanBAPRepository.save(persetujuan);
    }

    // Get current user from session
    private Users getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UserDetails) {
            String username = ((UserDetails) auth.getPrincipal()).getUsername();
            return usersRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new RuntimeException("User not authenticated");
    }
}
