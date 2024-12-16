package com.rpl.project_sista.controllers.dosen;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.dto.KomponenNilaiDTO;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.model.enums.TipePenilai;
import com.rpl.project_sista.model.enums.StatusSidang;
import com.rpl.project_sista.service.DosenDashboardService;
import com.rpl.project_sista.service.KomponenNilaiService;
import com.rpl.project_sista.service.NilaiSidangService;
import com.rpl.project_sista.repository.DosenRepository;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dosen")
public class DosenDashboardController {

    @Autowired
    private DosenDashboardService dosenDashboardService;

    @Autowired
    private KomponenNilaiService komponenNilaiService;

    @Autowired
    private NilaiSidangService nilaiSidangService;

    @Autowired
    private DosenRepository dosenRepo;

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
        sidangStatusColors.put(StatusSidang.terjadwal, "bg-yellow-100 text-yellow-800");
        sidangStatusColors.put(StatusSidang.berlangsung, "bg-blue-100 text-blue-800");
        sidangStatusColors.put(StatusSidang.selesai, "bg-green-100 text-green-800");
        sidangStatusColors.put(StatusSidang.dibatalkan, "bg-red-100 text-red-800");
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

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

        return "dosen/dashboard-dosen";
    }

    @GetMapping("/beri-nilai-bimbingan")
    public String beriNilaiBimbingan(HttpSession session,
                                @RequestParam Integer taId, 
                                Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<Dosen> dosen = dosenRepo.findById(userId);
        if (!dosen.isPresent()) {
            return "redirect:/login";
        }

        List<KomponenNilai> komponenNilaiList = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.pembimbing);
        List<KomponenNilaiDTO> komponenNilaiDTOList = komponenNilaiList.stream()
                .map(komponenNilai -> {
                    KomponenNilaiDTO dto = new KomponenNilaiDTO();
                    dto.setKomponenId(komponenNilai.getKomponenId().intValue());
                    dto.setNamaKomponen(komponenNilai.getNamaKomponen());
                    dto.setBobot(komponenNilai.getBobot());
                    dto.setNilai(0.0); // Default value
                    return dto;
                })
                .collect(Collectors.toList());

        model.addAttribute("komponenNilai", komponenNilaiDTOList);
        model.addAttribute("sidangId", taId);
        model.addAttribute("dosenId", dosen.get().getDosenId());
        model.addAttribute("dosen", dosen.get());
        
        return "dosen/beri-nilai-bimbingan";
    }

    @GetMapping("/beri-nilai-pembimbing")
    public String beriNilaiPembimbing(HttpSession session,
                                @RequestParam Integer taId, 
                                Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<Dosen> dosen = dosenRepo.findById(userId);
        if (!dosen.isPresent()) {
            return "redirect:/login";
        }

        List<KomponenNilai> komponenNilaiList = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.pembimbing);
        List<KomponenNilaiDTO> komponenNilaiDTOList = komponenNilaiList.stream()
                .map(komponenNilai -> {
                    KomponenNilaiDTO dto = new KomponenNilaiDTO();
                    dto.setKomponenId(komponenNilai.getKomponenId().intValue());
                    dto.setNamaKomponen(komponenNilai.getNamaKomponen());
                    dto.setBobot(komponenNilai.getBobot());
                    dto.setNilai(0.0); // Default value
                    return dto;
                })
                .collect(Collectors.toList());

        model.addAttribute("komponenNilai", komponenNilaiDTOList);
        model.addAttribute("taId", taId);
        model.addAttribute("dosen", dosen.get());
        
        return "dosen/beri-nilai-pembimbing";
    }

    @PostMapping("/hitung-nilai-akhir-pembimbing")
    public String hitungNilaiAkhirPembimbing(
        HttpSession session,
        @RequestParam Integer taId,
        @RequestParam Map<String, String> allParams, 
        Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<Dosen> dosen = dosenRepo.findById(userId);
        if (!dosen.isPresent()) {
            return "redirect:/login";
        }

        List<KomponenNilai> listNilai = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.pembimbing);

        // Hitung nilai akhir
        double nilaiAkhir = 0;
        for (KomponenNilai komp : listNilai) {
            String paramName = "nilai_" + komp.getKomponenId();
            if (allParams.containsKey(paramName)) {
                double nilaiKomponen = Double.parseDouble(allParams.get(paramName));
                nilaiAkhir += nilaiKomponen * komp.getBobot() / 100;
                nilaiSidangService.saveNilaiSidang(taId, komp.getKomponenId().intValue(), dosen.get().getDosenId(), nilaiKomponen);
            }
        }

        BigDecimal bobot = BigDecimal.valueOf(nilaiAkhir * 100);
        nilaiAkhir = bobot.setScale(2, RoundingMode.HALF_UP).doubleValue();

        model.addAttribute("listNilai", listNilai);
        model.addAttribute("nilaiAkhir", nilaiAkhir);
        model.addAttribute("dosen", dosen.get());

        return "redirect:/dosen/dashboard";
    }

    @GetMapping("/beri-nilai-penguji")
    public String beriNilaiPenguji(HttpSession session,
                                @RequestParam Integer taId, 
                                Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<Dosen> dosen = dosenRepo.findById(userId);
        if (!dosen.isPresent()) {
            return "redirect:/login";
        }

        List<KomponenNilai> komponenNilaiList = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.penguji);
        List<KomponenNilaiDTO> komponenNilaiDTOList = komponenNilaiList.stream()
                .map(komponenNilai -> {
                    KomponenNilaiDTO dto = new KomponenNilaiDTO();
                    dto.setKomponenId(komponenNilai.getKomponenId().intValue());
                    dto.setNamaKomponen(komponenNilai.getNamaKomponen());
                    dto.setBobot(komponenNilai.getBobot());
                    dto.setNilai(0.0); // Default value
                    return dto;
                })
                .collect(Collectors.toList());

        model.addAttribute("komponenNilaiList", komponenNilaiDTOList);
        model.addAttribute("taId", taId);
        model.addAttribute("dosen", dosen.get());
        
        return "dosen/beri-nilai-penguji";
    }

    @PostMapping("/hitung-nilai-akhir-penguji")
    public String hitungNilaiAkhirPenguji(
        HttpSession session,
        @RequestParam Integer taId,
        @RequestParam Map<String, String> allParams, 
        Model model) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<Dosen> dosen = dosenRepo.findById(userId);
        if (!dosen.isPresent()) {
            return "redirect:/login";
        }

        List<KomponenNilai> listNilai = komponenNilaiService.getKomponenNilaiByTipePenilai(TipePenilai.penguji);

        // Hitung nilai akhir
        double nilaiAkhir = 0;
        for (KomponenNilai komp : listNilai) {
            String paramName = "nilai_" + komp.getKomponenId();
            if (allParams.containsKey(paramName)) {
                double nilaiKomponen = Double.parseDouble(allParams.get(paramName));
                nilaiAkhir += nilaiKomponen * komp.getBobot() / 100;
                nilaiSidangService.saveNilaiSidang(taId, komp.getKomponenId().intValue(), dosen.get().getDosenId(), nilaiKomponen);
            }
        }

        BigDecimal bobot = BigDecimal.valueOf(nilaiAkhir * 100);
        nilaiAkhir = bobot.setScale(2, RoundingMode.HALF_UP).doubleValue();

        model.addAttribute("listNilai", listNilai);
        model.addAttribute("nilaiAkhir", nilaiAkhir);
        model.addAttribute("dosen", dosen.get());

        return "redirect:/dosen/dashboard";
    }

    @PostMapping("/dashboard/beriNilai")
    public ResponseEntity<?> beriNilai(@RequestBody Map<String, Object> payload, HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer taId = Integer.parseInt(payload.get("taId").toString());
        Double nilai = Double.parseDouble(payload.get("nilai").toString());
        String catatan = (String) payload.get("catatan");
        String type = (String) payload.get("type");

        try {
            if ("pembimbing".equals(type)) {
                dosenDashboardService.beriNilaiBimbingan(taId, nilai, catatan);
            } else {
                dosenDashboardService.beriNilaiPenguji(taId, nilai);
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
