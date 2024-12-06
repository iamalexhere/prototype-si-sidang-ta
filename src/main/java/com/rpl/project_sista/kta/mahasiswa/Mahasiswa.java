package com.rpl.project_sista.kta.mahasiswa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mahasiswa {
    private Integer mahasiswaId;
    private String npm;
    private String nama;
    private Integer userId; // Reference to the user
    private String statusTa; // Status of the thesis
}
