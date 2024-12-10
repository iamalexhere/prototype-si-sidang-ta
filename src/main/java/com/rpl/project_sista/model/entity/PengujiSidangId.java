package com.rpl.project_sista.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PengujiSidangId implements Serializable {
    private Long sidang;
    private Long dosen;
}
