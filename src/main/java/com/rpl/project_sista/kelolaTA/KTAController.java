package com.rpl.project_sista.kelolaTA;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class KTAController {
    
    @GetMapping("/kta")
    public String ktaView() {
        return "/kta/index";
    }

    @GetMapping("/kta/kelolaTA")
    public String kelolaView() {
        return "kta/kelola-ta";
    }
}
