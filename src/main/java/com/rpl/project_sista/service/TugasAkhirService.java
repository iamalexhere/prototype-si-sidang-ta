package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.TugasAkhir;
import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.repository.TugasAkhirRepository;
import java.util.List;
import java.util.Optional;
 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TugasAkhirService {

    @Autowired
    private TugasAkhirRepository tugasAkhirRepository;

    public List<TugasAkhir> findAllTugasAkhir() {
        return tugasAkhirRepository.findAll();
    }
    
     public Optional<TugasAkhir> findTugasAkhirById(Integer id) {
          return tugasAkhirRepository.findById(id);
     }


    @Transactional
    public TugasAkhir saveTugasAkhir(TugasAkhir tugasAkhir) {
        return tugasAkhirRepository.save(tugasAkhir);
    }

     @Transactional
     public void deleteTugasAkhir(Integer id) {
         tugasAkhirRepository.deleteById(id);
     }

   public boolean updateStatus(Long taId, StatusTA newStatus){
       return tugasAkhirRepository.updateStatus(taId, newStatus);
   }

    public List<TugasAkhir> findByStatus(StatusTA status) {
        return tugasAkhirRepository.findByStatus(status);
    }

     public List<TugasAkhir> findByMahasiswaIdAndStatus(Integer mahasiswaId, StatusTA status) {
      return tugasAkhirRepository.findByMahasiswaIdAndStatus(mahasiswaId,status);
  }

}