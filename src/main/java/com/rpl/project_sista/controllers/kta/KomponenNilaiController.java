package com.rpl.project_sista.controllers.kta;

import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.model.entity.Semester;
import com.rpl.project_sista.model.enums.TipePenilai;
import com.rpl.project_sista.repository.KomponenNilaiRepository;
import com.rpl.project_sista.repository.SemesterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/kta/komponen-nilai")
public class KomponenNilaiController {

    @Autowired
    private KomponenNilaiRepository komponenNilaiRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @GetMapping
    public String listKomponenNilai(Model model) {
        model.addAttribute("komponenList", komponenNilaiRepository.findAll());
        return "kta/komponen-nilai/list-komponen-nilai";
    }

    @GetMapping("/tambah")
    public String showAddForm(Model model) {
        model.addAttribute("komponenNilai", new KomponenNilai());
        model.addAttribute("semesterList", semesterRepository.findAll());
        model.addAttribute("tipePenilaiList", TipePenilai.values());
        return "kta/komponen-nilai/manajemen-komponen-nilai";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<KomponenNilai> komponenNilai = komponenNilaiRepository.findById(id);
        if (komponenNilai.isPresent()) {
            model.addAttribute("komponenNilai", komponenNilai.get());
            model.addAttribute("semesterList", semesterRepository.findAll());
            model.addAttribute("tipePenilaiList", TipePenilai.values());
            return "kta/komponen-nilai/manajemen-komponen-nilai";
        }
        return "redirect:/kta/komponen-nilai";
    }

    @PostMapping("/save")
    public String saveKomponenNilai(@ModelAttribute KomponenNilai komponenNilai, 
                                   @RequestParam Long semesterId,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Set the semester
            Optional<Semester> semester = semesterRepository.findById(semesterId);
            if (semester.isPresent()) {
                komponenNilai.setSemester(semester.get());
                komponenNilaiRepository.save(komponenNilai);
                redirectAttributes.addFlashAttribute("successMessage", 
                    "Komponen nilai berhasil " + (komponenNilai.getKomponenId() != null ? "diperbarui" : "ditambahkan"));
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Semester tidak ditemukan");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/kta/komponen-nilai";
    }

    @GetMapping("/delete/{id}")
    public String deleteKomponenNilai(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            komponenNilaiRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Komponen nilai berhasil dihapus");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error menghapus komponen nilai: " + e.getMessage());
        }
        return "redirect:/kta/komponen-nilai";
    }
}
