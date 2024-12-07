package com.rpl.project_sista.kelolaTA;

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
    public String ktaView() {
        return "/kta/index";
    }

    @GetMapping("/kta/kelolaTA")
    public String kelolaView() {
        return "/kta/kelola-ta";
    }

    @GetMapping("/kta/kelolaTA/tambahPeserta")
    public String tambahPesertaView() {
        return "/kta/tambah-peserta";
    }

    @GetMapping("/kta/kelolaTA/ubahPeserta")
    public String ubahPesertaView() {
        return "/kta/ubah-peserta";
    }

    @GetMapping("/kta/kelolaTA/hapusPeserta")
    public String hapusPesertaView() {
        return "/kta/hapus-peserta";
    }

    @GetMapping("/kta/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("email");
        return "redirect:/";
    }
}
