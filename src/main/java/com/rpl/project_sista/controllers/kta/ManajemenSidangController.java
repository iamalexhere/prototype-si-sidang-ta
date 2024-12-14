package com.rpl.project_sista.controllers.kta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rpl.project_sista.model.entity.*;
import com.rpl.project_sista.repository.*;
import com.rpl.project_sista.model.enums.StatusSidang;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.rpl.project_sista.model.entity.Dosen;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/kta/sidang")
public class ManajemenSidangController {

    private static final Logger logger = LoggerFactory.getLogger(ManajemenSidangController.class);

    @Autowired
    private SidangRepository sidangRepository;

    @Autowired
    private TugasAkhirRepository tugasAkhirRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @GetMapping
    public String listSidang(Model model) {
        model.addAttribute("pageTitle", "Manajemen Sidang");
        List<Sidang> sidangList = sidangRepository.findAll();
        model.addAttribute("sidangList", sidangList);
        return "kta/sidang/list-sidang";
    }

    @GetMapping("/tambah")
    public String showAddForm(Model model) {
        model.addAttribute("pageTitle", "Tambah Jadwal Sidang");
        model.addAttribute("sidangForm", new SidangForm());
        model.addAttribute("tugasAkhirList", tugasAkhirRepository.findAll());
        model.addAttribute("dosenList", dosenRepository.findAll());
        return "kta/sidang/manajamen-sidang";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        model.addAttribute("pageTitle", "Edit Jadwal Sidang");
        
        sidangRepository.findById(id).ifPresent(sidang -> {
            SidangForm form = new SidangForm();
            form.setSidangId(sidang.getSidangId().intValue());
            form.setTugasAkhirId(sidang.getTugasAkhir().getTaId().intValue());
            form.setRuangan(sidang.getRuangan());
            form.setJadwalTanggal(sidang.getJadwal().toLocalDate());
            form.setJadwalWaktu(sidang.getJadwal().toLocalTime());
            
            // Set penguji IDs if available
            if (sidang.getPenguji() != null && sidang.getPenguji().size() >= 2) {
                Object[] pengujiArray = sidang.getPenguji().toArray();
                form.setPenguji1Id(((Dosen)pengujiArray[0]).getDosenId());
                form.setPenguji2Id(((Dosen)pengujiArray[1]).getDosenId());
            }
            
            model.addAttribute("sidangForm", form);
            model.addAttribute("tugasAkhirList", tugasAkhirRepository.findAll());
            model.addAttribute("dosenList", dosenRepository.findAll());
        });
        
        return "kta/sidang/manajamen-sidang";
    }

    @GetMapping("/detail/{id}")
    public String showSidangDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("pageTitle", "Detail Sidang");
        sidangRepository.findById(id).ifPresentOrElse(
            sidang -> {
                logger.info("Sidang found: {}", sidang);
                logger.info("Tugas Akhir: {}", sidang.getTugasAkhir());
                logger.info("Pembimbing: {}", sidang.getTugasAkhir().getPembimbing());
                logger.info("Penguji: {}", sidang.getPenguji());
                
                model.addAttribute("sidang", sidang);
                model.addAttribute("tugasAkhir", sidang.getTugasAkhir());
                
                // Convert penguji set to ordered list
                List<Dosen> pengujiList = new ArrayList<>(sidang.getPenguji());
                // Sort penguji if needed (they should already be ordered by peran_penguji from the repository)
                model.addAttribute("pengujiList", pengujiList);
            },
            () -> {
                logger.error("Sidang not found with ID: {}", id);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sidang not found");
            }
        );
        return "kta/sidang/detail-sidang";
    }

    @PostMapping("/save")
    public String saveSidang(@ModelAttribute SidangForm form, RedirectAttributes redirectAttributes) {
        try {
            Sidang sidang = new Sidang();
            if (form.getSidangId() != null) {
                sidang = sidangRepository.findById(form.getSidangId()).orElse(new Sidang());
            }

            // Set basic info
            TugasAkhir ta = tugasAkhirRepository.findById(form.getTugasAkhirId()).orElseThrow();
            sidang.setTugasAkhir(ta);
            sidang.setRuangan(form.getRuangan());

            // Combine date and time
            LocalDateTime jadwal = LocalDateTime.of(form.getJadwalTanggal(), form.getJadwalWaktu());
            sidang.setJadwal(jadwal);

            // Set status
            if (sidang.getSidangId() == null) {
                sidang.setStatusSidang(StatusSidang.terjadwal);
            }

            // Set penguji IDs directly
            sidang.setPenguji1(form.getPenguji1Id());
            sidang.setPenguji2(form.getPenguji2Id());

            // Save sidang with penguji
            sidang = sidangRepository.save(sidang);

            redirectAttributes.addFlashAttribute("successMessage", 
                "Sidang berhasil " + (form.getSidangId() != null ? "diperbarui" : "dijadwalkan"));
            return "redirect:/kta/sidang";

        } catch (Exception e) {
            logger.error("Error saving sidang: ", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Terjadi kesalahan: " + e.getMessage());
            return "redirect:/kta/sidang/tambah";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteSidang(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            sidangRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Jadwal sidang berhasil dihapus");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus jadwal sidang");
        }
        return "redirect:/kta/sidang";
    }
}

class SidangForm {
    private Integer sidangId;
    private Integer tugasAkhirId;
    private LocalDate jadwalTanggal;
    private LocalTime jadwalWaktu;
    private String ruangan;
    private Integer penguji1Id;
    private Integer penguji2Id;

    // Getters and Setters
    public Integer getSidangId() { return sidangId; }
    public void setSidangId(Integer sidangId) { this.sidangId = sidangId; }
    
    public Integer getTugasAkhirId() { return tugasAkhirId; }
    public void setTugasAkhirId(Integer tugasAkhirId) { this.tugasAkhirId = tugasAkhirId; }
    
    public LocalDate getJadwalTanggal() { return jadwalTanggal; }
    public void setJadwalTanggal(LocalDate jadwalTanggal) { this.jadwalTanggal = jadwalTanggal; }
    
    public LocalTime getJadwalWaktu() { return jadwalWaktu; }
    public void setJadwalWaktu(LocalTime jadwalWaktu) { this.jadwalWaktu = jadwalWaktu; }
    
    public String getRuangan() { return ruangan; }
    public void setRuangan(String ruangan) { this.ruangan = ruangan; }
    
    public Integer getPenguji1Id() { return penguji1Id; }
    public void setPenguji1Id(Integer penguji1Id) { this.penguji1Id = penguji1Id; }
    
    public Integer getPenguji2Id() { return penguji2Id; }
    public void setPenguji2Id(Integer penguji2Id) { this.penguji2Id = penguji2Id; }
}
