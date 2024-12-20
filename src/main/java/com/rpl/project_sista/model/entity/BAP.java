package com.rpl.project_sista.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bap")
public class BAP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bap_id")
    private Long bapId;

    @OneToOne
    @JoinColumn(name = "sidang_id", nullable = false, unique = true)
    private Sidang sidang;

    @Column(name = "catatan_tambahan")
    private String catatanTambahan;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "bap", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<PersetujuanBAP> persetujuanSet;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
