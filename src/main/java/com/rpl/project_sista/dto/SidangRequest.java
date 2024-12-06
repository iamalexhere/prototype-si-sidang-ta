package com.rpl.project_sista.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Future;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SidangRequest {
    @NotNull(message = "Mahasiswa harus dipilih")
    private Long mahasiswaId;

    @NotNull(message = "Tanggal sidang harus diisi")
    @Future(message = "Tanggal sidang harus di masa depan")
    private LocalDate tanggalSidang;

    @NotNull(message = "Waktu sidang harus diisi")
    private LocalTime waktuSidang;

    @NotBlank(message = "Ruangan harus diisi")
    private String ruangan;

    @NotNull(message = "Penguji 1 harus dipilih")
    private Long penguji1Id;

    @NotNull(message = "Penguji 2 harus dipilih")
    private Long penguji2Id;
}
