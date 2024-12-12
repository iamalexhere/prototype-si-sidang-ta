package com.rpl.project_sista.Mahasiswa;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rpl.project_sista.ListMahasiswa;

import jakarta.servlet.http.HttpSession;

@Controller
public class MahasiswaController {
    @Autowired
    private MahasiswaRepository repo;

    @GetMapping("/")
    public String dashboardMahasiswa(Model model, HttpSession Session){
        String username = "hans";
        // String username = (String) session.getAttribute("username");
        Iterable<Mahasiswa> mahasiswa = this.repo.getMahasiswa(username);
        
        LocalDate current_date = LocalDate.now(ZoneId.of("Asia/Jakarta")); 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.forLanguageTag("id"));
        String formatted_date = current_date.format(formatter);

        model.addAttribute("tanggal", formatted_date);
        model.addAttribute("mahasiswa", mahasiswa);
        return "/dashboard-mahasiswa";
    }
}
