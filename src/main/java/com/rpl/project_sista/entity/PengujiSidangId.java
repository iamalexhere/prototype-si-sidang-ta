package com.rpl.project_sista.entity;

import java.io.Serializable;
import java.util.Objects;

public class PengujiSidangId implements Serializable {
    private Long sidang;
    private Long dosen;

    public PengujiSidangId() {}

    public PengujiSidangId(Long sidang, Long dosen) {
        this.sidang = sidang;
        this.dosen = dosen;
    }

    // Getters and Setters
    public Long getSidang() { return sidang; }
    public void setSidang(Long sidang) { this.sidang = sidang; }
    public Long getDosen() { return dosen; }
    public void setDosen(Long dosen) { this.dosen = dosen; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PengujiSidangId)) return false;
        PengujiSidangId that = (PengujiSidangId) o;
        return Objects.equals(sidang, that.sidang) && Objects.equals(dosen, that.dosen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sidang, dosen);
    }
}
