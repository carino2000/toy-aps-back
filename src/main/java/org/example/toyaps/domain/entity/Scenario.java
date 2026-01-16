package org.example.toyaps.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Scenario {
    @Id
    private String id;
    private String description;
    private String status;
    private LocalDateTime scheduleAt;
    private Integer makespan;

    @OneToMany(mappedBy = "scenario")
    private List<ScenarioJob> scenarioJobs;

    @PrePersist
    public void prePersist() {
        // 8-4-4-4-12
        this.id = UUID.randomUUID().toString().split("-")[4].toUpperCase();
        this.status = "READY";
        this.makespan = 0;
    }
}
