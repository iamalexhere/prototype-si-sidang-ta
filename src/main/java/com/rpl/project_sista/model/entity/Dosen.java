package com.rpl.project_sista.model.entity;

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
@Table(name = "dosen")
@PrimaryKeyJoinColumn(name = "user_id")
public class Dosen extends Users {
    
    @Column(name = "dosen_id")
    private Integer dosenId;

    @Column(unique = true, nullable = false)
    private String nip;
    
    @Column(nullable = false)
    private String nama;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Constructor for existing code compatibility
    public Dosen(Integer userId, String nip, String nama) {
        super(userId, null, null, null, UserRole.dosen, LocalDateTime.now(), true);
        this.nip = nip;
        this.nama = nama;
        this.createdAt = LocalDateTime.now();
    }

    // Constructor for new user creation
    public Dosen(String nip, String nama, String username, String email, String passwordHash) {
        super(null, username, email, passwordHash, UserRole.dosen, LocalDateTime.now(), true);
        this.nip = nip;
        this.nama = nama;
        this.createdAt = LocalDateTime.now();
    }

    public Integer getDosenId() {
        return dosenId;
    }

    public void setDosenId(Integer dosenId) {
        this.dosenId = dosenId;
    }

    // Additional getter and setter methods
    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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
