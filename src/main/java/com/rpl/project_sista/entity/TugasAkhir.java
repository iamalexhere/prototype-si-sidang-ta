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
@Table(name = "tugas_akhir")
public class TugasAkhir {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ta_id")
    private Long taId;

    @ToString.Exclude
    @OneToOne
    @JoinColumn(name = "mahasiswa_id", nullable = false)
    private Mahasiswa mahasiswa;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(name = "judul", nullable = false)
    private String judul;

    @Column(name = "topik", nullable = false)
    private String topik;

    @Column(name = "jenis_ta", nullable = false)
    @Enumerated(EnumType.STRING)
    private JenisTA jenisTA;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusTA status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
        name = "pembimbing_ta",
        joinColumns = @JoinColumn(name = "ta_id"),
        inverseJoinColumns = @JoinColumn(name = "dosen_id")
    )
    private Set<Dosen> pembimbing;

    public TugasAkhir() {
        this.createdAt = LocalDateTime.now();
    }
}
