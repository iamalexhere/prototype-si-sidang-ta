package com.rpl.project_sista.kelolaTA;

import lombok.Data;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

@Data
public class Peserta {
    @NotNull
    @Size(min=2, max=100)
    private final String nama;
}
