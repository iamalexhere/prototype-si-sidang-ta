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
@Table(name = "sidang")
public class Sidang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sidang_id")
    private Long sidangId;

    @OneToOne
    @JoinColumn(name = "ta_id", nullable = false, unique = true)
    private TugasAkhir tugasAkhir;

    @Column(name = "jadwal", nullable = false)
    private LocalDateTime jadwal;

    @Column(name = "ruangan", nullable = false, length = 50)
    private String ruangan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_sidang")
    private StatusSidang statusSidang = StatusSidang.terjadwal;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
