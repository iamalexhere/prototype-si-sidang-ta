package com.rpl.project_sista.CatatanSidang;

import java.time.LocalDateTime;

public class Catatan {
    private Integer catatanId;
    private Integer sidangId;
    private Integer dosenId;
    private String isiCatatan;
    private LocalDateTime createdAt;

    // Constructors
    public Catatan() {}

    public Catatan(Integer sidangId, Integer dosenId, String isiCatatan) {
        this.sidangId = sidangId;
        this.dosenId = dosenId;
        this.isiCatatan = isiCatatan;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getCatatanId() {
        return catatanId;
    }

    public void setCatatanId(Integer catatanId) {
        this.catatanId = catatanId;
    }

    public Integer getSidangId() {
        return sidangId;
    }

    public void setSidangId(Integer sidangId) {
        this.sidangId = sidangId;
    }

    public Integer getDosenId() {
        return dosenId;
    }

    public void setDosenId(Integer dosenId) {
        this.dosenId = dosenId;
    }

    public String getIsiCatatan() {
        return isiCatatan;
    }

    public void setIsiCatatan(String isiCatatan) {
        this.isiCatatan = isiCatatan;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
