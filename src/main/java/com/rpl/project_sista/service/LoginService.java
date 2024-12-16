package com.rpl.project_sista.service;

import com.rpl.project_sista.model.entity.Users;
import com.rpl.project_sista.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;

@Service
public class LoginService {

    @Autowired
    private UsersRepository usersRepository;

    public Users authenticate(String email, String password) {
        Users user = usersRepository.findByEmail(email);
        
        if (user != null && BCrypt.checkpw(password, user.getPasswordHash())) {
            return user;
        }
        
        return null;
    }
}
