package com.rpl.project_sista.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@jakarta.persistence.Entity
@Table(name = "mahasiswa")
public class Mahasiswa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mahasiswa_id")
    private Long mahasiswaId;

    @Column(name = "npm", unique = true, nullable = false, length = 20)
    private String npm;

    @Column(name = "nama", nullable = false, length = 100)
    private String nama;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "status_ta")
    @Enumerated(EnumType.STRING)
    private StatusTA statusTa;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ToString.Exclude
    @OneToOne(mappedBy = "mahasiswa", cascade = CascadeType.ALL)
    private TugasAkhir tugasAkhir;

    public Mahasiswa() {
        this.createdAt = LocalDateTime.now();
    }

    public Mahasiswa(String npm, String nama, Long userId) {
        this.npm = npm;
        this.nama = nama;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }
}
