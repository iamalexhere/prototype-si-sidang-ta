package com.rpl.project_sista.kta.dosen;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class DosenController {
    @Autowired
    private DosenRepository dosenRepository;

    @GetMapping("/dosen")
    public String listDosen(Model model) {
        List<Dosen> dosenList = dosenRepository.findAll();
        model.addAttribute("dosenList", dosenList);
        return "kta/dosen/index"; // Thymeleaf template for listing dosen
    }

    @GetMapping("/dosen/tambah")
    public String showAddDosenForm() {
        return "kta/dosen/tambah-dosen"; // Thymeleaf template for adding dosen
    }

    @PostMapping("/dosen/tambah")
    public String addDosen(@RequestParam String nip, @RequestParam String nama, @RequestParam(required = false) Integer userId) {
        Dosen newDosen = new Dosen(null, nip, nama, userId, null); // Include userId
        dosenRepository.save(newDosen);
        return "redirect:/dosen"; // Redirect to the list after adding
    }

    @GetMapping("/dosen/ubah")
    public String showUpdateDosenForm(@RequestParam Integer id, Model model) {
        Dosen dosen = dosenRepository.findById(id).orElseThrow(() -> new RuntimeException("Dosen not found"));
        model.addAttribute("dosen", dosen);
        return "kta/dosen/ubah-dosen"; // Thymeleaf template for editing dosen
    }

    @PostMapping("/dosen/ubah")
    public String updateDosen(@RequestParam Integer id, @RequestParam String nip, @RequestParam String nama, @RequestParam(required = false) Integer userId) {
        Dosen existingDosen = dosenRepository.findById(id).orElseThrow(() -> new RuntimeException("Dosen not found"));
        existingDosen.setNip(nip);
        existingDosen.setNama(nama);
        if (userId != null) {
            existingDosen.setUserId(userId);
        }
        dosenRepository.save(existingDosen);
        return "redirect:/dosen"; // Redirect to the list after updating
    }

    @PostMapping("/dosen/hapus")
    public String deleteDosen(@RequestParam Integer id) {
        dosenRepository.findById(id).ifPresent(dosen -> dosenRepository.save(new Dosen(id, dosen.getNip(), dosen.getNama(), dosen.getUserId(), null))); // Ensure userId is handled correctly
        dosenRepository.deleteById(id); // Call deleteById method to remove the dosen
        return "redirect:/dosen"; // Redirect to the list after deleting
    }
}
