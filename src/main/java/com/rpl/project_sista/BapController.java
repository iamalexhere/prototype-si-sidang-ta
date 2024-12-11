   //import org.springframework.core.io.InputStreamResource;
   package com.rpl.project_sista;
   import org.springframework.beans.factory.annotation.Autowired;
   import org.springframework.core.io.InputStreamResource;
   import org.springframework.http.HttpHeaders;
   import org.springframework.http.MediaType;
   import org.springframework.http.ResponseEntity;
   import org.springframework.stereotype.Controller;
   import org.springframework.ui.Model;
   import org.springframework.web.bind.annotation.GetMapping;
   import org.thymeleaf.context.Context;
   import org.thymeleaf.spring6.SpringTemplateEngine;
   import org.xhtmlrenderer.pdf.ITextRenderer;

import jakarta.annotation.PostConstruct;

import java.io.ByteArrayInputStream;
   import java.io.ByteArrayOutputStream;

   @Controller
   public class BapController {
      
      @Autowired
      private SpringTemplateEngine templateEngine;

      @Autowired
      private BapRepository bapRepository;
      private String nama;
      private String semester;
      private String tahunAkademik;
      private String npm;
      private String namaz;
      private String topik;
      private String pembimbingTunggal;
      //private String PembimbingPendamping;
      private String PengujiKetua;
      private String PengujiDosen;
      private String fullDate;


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
         //PengujiKetua=bapRepository.findKetuaPenguji(nama);
         PengujiDosen=bapRepository.findAnggotaPenguji(nama); //ini di comment dulu karena kalau engga error anjay wkwkwk
         fullDate=bapRepository.findTanggal(nama);;

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
         model.addAttribute("PengujiKetua","");//
         model.addAttribute("PengujiDosen",PengujiDosen);//ini karena belum jadi belum ditulis
         model.addAttribute("KetuaNilai","100");
         model.addAttribute("KetuaBobot","30");
         model.addAttribute("KetuaNa","40");
         model.addAttribute("PengujiNilai","80");
         model.addAttribute("PengujiBobot","20");
         model.addAttribute("PengujiNa","40");
         model.addAttribute("PembimbingNilai","90");
         model.addAttribute("PembimbingBobot","30");
         model.addAttribute("PembimbingNa","60");
         model.addAttribute("KoorNilai","78");
         model.addAttribute("KoorBobot","15");
         model.addAttribute("KoorNa","25");
         model.addAttribute("bobotTotal","100");
         model.addAttribute("NaTotal","78");

         model.addAttribute("fullDate",fullDate);
         return "BAP";
      }


      @GetMapping("/dondon")
      public String dondon(){
         return "ButtonBap";
      }

      @GetMapping("/download-bap")
      public ResponseEntity<?> downloadBap() {
        // Data yang akan disisipkan ke template
         Context context = new Context();
         context.setVariable("Semester", semester);
         context.setVariable("tahunAkademik", tahunAkademik);
         context.setVariable("NPM", npm);
         context.setVariable("Nama", nama);
         context.setVariable("Topik", topik);
         context.setVariable("PembimbingTunggal", pembimbingTunggal);
         context.setVariable("PembimbingPendamping","");
         context.setVariable("PengujiKetua", "");
         context.setVariable("PengujiDosen", PengujiDosen);
         context.setVariable("KetuaNilai", "100");
         context.setVariable("KetuaBobot", "30");
         context.setVariable("KetuaNa", "40");
         context.setVariable("PengujiNilai", "80");
         context.setVariable("PengujiBobot", "20");
         context.setVariable("PengujiNa", "40");
         context.setVariable("PembimbingNilai", "90");
         context.setVariable("PembimbingBobot", "30");
         context.setVariable("PembimbingNa", "60");
         context.setVariable("KoorNilai", "78");
         context.setVariable("KoorBobot", "15");
         context.setVariable("KoorNa", "25");
         context.setVariable("bobotTotal", "100");
         context.setVariable("NaTotal", "78");
         context.setVariable("fullDate", fullDate);

         String htmlContent = templateEngine.process("BAP", context);

         // Konversi HTML ke PDF menggunakan Flying Saucer
         ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
         try {
             ITextRenderer renderer = new ITextRenderer();
             renderer.setDocumentFromString(htmlContent);
             renderer.layout();
             renderer.createPDF(pdfOutputStream);
             renderer.setDocumentFromString(htmlContent, "http://localhost:8080/");
         } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Terjadi kesalahan saat membuat PDF: " + e.getMessage());
        }

         ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfOutputStream.toByteArray());
 
         return ResponseEntity.ok()
                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=BAP.pdf")
                 .contentType(MediaType.APPLICATION_PDF)
                 .body(new InputStreamResource(inputStream));
      } 

   }
