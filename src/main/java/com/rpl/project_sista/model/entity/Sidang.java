package com.rpl.project_sista.model.entity;

import com.rpl.project_sista.model.enums.StatusSidang;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

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

    @Column(nullable = false)
    private LocalDateTime jadwal;

    @Column(nullable = false)
    private String ruangan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_sidang")
    private StatusSidang statusSidang = StatusSidang.terjadwal;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany
    @JoinTable(
        name = "penguji_sidang",
        joinColumns = @JoinColumn(name = "sidang_id"),
        inverseJoinColumns = @JoinColumn(name = "dosen_id")
    )
    private Set<Dosen> penguji;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
