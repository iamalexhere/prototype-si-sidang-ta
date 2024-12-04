package com.rpl.project_sista.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@ToString
@jakarta.persistence.Entity
@Table(name = "dosen")
public class Dosen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dosen_id")
    private Long dosenId;

    @Column(name = "nip", unique = true, nullable = false, length = 20)
    private String nip;

    @Column(name = "nama", nullable = false, length = 100)
    private String nama;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ToString.Exclude
    @ManyToMany(mappedBy = "pembimbing")
    private Set<TugasAkhir> tugasAkhirBimbingan;

    // Constructor with required fields
    public Dosen(String nip, String nama, Long userId) {
        this.nip = nip;
        this.nama = nama;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }

    // Default constructor
    public Dosen() {
        this.createdAt = LocalDateTime.now();
    }
}
