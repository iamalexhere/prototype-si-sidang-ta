package com.rpl.project_sista.mhs;

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
public class MahasiswaController {
    
    @GetMapping("/mahasiswa")
    public String mhsView(HttpSession httpSession) {
        if(httpSession.getAttribute("email") != null) {
            return "/mhs/index";
        }else {
            return "redirect:/";
        }
    }

    @GetMapping("/mahasiswa/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("email");
        return "redirect:/";
    }
}
