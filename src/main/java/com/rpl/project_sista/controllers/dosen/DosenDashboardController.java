package com.rpl.project_sista.controllers.dosen;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.dto.KomponenNilaiDTO;
import com.rpl.project_sista.jdbcrepository.JdbcDosenRepository;
import com.rpl.project_sista.jdbcrepository.JdbcKomponenNilaiRepository;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.model.enums.TipePenilai;
import com.rpl.project_sista.model.enums.StatusSidang;
import com.rpl.project_sista.service.DosenDashboardService;
import com.rpl.project_sista.service.KomponenNilaiService;
import com.rpl.project_sista.service.NilaiSidangService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dosen/dashboard")
public class DosenDashboardController {

    @Autowired
    private DosenDashboardService dosenDashboardService;

    @Autowired 
    private JdbcDosenRepository dosenRepo;

    @Autowired
    private KomponenNilaiService komponenNilaiService;

    @Autowired
    private JdbcKomponenNilaiRepository nilaiRepo;

    @Autowired
    private NilaiSidangService nilaiSidangService;

    private final Map<StatusTA, String> taStatusColors = new HashMap<>();
    private final Map<StatusSidang, String> sidangStatusColors = new HashMap<>();

    public DosenDashboardController() {
        // Initialize TA status colors
        taStatusColors.put(StatusTA.draft, "bg-gray-100 text-gray-800");
        taStatusColors.put(StatusTA.diajukan, "bg-yellow-100 text-yellow-800");
        taStatusColors.put(StatusTA.diterima, "bg-green-100 text-green-800");
        taStatusColors.put(StatusTA.ditolak, "bg-red-100 text-red-800");
        taStatusColors.put(StatusTA.dalam_pengerjaan, "bg-blue-100 text-blue-800");
        taStatusColors.put(StatusTA.selesai, "bg-purple-100 text-purple-800");

        // Initialize Sidang status colors
        sidangStatusColors.put(StatusSidang.terjadwal, "bg-blue-100 text-blue-800");
        sidangStatusColors.put(StatusSidang.berlangsung, "bg-yellow-100 text-yellow-800");
        sidangStatusColors.put(StatusSidang.selesai, "bg-green-100 text-green-800");
        sidangStatusColors.put(StatusSidang.dibatalkan, "bg-red-100 text-red-800");
    }


    @GetMapping
    public String showDashboard(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        Optional<Dosen> dosen = dosenRepo.findById(userId);
        
        if (!dosen.isPresent()) {
            return "redirect:/login";
        }

        // Get data using dosen.getDosenId() instead of userId
        List<TugasAkhir> bimbinganList = dosenDashboardService.getTugasAkhirBimbingan(dosen.get().getDosenId());
        List<Sidang> sidangList = dosenDashboardService.getSidangPenguji(dosen.get().getDosenId());

        // Add to model
        model.addAttribute("pageTitle", "Dashboard Dosen");
        model.addAttribute("bimbinganList", bimbinganList);
        model.addAttribute("sidangList", sidangList);
        model.addAttribute("taStatusColors", taStatusColors);
        model.addAttribute("sidangStatusColors", sidangStatusColors);
        model.addAttribute("dosen", dosen.get());
        model.addAttribute("dosenId", dosen.get().getDosenId());

        return "dosen/dashboard-dosen";
    }

    @GetMapping("/beriNilaiBimbingan")
    public String beriNilaiBimbingan(HttpSession session,
                                    @RequestParam Integer taId, 
                                    Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        Integer TaId = (Integer) session.getAttribute("taId");
        List<KomponenNilai> listNilai = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.pembimbing);
        List<KomponenNilaiDTO> nilaiSidangList = nilaiSidangService.findAllNilaiByIdSidang(taId);

        // Buat Map dari List komponenNilai agar mudah mencari bobot berdasarkan idKomponen
        Map<Long, Float> bobotMap = listNilai.stream()
                .collect(Collectors.toMap(KomponenNilai::getKomponenId, KomponenNilai::getBobot));

        // Hitung nilai akhir
        double totalNilai = 0.0;

        for (KomponenNilaiDTO nilaiSidang : nilaiSidangList) {
            int idKomponen = nilaiSidang.getKomponenId();
            double nilai = nilaiSidang.getNilai();

            System.out.println("==============NILAI SIDANG===============================");
            System.out.println(nilaiSidang.getKomponenId()+" "+nilaiSidang.getNilai());

            // Cari bobot dari Map, jika idKomponen tidak ada maka gunakan bobot default 0
            float bobot = bobotMap.getOrDefault((long) idKomponen, 0.0f);

            totalNilai += nilai * bobot;
        }
        
        // Mengatur bobot dalam persentase
        for (KomponenNilai komp : listNilai) {
            BigDecimal bobot = BigDecimal.valueOf(komp.getBobot() * 100);
            komp.setBobot(bobot.setScale(2, RoundingMode.HALF_UP).floatValue());
        }

        // Get data
        Optional<Dosen> listDosen = this.dosenRepo.findById(dosenId);
        if (!listDosen.isPresent()) {
            return "redirect:/login";
        }

        List<KomponenNilai> komponenNilaiList = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.pembimbing);
        List<KomponenNilaiDTO> komponenNilaiDTOList = komponenNilaiList.stream()
                .map(komponenNilai -> {
                    KomponenNilaiDTO dto = new KomponenNilaiDTO();
                    dto.setKomponenId(komponenNilai.getKomponenId().intValue());
                    dto.setNama(komponenNilai.getNamaKomponen());
                    dto.setBobot(komponenNilai.getBobot());
                    dto.setNilai(0.0); // Default value
                    return dto;
                })
                .collect(Collectors.toList());

        model.addAttribute("komponenNilaiList", komponenNilaiDTOList);
        model.addAttribute("dosenId", dosenId);
        model.addAttribute("taId", taId);
        
        return "dosen/beri-nilai-bimbingan";
    }

    @GetMapping("/beriNilaiPenguji")
    public String beriNilaiPenguji(HttpSession session,
                                    @RequestParam Integer taId, 
                                    Model model) {
        Integer dosenId = (Integer) session.getAttribute("userId");
        if (dosenId == null) {
            return "redirect:/login";
        }

        // Get data
        Optional<Dosen> listDosen = this.dosenRepo.findById(dosenId);
        if (!listDosen.isPresent()) {
            return "redirect:/login";
        }

        List<KomponenNilai> komponenNilaiList = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.penguji);
        List<KomponenNilaiDTO> komponenNilaiDTOList = komponenNilaiList.stream()
                .map(komponenNilai -> {
                    KomponenNilaiDTO dto = new KomponenNilaiDTO();
                    dto.setKomponenId(komponenNilai.getKomponenId().intValue());
                    dto.setNama(komponenNilai.getNamaKomponen());
                    dto.setBobot(komponenNilai.getBobot());
                    dto.setNilai(0.0); // Default value
                    return dto;
                })
                .collect(Collectors.toList());

        model.addAttribute("komponenNilaiList", komponenNilaiDTOList);
        model.addAttribute("dosenId", dosenId);
        model.addAttribute("taId", taId);
        
        return "dosen/beri-nilai-penguji";
    }

    
    @PostMapping("/hitungNilaiAkhirPembimbing")
    public String hitungNilaiAkhirPembimbing(
        HttpSession session,
        @RequestParam Map<String, String> allParams, Model model) {
        // Ambil daftar komponen nilai
        Integer dosenId = (Integer) session.getAttribute("userId");
        if (dosenId == null) {
            return "redirect:/login";
        }

        List<KomponenNilai> listNilai = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.pembimbing);

        // Hitung nilai akhir
        double nilaiAkhir = 0;
        for (KomponenNilai komp : listNilai) {
            String paramName = "nilai_" + komp.getKomponenId(); // Input harus memiliki id unik
            if (allParams.containsKey(paramName)) {
                double nilaiKomponen = Double.parseDouble(allParams.get(paramName));
                nilaiAkhir += nilaiKomponen * komp.getBobot() / 100; // Bobot dalam %
                // Menyimpan nilai sidang ke database
                nilaiSidangService.saveNilaiSidang(taId, komp.getKomponenId().intValue(), this.dosenId, nilaiKomponen);
                System.out.println(komp.getKomponenId()+" "+this.dosenId+" "+nilaiKomponen);
            }
            
        }

        // Mengatur bobot dan menyiapkan model
        for (KomponenNilai komp : listNilai) {
            BigDecimal bobot = BigDecimal.valueOf(komp.getBobot() * 100);
            komp.setBobot(bobot.setScale(2, RoundingMode.HALF_UP).floatValue());
        }

        BigDecimal bobot = BigDecimal.valueOf(nilaiAkhir * 100);
        nilaiAkhir = bobot.setScale(2, RoundingMode.HALF_UP).doubleValue();

        model.addAttribute("listNilai", listNilai);
        model.addAttribute("nilaiAkhir", nilaiAkhir);
        model.addAttribute("dosenId", dosenId);

        return "dosen/beri-nilai-pembimbing"; // Ganti dengan tampilan yang sesuai
    }

    @PostMapping("/hitungNilaiAkhirPenguji")
    public String hitungNilaiAkhirPenguji(
        HttpSession session,
        @RequestParam Map<String, String> allParams, Model model) {
        // Ambil daftar komponen nilai
        Integer dosenId = (Integer) session.getAttribute("userId");
        if (dosenId == null) {
            return "redirect:/login";
        }

        List<KomponenNilai> listNilai = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.penguji);

        // Hitung nilai akhir
        double nilaiAkhir = 0;
        for (KomponenNilai komp : listNilai) {
            String paramName = "nilai_" + komp.getKomponenId(); // Input harus memiliki id unik
            if (allParams.containsKey(paramName)) {
                double nilaiKomponen = Double.parseDouble(allParams.get(paramName));
                nilaiAkhir += nilaiKomponen * komp.getBobot() / 100; // Bobot dalam %
                // Menyimpan nilai sidang ke database
                nilaiSidangService.saveNilaiSidang(taId, komp.getKomponenId().intValue(), this.dosenId, nilaiKomponen);
                System.out.println(komp.getKomponenId()+" "+this.dosenId+" "+nilaiKomponen);
            }
            
        }

        // Mengatur bobot dan menyiapkan model
        for (KomponenNilai komp : listNilai) {
            BigDecimal bobot = BigDecimal.valueOf(komp.getBobot() * 100);
            komp.setBobot(bobot.setScale(2, RoundingMode.HALF_UP).floatValue());
        }

        BigDecimal bobot = BigDecimal.valueOf(nilaiAkhir * 100);
        nilaiAkhir = bobot.setScale(2, RoundingMode.HALF_UP).doubleValue();

        model.addAttribute("listNilai", listNilai);
        model.addAttribute("nilaiAkhir", nilaiAkhir);
        model.addAttribute("dosenId", dosenId);

        return "dosen/beri-nilai-penguji"; // Ganti dengan tampilan yang sesuai
    }


}
