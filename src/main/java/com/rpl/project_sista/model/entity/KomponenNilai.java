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

    @Column(name = "tugas_akhir_id")
    private Long tugasAkhirId;

    @Column(nullable = false)
    private Double nilai;

    private String catatan;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getKomponenId() {
        return komponenId;
    }

    public void setKomponenId(Long komponenId) {
        this.komponenId = komponenId;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public String getNamaKomponen() {
        return namaKomponen;
    }

    public void setNamaKomponen(String namaKomponen) {
        this.namaKomponen = namaKomponen;
    }

    public Float getBobot() {
        return bobot;
    }

    public void setBobot(Float bobot) {
        this.bobot = bobot;
    }

    public TipePenilai getTipePenilai() {
        return tipePenilai;
    }

    public void setTipePenilai(TipePenilai tipePenilai) {
        this.tipePenilai = tipePenilai;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getTugasAkhirId() {
        return tugasAkhirId;
    }

    public void setTugasAkhirId(Long tugasAkhirId) {
        this.tugasAkhirId = tugasAkhirId;
    }

    public Double getNilai() {
        return nilai;
    }

    public void setNilai(Double nilai) {
        this.nilai = nilai;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
}
