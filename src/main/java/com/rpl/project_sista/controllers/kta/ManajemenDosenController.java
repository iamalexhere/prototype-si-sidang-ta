package com.rpl.project_sista.controllers.kta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.repository.DosenRepository;

import java.util.List;

@Controller
@RequestMapping("/kta/dosen")
public class ManajemenDosenController {
    @Autowired
    private DosenRepository dosenRepository;

    @GetMapping
    public String listDosen(Model model) {
        List<Dosen> dosenList = dosenRepository.findAll();
        model.addAttribute("dosenList", dosenList);
        return "kta/dosen/index";
    }

    @GetMapping("/tambah")
    public String showAddDosenForm(Model model) {
        model.addAttribute("dosen", new Dosen());
        model.addAttribute("title", "Tambah Dosen");
        return "kta/dosen/tambah-dosen";
    }

    @PostMapping("/tambah")
    public String addDosen(@RequestParam String nip, @RequestParam String nama,
                          @RequestParam String username, @RequestParam String email,
                          @RequestParam String password) {
        Dosen newDosen = new Dosen(nip, nama, username, email, password);
        dosenRepository.save(newDosen);
        return "redirect:/kta/dosen";
    }

    @GetMapping("/ubah")
    public String showUpdateDosenForm(@RequestParam Integer id, Model model) {
        Dosen dosen = dosenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dosen not found"));
        model.addAttribute("dosen", dosen);
        model.addAttribute("title", "Ubah Data Dosen");
        return "kta/dosen/ubah-dosen";
    }

    @PostMapping("/ubah")
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
        return "redirect:/kta/dosen";
    }

    @PostMapping("/hapus")
    public String deleteDosen(@RequestParam Integer id) {
        dosenRepository.deleteById(id);
        return "redirect:/kta/dosen";
    }
}
