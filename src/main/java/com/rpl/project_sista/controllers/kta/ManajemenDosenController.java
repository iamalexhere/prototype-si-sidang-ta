package com.rpl.project_sista.controllers.kta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.service.DosenService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/kta/dosen")
public class ManajemenDosenController {

    @Autowired
    private DosenService dosenService;

    @GetMapping
    public String listDosen(Model model) {
        model.addAttribute("pageTitle", "Manajemen Dosen");
        List < Dosen > dosenList = dosenService.findAllDosen();
        model.addAttribute("dosenList", dosenList);
        return "kta/dosen/index";
    }

    @GetMapping("/tambah")
    public String showAddDosenForm(Model model) {
        model.addAttribute("pageTitle", "Tambah Dosen");
        model.addAttribute("dosen", new Dosen());
        model.addAttribute("title", "Tambah Dosen");
        return "kta/dosen/tambah-dosen";
    }

    @GetMapping("/ubah")
    public String showUpdateDosenForm(@RequestParam Integer id, Model model) {
        model.addAttribute("pageTitle", "Ubah Data Dosen");
        Optional < Dosen > dosen = dosenService.findDosenById(id);

        if (dosen.isPresent()) {
            model.addAttribute("dosen", dosen.get());
            model.addAttribute("title", "Ubah Data Dosen");
            return "kta/dosen/ubah-dosen";
        } else {
            return "redirect:/kta/dosen";
        }

    }

    @PostMapping("/tambah")
    public String addDosen(@ModelAttribute Dosen dosen, RedirectAttributes redirectAttributes) {
        try {
            dosenService.saveDosen(dosen);
            redirectAttributes.addFlashAttribute("successMessage", "Dosen berhasil ditambahkan.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menambahkan Dosen.");
            return "redirect:/kta/dosen/tambah";
        }
        return "redirect:/kta/dosen";
    }


    @PostMapping("/ubah")
    public String updateDosen(@ModelAttribute Dosen dosen, RedirectAttributes redirectAttributes) {
        try {
            dosenService.saveDosen(dosen);
            redirectAttributes.addFlashAttribute("successMessage", "Dosen berhasil diubah.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal mengubah dosen");
        }
        return "redirect:/kta/dosen";
    }


    @PostMapping("/hapus")
    public String deleteDosen(@RequestParam Integer id, RedirectAttributes redirectAttributes) {
        try {
            dosenService.deleteDosen(id);
            redirectAttributes.addFlashAttribute("successMessage", "Dosen berhasil dihapus.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus dosen");
        }
        return "redirect:/kta/dosen";
    }
}