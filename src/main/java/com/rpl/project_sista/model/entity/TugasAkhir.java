package com.rpl.project_sista.model.entity;

import com.rpl.project_sista.model.enums.JenisTA;
import com.rpl.project_sista.model.enums.StatusTA;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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

    @Column(nullable = false)
    private String judul;

    @Column(nullable = false)
    private String topik;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_ta", nullable = false)
    private JenisTA jenisTA;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusTA status = StatusTA.draft;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
        name = "pembimbing_ta",
        joinColumns = @JoinColumn(name = "ta_id"),
        inverseJoinColumns = @JoinColumn(name = "dosen_id")
    )
    private Set<Dosen> pembimbing = new HashSet<>();

    public TugasAkhir(Long taId, Mahasiswa mahasiswa, Semester semester, String judul, String topik, JenisTA jenisTA, StatusTA status, LocalDateTime createdAt) {
        this.taId = taId;
        this.mahasiswa = mahasiswa;
        this.semester = semester;
        this.judul = judul;
        this.topik = topik;
        this.jenisTA = jenisTA;
        this.status = status;
        this.createdAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Helper method to add a pembimbing
    public void addPembimbing(Dosen dosen) {
        if (this.pembimbing == null) {
            this.pembimbing = new HashSet<>();
        }
        this.pembimbing.add(dosen);
    }

    // Helper method to remove a pembimbing
    public void removePembimbing(Dosen dosen) {
        if (this.pembimbing != null) {
            this.pembimbing.remove(dosen);
        }
    }

    public Long getTaId(){
        return this.taId;
    }
}
