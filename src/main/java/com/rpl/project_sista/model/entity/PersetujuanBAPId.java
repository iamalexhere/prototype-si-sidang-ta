package com.rpl.project_sista.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersetujuanBAPId implements Serializable {
    private Long bap;
    private Long user;
}
