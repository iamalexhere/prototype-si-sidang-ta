package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Dosen;
import com.rpl.project_sista.model.enums.StatusSidang;
import com.rpl.project_sista.repository.SidangRepository;

 import java.time.LocalDateTime;
 import java.util.List;
 import java.util.Optional;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SidangService {

    @Autowired
    private SidangRepository sidangRepository;

    public List<Sidang> findAllSidang() {
        return sidangRepository.findAll();
    }

    public Optional<Sidang> findSidangById(Integer id) {
        return sidangRepository.findById(id);
    }
     public Optional<Sidang> findByTugasAkhirId(Integer taId){
        return sidangRepository.findByTugasAkhirId(taId);
     }

       public List<Sidang> findByDosenPengujiId(Integer dosenId) {
         return sidangRepository.findByDosenPengujiId(dosenId);
       }

    @Transactional
    public Sidang saveSidang(Sidang sidang) {
        return sidangRepository.save(sidang);
    }

     @Transactional
     public void deleteSidang(Integer id) {
         sidangRepository.deleteById(id);
     }
    public boolean isJadwalAvailable(LocalDateTime waktuMulai, LocalDateTime waktuSelesai, String ruangan){
         return sidangRepository.isJadwalAvailable(waktuMulai,waktuSelesai,ruangan);
    }
     public boolean isDosenAvailable(Dosen dosen, LocalDateTime waktuMulai, LocalDateTime waktuSelesai){
      return sidangRepository.isDosenAvailable(dosen,waktuMulai,waktuSelesai);
     }

      public List<Sidang> findOverlappingSidang(LocalDateTime waktuMulai, LocalDateTime waktuSelesai, String ruangan){
        return sidangRepository.findOverlappingSidang(waktuMulai, waktuSelesai, ruangan);
     }

      public List<Sidang> findDosenOverlappingSidang(Dosen dosen, LocalDateTime waktuMulai, LocalDateTime waktuSelesai){
        return sidangRepository.findDosenOverlappingSidang(dosen, waktuMulai, waktuSelesai);
     }

   public boolean updateStatus(Long sidangId, StatusSidang newStatus){
      return sidangRepository.updateStatus(sidangId, newStatus);
   }

  public List<Sidang> findByStatus(StatusSidang status) {
      return sidangRepository.findByStatus(status);
  }
}