package com.rpl.project_sista.model.entity;

import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.model.enums.UserRole;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "mahasiswa")
@PrimaryKeyJoinColumn(name = "user_id")
public class Mahasiswa extends Users {
    @Column(unique = true, nullable = false)
    private String npm;
    
    @Column(nullable = false)
    private String nama;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status_ta")
    private StatusTA statusTa;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructor for existing code compatibility
    public Mahasiswa(Integer userId, String npm, String nama, String statusTa) {
        super(userId, null, null, null, UserRole.mahasiswa, LocalDateTime.now(), true);
        this.npm = npm;
        this.nama = nama;
        this.statusTa = statusTa != null ? StatusTA.valueOf(statusTa) : StatusTA.draft;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for new user creation
    public Mahasiswa(String npm, String nama, StatusTA statusTa,
                    String username, String email, String passwordHash) {
        super(null, username, email, passwordHash, UserRole.mahasiswa, LocalDateTime.now(), true);
        this.npm = npm;
        this.nama = nama;
        this.statusTa = statusTa;
        this.createdAt = LocalDateTime.now();
    }

    public Integer getMahasiswaId() {
        return super.getUserId();
    }

    public void setMahasiswaId(Integer mahasiswaId) {
        super.setUserId(mahasiswaId);
    }

    // Additional getter and setter methods
    public String getNpm() {
        return npm;
    }

    public void setNpm(String npm) {
        this.npm = npm;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public StatusTA getStatusTa() {
        return statusTa;
    }

    public void setStatusTa(StatusTA statusTa) {
        this.statusTa = statusTa;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Delegate methods for Users properties
    public String getUsername() {
        return super.getUsername();
    }

    public void setUsername(String username) {
        super.setUsername(username);
    }

    public String getEmail() {
        return super.getEmail();
    }

    public void setEmail(String email) {
        super.setEmail(email);
    }

    public String getPasswordHash() {
        return super.getPasswordHash();
    }

    public void setPasswordHash(String passwordHash) {
        super.setPasswordHash(passwordHash);
    }

    public UserRole getRole() {
        return super.getRole();
    }

    public void setRole(UserRole role) {
        super.setRole(role);
    }

    public Boolean getIsActive() {
        return super.getIsActive();
    }

    public void setIsActive(Boolean isActive) {
        super.setIsActive(isActive);
    }
}
