package com.rpl.project_sista.service;

import com.rpl.project_sista.dto.MahasiswaCreateRequest;
import com.rpl.project_sista.entity.Mahasiswa;
import com.rpl.project_sista.entity.User;
import com.rpl.project_sista.entity.UserRole;
import com.rpl.project_sista.repository.MahasiswaRepository;
import com.rpl.project_sista.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MahasiswaService {
    
    @Autowired
    private MahasiswaRepository mahasiswaRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(MahasiswaService.class);

    public List<Mahasiswa> getAllMahasiswa() {
        return mahasiswaRepository.findAll();
    }

    public Mahasiswa getMahasiswaById(Long id) {
        return mahasiswaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan"));
    }

    @Transactional
    public Mahasiswa createMahasiswa(MahasiswaCreateRequest request) {
        // Validasi data
        if (mahasiswaRepository.existsByNpm(request.getNpm())) {
            throw new RuntimeException("NPM sudah terdaftar");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username sudah digunakan");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email sudah digunakan");
        }

        // Buat user baru
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        logger.info("Creating user with role: {}", UserRole.mahasiswa.name());
        user.setRole(UserRole.valueOf(UserRole.mahasiswa.name()));
        user.setIsActive(true);
        
        user = userRepository.save(user);

        // Buat mahasiswa baru
        Mahasiswa mahasiswa = new Mahasiswa();
        mahasiswa.setNpm(request.getNpm());
        mahasiswa.setNama(request.getNama());
        mahasiswa.setUser(user);
        
        return mahasiswaRepository.save(mahasiswa);
    }
}
