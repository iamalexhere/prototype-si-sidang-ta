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
@Table(name = "tugas_akhir")
public class TugasAkhir {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ta_id")
    private Long taId;

    @ManyToOne
    @JoinColumn(name = "mahasiswa_id", nullable = false)
    private Mahasiswa mahasiswa;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(name = "judul", nullable = false, columnDefinition = "TEXT")
    private String judul;

    @Column(name = "topik", nullable = false)
    private String topik;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_ta", nullable = false)
    private JenisTA jenisTA;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusTA status = StatusTA.draft;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
