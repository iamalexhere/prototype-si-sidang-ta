package com.rpl.project_sista.model.entity;

import com.rpl.project_sista.model.enums.Periode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "semester", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tahun_ajaran", "periode"})
})
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Long semesterId;

    @Column(name = "tahun_ajaran", nullable = false)
    private String tahunAjaran;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Periode periode;

    @Column(name = "is_active")
    private Boolean isActive = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
