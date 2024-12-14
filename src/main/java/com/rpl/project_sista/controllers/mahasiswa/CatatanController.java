package com.rpl.project_sista.controllers.mahasiswa;

import com.rpl.project_sista.jdbcrepository.JdbcCatatanRepository;
import com.rpl.project_sista.model.entity.CatatanRevisi;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/mahasiswa/catatan")
public class CatatanController {

    @Autowired
    private JdbcCatatanRepository catatanRepository;

    // Serve the catatan-sidang.html page
    @GetMapping
    public String showCatatanPage(HttpSession session, Model model) {
        Integer mahasiswaId = (Integer) session.getAttribute("mahasiswaId");
        Long mahasiswaIdLong = (mahasiswaId != null) ? Long.valueOf(mahasiswaId) : null;

        List<CatatanRevisi> catatanList = catatanRepository.findCatatanByMahasiswaId(mahasiswaIdLong);
        if (catatanList.isEmpty()) {
            model.addAttribute("message", "Mahasiswa ID tidak ditemukan.");
        } else {
            model.addAttribute("catatanList", catatanList);
        }
        return "mahasiswa/catatan-sidang";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("email");
        session.removeAttribute("mahasiswaId");
        return "redirect:/";
    }
}