package org.example.toyaps.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Job {
    @Id
    private String id;
    private String name;
    private String description;
    private boolean active;

    @OneToMany(mappedBy = "job")
    private List<Task> tasks;
}
