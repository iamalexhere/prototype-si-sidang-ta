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
@Table(name = "catatan_revisi")
public class CatatanRevisi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "catatan_id")
    private Long catatanId;

    @ManyToOne
    @JoinColumn(name = "sidang_id", nullable = false)
    private Sidang sidang;

    @ManyToOne
    @JoinColumn(name = "dosen_id", nullable = false)
    private Dosen dosen;

    @Column(name = "isi_catatan", nullable = false)
    private String isiCatatan;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}