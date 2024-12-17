package com.rpl.project_sista.controllers.mahasiswa;

import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.service.MahasiswaDashboardService;
import com.rpl.project_sista.service.NilaiSidangService;
import com.rpl.project_sista.dto.KomponenNilaiDTO;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mahasiswa/nilai")
public class MahasiswaNilaiController {

    @Autowired
    private MahasiswaDashboardService mahasiswaDashboardService;

    @Autowired
    private NilaiSidangService nilaiSidangService;

    private static final Map<String, Float> BOBOT_PENILAI = new HashMap<>();
    static {
        BOBOT_PENILAI.put("PEMBIMBING", 0.20f); // 20%
        BOBOT_PENILAI.put("PENGUJI_1", 0.35f); // 35%
        BOBOT_PENILAI.put("PENGUJI_2", 0.35f); // 35%
        BOBOT_PENILAI.put("KTA", 0.10f); // 10%
    }

    @GetMapping
    public String showNilai(HttpSession session, Model model) {
        int mahasiswaId = (int) session.getAttribute("mahasiswaId");
        
        // Get Tugas Akhir data
        TugasAkhir tugasAkhir = mahasiswaDashboardService.getTugasAkhir(mahasiswaId);
        if (tugasAkhir == null) {
            model.addAttribute("error", "Tugas Akhir tidak ditemukan");
            return "mahasiswa/nilai-mahasiswa";
        }

        // Get Sidang data
        Sidang sidang = mahasiswaDashboardService.getSidang(tugasAkhir.getTaId());
        if (sidang == null) {
            model.addAttribute("error", "Sidang tidak ditemukan");
            return "mahasiswa/nilai-mahasiswa";
        }

        // Get all nilai components for this sidang
        List<KomponenNilaiDTO> nilaiList = nilaiSidangService.findAllNilaiByIdSidang(sidang.getSidangId().intValue());
        
        // Calculate nilai per penilai
        Map<String, Float> nilaiPerPenilai = new HashMap<>();
        
        // Group nilai by penilai and calculate average
        Map<String, List<KomponenNilaiDTO>> nilaiByPenilai = nilaiList.stream()
            .collect(Collectors.groupingBy(KomponenNilaiDTO::getTipePenilai));

        // Calculate average nilai for each penilai
        nilaiByPenilai.forEach((tipePenilai, komponenList) -> {
            float avgNilai = (float) komponenList.stream()
                .mapToDouble(KomponenNilaiDTO::getNilai)
                .average()
                .orElse(0.0);
            nilaiPerPenilai.put(tipePenilai, avgNilai);
        });

        // Add KTA's fixed nilai (100 for kedisiplinan)
        nilaiPerPenilai.put("KTA", 100.0f);

        // Calculate final nilai
        float nilaiAkhir = 0.0f;
        for (Map.Entry<String, Float> entry : BOBOT_PENILAI.entrySet()) {
            String tipePenilai = entry.getKey();
            Float bobot = entry.getValue();
            Float nilai = nilaiPerPenilai.getOrDefault(tipePenilai, 0.0f);
            nilaiAkhir += nilai * bobot;
        }

        model.addAttribute("nilaiPerPenilai", nilaiPerPenilai);
        model.addAttribute("nilaiAkhir", nilaiAkhir);
        model.addAttribute("bobotPenilai", BOBOT_PENILAI);
        model.addAttribute("nilaiComponents", nilaiList);
        
        return "mahasiswa/nilai-mahasiswa";
    }
}
