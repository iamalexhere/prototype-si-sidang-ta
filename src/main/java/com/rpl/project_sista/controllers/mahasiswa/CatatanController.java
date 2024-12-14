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
        Object mahasiswaIdObj = session.getAttribute("mahasiswaId");
        Long mahasiswaId = null;

        if (mahasiswaIdObj instanceof Long) {
            mahasiswaId = (Long) mahasiswaIdObj; // Safe cast if it is Long
        } else if (mahasiswaIdObj instanceof Integer) {
            mahasiswaId = ((Integer) mahasiswaIdObj).longValue(); // Convert Integer to Long
        }
        System.out.println("Mahasiswa ID from session: " + mahasiswaId);

        if (mahasiswaId != null) {
            List<CatatanRevisi> catatanList = catatanRepository.findCatatanByMahasiswaId(mahasiswaId);
            model.addAttribute("catatanList", catatanList);
            model.addAttribute("mahasiswaId", mahasiswaId);
            return "mahasiswa/catatan-sidang"; 
        } else {
            model.addAttribute("errorMessage", "Mahasiswa ID tidak ditemukan.");
            return "mahasiswa/error"; 
        }
    }

    @GetMapping("/mahasiswa/catatan/{mahasiswaId}")
    @ResponseBody
    public List<CatatanRevisi> getCatatanByMahasiswaId(@PathVariable Long mahasiswaId) {
        return catatanRepository.findCatatanByMahasiswaId(mahasiswaId);
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("email");
        session.removeAttribute("mahasiswaId");
        session.invalidate();
        return "redirect:/";
    }
}