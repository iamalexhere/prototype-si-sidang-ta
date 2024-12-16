package com.rpl.project_sista.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KomponenNilaiDTO {
    private Long id;
    private int komponenId;
    private String namaKomponen;
    private Float bobot;
    private Double nilai;
}
