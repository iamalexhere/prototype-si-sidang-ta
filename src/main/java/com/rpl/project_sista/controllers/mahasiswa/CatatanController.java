package com.rpl.project_sista.controllers.mahasiswa;

import com.rpl.project_sista.jdbcrepository.JdbcCatatanRepository;
import com.rpl.project_sista.model.entity.CatatanRevisi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/mahasiswa/catatan")
public class CatatanController {

    @Autowired
    private JdbcCatatanRepository catatanRepository;

    // Serve the catatan-sidang.html page
    @GetMapping
    public String showCatatanPage() {
        return "mahasiswa/catatan-sidang"; // Pastikan file ini ada di src/main/resources/templates
    }

    // Get all catatan
    @GetMapping("/all")
    @ResponseBody
    public List<CatatanRevisi> getAllCatatan() {
        return catatanRepository.findAll();
    }

    // Get catatan by sidang ID
    @GetMapping("/sidang/{sidangId}")
    @ResponseBody
    public List<CatatanRevisi> getCatatanBySidangId(@PathVariable Long sidangId) {
        return catatanRepository.findBySidangId(sidangId);
    }

    // Get catatan by dosen ID
    @GetMapping("/dosen/{dosenId}")
    @ResponseBody
    public List<CatatanRevisi> getCatatanByDosenId(@PathVariable Long dosenId) {
        return catatanRepository.findByDosenId(dosenId);
    }

    // Get catatan by ID
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<CatatanRevisi> getCatatanById(@PathVariable Long id) {
        Optional<CatatanRevisi> catatan = catatanRepository.findById(id);
        return catatan.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Create a new catatan
    @PostMapping
    @ResponseBody
    public CatatanRevisi createCatatan(@RequestBody CatatanRevisi catatan) {
        return catatanRepository.save(catatan);
    }

    // Delete a catatan by ID
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteCatatan(@PathVariable Long id) {
        Optional<CatatanRevisi> catatan = catatanRepository.findById(id);
        if (catatan.isPresent()) {
            catatanRepository.delete(catatan.get());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
