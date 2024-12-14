package com.rpl.project_sista.controllers.dosen;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.model.enums.StatusSidang;
import com.rpl.project_sista.service.DosenDashboardService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dosen/dashboard")
public class DosenDashboardController {

    @Autowired
    private DosenDashboardService dosenDashboardService;

    private final Map<StatusTA, String> taStatusColors = new HashMap<>();
    private final Map<StatusSidang, String> sidangStatusColors = new HashMap<>();

    public DosenDashboardController() {
        // Initialize TA status colors
        taStatusColors.put(StatusTA.draft, "bg-gray-100 text-gray-800");
        taStatusColors.put(StatusTA.diajukan, "bg-yellow-100 text-yellow-800");
        taStatusColors.put(StatusTA.diterima, "bg-green-100 text-green-800");
        taStatusColors.put(StatusTA.ditolak, "bg-red-100 text-red-800");
        taStatusColors.put(StatusTA.dalam_pengerjaan, "bg-blue-100 text-blue-800");
        taStatusColors.put(StatusTA.selesai, "bg-purple-100 text-purple-800");

        // Initialize Sidang status colors
        sidangStatusColors.put(StatusSidang.terjadwal, "bg-blue-100 text-blue-800");
        sidangStatusColors.put(StatusSidang.berlangsung, "bg-yellow-100 text-yellow-800");
        sidangStatusColors.put(StatusSidang.selesai, "bg-green-100 text-green-800");
        sidangStatusColors.put(StatusSidang.dibatalkan, "bg-red-100 text-red-800");
    }

    @GetMapping
    public String showDashboard(HttpSession session, Model model) {
        // Get data
        int dosenId = (int) session.getAttribute("dosenId");
        List<TugasAkhir> bimbinganList = dosenDashboardService.getTugasAkhirBimbingan(dosenId);
        List<Sidang> sidangList = dosenDashboardService.getSidangPenguji(dosenId);

        // Add to model
        model.addAttribute("pageTitle", "Dashboard Dosen");
        model.addAttribute("bimbinganList", bimbinganList);
        model.addAttribute("sidangList", sidangList);
        model.addAttribute("taStatusColors", taStatusColors);
        model.addAttribute("sidangStatusColors", sidangStatusColors);

        return "dosen/dashboard-dosen";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("email");
        session.removeAttribute("dosenId");
        return "redirect:/";
    }
}
