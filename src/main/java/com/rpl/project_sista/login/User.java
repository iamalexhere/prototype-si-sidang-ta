package com.rpl.project_sista.login;

import lombok.Data;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

@Data
public class User {
    
    @NotNull
    @Size(min=2, max=100)
    private final String email;

    @NotNull
    @Size(min=2, max=255)
    private final String password;
}
