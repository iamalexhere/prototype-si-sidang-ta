package com.rpl.project_sista.controllers.dosen;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.jdbcrepository.JdbcDosenRepository;
import com.rpl.project_sista.jdbcrepository.JdbcKomponenNilaiRepository;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.model.enums.StatusSidang;
import com.rpl.project_sista.service.DosenDashboardService;
import com.rpl.project_sista.service.DosenService;
import com.rpl.project_sista.service.KomponenNilaiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/dosen/beriNilai")
public class DosenNilaiController {

    @Autowired
    private KomponenNilaiService komponenNilaiService;

    @Autowired
    private JdbcKomponenNilaiRepository nilaiRepo;

    @GetMapping
    public String showKomponenNilai(@RequestParam Integer dosenId, Integer mahasiswa_id, Model model){
        return "dosen/";
    }
    
}
