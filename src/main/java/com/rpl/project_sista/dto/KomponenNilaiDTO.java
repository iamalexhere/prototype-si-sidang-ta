package com.rpl.project_sista.dto;

public class KomponenNilaiDTO {
    private Long komponenId;
    private float nilai;
    private String namaKomponen;
    private String namaDosen;
    private String tipePenilai;

    public KomponenNilaiDTO(Long komponenId, float nilai, String namaKomponen, String namaDosen, String tipePenilai) {
        this.komponenId = komponenId;
        this.nilai = nilai;
        this.namaKomponen = namaKomponen;
        this.namaDosen = namaDosen;
        this.tipePenilai = tipePenilai;
    }

    // Getters
    public Long getKomponenId() {
        return komponenId;
    }

    public float getNilai() {
        return nilai;
    }

    public String getNamaKomponen() {
        return namaKomponen;
    }

    public String getNamaDosen() {
        return namaDosen;
    }

    public String getTipePenilai() {
        return tipePenilai;
    }

    // Setters
    public void setKomponenId(Long komponenId) {
        this.komponenId = komponenId;
    }

    public void setNilai(float nilai) {
        this.nilai = nilai;
    }

    public void setNamaKomponen(String namaKomponen) {
        this.namaKomponen = namaKomponen;
    }

    public void setNamaDosen(String namaDosen) {
        this.namaDosen = namaDosen;
    }

    public void setTipePenilai(String tipePenilai) {
        this.tipePenilai = tipePenilai;
    }

    public void setTipePenilai(Integer tipePenilai) {
        this.tipePenilai = String.valueOf(tipePenilai);
    }

    public void setTipePenilai(Boolean tipePenilai) {
        this.tipePenilai = String.valueOf(tipePenilai);
    }
}
