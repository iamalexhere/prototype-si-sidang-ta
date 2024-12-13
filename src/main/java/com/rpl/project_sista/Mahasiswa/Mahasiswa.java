package com.rpl.project_sista.Mahasiswa;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import lombok.Data;

@Data
public class Mahasiswa {
    private final String nama;
    private final String tempat;
    private final String tanggalSidang;
    private final Time jamSidang;
    private final String dosenPenguji1;
    private final String emailPenguji1;
    private final String dosenPenguji2;
    private final String emailPenguji2;
    private final String judul_ta;
}
