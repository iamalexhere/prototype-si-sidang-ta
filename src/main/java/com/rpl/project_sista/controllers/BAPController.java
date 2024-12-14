package com.rpl.project_sista.controllers;

import com.rpl.project_sista.model.entity.BAP;
import com.rpl.project_sista.service.BAPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/bap")
public class BAPController {

    @Autowired
    private BAPService bapService;

    @GetMapping("/sidang/{sidangId}")
    public String viewBAP(@PathVariable Integer sidangId, Model model) {
        BAP bap = bapService.getBAPBySidangId(sidangId);
        model.addAttribute("bap", bap);
        return "BAP";
    }

    @GetMapping("/download/{sidangId}")
    public String downloadBAP(@PathVariable Integer sidangId, Model model) {
        BAP bap = bapService.getBAPBySidangId(sidangId);
        model.addAttribute("bap", bap);
        return "BAPdon";
    }

    @PostMapping("/upload/{sidangId}")
    public String uploadSignedBAP(@PathVariable Integer sidangId, 
                                @RequestParam("file") MultipartFile file,
                                @RequestParam("userType") String userType) {
        bapService.uploadSignedBAP(sidangId, file, userType);
        return "redirect:/bap/sidang/" + sidangId;
    }

    @PostMapping("/approve/{sidangId}")
    public String approveBAP(@PathVariable Integer sidangId, 
                           @RequestParam("userType") String userType) {
        bapService.approveBAP(sidangId, userType);
        return "redirect:/bap/sidang/" + sidangId;
    }
}
