package com.rpl.project_sista.controllers.kta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.repository.MahasiswaRepository;

import java.util.List;

@Controller
@RequestMapping("/kta")
public class ManajemenMahasiswaController {
    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @GetMapping("/mahasiswa")
    public String listMahasiswa(Model model) {
        List<Mahasiswa> mahasiswaList = mahasiswaRepository.findAll();
        model.addAttribute("mahasiswaList", mahasiswaList);
        model.addAttribute("title", "Data Mahasiswa");
        return "kta/mahasiswa/index";
    }

    @GetMapping("/mahasiswa/tambah")
    public String showAddMahasiswaForm(Model model) {
        model.addAttribute("mahasiswa", new Mahasiswa());
        model.addAttribute("title", "Tambah Mahasiswa");
        return "kta/mahasiswa/tambah-peserta";
    }

    @PostMapping("/mahasiswa/tambah")
    public String addMahasiswa(@RequestParam String npm, @RequestParam String nama,
                             @RequestParam String username, @RequestParam String email,
                             @RequestParam String password) {
        Mahasiswa newMahasiswa = new Mahasiswa(npm, nama, StatusTA.draft,
                                             username, email, password);
        mahasiswaRepository.save(newMahasiswa);
        return "redirect:/mahasiswa";
    }

    @GetMapping("/mahasiswa/ubah")
    public String showUpdateMahasiswaForm(@RequestParam Integer id, Model model) {
        Mahasiswa mahasiswa = mahasiswaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mahasiswa not found"));
        model.addAttribute("mahasiswa", mahasiswa);
        model.addAttribute("title", "Ubah Data Mahasiswa");
        model.addAttribute("statusTaList", StatusTA.values());
        return "kta/mahasiswa/ubah-peserta";
    }

    @PostMapping("/mahasiswa/ubah")
    public String updateMahasiswa(@RequestParam Integer id, @RequestParam String npm,
                                @RequestParam String nama, @RequestParam String username,
                                @RequestParam String email, @RequestParam(required = false) String password,
                                @RequestParam StatusTA statusTa) {
        Mahasiswa existingMahasiswa = mahasiswaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mahasiswa not found"));
        existingMahasiswa.setNpm(npm);
        existingMahasiswa.setNama(nama);
        existingMahasiswa.setUsername(username);
        existingMahasiswa.setEmail(email);
        existingMahasiswa.setStatusTa(statusTa);
        if (password != null && !password.isEmpty()) {
            existingMahasiswa.setPasswordHash(password);
        }
        mahasiswaRepository.save(existingMahasiswa);
        return "redirect:/mahasiswa";
    }

    @PostMapping("/mahasiswa/hapus")
    public String deleteMahasiswa(@RequestParam Integer id) {
        mahasiswaRepository.deleteById(id);
        return "redirect:/mahasiswa";
    }
}
