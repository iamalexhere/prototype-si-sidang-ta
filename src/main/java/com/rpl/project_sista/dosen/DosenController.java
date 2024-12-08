package com.rpl.project_sista.dosen;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class DosenController {
    
    @GetMapping("/dosen")
    public String dosenView(HttpSession httpSession) {
        if(httpSession.getAttribute("email") != null) {
            return "/dosen/index";
        }else {
            return "redirect:/";
        }
    }

    @GetMapping("/dosen/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("email");
        return "redirect:/";
    }
}
