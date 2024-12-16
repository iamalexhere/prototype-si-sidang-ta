package com.rpl.project_sista.controllers;

import com.rpl.project_sista.model.entity.Users;
import com.rpl.project_sista.service.LoginService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private LoginService loginService;

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("users", new Users());
        return "login/index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("users") Users user, 
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        Users authenticatedUser = loginService.authenticate(user.getEmail(), user.getPasswordHash());
        
        if (authenticatedUser != null) {
            session.setAttribute("userId", authenticatedUser.getUserId());
            session.setAttribute("userRole", authenticatedUser.getRole());
            
            // Redirect based on role
            switch (authenticatedUser.getRole()) {
                case dosen:
                    return "redirect:/dosen/dashboard";
                case mahasiswa:
                    return "redirect:/mahasiswa/dashboard";
                case admin:
                    return "redirect:/kta/dashboard";
                default:
                    return "redirect:/login";
            }
        }
        
        redirectAttributes.addFlashAttribute("error", "Invalid email or password");
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
