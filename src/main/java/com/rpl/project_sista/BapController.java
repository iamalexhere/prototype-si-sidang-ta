   package com.rpl.project_sista;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.annotation.PostConstruct;
import org.springframework.dao.EmptyResultDataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class BapController {
    
    private static final Logger logger = LoggerFactory.getLogger(BapController.class);

    @Autowired
    private BapRepository bapRepository;
    private String nama;
    private String semester;
    private String tahunAkademik;
    private String npm;
    private String topik;
    private String pembimbingTunggal;
    private String PengujiKetua;
    private String PengujiDosen;
    private String fullDate;
    private double bobotPembimbing;
    private double bobotPenguji;
    private double nilaiKetua;
    private double nilaiPenguji;
    private double nilaiPembimbing;

    @PostConstruct
    public void init() {
        try {
            nama = bapRepository.findNama("6182201001@student.unpar.ac.id");
            if (nama != null) {
                semester = bapRepository.findSemester(nama);
                tahunAkademik = bapRepository.findTahunAkademik(nama);
                npm = bapRepository.findNpm(nama);
                topik = bapRepository.findTopik(nama);
                pembimbingTunggal = bapRepository.findPembimbingTunggal(nama);
                PengujiKetua = bapRepository.findKetuaPenguji(nama);
                PengujiDosen = bapRepository.findAnggotaPenguji(nama);
                fullDate = bapRepository.findTanggal(nama);
                bobotPembimbing = bapRepository.findBobotPembimbing(1);
                bobotPenguji = bapRepository.findBobotPenguji(1);
                nilaiKetua = bapRepository.findNilaiKetua(PengujiKetua);
                nilaiPenguji = bapRepository.findNilaiPenguji(PengujiDosen);
                nilaiPembimbing = bapRepository.findNilaiPembimbing(pembimbingTunggal);
            }
        } catch (EmptyResultDataAccessException e) {
            logger.warn("User not found: manuel@student.unpar.ac.id");
            // Set default values or handle the case where user is not found
            nama = "Default User";
            semester = "Default Semester";
            tahunAkademik = "Default Year";
        } catch (Exception e) {
            logger.error("Error initializing BAP controller", e);
            // Set default values or handle other errors
        }
    }

    @GetMapping("/bap")
    public String bapbap(Model model) {
        model.addAttribute("semester", semester); //
        model.addAttribute("tahunAkademik", tahunAkademik);// 
        model.addAttribute("NPM", npm);// 
        model.addAttribute("Nama", nama);// 
        model.addAttribute("Topik", topik);// 
        model.addAttribute("PembimbingTunggal", pembimbingTunggal);// 
        model.addAttribute("PembimbingPendamping", "");// ini karena gaada nanti bisa kosong aja 
        model.addAttribute("PengujiKetua", PengujiKetua);// 
        model.addAttribute("PengujiDosen", PengujiDosen); 
        model.addAttribute("KetuaNilai", nilaiKetua); 
        model.addAttribute("KetuaBobot", bobotPenguji);// 
        model.addAttribute("KetuaNa", ((nilaiKetua * bobotPenguji) / 100)); 
        model.addAttribute("PengujiNilai", nilaiPenguji); 
        model.addAttribute("PengujiBobot", bobotPenguji);// 
        model.addAttribute("PengujiNa", ((nilaiPenguji * bobotPenguji) / 100)); 
        model.addAttribute("PembimbingNilai", nilaiPembimbing); 
        model.addAttribute("PembimbingBobot", bobotPembimbing);// 
        model.addAttribute("PembimbingNa", (((nilaiPembimbing) * bobotPembimbing) / 100)); 
        model.addAttribute("KoorNilai", "100"); 
        model.addAttribute("KoorBobot", "10"); 
        model.addAttribute("KoorNa", "10"); 
        model.addAttribute("bobotTotal", "100"); 
        model.addAttribute("NaTotal", ((nilaiKetua * bobotPenguji) / 100) + ((nilaiPenguji * bobotPenguji) / 100) + (((nilaiPembimbing) * bobotPembimbing) / 100) + 10); 

        model.addAttribute("fullDate", fullDate);// 
        return "BAP";
    }

    @GetMapping("/BAPdon")
    public String bapbapbap(Model model) {
        model.addAttribute("semester", semester); //
        model.addAttribute("tahunAkademik", tahunAkademik);// 
        model.addAttribute("NPM", npm);// 
        model.addAttribute("Nama", nama);// 
        model.addAttribute("Topik", topik);// 
        model.addAttribute("PembimbingTunggal", pembimbingTunggal);// 
        model.addAttribute("PembimbingPendamping", "");// ini karena gaada nanti bisa kosong aja 
        model.addAttribute("PengujiKetua", PengujiKetua);// 
        model.addAttribute("PengujiDosen", PengujiDosen); 
        model.addAttribute("KetuaNilai", nilaiKetua); 
        model.addAttribute("KetuaBobot", bobotPenguji);// 
        model.addAttribute("KetuaNa", ((nilaiKetua * bobotPenguji) / 100)); 
        model.addAttribute("PengujiNilai", nilaiPenguji); 
        model.addAttribute("PengujiBobot", bobotPenguji);// 
        model.addAttribute("PengujiNa", ((nilaiPenguji * bobotPenguji) / 100)); 
        model.addAttribute("PembimbingNilai", nilaiPembimbing); 
        model.addAttribute("PembimbingBobot", bobotPembimbing);// 
        model.addAttribute("PembimbingNa", (((nilaiPembimbing) * bobotPembimbing) / 100)); 
        model.addAttribute("KoorNilai", "100"); 
        model.addAttribute("KoorBobot", "10"); 
        model.addAttribute("KoorNa", "10"); 
        model.addAttribute("bobotTotal", "100"); 
        model.addAttribute("NaTotal", ((nilaiKetua * bobotPenguji) / 100) + ((nilaiPenguji * bobotPenguji) / 100) + (((nilaiPembimbing) * bobotPembimbing) / 100) + 10); 

        model.addAttribute("fullDate", fullDate);// 
        return "BAPdon";
    }

    @GetMapping("/dondon")
    public String dondon() {
        return "ButtonBap";
    }
}
