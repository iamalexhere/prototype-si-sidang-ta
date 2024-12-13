package com.rpl.project_sista.Dosen;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rpl.project_sista.Mahasiswa.MahasiswaRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dosen")
public class DosenController {
    @Autowired
    private DosenRepository repo;

    @GetMapping({"/", "/dashboard"})
    public String dashboardDosen(Model model, HttpSession session, 
        @RequestParam(required = false) String nama_mahasiswa, 
        @RequestParam(required = false) String npm_mahasiswa, @RequestParam(required = false) String periode,
        @RequestParam(required = false) String tahun_ajaran){

        String username = "mariskha";
        // String username = (String) session.getAttribute("username");
        Iterable<ListMahasiswa> mahasiswaPembimbing = this.repo.findAllPembimbing(username, nama_mahasiswa, npm_mahasiswa, periode, tahun_ajaran);
        Iterable<ListMahasiswa> mahasiswaPenguji = this.repo.findAllPenguji(username, nama_mahasiswa, npm_mahasiswa, periode, tahun_ajaran);

        LocalDate current_date = LocalDate.now(ZoneId.of("Asia/Jakarta")); 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", Locale.forLanguageTag("id"));
        String formatted_date = current_date.format(formatter);

        model.addAttribute("currentUser", username);
        model.addAttribute("nama_dosen", this.repo.getName(username));
        model.addAttribute("tanggal", formatted_date);
        model.addAttribute("mahasiswaDiBimbing", mahasiswaPembimbing);
        model.addAttribute("mahasiswaDiUji", mahasiswaPenguji);
        model.addAttribute("nama_mahasiswa", nama_mahasiswa);
        model.addAttribute("npm_mahasiswa", npm_mahasiswa);
        model.addAttribute("periode", periode);
        model.addAttribute("tahun_ajaran", tahun_ajaran);
        model.addAttribute("nama_dosen", "Marishka");
        return "/dosen/dashboard-dosen";
    }

}
