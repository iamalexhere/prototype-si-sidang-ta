package com.rpl.project_sista.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nilai_sidang", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"sidang_id", "komponen_id", "dosen_id"})
})
public class NilaiSidang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nilai_id")
    private Long nilaiId;

    @ManyToOne
    @JoinColumn(name = "sidang_id", nullable = false)
    private Sidang sidang;

    @ManyToOne
    @JoinColumn(name = "komponen_id", nullable = false)
    private KomponenNilai komponenNilai;

    @ManyToOne
    @JoinColumn(name = "dosen_id", nullable = false)
    private Dosen dosen;

    @Column(nullable = false)
    private Float nilai;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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
