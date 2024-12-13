package com.rpl.project_sista;

import lombok.Data;

@Data
public class Bap {
    private final String semester;
    private final String tahunAkademik;
    private final String npm;
    private final String mahasiswa;
    private final String topik;
    private final String pembimbing;
    private final String penguji1;
    private final String penguji2;
    private final double bobotPenguji;
    private final double bobotPembimbing;
    private final double bobotKoor = 10.0;
    private final double nilaiPenguji1;
    private final double nilaiAkhirPenguji1;
    private final double nilaiPenguji2;
    private final double nilaiAkhirPenguji2;
    private final double nilaiPembimbing;
    private final double nilaiAkhirPembimbing;
    private final double nilaiKoor = 100.0;
    private final double nilaiAkhirKoor;
    private final double bobotTotal = 100.0;
    private final double nilaiAkhirTotal;
    private final String date;
}
