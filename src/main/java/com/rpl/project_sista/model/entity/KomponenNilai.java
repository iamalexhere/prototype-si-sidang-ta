package com.rpl.project_sista.model.entity;

import com.rpl.project_sista.model.enums.TipePenilai;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "komponen_nilai")
public class KomponenNilai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "komponen_id")
    private Long komponenId;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    @Column(name = "nama_komponen", nullable = false)
    private String namaKomponen;

    @Column(nullable = false)
    private Float bobot;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipe_penilai", nullable = false)
    private TipePenilai tipePenilai;

    private String deskripsi;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
