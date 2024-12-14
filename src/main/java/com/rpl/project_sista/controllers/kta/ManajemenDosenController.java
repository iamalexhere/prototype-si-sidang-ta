package com.rpl.project_sista.controllers.kta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.repository.DosenRepository;
import com.rpl.project_sista.service.DosenService;

import java.util.List;

@Controller
@RequestMapping("/kta/dosen")
public class ManajemenDosenController {

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private DosenService dosenService;

    @GetMapping
    public String listDosen(Model model) {
        model.addAttribute("pageTitle", "Manajemen Dosen");
        List<Dosen> dosenList = dosenRepository.findAll();
        model.addAttribute("dosenList", dosenList);
        return "kta/dosen/index";
    }

    @GetMapping("/tambah")
    public String showAddDosenForm(Model model) {
        model.addAttribute("pageTitle", "Tambah Dosen");
        model.addAttribute("dosen", new Dosen());
        return "kta/dosen/tambah-dosen";
    }

    @GetMapping("/ubah")
    public String showUpdateDosenForm(@RequestParam Integer id, Model model) {
        model.addAttribute("pageTitle", "Ubah Data Dosen");
        Dosen dosen = dosenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dosen tidak ditemukan"));
        model.addAttribute("dosen", dosen);
        return "kta/dosen/ubah-dosen";
    }

    @PostMapping("/tambah")
    public String addDosen(@RequestParam String nip, @RequestParam String nama,
                         @RequestParam String username, @RequestParam String email,
                         @RequestParam String password, RedirectAttributes redirectAttributes) {
        try {
            Dosen newDosen = new Dosen(nip, nama, username, email, password);
            dosenRepository.save(newDosen);
            redirectAttributes.addFlashAttribute("successMessage", "Dosen berhasil ditambahkan");
            return "redirect:/kta/dosen";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menambahkan dosen: " + e.getMessage());
            return "redirect:/kta/dosen";
        }
    }

    @PostMapping("/ubah")
    public String updateDosen(@RequestParam Integer id, @RequestParam String nip,
                            @RequestParam String nama, @RequestParam String username,
                            @RequestParam String email, @RequestParam(required = false) String password,
                            RedirectAttributes redirectAttributes) {
        try {
            Dosen existingDosen = dosenRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Dosen tidak ditemukan"));
            existingDosen.setNip(nip);
            existingDosen.setNama(nama);
            existingDosen.setUsername(username);
            existingDosen.setEmail(email);
            if (password != null && !password.isEmpty()) {
                existingDosen.setPasswordHash(password);
            }
            dosenRepository.save(existingDosen);
            redirectAttributes.addFlashAttribute("successMessage", "Data dosen berhasil diperbarui");
            return "redirect:/kta/dosen";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal memperbarui data dosen: " + e.getMessage());
            return "redirect:/kta/dosen";
        }
    }

    @PostMapping("/hapus")
    public String deleteDosen(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            dosenService.deleteDosen(id);
            redirectAttributes.addFlashAttribute("successMessage", "Dosen berhasil dihapus");
            return "redirect:/kta/dosen";
        } catch (DataIntegrityViolationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/kta/dosen";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus dosen: " + e.getMessage());
            return "redirect:/kta/dosen";
        }
    }
}
