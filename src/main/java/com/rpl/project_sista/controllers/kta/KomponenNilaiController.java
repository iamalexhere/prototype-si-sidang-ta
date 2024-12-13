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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        model.addAttribute("pageTitle", "Daftar Komponen Nilai");
        model.addAttribute("komponenList", komponenNilaiRepository.findAll());
        return "kta/komponen-nilai/list-komponen-nilai";
    }

    @GetMapping("/tambah")
    public String showAddForm(Model model) {
        model.addAttribute("pageTitle", "Tambah Komponen Nilai");
        model.addAttribute("komponenNilai", new KomponenNilai());
        model.addAttribute("semesterList", semesterRepository.findAll());
        model.addAttribute("tipePenilaiList", TipePenilai.values());
        
        // Add predefined component suggestions
        model.addAttribute("pengujiComponents", getPengujiComponents());
        model.addAttribute("pembimbingComponents", getPembimbingComponents());
        
        return "kta/komponen-nilai/manajemen-komponen-nilai";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("pageTitle", "Edit Komponen Nilai");
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

                // Calculate total weight for this type of evaluator in the semester
                float totalWeight = komponenNilaiRepository.findBySemesterAndTipePenilai(semester.get(), komponenNilai.getTipePenilai())
                    .stream()
                    .filter(k -> !k.getKomponenId().equals(komponenNilai.getKomponenId())) // Exclude current component if editing
                    .map(KomponenNilai::getBobot)
                    .reduce(0f, Float::sum);

                // Add current component weight
                totalWeight += komponenNilai.getBobot();

                // Validate total weight
                if (totalWeight > 100) {
                    redirectAttributes.addFlashAttribute("errorMessage", 
                        "Total bobot untuk " + komponenNilai.getTipePenilai() + " tidak boleh melebihi 100%. " +
                        "Sisa bobot yang tersedia: " + (100 - (totalWeight - komponenNilai.getBobot())) + "%");
                    return "redirect:/kta/komponen-nilai";
                }

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

    private List<Map<String, Object>> getPengujiComponents() {
        List<Map<String, Object>> components = new ArrayList<>();
        components.add(createComponent("Tata tulis laporan", 15.0f));
        components.add(createComponent("Kelengkapan materi", 20.0f));
        components.add(createComponent("Pencapaian tujuan", 25.0f));
        components.add(createComponent("Penguasaan materi", 30.0f));
        components.add(createComponent("Presentasi", 10.0f));
        return components;
    }

    private List<Map<String, Object>> getPembimbingComponents() {
        List<Map<String, Object>> components = new ArrayList<>();
        components.add(createComponent("Tata tulis laporan", 20.0f));
        components.add(createComponent("Kelengkapan materi", 20.0f));
        components.add(createComponent("Proses bimbingan", 30.0f));
        components.add(createComponent("Penguasaan materi", 30.0f));
        return components;
    }

    private Map<String, Object> createComponent(String name, Float weight) {
        Map<String, Object> component = new HashMap<>();
        component.put("name", name);
        component.put("weight", weight);
        return component;
    }
}
