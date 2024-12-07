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
        return "kta/dosen/index";
    }

    @GetMapping("/dosen/tambah")
    public String showAddDosenForm(Model model) {
        model.addAttribute("dosen", new Dosen());
        model.addAttribute("title", "Tambah Dosen");
        return "kta/dosen/tambah-dosen";
    }

    @PostMapping("/dosen/tambah")
    public String addDosen(@RequestParam String nip, @RequestParam String nama,
                          @RequestParam String username, @RequestParam String email,
                          @RequestParam String password) {
        Dosen newDosen = new Dosen(nip, nama, username, email, password);
        dosenRepository.save(newDosen);
        return "redirect:/dosen";
    }

    @GetMapping("/dosen/ubah")
    public String showUpdateDosenForm(@RequestParam Integer id, Model model) {
        Dosen dosen = dosenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dosen not found"));
        model.addAttribute("dosen", dosen);
        model.addAttribute("title", "Ubah Data Dosen");
        return "kta/dosen/ubah-dosen";
    }

    @PostMapping("/dosen/ubah")
    public String updateDosen(@RequestParam Integer id, @RequestParam String nip,
                            @RequestParam String nama, @RequestParam String username,
                            @RequestParam String email, @RequestParam(required = false) String password) {
        Dosen existingDosen = dosenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dosen not found"));
        existingDosen.setNip(nip);
        existingDosen.setNama(nama);
        existingDosen.setUsername(username);
        existingDosen.setEmail(email);
        if (password != null && !password.isEmpty()) {
            existingDosen.setPasswordHash(password);
        }
        dosenRepository.save(existingDosen);
        return "redirect:/dosen";
    }

    @PostMapping("/dosen/hapus")
    public String deleteDosen(@RequestParam Integer id) {
        dosenRepository.deleteById(id);
        return "redirect:/dosen";
    }
}
