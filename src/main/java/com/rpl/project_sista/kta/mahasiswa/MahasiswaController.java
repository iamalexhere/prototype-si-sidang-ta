package com.rpl.project_sista.kta.mahasiswa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MahasiswaController {
    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @GetMapping("/mahasiswa")
    public String listMahasiswa(Model model) {
        List<Mahasiswa> mahasiswaList = mahasiswaRepository.findAll();
        model.addAttribute("mahasiswaList", mahasiswaList);
        return "mahasiswa/index"; // Thymeleaf template for listing mahasiswa
    }

    @GetMapping("/mahasiswa/tambah")
    public String showAddMahasiswaForm() {
        return "mahasiswa/tambah-mahasiswa"; // Thymeleaf template for adding mahasiswa
    }

    @PostMapping("/mahasiswa/tambah")
    public String addMahasiswa(@RequestParam String npm, @RequestParam String nama) {
        Mahasiswa newMahasiswa = new Mahasiswa(null, npm, nama, null, null);
        mahasiswaRepository.save(newMahasiswa);
        return "redirect:/mahasiswa"; // Redirect to the list after adding
    }

    @GetMapping("/mahasiswa/ubah")
    public String showUpdateMahasiswaForm(@RequestParam Integer id, Model model) {
        Mahasiswa mahasiswa = mahasiswaRepository.findById(id).orElseThrow(() -> new RuntimeException("Mahasiswa not found"));
        model.addAttribute("mahasiswa", mahasiswa);
        return "mahasiswa/ubah-mahasiswa"; // Thymeleaf template for editing mahasiswa
    }

    @PostMapping("/mahasiswa/ubah")
    public String updateMahasiswa(@RequestParam Integer id, @RequestParam String npm, @RequestParam String nama) {
        Mahasiswa mahasiswa = new Mahasiswa(id, npm, nama, null, null);
        mahasiswaRepository.save(mahasiswa);
        return "redirect:/mahasiswa"; // Redirect to the list after updating
    }

    @PostMapping("/mahasiswa/hapus")
    public String deleteMahasiswa(@RequestParam Integer id) {
        mahasiswaRepository.findById(id).ifPresent(mahasiswa -> mahasiswaRepository.save(new Mahasiswa(id, mahasiswa.getNpm(), mahasiswa.getNama(), null, null))); // Logic to delete mahasiswa
        return "redirect:/mahasiswa"; // Redirect to the list after deleting
    }
}
