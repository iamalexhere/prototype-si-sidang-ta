package com.rpl.project_sista.controllers.kta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rpl.project_sista.model.entity.*;
import com.rpl.project_sista.repository.*;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.model.enums.JenisTA;

import java.time.LocalDateTime;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/kta/tugas-akhir")
public class ManajemenTugasAkhirController {

    private final Logger logger = LoggerFactory.getLogger(ManajemenTugasAkhirController.class);

    @Autowired
    private TugasAkhirRepository tugasAkhirRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @Autowired
    private SemesterRepository semesterRepository;

    @GetMapping
    public String listTugasAkhir(Model model) {
        List<TugasAkhir> tugasAkhirList = tugasAkhirRepository.findAll();
        model.addAttribute("tugasAkhirList", tugasAkhirList);
        return "kta/tugas-akhir/list-tugas-akhir";
    }

    @GetMapping("/tambah")
    public String showAddForm(Model model) {
        model.addAttribute("tugasAkhir", new TugasAkhir());
        model.addAttribute("mahasiswaList", mahasiswaRepository.findAll());
        model.addAttribute("dosenList", dosenRepository.findAll());
        model.addAttribute("semesterList", semesterRepository.findAll());
        model.addAttribute("jenisTAList", JenisTA.values());
        model.addAttribute("statusTAList", StatusTA.values());
        return "kta/tugas-akhir/manajemen-tugas-akhir";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        tugasAkhirRepository.findById(id.intValue()).ifPresent(tugasAkhir -> {
            model.addAttribute("tugasAkhir", tugasAkhir);
            model.addAttribute("mahasiswaList", mahasiswaRepository.findAll());
            model.addAttribute("dosenList", dosenRepository.findAll());
            model.addAttribute("semesterList", semesterRepository.findAll());
            model.addAttribute("jenisTAList", JenisTA.values());
            model.addAttribute("statusTAList", StatusTA.values());
        });
        return "kta/tugas-akhir/manajemen-tugas-akhir";
    }

    @PostMapping("/save")
    public String saveTugasAkhir(@ModelAttribute TugasAkhir tugasAkhir, 
                            @RequestParam(required = false) List<Integer> pembimbingIds,
                            RedirectAttributes redirectAttributes) {
        try {
            // Set pembimbing
            if (pembimbingIds != null && !pembimbingIds.isEmpty()) {
                Set<Dosen> pembimbing = new HashSet<>();
                for (Integer dosenId : pembimbingIds) {
                    dosenRepository.findById(dosenId).ifPresent(pembimbing::add);
                }
                tugasAkhir.setPembimbing(pembimbing);
            }

            // Save Tugas Akhir
            tugasAkhir = tugasAkhirRepository.save(tugasAkhir);

            redirectAttributes.addFlashAttribute("successMessage", 
                "Tugas Akhir berhasil " + (tugasAkhir.getTaId() != null ? "diperbarui" : "ditambahkan"));
            return "redirect:/kta/tugas-akhir";
        } catch (Exception e) {
            logger.error("Error saving Tugas Akhir: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
            return "redirect:/kta/tugas-akhir/tambah";
        }
    }

    @GetMapping("/detail/{id}")
    public String showTugasAkhirDetail(@PathVariable Long id, Model model) {
        tugasAkhirRepository.findById(id.intValue()).ifPresentOrElse(
            tugasAkhir -> {
                logger.info("Tugas Akhir found: {}", tugasAkhir);
                model.addAttribute("tugasAkhir", tugasAkhir);
                model.addAttribute("pembimbingList", tugasAkhir.getPembimbing());
            },
            () -> {
                logger.error("Tugas Akhir not found with ID: {}", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tugas Akhir not found");
            }
        );
        return "kta/tugas-akhir/detail-tugas-akhir";
    }
}
