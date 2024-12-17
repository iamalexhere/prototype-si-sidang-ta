package com.rpl.project_sista.dto;

public class KomponenNilaiDTO {
    private int komponenId;
    private float nilai;
    private String namaKomponen;
    private String namaDosen;
    private String tipePenilai;

    public KomponenNilaiDTO(int komponenId, float nilai, String namaKomponen, String namaDosen, String tipePenilai) {
        this.komponenId = komponenId;
        this.nilai = nilai;
        this.namaKomponen = namaKomponen;
        this.namaDosen = namaDosen;
        this.tipePenilai = tipePenilai;
    }

    // Getters
    public int getKomponenId() {
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
    public void setKomponenId(int komponenId) {
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
}
