package com.rpl.project_sista.login;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    
    @GetMapping
    public String loginView() {
        return "/login/index";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email) {
        if(email.equals("kta@lect.unpar.ac.id")) {
            return "redirect:/kta";
            //belum cek pass
        }else if(email.equals("dos@lect.unpar.ac.id")) {
            return "redirect:/dos";
            //belum cek pass
        }else {
            return "redirect:/mhs";
            //belum cek pass
        }
    }

}
