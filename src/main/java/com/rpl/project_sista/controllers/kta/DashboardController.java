package com.rpl.project_sista.controllers.kta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rpl.project_sista.repository.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/kta")
public class DashboardController {

    private final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private TugasAkhirRepository tugasAkhirRepository;

    @Autowired
    private SidangRepository sidangRepository;

    @Autowired
    private MahasiswaRepository mahasiswaRepository;

    @Autowired
    private DosenRepository dosenRepository;

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        logger.info("Accessing dashboard page");

        // Add page title and section
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("section", "dashboard");

        // Add counts
        try {
            model.addAttribute("totalTugasAkhir", tugasAkhirRepository.findAll().size());
            model.addAttribute("totalSidangTerjadwal", sidangRepository.findAll().size());
            model.addAttribute("totalMahasiswa", mahasiswaRepository.findAll().size());
            model.addAttribute("totalDosen", dosenRepository.findAll().size());
        } catch (Exception e) {
            logger.error("Error fetching dashboard statistics", e);
            model.addAttribute("totalTugasAkhir", 0);
            model.addAttribute("totalSidangTerjadwal", 0);
            model.addAttribute("totalMahasiswa", 0);
            model.addAttribute("totalDosen", 0);
        }

        // Add recent activities (dummy data for now)
        List<Map<String, String>> recentActivities = new ArrayList<>();
        recentActivities.add(Map.of(
            "timestamp", "2 jam yang lalu",
            "description", "Sidang TA telah dijadwalkan untuk mahasiswa Budi Santoso"
        ));
        recentActivities.add(Map.of(
            "timestamp", "3 jam yang lalu",
            "description", "Tugas Akhir baru telah didaftarkan oleh Ani Wijaya"
        ));
        model.addAttribute("recentActivities", recentActivities);

        // Add notifications (dummy data for now)
        List<Map<String, String>> notifications = new ArrayList<>();
        notifications.add(Map.of(
            "title", "Deadline Pendaftaran Sidang",
            "message", "Deadline pendaftaran sidang periode ini akan berakhir dalam 3 hari",
            "time", "2h ago"
        ));
        notifications.add(Map.of(
            "title", "Pengumuman Penting",
            "message", "Rapat koordinasi dosen pembimbing akan diadakan besok",
            "time", "5h ago"
        ));
        model.addAttribute("notifications", notifications);

        return "kta/dashboard";
    }
}
