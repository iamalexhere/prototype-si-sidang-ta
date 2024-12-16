package com.rpl.project_sista.dto;

public class KomponenNilaiDTO {
    private int komponenId;
    private double nilai;

    public KomponenNilaiDTO(int komponenId, double nilai) {
        this.komponenId = komponenId;
        this.nilai = nilai;
    }

    public int getKomponenId() {
        return komponenId;
    }

    public double getNilai() {
        return nilai;
    }
}
