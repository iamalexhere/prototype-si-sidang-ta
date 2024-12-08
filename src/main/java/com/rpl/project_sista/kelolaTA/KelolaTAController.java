package com.rpl.project_sista.kelolaTA;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class KelolaTAController {

    @Autowired
    KelolaTARepository kelolaTARepository;
    
    @GetMapping("/kta/kelolaTA")
    public String kelolaView() {
        return "/kelolaTA/index";
    }

    @GetMapping("/kta/kelolaTA/tambahPeserta")
    public String tambahPesertaView() {
        return "/kelolaTA/tambah-peserta";
    }

    @GetMapping("/kta/kelolaTA/ubahPeserta")
    public String ubahPesertaView() {
        return "/kelolaTA/ubah-peserta";
    }

    @GetMapping("/kta/kelolaTA/hapusPeserta")
    public String hapusPesertaView(Model model) {
        model.addAttribute("peserta", new Peserta(null));
        return "/kelolaTA/hapus-peserta";
    }

    @PostMapping("/kta/kelolaTA/hapusPeserta/cariPeserta")
    public String showHasilCari(@Valid Peserta peserta, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "/kelolaTA/hapus-peserta";
        }else {
            List<Peserta> listpeserta = this.kelolaTARepository.findByNama(peserta.getNama());
            if(listpeserta.isEmpty()) {
                bindingResult.rejectValue(
                    "nama",
                    "NameNotFound",
                    "Peserta tidak terdaftar!"
                );
                return "/kelolaTA/hapus-peserta";
            }else {
                List<MahasiswaTA> listmhs = this.kelolaTARepository.findMahasiswa(peserta.getNama());
                if(listmhs.isEmpty()) {
                    System.out.println("lmao");
                }else {
                    System.out.println(listmhs.getFirst().info());
                }
                
                model.addAttribute("listmhs", listmhs);
                return "/kelolaTA/hapus-peserta";
            }
        }
    }
}
