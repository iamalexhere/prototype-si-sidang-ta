package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.Semester;
import com.rpl.project_sista.repository.SemesterRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SemesterService {

    @Autowired
    private SemesterRepository semesterRepository;

    public List < Semester > findAllSemester() {
        return semesterRepository.findAll();
    }

    public Optional < Semester > findSemesterById(Long id) {
        return semesterRepository.findById(id);
    }

    public Optional < Semester > findActiveSemester() {
        return semesterRepository.findActiveSemester();
    }

    @Transactional
    public Semester saveSemester(Semester semester) {
        return semesterRepository.save(semester);
    }

    @Transactional
    public void deleteSemester(Long id) {
        semesterRepository.deleteById(id);
    }

}