package com.rpl.project_sista.controllers.dosen;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.dto.KomponenNilaiDTO;
import com.rpl.project_sista.jdbcrepository.JdbcCatatanRepository;
import com.rpl.project_sista.jdbcrepository.JdbcDosenDashboardRepository;
import com.rpl.project_sista.jdbcrepository.JdbcDosenRepository;
import com.rpl.project_sista.jdbcrepository.JdbcKomponenNilaiRepository;
import com.rpl.project_sista.model.entity.CatatanRevisi;
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

    @Autowired
    private JdbcCatatanRepository jdbcCatatanRepository;

    @Autowired
    private JdbcDosenDashboardRepository jdbcDosenDashboardRepository;


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
        // Get data
        int dosenId = (int) session.getAttribute("dosenId");
        List<TugasAkhir> bimbinganList = dosenDashboardService.getTugasAkhirBimbingan(dosenId);
        List<Sidang> sidangList = dosenDashboardService.getSidangPenguji(dosenId);
        Optional<Dosen> listDosen = this.dosenRepo.findById(dosenId);
        Dosen dosen = listDosen.get();

        // Add to model
        model.addAttribute("pageTitle", "Dashboard Dosen");
        model.addAttribute("bimbinganList", bimbinganList);
        model.addAttribute("sidangList", sidangList);
        model.addAttribute("taStatusColors", taStatusColors);
        model.addAttribute("sidangStatusColors", sidangStatusColors);
        model.addAttribute("dosen", dosen);
        model.addAttribute("dosenId", dosenId);


        return "dosen/dashboard-dosen";
    }

    private int dosenId;
    private int taId;
    private double nilaiAkhirPembimbing;

    @GetMapping("/beriNilaiBimbingan")
    public String beriNilaiBimbingan(@RequestParam Integer dosenId, 
                                    @RequestParam Integer taId, 
                                    Model model) {
        this.dosenId = dosenId;
        this.taId = taId;
        List<KomponenNilai> listNilai = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.pembimbing);
        List<KomponenNilaiDTO> nilaiSidangList = nilaiSidangService.findAllNilaiByIdSidang(taId);

        // Buat Map dari List komponenNilai agar mudah mencari bobot berdasarkan idKomponen
        Map<Long, Float> bobotMap = listNilai.stream()
                .collect(Collectors.toMap(KomponenNilai::getKomponenId, KomponenNilai::getBobot));

        // Hitung nilai akhir
        double totalNilai = 0.0;

        for (KomponenNilaiDTO nilaiSidang : nilaiSidangList) {
            Long idKomponen = nilaiSidang.getKomponenId();
            double nilai = nilaiSidang.getNilai();

            System.out.println("==============NILAI SIDANG===============================");
            System.out.println(nilaiSidang.getKomponenId()+" "+nilaiSidang.getNilai());

            // Cari bobot dari Map, jika idKomponen tidak ada maka gunakan bobot default 0
            float bobot = bobotMap.getOrDefault((long) idKomponen, 0.0f);

            totalNilai += nilai * bobot;
        }
        
        // Mengatur bobot dalam persentase
        for (KomponenNilai komp : listNilai) {
            BigDecimal bobot = BigDecimal.valueOf(komp.getBobot());
            komp.setBobot(bobot.setScale(2, RoundingMode.HALF_UP).floatValue());
        }

        model.addAttribute("listNilai", listNilai);
        model.addAttribute("nilaiAkhir", 0); // Nilai awal
        model.addAttribute("dosenId", dosenId);
        model.addAttribute("nilaiAkhir", String.format("%.2f", totalNilai));
        return "dosen/beri_nilai_pembimbing";
    }

    @GetMapping("/beriNilaiPenguji")
    public String beriNilaiPenguji(@RequestParam Integer dosenId, 
                                    @RequestParam Integer taId, 
                                    Model model) {
        this.dosenId = dosenId;
        this.taId = taId;

        List<KomponenNilai> listNilai = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.penguji);
        List<KomponenNilaiDTO> nilaiSidangList = nilaiSidangService.findAllNilaiByIdSidang(taId);

        // Buat Map dari List komponenNilai agar mudah mencari bobot berdasarkan idKomponen
        Map<Long, Float> bobotMap = listNilai.stream()
                .collect(Collectors.toMap(KomponenNilai::getKomponenId, KomponenNilai::getBobot));

        // Hitung nilai akhir
        double totalNilai = 0.0;

        for (KomponenNilaiDTO nilaiSidang : nilaiSidangList) {
            Long idKomponen = nilaiSidang.getKomponenId();
            double nilai = nilaiSidang.getNilai();

            // Cari bobot dari Map, jika idKomponen tidak ada maka gunakan bobot default 0
            float bobot = bobotMap.getOrDefault((long) idKomponen, 0.0f);

            totalNilai += nilai * bobot;
        }
        
        // Mengatur bobot dalam persentase
        for (KomponenNilai komp : listNilai) {
            BigDecimal bobot = BigDecimal.valueOf(komp.getBobot());
            komp.setBobot(bobot.setScale(2, RoundingMode.HALF_UP).floatValue());
        }

        model.addAttribute("listNilai", listNilai);
        model.addAttribute("nilaiAkhir", 0); // Nilai awal
        model.addAttribute("dosenId", dosenId);
        model.addAttribute("nilaiAkhir", String.format("%.2f", totalNilai));
        return "dosen/beri_nilai_penguji";
    }

    
    @PostMapping("/hitungNilaiAkhirPembimbing")
    public String hitungNilaiAkhirPembimbing(
        @RequestParam Map<String, String> allParams, Model model) {
        // Ambil daftar komponen nilai
        List<KomponenNilai> listNilai = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.pembimbing);

        // Hitung nilai akhir
        double nilaiAkhir = 0;
        for (KomponenNilai komp : listNilai) {
            String paramName = "nilai_" + komp.getKomponenId(); // Input harus memiliki id unik
            if (allParams.containsKey(paramName)) {
                float nilaiKomponen = Float.parseFloat(allParams.get(paramName));
                nilaiAkhir += nilaiKomponen * komp.getBobot() / 100; // Bobot dalam %
                // Menyimpan nilai sidang ke database
                nilaiSidangService.saveNilaiSidang(taId, komp.getKomponenId().intValue(), this.dosenId, nilaiKomponen);
                System.out.println(komp.getKomponenId()+" "+this.dosenId+" "+nilaiKomponen);
            }
            
        }

        // Mengatur bobot dan menyiapkan model
        for (KomponenNilai komp : listNilai) {
            BigDecimal bobot = BigDecimal.valueOf(komp.getBobot());
            komp.setBobot(bobot.setScale(2, RoundingMode.HALF_UP).floatValue());
        }

        BigDecimal bobot = BigDecimal.valueOf(nilaiAkhir);
        nilaiAkhir = bobot.setScale(2, RoundingMode.HALF_UP).doubleValue();

        this.nilaiAkhirPembimbing = nilaiAkhir;

        model.addAttribute("listNilai", listNilai);
        model.addAttribute("nilaiAkhir", nilaiAkhir);
        model.addAttribute("dosenId", this.dosenId);

        return "dosen/beri_nilai_pembimbing"; // Ganti dengan tampilan yang sesuai
    }

    @PostMapping("/hitungNilaiAkhirPenguji")
    public String hitungNilaiAkhirPenguji(
        @RequestParam Map<String, String> allParams, Model model) {
        // Ambil daftar komponen nilai
        List<KomponenNilai> listNilai = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.penguji);

        // Hitung nilai akhir
        double nilaiAkhir = 0;
        for (KomponenNilai komp : listNilai) {
            String paramName = "nilai_" + komp.getKomponenId(); // Input harus memiliki id unik
            if (allParams.containsKey(paramName)) {
                float nilaiKomponen = Float.parseFloat(allParams.get(paramName));
                nilaiAkhir += nilaiKomponen * komp.getBobot() / 100; // Bobot dalam %
                // Menyimpan nilai sidang ke database
                nilaiSidangService.saveNilaiSidang(taId, komp.getKomponenId().intValue(), this.dosenId, nilaiKomponen);
                System.out.println(komp.getKomponenId()+" "+this.dosenId+" "+nilaiKomponen);
            }
            
        }

        // Mengatur bobot dan menyiapkan model
        for (KomponenNilai komp : listNilai) {
            BigDecimal bobot = BigDecimal.valueOf(komp.getBobot());
            komp.setBobot(bobot.setScale(2, RoundingMode.HALF_UP).floatValue());
        }

        BigDecimal bobot = BigDecimal.valueOf(nilaiAkhir);
        nilaiAkhir = bobot.setScale(2, RoundingMode.HALF_UP).doubleValue();

        model.addAttribute("listNilai", listNilai);
        model.addAttribute("nilaiAkhir", nilaiAkhir);
        model.addAttribute("dosenId", this.dosenId);

        return "dosen/beri_nilai_penguji"; // Ganti dengan tampilan yang sesuai
    }

    @PostMapping("/simpanCatatan")
    public String simpanCatatan(@RequestParam("isiCatatan") String isiCatatan,
                                HttpSession session,
                                Model model) {
                                    
        Object dosenIdObj = session.getAttribute("dosenId");
        Integer dosenId = null;

        if (dosenIdObj instanceof Long) {
            dosenId = ((Long) dosenIdObj).intValue(); // Convert Long to Integer
        } else if (dosenIdObj instanceof Integer) {
            dosenId = (Integer) dosenIdObj; // Safe cast if it is Integer
        }

        Sidang sidang = jdbcDosenDashboardRepository.findSidangByTugasAkhirId();

        // Buat instance CatatanRevisi
        CatatanRevisi catatan = new CatatanRevisi();

        Dosen dosen = new Dosen();
        dosen.setDosenId(dosenId);

        catatan.setSidang(sidang);
        catatan.setDosen(dosen);
        catatan.setIsiCatatan(isiCatatan);

        // Simpan catatan menggunakan JdbcCatatanRepository
        jdbcCatatanRepository.save(catatan);

        // Redirect kembali ke halaman dashboard
        return "redirect:/dosen/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("email");
        session.removeAttribute("dosenId");
        return "redirect:/";
    }
}
