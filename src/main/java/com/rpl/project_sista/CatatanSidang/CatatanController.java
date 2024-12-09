package com.rpl.project_sista.CatatanSidang;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catatan")
public class CatatanController {

    @Autowired
    private CatatanRepository catatanRepository;

    // Get all catatan
    @GetMapping
    public List<Catatan> getAllCatatan() {
        return catatanRepository.findAll();
    }

    // Get catatan by sidang ID
    @GetMapping("/sidang/{sidangId}")
    public List<Catatan> getCatatanBySidangId(@PathVariable Integer sidangId) {
        return catatanRepository.findBySidangId(sidangId);
    }

    // Get catatan by dosen ID
    @GetMapping("/dosen/{dosenId}")
    public List<Catatan> getCatatanByDosenId(@PathVariable Integer dosenId) {
        return catatanRepository.findByDosenId(dosenId);
    }

    // Create a new catatan
    @PostMapping
    public Catatan createCatatan(@RequestBody Catatan catatan) {
        return catatanRepository.save(catatan);
    }

    // Delete a catatan by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCatatan(@PathVariable Long id) {
        return catatanRepository.findById(id)
                .map(catatan -> {
                    catatanRepository.delete(catatan);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

