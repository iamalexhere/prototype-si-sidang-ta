package com.rpl.project_sista.controller;

import com.rpl.project_sista.entity.Dosen;
import com.rpl.project_sista.entity.Mahasiswa;
import com.rpl.project_sista.service.DosenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dosen")
public class DosenController {

    private final DosenService dosenService;

    @Autowired
    public DosenController(DosenService dosenService) {
        this.dosenService = dosenService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // TODO: Replace this with actual logged-in user logic
        // For now, we'll get the first dosen as an example
        Dosen dosen = dosenService.getAllDosen()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No dosen found"));

        // Get the list of supervised students
        List<Mahasiswa> mahasiswaBimbingan = dosenService.getMahasiswaBimbingan(dosen.getDosenId());

        // Add attributes to the model
        model.addAttribute("dosen", dosen);
        model.addAttribute("mahasiswaBimbingan", mahasiswaBimbingan);
        
        return "Dosen/dashboard-dosen";
    }
}
