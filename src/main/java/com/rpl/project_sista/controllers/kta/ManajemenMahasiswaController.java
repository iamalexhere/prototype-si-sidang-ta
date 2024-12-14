package com.rpl.project_sista.controllers.kta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.repository.MahasiswaRepository;
import com.rpl.project_sista.service.MahasiswaService;

import java.util.List;

@Controller
@RequestMapping("/kta/mahasiswa")
public class ManajemenMahasiswaController {

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private MahasiswaService mahasiswaService;

    @GetMapping
    public String listMahasiswa(Model model) {
        model.addAttribute("pageTitle", "Manajemen Mahasiswa");
        List<Mahasiswa> mahasiswaList = mahasiswaRepository.findAll();
        model.addAttribute("mahasiswaList", mahasiswaList);
        return "kta/mahasiswa/index";
    }

    @GetMapping("/tambah")
    public String showAddMahasiswaForm(Model model) {
        model.addAttribute("pageTitle", "Tambah Mahasiswa");
        model.addAttribute("mahasiswa", new Mahasiswa());
        return "kta/mahasiswa/tambah-peserta";
    }

    @GetMapping("/ubah")
    public String showUpdateMahasiswaForm(@RequestParam Integer id, Model model) {
        model.addAttribute("pageTitle", "Ubah Data Mahasiswa");
        Mahasiswa mahasiswa = mahasiswaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mahasiswa not found"));
        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("statusTaList", StatusTA.values());
        return "kta/mahasiswa/ubah-peserta";
    }

    @PostMapping("/tambah")
    public String addMahasiswa(@RequestParam String npm, @RequestParam String nama,
                         @RequestParam String username, @RequestParam String email,
                         @RequestParam String password, RedirectAttributes redirectAttributes) {
        try {
            Mahasiswa newMahasiswa = new Mahasiswa(npm, nama, StatusTA.draft,
                                         username, email, password);
            mahasiswaRepository.save(newMahasiswa);
            redirectAttributes.addFlashAttribute("successMessage", "Mahasiswa berhasil ditambahkan");
            return "redirect:/kta/mahasiswa";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menambahkan mahasiswa: " + e.getMessage());
            return "redirect:/kta/mahasiswa";
        }
    }

    @PostMapping("/ubah")
    public String updateMahasiswa(@RequestParam Integer id, @RequestParam String npm,
                            @RequestParam String nama, @RequestParam String username,
                            @RequestParam String email, @RequestParam(required = false) String password,
                            @RequestParam StatusTA statusTa, RedirectAttributes redirectAttributes) {
        try {
            Mahasiswa existingMahasiswa = mahasiswaRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan"));
            existingMahasiswa.setNpm(npm);
            existingMahasiswa.setNama(nama);
            existingMahasiswa.setUsername(username);
            existingMahasiswa.setEmail(email);
            existingMahasiswa.setStatusTa(statusTa);
            if (password != null && !password.isEmpty()) {
                existingMahasiswa.setPasswordHash(password);
            }
            mahasiswaRepository.save(existingMahasiswa);
            redirectAttributes.addFlashAttribute("successMessage", "Data mahasiswa berhasil diperbarui");
            return "redirect:/kta/mahasiswa";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal memperbarui data mahasiswa: " + e.getMessage());
            return "redirect:/kta/mahasiswa";
        }
    }

    @PostMapping("/hapus")
    public String deleteMahasiswa(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            mahasiswaService.deleteMahasiswa(id);
            redirectAttributes.addFlashAttribute("successMessage", "Mahasiswa berhasil dihapus");
            return "redirect:/kta/mahasiswa";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus mahasiswa: " + e.getMessage());
            return "redirect:/kta/mahasiswa";
        }
    }
}
