package com.rpl.project_sista.controllers.kta;

import com.rpl.project_sista.model.entity.NilaiSidang;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.service.NilaiSidangService;
import com.rpl.project_sista.service.SidangService;
import com.rpl.project_sista.service.DosenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/kta/nilai-sidang")
public class NilaiSidangController {

    @Autowired
    private NilaiSidangService nilaiSidangService;
    
    @Autowired
    private SidangService sidangService;
    
    @Autowired
    private DosenService dosenService;


   @GetMapping
    public String listNilaiSidang(Model model) {
        model.addAttribute("pageTitle", "Daftar Nilai Sidang");
       List<NilaiSidang> nilaiSidangList = nilaiSidangService.findAllNilaiSidang();
          model.addAttribute("nilaiSidangList", nilaiSidangList);
        return "kta/nilai-sidang/list-nilai-sidang";
    }


    @GetMapping("/tambah")
     public String showAddNilaiSidangForm(Model model, @RequestParam Integer sidangId) {
        model.addAttribute("pageTitle", "Tambah Nilai Sidang");
         model.addAttribute("nilaiSidang", new NilaiSidang());
        Optional<Sidang> sidang = sidangService.findSidangById(sidangId);
         if (sidang.isPresent()) {
             model.addAttribute("sidang", sidang.get());
              model.addAttribute("dosenList", dosenService.findAllDosen());
           return "kta/nilai-sidang/manajemen-nilai-sidang";
        } else {
            return "redirect:/kta/sidang";
        }
   }

   @GetMapping("/ubah")
   public String showUpdateNilaiSidangForm(@RequestParam Long id, Model model) {
         model.addAttribute("pageTitle", "Ubah Nilai Sidang");
        Optional<NilaiSidang> nilaiSidangOptional = nilaiSidangService.findNilaiSidangById(id);

       if (nilaiSidangOptional.isPresent()){
         NilaiSidang nilaiSidang = nilaiSidangOptional.get();
          model.addAttribute("nilaiSidang", nilaiSidang);
           model.addAttribute("dosenList", dosenService.findAllDosen());
           model.addAttribute("sidang", nilaiSidang.getSidang());
            return "kta/nilai-sidang/manajemen-nilai-sidang";
        }else{
           return "redirect:/kta/sidang";
      }
   }


   @PostMapping("/save")
    public String saveNilaiSidang(@ModelAttribute NilaiSidang nilaiSidang, 
          @RequestParam Integer sidangId, @RequestParam Integer dosenId, RedirectAttributes redirectAttributes) {
        try {
            Optional<Sidang> sidang = sidangService.findSidangById(sidangId);
           Optional<Dosen> dosen = dosenService.findDosenById(dosenId);
           if(sidang.isPresent() && dosen.isPresent()){
              nilaiSidang.setSidang(sidang.get());
             nilaiSidang.setDosen(dosen.get());
             nilaiSidangService.saveNilaiSidang(nilaiSidang);
             redirectAttributes.addFlashAttribute("successMessage", "Nilai Sidang Berhasil Disimpan");
          }else{
              redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyimpan nilai sidang.");
              return "redirect:/kta/sidang";
          }
          }catch(Exception e){
              redirectAttributes.addFlashAttribute("errorMessage", "Gagal menyimpan nilai sidang, " + e.getMessage());
               return "redirect:/kta/sidang";
       }
          return "redirect:/kta/sidang";
  }

   @PostMapping("/hapus")
   public String deleteNilaiSidang(@RequestParam Long id, RedirectAttributes redirectAttributes) {
       try {
         nilaiSidangService.deleteNilaiSidang(id);
         redirectAttributes.addFlashAttribute("successMessage", "Nilai Sidang berhasil dihapus");
      } catch (Exception e) {
        redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus nilai sidang.");
        }
      return "redirect:/kta/nilai-sidang";
    }
}