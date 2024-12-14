package com.rpl.project_sista.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.rpl.project_sista.model.entity.Users;
import com.rpl.project_sista.repository.LoginRepository;
import com.rpl.project_sista.config.SecurityConfig;

@Service
public class LoginService {
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users login(String email, String password, BindingResult bindingResult) {
        List<Users> userInfo = this.loginRepository.findUserByEmail(email);
        if(userInfo.isEmpty()) {
            bindingResult.rejectValue(
                "email",
                "EmailNotFound",
                "Email tidak terdaftar!"
            );
            return null;
        }else {
            if(passwordEncoder.matches(password, userInfo.getFirst().getPasswordHash())) {
                return userInfo.getFirst();
            }else {
                bindingResult.rejectValue(
                "passwordHash",
                "PasswordMisMatch",
                "Password salah!"
                );
                return null;
            }
        }
    }

    public int findMhsId(int userId) {
        return this.loginRepository.findMhsId(userId);
    }

    public int findDosenId(int userId) {
        return this.loginRepository.findDosenId(userId);
    }
}
