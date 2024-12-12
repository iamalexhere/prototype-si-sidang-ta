package com.rpl.project_sista.controllers.kta;

import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.enums.StatusSidang;
import com.rpl.project_sista.service.DosenService;
import com.rpl.project_sista.service.SidangService;
import com.rpl.project_sista.service.TugasAkhirService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Controller
@RequestMapping("/kta/sidang")
public class ManajemenSidangController {

    @Autowired
    private SidangService sidangService;

    @Autowired
    private TugasAkhirService tugasAkhirService;

    @Autowired
    private DosenService dosenService;

    @GetMapping
    public String listSidang(Model model) {
        model.addAttribute("pageTitle", "Daftar Sidang");
        List<Sidang> sidangList = sidangService.findAllSidang();
        model.addAttribute("sidangList", sidangList);
        return "kta/sidang/list-sidang";
    }

    @GetMapping("/tambah")
    public String showAddForm(Model model) {
        model.addAttribute("pageTitle", "Jadwalkan Sidang");
        model.addAttribute("sidang", new Sidang());
        model.addAttribute("tugasAkhirList", tugasAkhirService.findAllTugasAkhir());
        model.addAttribute("dosenList", dosenService.findAllDosen());
         return "kta/sidang/manajamen-sidang";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
          model.addAttribute("pageTitle", "Edit Jadwal Sidang");
           Optional<Sidang> sidang = sidangService.findSidangById(id);

        if(sidang.isPresent()){
           model.addAttribute("sidang", sidang.get());
            model.addAttribute("tugasAkhirList", tugasAkhirService.findAllTugasAkhir());
             model.addAttribute("dosenList", dosenService.findAllDosen());
            return "kta/sidang/manajamen-sidang";
         }else{
            return "redirect:/kta/sidang";
       }
    }
  

    @PostMapping("/save")
        public String saveSidang(@ModelAttribute Sidang sidang,
                             RedirectAttributes redirectAttributes) {
            try {

             sidangService.saveSidang(sidang);
             redirectAttributes.addFlashAttribute("successMessage", "Jadwal Sidang berhasil disimpan.");
               return "redirect:/kta/sidang";
              }catch(Exception e){
                  redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyimpan jadwal sidang. " + e.getMessage());
                   return "redirect:/kta/sidang/tambah";
          }
        }



    @GetMapping("/detail/{id}")
    public String showSidangDetail(@PathVariable Integer id, Model model) {
      model.addAttribute("pageTitle", "Detail Sidang");
        Optional<Sidang> sidangOptional = sidangService.findSidangById(id);

        if (sidangOptional.isPresent()){
        Sidang sidang = sidangOptional.get();
          model.addAttribute("sidang", sidang);

         List<Dosen> pengujiList =  dosenService.findAllDosen().stream()
                                     .filter(dosen -> sidang.getPenguji().contains(dosen))
                                     .toList();
          model.addAttribute("pengujiList", pengujiList);
             return "kta/sidang/detail-sidang";
        }else{
             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Jadwal sidang tidak ditemukan");
       }
    }

    @PostMapping("/delete/{id}")
      public String deleteSidang(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try{
            sidangService.deleteSidang(id);
           redirectAttributes.addFlashAttribute("successMessage", "Jadwal sidang berhasil dihapus.");
        }catch (Exception e){
          redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus jadwal sidang." + e.getMessage());
       }
          return "redirect:/kta/sidang";
      }
}