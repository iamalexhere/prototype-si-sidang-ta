package com.rpl.project_sista.kta.dosen;

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
@Table(name = "dosen")
@PrimaryKeyJoinColumn(name = "user_id")
public class Dosen extends Users {
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
}
