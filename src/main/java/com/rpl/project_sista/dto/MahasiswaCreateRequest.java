package com.rpl.project_sista.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Data
public class MahasiswaCreateRequest {
    @NotBlank(message = "NPM tidak boleh kosong")
    @Size(min = 10, max = 20, message = "NPM harus antara 10 dan 20 karakter")
    private String npm;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(max = 100, message = "Nama tidak boleh lebih dari 100 karakter")
    private String nama;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    private String email;

    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 3, max = 50, message = "Username harus antara 3 dan 50 karakter")
    private String username;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    private String password;
}
