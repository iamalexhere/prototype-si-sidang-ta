package com.rpl.project_sista.controllers.mahasiswa;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Mahasiswa;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.model.enums.StatusSidang;
import com.rpl.project_sista.service.MahasiswaDashboardService;
import com.rpl.project_sista.repository.MahasiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/mahasiswa/dashboard")
public class MahasiswaDashboardController {

    @Autowired
    private MahasiswaDashboardService mahasiswaDashboardService;

    @Autowired 
    private MahasiswaRepository mahasiswaRepository;

    private final Map<StatusTA, String> taStatusColors;
    private final Map<StatusSidang, String> sidangStatusColors;

    public MahasiswaDashboardController() {
        taStatusColors = new HashMap<>();
        taStatusColors.put(StatusTA.draft, "bg-gray-100 text-gray-800");
        taStatusColors.put(StatusTA.diajukan, "bg-yellow-100 text-yellow-800");
        taStatusColors.put(StatusTA.diterima, "bg-green-100 text-green-800");
        taStatusColors.put(StatusTA.ditolak, "bg-red-100 text-red-800");
        taStatusColors.put(StatusTA.dalam_pengerjaan, "bg-blue-100 text-blue-800");
        taStatusColors.put(StatusTA.selesai, "bg-purple-100 text-purple-800");

        sidangStatusColors = new HashMap<>();
        sidangStatusColors.put(StatusSidang.terjadwal, "bg-yellow-100 text-yellow-800");
        sidangStatusColors.put(StatusSidang.berlangsung, "bg-blue-100 text-blue-800");
        sidangStatusColors.put(StatusSidang.selesai, "bg-green-100 text-green-800");
        sidangStatusColors.put(StatusSidang.dibatalkan, "bg-red-100 text-red-800");
    }

    @GetMapping
    public String showDashboard(HttpSession session, Model model) {
        // Get user ID from session
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        // Get mahasiswa by user ID
        Optional<Mahasiswa> mahasiswa = mahasiswaRepository.findByUserId(userId);
        if (!mahasiswa.isPresent()) {
            return "redirect:/login";
        }

        // Get Tugas Akhir data
        TugasAkhir tugasAkhir = mahasiswaDashboardService.getTugasAkhir(mahasiswa.get().getMahasiswaId().longValue());
        
        // Get Sidang data if Tugas Akhir exists
        Sidang sidang = null;
        if (tugasAkhir != null) {
            sidang = mahasiswaDashboardService.getSidang(tugasAkhir.getTaId());
        }

        // Add to model
        model.addAttribute("pageTitle", "Dashboard Mahasiswa");
        model.addAttribute("tugasAkhir", tugasAkhir);
        model.addAttribute("sidang", sidang);
        model.addAttribute("taStatusColors", taStatusColors);
        model.addAttribute("sidangStatusColors", sidangStatusColors);
        model.addAttribute("mahasiswa", mahasiswa.get());

        return "mahasiswa/dashboard-mahasiswa";
    }
}
