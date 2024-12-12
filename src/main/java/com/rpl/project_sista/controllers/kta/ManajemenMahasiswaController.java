// src/main/java/com/rpl/project_sista/controllers/kta/ManajemenMahasiswaController.java
package com.rpl.project_sista.controllers.kta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.service.MahasiswaService;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/kta/mahasiswa")
public class ManajemenMahasiswaController {

    @Autowired
    private MahasiswaService mahasiswaService;

    @GetMapping
    public String listMahasiswa(Model model) {
        model.addAttribute("pageTitle", "Manajemen Mahasiswa");
        List<Mahasiswa> mahasiswaList = mahasiswaService.findAllMahasiswa();
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
        Optional<Mahasiswa> mahasiswa = mahasiswaService.findMahasiswaById(id);

       if(mahasiswa.isPresent()){
         model.addAttribute("mahasiswa", mahasiswa.get());
         model.addAttribute("statusTaList", StatusTA.values());
         return "kta/mahasiswa/ubah-peserta";
       }else{
         return "redirect:/kta/mahasiswa";
       }
        
    }

    @PostMapping("/tambah")
    public String addMahasiswa(@ModelAttribute Mahasiswa mahasiswa, RedirectAttributes redirectAttributes) {
        try {
           mahasiswa.setStatusTa(StatusTA.draft);
           mahasiswaService.saveMahasiswa(mahasiswa);
           redirectAttributes.addFlashAttribute("successMessage", "Mahasiswa berhasil ditambahkan.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menambahkan mahasiswa.");
            return "redirect:/kta/mahasiswa/tambah";
        }
          return "redirect:/kta/mahasiswa";
    }


   @PostMapping("/ubah")
   public String updateMahasiswa(@ModelAttribute Mahasiswa mahasiswa, RedirectAttributes redirectAttributes) {
       try {
          mahasiswaService.saveMahasiswa(mahasiswa);
          redirectAttributes.addFlashAttribute("successMessage", "Mahasiswa berhasil diubah.");
       } catch (Exception e) {
           redirectAttributes.addFlashAttribute("errorMessage", "Gagal mengubah mahasiswa");
       }
         return "redirect:/kta/mahasiswa";
   }


    @PostMapping("/hapus")
    public String deleteMahasiswa(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
          mahasiswaService.deleteMahasiswa(id);
           redirectAttributes.addFlashAttribute("successMessage", "Mahasiswa berhasil dihapus.");
        } catch (Exception e) {
          redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus mahasiswa.");
        }
        return "redirect:/kta/mahasiswa";
    }
}