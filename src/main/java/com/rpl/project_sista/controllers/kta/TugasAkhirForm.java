package com.rpl.project_sista.controllers.kta;

import com.rpl.project_sista.model.enums.StatusTA;
import com.rpl.project_sista.model.enums.JenisTA;

public class TugasAkhirForm {
    private Integer tugasAkhirId;
    private Integer mahasiswaId;
    private Integer semesterId;
    private String judul;
    private String topik;
    private JenisTA jenisTA;
    private StatusTA status;
    private Integer pembimbing1Id;
    private Integer pembimbing2Id;

    // Getters and Setters
    public Integer getTugasAkhirId() {
        return tugasAkhirId;
    }

    public void setTugasAkhirId(Integer tugasAkhirId) {
        this.tugasAkhirId = tugasAkhirId;
    }

    public Integer getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(Integer mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
    }

    public Integer getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getTopik() {
        return topik;
    }

    public void setTopik(String topik) {
        this.topik = topik;
    }

    public JenisTA getJenisTA() {
        return jenisTA;
    }

    public void setJenisTA(JenisTA jenisTA) {
        this.jenisTA = jenisTA;
    }

    public StatusTA getStatus() {
        return status;
    }

    public void setStatus(StatusTA status) {
        this.status = status;
    }

    public Integer getPembimbing1Id() {
        return pembimbing1Id;
    }

    public void setPembimbing1Id(Integer pembimbing1Id) {
        this.pembimbing1Id = pembimbing1Id;
    }

    public Integer getPembimbing2Id() {
        return pembimbing2Id;
    }

    public void setPembimbing2Id(Integer pembimbing2Id) {
        this.pembimbing2Id = pembimbing2Id;
    }
}
