package com.rpl.project_sista.model.entity;

import com.rpl.project_sista.model.enums.StatusSidang;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate jadwalTanggal;

    @Transient
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime jadwalWaktu;

    @Transient
    private Integer penguji1;

    @Transient
    private Integer penguji2;

    public void setJadwalTanggal(LocalDate jadwalTanggal) {
        this.jadwalTanggal = jadwalTanggal;
        updateJadwal();
    }

    public void setJadwalWaktu(LocalTime jadwalWaktu) {
        this.jadwalWaktu = jadwalWaktu;
        updateJadwal();
    }

    private void updateJadwal() {
        if (jadwalTanggal != null && jadwalWaktu != null) {
            this.jadwal = LocalDateTime.of(jadwalTanggal, jadwalWaktu);
        }
    }

    public LocalDate getJadwalTanggal() {
        return jadwal != null ? jadwal.toLocalDate() : null;
    }

    public LocalTime getJadwalWaktu() {
        return jadwal != null ? jadwal.toLocalTime() : null;
    }

    public void setPenguji1(Integer penguji1Id) {
        this.penguji1 = penguji1Id;
    }

    public void setPenguji2(Integer penguji2Id) {
        this.penguji2 = penguji2Id;
    }

    public Integer getPenguji1() {
        return this.penguji1;
    }

    public Integer getPenguji2() {
        return this.penguji2;
    }

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

    public void setTaId(int int1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setTaId'");
    }
}
