package com.rpl.project_sista.kta.mahasiswa;

import com.rpl.project_sista.kta.users.Users;
import com.rpl.project_sista.kta.users.UserRole;
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
}
