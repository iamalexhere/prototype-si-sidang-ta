package com.rpl.project_sista.model.entity;

import com.rpl.project_sista.model.enums.PeranPenguji;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @JoinColumn(name = "sidang_id")
    private Sidang sidang;

    @Id
    @ManyToOne
    @JoinColumn(name = "dosen_id")
    private Dosen dosen;

    @Enumerated(EnumType.STRING)
    @Column(name = "peran_penguji", nullable = false)
    private PeranPenguji peranPenguji;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @PrePersist
    protected void onCreate() {
        assignedAt = LocalDateTime.now();
    }
}
