package com.rpl.project_sista.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persetujuan_bap")
@IdClass(PersetujuanBAPId.class)
public class PersetujuanBAP {
    @Id
    @ManyToOne
    @JoinColumn(name = "bap_id")
    private BAP bap;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "is_approved")
    private Boolean isApproved = false;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
}
