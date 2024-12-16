package com.rpl.project_sista.controllers.dosen;

import com.rpl.project_sista.model.entity.BAP;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.service.DosenDashboardService;
import com.rpl.project_sista.jdbcrepository.JdbcDosenRepository;
import com.rpl.project_sista.service.BAPService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/dosen/dashboard")
public class DosenBAPController {

    @Autowired
    private DosenDashboardService dosenDashboardService;

    @Autowired
    private JdbcDosenRepository dosenRepo;

    @Autowired
    private BAPService bapService;

    @GetMapping("/bap")
    public String showBAPManagement(@RequestParam("dosenId") Integer dosenId, Model model) {
        // Get dosen information
        Optional<Dosen> dosen = dosenRepo.findById(dosenId);
        if (!dosen.isPresent()) {
            return "redirect:/error";
        }

        // Get list of supervised students with BAP
        List<TugasAkhir> bimbinganList = dosenDashboardService.getTugasAkhirBimbingan(dosenId);
        
        // Get list of examined students with BAP
        List<Sidang> sidangList = dosenDashboardService.getSidangPenguji(dosenId);

        // Add status colors for UI
        Map<String, String> statusColors = new HashMap<>();
        statusColors.put("Disetujui", "bg-green-100 text-green-800");
        statusColors.put("Belum Disetujui", "bg-yellow-100 text-yellow-800");

        model.addAttribute("dosen", dosen.get());
        model.addAttribute("bimbinganList", bimbinganList);
        model.addAttribute("sidangList", sidangList);
        model.addAttribute("statusColors", statusColors);

        return "dosen/bap-management";
    }

    @PostMapping("/bap/approve/{bapId}")
    @ResponseBody
    public ResponseEntity<String> approveBap(@PathVariable Long bapId, @RequestParam Integer dosenId) {
        try {
            BAP bap = bapService.approveBap(bapId, dosenId.longValue());
            return ResponseEntity.ok("BAP approved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                               .body("Error approving BAP: " + e.getMessage());
        }
    }

    @GetMapping("/bap/download/{bapId}")
    public ResponseEntity<Resource> downloadBap(@PathVariable Long bapId) {
        try {
            return bapService.downloadBap(bapId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
