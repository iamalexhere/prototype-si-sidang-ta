package com.rpl.project_sista.controller;

import com.rpl.project_sista.dto.SidangRequest;
import com.rpl.project_sista.entity.Sidang;
import com.rpl.project_sista.entity.Mahasiswa;
import com.rpl.project_sista.service.SidangService;
import com.rpl.project_sista.service.MahasiswaService;
import com.rpl.project_sista.service.DosenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/sidang")
public class SidangController {

    @Autowired
    private SidangService sidangService;

    @Autowired
    private MahasiswaService mahasiswaService;

    @Autowired
    private DosenService dosenService;

    @GetMapping("/jadwal")
    public String showJadwalForm(Model model) {
        model.addAttribute("sidangRequest", new SidangRequest());
        model.addAttribute("mahasiswaList", mahasiswaService.getAllMahasiswa());
        model.addAttribute("dosenList", dosenService.getAllDosen());
        return "jadwal-sidang";
    }

    @PostMapping("/jadwal")
    public String jadwalkanSidang(
            @Valid @ModelAttribute("sidangRequest") SidangRequest request,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("mahasiswaList", mahasiswaService.getAllMahasiswa());
            model.addAttribute("dosenList", dosenService.getAllDosen());
            return "jadwal-sidang";
        }

        try {
            Sidang sidang = sidangService.jadwalkanSidang(request);
            redirectAttributes.addFlashAttribute("success", 
                "Berhasil menjadwalkan sidang untuk mahasiswa dengan NPM " + 
                sidang.getTugasAkhir().getMahasiswa().getNpm());
            return "redirect:/sidang/list";
        } catch (RuntimeException e) {
            result.rejectValue("mahasiswaId", "error.sidang", e.getMessage());
            model.addAttribute("mahasiswaList", mahasiswaService.getAllMahasiswa());
            model.addAttribute("dosenList", dosenService.getAllDosen());
            return "jadwal-sidang";
        }
    }

    @GetMapping("/list")
    public String listSidang(Model model) {
        List<Sidang> sidangList = sidangService.getAllSidang();
        model.addAttribute("sidangList", sidangList);
        return "list-sidang";
    }

    @GetMapping("/mahasiswa/{mahasiswaId}")
    public String listSidangMahasiswa(@PathVariable Long mahasiswaId, Model model) {
        List<Sidang> sidangList = sidangService.getSidangByMahasiswaId(mahasiswaId);
        Mahasiswa mahasiswa = mahasiswaService.getMahasiswaById(mahasiswaId);
        model.addAttribute("sidangList", sidangList);
        model.addAttribute("mahasiswa", mahasiswa);
        return "list-sidang-mahasiswa";
    }
}
