   //import org.springframework.core.io.InputStreamResource;
   package com.rpl.project_sista;
   import org.springframework.beans.factory.annotation.Autowired;
   //import org.springframework.core.io.InputStreamResource;
   //import org.springframework.http.HttpHeaders;
   //import org.springframework.http.MediaType;
   //import org.springframework.http.ResponseEntity;
   import org.springframework.stereotype.Controller;
   import org.springframework.ui.Model;
   import org.springframework.web.bind.annotation.GetMapping;
   //import org.thymeleaf.context.Context;
   //import org.thymeleaf.spring6.SpringTemplateEngine;
   //import org.xhtmlrenderer.pdf.ITextRenderer;

import jakarta.annotation.PostConstruct;

//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;

   @Controller
   public class BapController {
      
      /* @Autowired
      private SpringTemplateEngine templateEngine; */

      @Autowired
      private BapRepository bapRepository;
      private String nama;
      private String semester;
      private String tahunAkademik;
      private String npm;
      //private String namaz;
      private String topik;
      private String pembimbingTunggal;
      //private String PembimbingPendamping;
      private String PengujiKetua;
      private String PengujiDosen;
      private String fullDate;
      private double bobotPembimbing;
      private double bobotPenguji;
      private double nilaiKetua;
      private double nilaiPenguji;
      private double nilaiPembimbing;


      // ini tuh nama nantinya di ambil dari session aja,tapi sebelumnya dari session tuh bakal diubah terlebih dahulu aja 
      @PostConstruct
      public void init() {
         nama=bapRepository.findNama("manuel@student.unpar.ac.id");
         semester = bapRepository.findSemester(nama);
         tahunAkademik=bapRepository.findTahunAkademik(nama);
         npm= bapRepository.findNpm(nama);
         topik=bapRepository.findTopik(nama);
         pembimbingTunggal=bapRepository.findPembimbingTunggal(nama);
         //PembimbingPendamping=bapRepository.pe
         PengujiKetua=bapRepository.findKetuaPenguji(nama);
         PengujiDosen=bapRepository.findAnggotaPenguji(nama);
         fullDate=bapRepository.findTanggal(nama);
         bobotPembimbing=bapRepository.findBobotPembimbing(1);
         bobotPenguji=bapRepository.findBobotPenguji(1);
         nilaiKetua=bapRepository.findNilaiKetua(PengujiKetua);
         nilaiPenguji=bapRepository.findNilaiPenguji(PengujiDosen);
         nilaiPembimbing=bapRepository.findNilaiPembimbing(pembimbingTunggal);

      }

      @GetMapping("/bap")
      public  String bapbap(Model model){

         model.addAttribute("semester", semester); //
         model.addAttribute("tahunAkademik", tahunAkademik);//
         model.addAttribute("NPM",npm);//
         model.addAttribute("Nama",nama);//
         model.addAttribute("Topik",topik);//
         model.addAttribute("PembimbingTunggal",pembimbingTunggal);//
         model.addAttribute("PembimbingPendamping","");// ini karena gaada nanti bisa kosong aja
         model.addAttribute("PengujiKetua",PengujiKetua);//
         model.addAttribute("PengujiDosen",PengujiDosen);
         model.addAttribute("KetuaNilai",nilaiKetua);
         model.addAttribute("KetuaBobot",bobotPenguji);//
         model.addAttribute("KetuaNa", ((nilaiKetua * bobotPenguji) / 100));
         model.addAttribute("PengujiNilai",nilaiPenguji);
         model.addAttribute("PengujiBobot",bobotPenguji);//
         model.addAttribute("PengujiNa", ((nilaiPenguji * bobotPenguji) / 100));
         model.addAttribute("PembimbingNilai",nilaiPembimbing);
         model.addAttribute("PembimbingBobot",bobotPembimbing);//
         model.addAttribute("PembimbingNa", (((nilaiPembimbing) * bobotPembimbing) / 100));
         model.addAttribute("KoorNilai","100");
         model.addAttribute("KoorBobot","10");
         model.addAttribute("KoorNa","10");
         model.addAttribute("bobotTotal","100");
         model.addAttribute("NaTotal", ((nilaiKetua * bobotPenguji) / 100) + ((nilaiPenguji * bobotPenguji) / 100) + (((nilaiPembimbing) * bobotPembimbing) / 100) + 10);

         model.addAttribute("fullDate",fullDate);//
         return "BAP";
      }
      @GetMapping("/BAPdon")
      public  String bapbapbap(Model model){
         

         model.addAttribute("semester", semester); //
         model.addAttribute("tahunAkademik", tahunAkademik);//
         model.addAttribute("NPM",npm);//
         model.addAttribute("Nama",nama);//
         model.addAttribute("Topik",topik);//
         model.addAttribute("PembimbingTunggal",pembimbingTunggal);//
         model.addAttribute("PembimbingPendamping","");// ini karena gaada nanti bisa kosong aja
         model.addAttribute("PengujiKetua",PengujiKetua);//
         model.addAttribute("PengujiDosen",PengujiDosen);
         model.addAttribute("KetuaNilai",nilaiKetua);
         model.addAttribute("KetuaBobot",bobotPenguji);//
         model.addAttribute("KetuaNa", ((nilaiKetua * bobotPenguji) / 100));
         model.addAttribute("PengujiNilai",nilaiPenguji);
         model.addAttribute("PengujiBobot",bobotPenguji);//
         model.addAttribute("PengujiNa", ((nilaiPenguji * bobotPenguji) / 100));
         model.addAttribute("PembimbingNilai",nilaiPembimbing);
         model.addAttribute("PembimbingBobot",bobotPembimbing);//
         model.addAttribute("PembimbingNa", (((nilaiPembimbing) * bobotPembimbing) / 100));
         model.addAttribute("KoorNilai","100");
         model.addAttribute("KoorBobot","10");
         model.addAttribute("KoorNa","10");
         model.addAttribute("bobotTotal","100");
         model.addAttribute("NaTotal", ((nilaiKetua * bobotPenguji) / 100) + ((nilaiPenguji * bobotPenguji) / 100) + (((nilaiPembimbing) * bobotPembimbing) / 100) + 10);

         model.addAttribute("fullDate",fullDate);//
         return "BAPdon";
      }


      @GetMapping("/dondon")
      public String dondon(){
         return "ButtonBap";
      } 

   }
