package com.rpl.project_sista.controller;

import com.rpl.project_sista.dto.MahasiswaCreateRequest;
import com.rpl.project_sista.entity.Mahasiswa;
import com.rpl.project_sista.service.MahasiswaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/mahasiswa")
public class MahasiswaController {

    @Autowired
    private MahasiswaService mahasiswaService;

    @GetMapping("/tambah")
    public String showTambahForm(Model model) {
        model.addAttribute("mahasiswaRequest", new MahasiswaCreateRequest());
        return "tambah-peserta";
    }

    @PostMapping("/tambah")
    public String tambahMahasiswa(
            @Valid @ModelAttribute("mahasiswaRequest") MahasiswaCreateRequest request,
            BindingResult result,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "tambah-peserta";
        }

        try {
            Mahasiswa mahasiswa = mahasiswaService.createMahasiswa(request);
            redirectAttributes.addFlashAttribute("success", 
                "Berhasil menambahkan mahasiswa dengan NPM " + mahasiswa.getNpm());
            return "redirect:/mahasiswa/list";
        } catch (RuntimeException e) {
            result.rejectValue("npm", "error.mahasiswa", e.getMessage());
            return "tambah-peserta";
        }
    }
}
