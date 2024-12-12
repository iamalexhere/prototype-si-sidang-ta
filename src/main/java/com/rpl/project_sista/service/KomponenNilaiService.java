package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.KomponenNilai;
import com.rpl.project_sista.model.entity.Semester;
import com.rpl.project_sista.model.enums.TipePenilai;
import com.rpl.project_sista.repository.KomponenNilaiRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KomponenNilaiService {
	@Autowired
	private KomponenNilaiRepository komponenNilaiRepository;
 
	public List<KomponenNilai> findAllKomponenNilai() {
		return komponenNilaiRepository.findAll();
	}
	
	public Optional<KomponenNilai> findKomponenNilaiById(Long id) {
		return komponenNilaiRepository.findById(id);
	}
 
	public List<KomponenNilai> findBySemester(Semester semester) {
	 	return komponenNilaiRepository.findBySemester(semester);
  	}
 
	public List<KomponenNilai> findBySemesterAndTipePenilai(Semester semester, TipePenilai tipePenilai) {
		return komponenNilaiRepository.findBySemesterAndTipePenilai(semester, tipePenilai);
 	}
 
	@Transactional
	public KomponenNilai saveKomponenNilai(KomponenNilai komponenNilai) {
		return komponenNilaiRepository.save(komponenNilai);
	}
 
	@Transactional
	public void deleteKomponenNilai(Long id) {
		komponenNilaiRepository.deleteById(id);
	}
	 
	public long count() {
		return komponenNilaiRepository.count();
   
	} 
}