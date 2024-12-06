package com.rpl.project_sista.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dosen")
public class Dosen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dosen_id")
    private Long dosenId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "nip", unique = true, nullable = false)
    private String nip;

    @Column(name = "nama", nullable = false)
    private String nama;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
