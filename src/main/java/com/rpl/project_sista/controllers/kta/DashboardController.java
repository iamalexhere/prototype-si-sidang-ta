package com.rpl.project_sista.controllers.kta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;
import com.rpl.project_sista.model.enums.UserRole;
import com.rpl.project_sista.repository.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
    public String dashboard(HttpSession session, Model model) {
        // Check if user is logged in and is KTA
        Integer userId = (Integer) session.getAttribute("userId");
        UserRole role = (UserRole) session.getAttribute("userRole");
        
        if (userId == null || role != UserRole.admin) {
            logger.warn("Unauthorized access attempt to KTA dashboard");
            return "redirect:/login";
        }

        logger.info("Accessing dashboard page");

        // Add page title and section
        model.addAttribute("pageTitle", "Dashboard KTA");
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
        Map<String, String> activity1 = new HashMap<>();
        activity1.put("timestamp", "2 jam yang lalu");
        activity1.put("description", "Sidang TA telah dijadwalkan untuk mahasiswa Budi Santoso");
        recentActivities.add(activity1);

        Map<String, String> activity2 = new HashMap<>();
        activity2.put("timestamp", "3 jam yang lalu");
        activity2.put("description", "Tugas Akhir baru telah didaftarkan oleh Ani Wijaya");
        recentActivities.add(activity2);
        
        model.addAttribute("recentActivities", recentActivities);

        // Add notifications (dummy data for now)
        List<Map<String, String>> notifications = new ArrayList<>();
        Map<String, String> notif1 = new HashMap<>();
        notif1.put("title", "Deadline Pendaftaran Sidang");
        notif1.put("message", "Deadline pendaftaran sidang periode ini akan berakhir dalam 3 hari");
        notif1.put("time", "2h ago");
        notifications.add(notif1);

        Map<String, String> notif2 = new HashMap<>();
        notif2.put("title", "Pengumuman Penting");
        notif2.put("message", "Rapat koordinasi dosen pembimbing akan diadakan besok");
        notif2.put("time", "5h ago");
        notifications.add(notif2);
        
        model.addAttribute("notifications", notifications);

        return "kta/dashboard";
    }
}
