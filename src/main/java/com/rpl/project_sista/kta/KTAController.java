package com.rpl.project_sista.kta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;

@Controller
public class KTAController {
    
    @GetMapping("/kta")
    public String ktaView(HttpSession httpSession) {
        if(httpSession.getAttribute("email") != null) {
            return "/kta/index";
        }else {
            return "redirect:/";
        }
    }

    @GetMapping("/kta/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("email");
        return "redirect:/";
    }
}