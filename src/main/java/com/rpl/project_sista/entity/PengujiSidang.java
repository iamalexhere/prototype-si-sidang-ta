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
@Table(name = "penguji_sidang")
@IdClass(PengujiSidangId.class)
public class PengujiSidang {
    @Id
    @ManyToOne
    @JoinColumn(name = "sidang_id", nullable = false)
    private Sidang sidang;

    @Id
    @ManyToOne
    @JoinColumn(name = "dosen_id", nullable = false)
    private Dosen dosen;

    @Enumerated(EnumType.STRING)
    @Column(name = "peran_penguji", nullable = false)
    private PeranPenguji peran;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }
}
