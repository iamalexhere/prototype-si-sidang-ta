package com.rpl.project_sista.service;

import com.rpl.project_sista.dto.DosenCreateRequest;
import com.rpl.project_sista.entity.Dosen;
import com.rpl.project_sista.entity.User;
import com.rpl.project_sista.entity.UserRole;
import com.rpl.project_sista.repository.DosenRepository;
import com.rpl.project_sista.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class DosenService {

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Dosen> getAllDosen() {
        return dosenRepository.findAll();
    }

    @Transactional
    public Dosen createDosen(DosenCreateRequest request) {
        // Validasi NIP unik
        if (dosenRepository.existsByNip(request.getNip())) {
            throw new RuntimeException("NIP sudah terdaftar");
        }

        // Create user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.dosen);
        
        user = userRepository.save(user);

        // Create dosen
        Dosen dosen = new Dosen();
        dosen.setUser(user);
        dosen.setNip(request.getNip());
        dosen.setNama(request.getNama());

        return dosenRepository.save(dosen);
    }

    public Dosen getDosenById(Long id) {
        return dosenRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Dosen tidak ditemukan"));
    }

    public Dosen getDosenByNip(String nip) {
        return dosenRepository.findByNip(nip)
            .orElseThrow(() -> new RuntimeException("Dosen tidak ditemukan"));
    }
}
