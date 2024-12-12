package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.BAP;
import com.rpl.project_sista.model.entity.Sidang;
import com.rpl.project_sista.model.entity.Users;
import com.rpl.project_sista.repository.BAPRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class BAPService {

    @Autowired
    private BAPRepository bapRepository;

    public List < BAP > findAllBAP() {
        return bapRepository.findAll();
    }

    public Optional < BAP > findBAPById(Long id) {
        return bapRepository.findById(id);
    }

    public Optional < BAP > findBySidang(Sidang sidang) {
        return bapRepository.findBySidang(sidang);
    }

    @Transactional
    public BAP saveBAP(BAP bap) {
        return bapRepository.save(bap);
    }

    @Transactional
    public void deleteBAP(Long id) {
        bapRepository.deleteById(id);
    }
    public Set < Users > findRequiredApprovers(BAP bap) {
        return bapRepository.findRequiredApprovers(bap);
    }

    public Set < Users > findApprovers(BAP bap) {
        return bapRepository.findApprovers(bap);
    }

    public boolean isFullyApproved(BAP bap) {
        return bapRepository.isFullyApproved(bap);
    }

    public void addApprover(BAP bap, Users user) {
        bapRepository.addApprover(bap, user);
    }

    public void removeApprover(BAP bap, Users user) {
        bapRepository.removeApprover(bap, user);
    }
}