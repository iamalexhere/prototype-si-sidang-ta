package com.rpl.project_sista.login;

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
public class LoginController {
    
    @Autowired
    LoginRepository loginRepository;

    @GetMapping
    public String loginView(HttpSession httpSession, Model model) {
        String email = (String) httpSession.getAttribute("email");
        if(email == null) {
            model.addAttribute("user", new User(null, null));
            return "/login/index";
        }else {
            if(email.equals("kta@unpar.ac.id")) {
                return "redirect:/kta";
            }else if(email.contains("@unpar")) {
                return "redirect:/dosen";
            }else {
                return "redirect:/mahasiswa";
            }
        }   
    }

    @PostMapping("/login")
    public String login(@Valid User user, BindingResult bindingResult, HttpSession httpSession) {
        if(bindingResult.hasErrors()) {
            return "/login/index";
        }else {
            List<User> userInfo = this.loginRepository.findByEmail(user.getEmail());
            if(userInfo.isEmpty()) {
                bindingResult.rejectValue(
                    "email",
                    "EmailNotFound",
                    "Email tidak terdaftar!"
                );
                return "/login/index";
            }else {
                if(userInfo.getFirst().getEmail().equals("kta@unpar.ac.id")) {
                    if(userInfo.getFirst().getPassword().equals(user.getPassword())) {
                        httpSession.setAttribute("email", user.getEmail());
                        return "redirect:/kta";
                    }else {
                        bindingResult.rejectValue(
                            "password",
                            "PasswordMismatch", 
                            "Password salah!"
                        );
                        return "/login/index";
                    }
                }else if(userInfo.getFirst().getEmail().contains("@unpar")) {
                    if(userInfo.getFirst().getPassword().equals(user.getPassword())) {
                        httpSession.setAttribute("email", user.getEmail());
                        return "redirect:/dosen";
                    }else {
                        bindingResult.rejectValue(
                            "password",
                            "PasswordMismatch", 
                            "Password salah!"
                        );
                        return "/login/index";
                    }
                }else {
                    if(userInfo.getFirst().getPassword().equals(user.getPassword())) {
                        httpSession.setAttribute("email", user.getEmail());
                        return "redirect:/mahasiswa";
                    }else {
                        bindingResult.rejectValue(
                            "password",
                            "PasswordMismatch", 
                            "Password salah!"
                        );
                        return "/login/index";
                    }
                }
            }
        }
    }
}
