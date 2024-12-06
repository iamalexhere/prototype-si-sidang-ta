package com.rpl.project_sista.service;

import com.rpl.project_sista.dto.SidangRequest;
import com.rpl.project_sista.entity.*;
import com.rpl.project_sista.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SidangService {
    
    @Autowired
    private SidangRepository sidangRepository;
    
    @Autowired
    private PengujiSidangRepository pengujiSidangRepository;
    
    @Autowired
    private MahasiswaRepository mahasiswaRepository;
    
    @Autowired
    private DosenRepository dosenRepository;
    
    @Autowired
    private TugasAkhirRepository tugasAkhirRepository;

    @Transactional
    public Sidang jadwalkanSidang(SidangRequest request) {
        // Validasi mahasiswa
        Mahasiswa mahasiswa = mahasiswaRepository.findById(request.getMahasiswaId())
            .orElseThrow(() -> new RuntimeException("Mahasiswa tidak ditemukan"));

        // Validasi tugas akhir
        TugasAkhir tugasAkhir = tugasAkhirRepository.findByMahasiswa(mahasiswa)
            .orElseThrow(() -> new RuntimeException("Tugas Akhir tidak ditemukan"));

        // Validasi dosen penguji
        Dosen penguji1 = dosenRepository.findById(request.getPenguji1Id())
            .orElseThrow(() -> new RuntimeException("Penguji 1 tidak ditemukan"));
        
        Dosen penguji2 = dosenRepository.findById(request.getPenguji2Id())
            .orElseThrow(() -> new RuntimeException("Penguji 2 tidak ditemukan"));

        if (penguji1.equals(penguji2)) {
            throw new RuntimeException("Penguji 1 dan Penguji 2 tidak boleh sama");
        }

        // Buat jadwal sidang
        LocalDateTime jadwalSidang = LocalDateTime.of(
            request.getTanggalSidang(),
            request.getWaktuSidang()
        );

        // Cek ketersediaan ruangan
        if (sidangRepository.existsByRuanganAndJadwal(request.getRuangan(), jadwalSidang)) {
            throw new RuntimeException("Ruangan sudah digunakan pada waktu tersebut");
        }

        // Cek ketersediaan dosen
        if (pengujiSidangRepository.existsByDosen_DosenIdAndSidang_Jadwal(penguji1.getDosenId(), jadwalSidang) ||
            pengujiSidangRepository.existsByDosen_DosenIdAndSidang_Jadwal(penguji2.getDosenId(), jadwalSidang)) {
            throw new RuntimeException("Dosen penguji sudah memiliki jadwal sidang pada waktu tersebut");
        }

        // Simpan sidang
        Sidang sidang = new Sidang();
        sidang.setTugasAkhir(tugasAkhir);
        sidang.setJadwal(jadwalSidang);
        sidang.setRuangan(request.getRuangan());
        sidang.setStatusSidang(StatusSidang.terjadwal);
        
        sidang = sidangRepository.save(sidang);

        // Simpan penguji sidang
        PengujiSidang pengujiSidang1 = new PengujiSidang();
        pengujiSidang1.setSidang(sidang);
        pengujiSidang1.setDosen(penguji1);
        pengujiSidang1.setPeran(PeranPenguji.penguji1);
        pengujiSidangRepository.save(pengujiSidang1);

        PengujiSidang pengujiSidang2 = new PengujiSidang();
        pengujiSidang2.setSidang(sidang);
        pengujiSidang2.setDosen(penguji2);
        pengujiSidang2.setPeran(PeranPenguji.penguji2);
        pengujiSidangRepository.save(pengujiSidang2);

        return sidang;
    }

    public List<Sidang> getAllSidang() {
        return sidangRepository.findAll();
    }

    public List<Sidang> getSidangByMahasiswaId(Long mahasiswaId) {
        return sidangRepository.findByTugasAkhir_Mahasiswa_MahasiswaId(mahasiswaId);
    }

    public List<PengujiSidang> getPengujiSidang(Long sidangId) {
        return pengujiSidangRepository.findBySidang_SidangId(sidangId);
    }
}
