package com.rpl.project_sista.kta.dosen;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dosen {
    private Integer dosenId;
    private String nip;
    private String nama;
    private Integer userId; // Reference to the user
    // Removed email field
}
