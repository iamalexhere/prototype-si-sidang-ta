package com.rpl.project_sista.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DosenCreateRequest {
    @NotBlank(message = "NIP tidak boleh kosong")
    @Pattern(regexp = "\\d{18}", message = "NIP harus terdiri dari 18 digit angka")
    private String nip;

    @NotBlank(message = "Nama tidak boleh kosong")
    @Size(min = 3, max = 100, message = "Nama harus antara 3-100 karakter")
    private String nama;

    @NotBlank(message = "Email tidak boleh kosong")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Email tidak valid")
    private String email;

    @NotBlank(message = "Username tidak boleh kosong")
    @Size(min = 3, max = 50, message = "Username harus antara 3-50 karakter")
    private String username;

    @NotBlank(message = "Password tidak boleh kosong")
    @Size(min = 6, message = "Password minimal 6 karakter")
    private String password;
}
