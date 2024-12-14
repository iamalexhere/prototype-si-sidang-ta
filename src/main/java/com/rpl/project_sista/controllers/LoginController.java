package com.rpl.project_sista.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rpl.project_sista.model.entity.Users;
import com.rpl.project_sista.model.enums.UserRole;
import com.rpl.project_sista.service.LoginService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping
    public String loginView(HttpSession session, Model model) {
        String email = (String) session.getAttribute("email");
        if(email == null) {
            model.addAttribute("users", new Users());
            return "/login/index";
        }else {
            if(email.equals("kta@unpar.ac.id")) {
                return "redirect:/kta/dashboard";
            }else if(email.contains("@unpar")) {
                return "redirect:/dosen/dashboard";
            }else {
                return "redirect:/mahasiswa/dashboard";
            }
        }
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String passwordHash, @Valid Users user, BindingResult bindingResult, HttpSession httpSession) {
        if(bindingResult.hasErrors()) {
            return "/login/index";
        }else {
            Users cekUser = this.loginService.login(email, passwordHash, bindingResult);
            if(cekUser == null) {
                return "/login/index";
            }else {
                if(cekUser.getRole() == UserRole.dosen) {
                    int dosenId = this.loginService.findDosenId(cekUser.getUserId());
                    httpSession.setAttribute("email", email);
                    httpSession.setAttribute("dosenId", dosenId);
                    return "redirect:/dosen/dashboard";
                }else if(cekUser.getRole() == UserRole.mahasiswa){
                    int mhsId = this.loginService.findMhsId(cekUser.getUserId());
                    httpSession.setAttribute("email", email);
                    httpSession.setAttribute("mahasiswaId", mhsId);
                    return "redirect:/mahasiswa/dashboard";
                }else {
                    httpSession.setAttribute("email", email);
                    return "redirect:/kta/dashboard";
                }
            }
        }
    }
}
