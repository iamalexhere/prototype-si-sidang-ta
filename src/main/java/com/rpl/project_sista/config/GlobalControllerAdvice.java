package com.rpl.project_sista.config;

import com.rpl.project_sista.entity.UserRole;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {
    
    @ModelAttribute("userRole")
    public String getUserRole() {
        // TODO: Get actual user role from security context
        // For now, return default role
        return UserRole.mahasiswa.name();
    }
}
