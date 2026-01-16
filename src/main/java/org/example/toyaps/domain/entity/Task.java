package org.example.toyaps.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {
    @Id
    private String id;
    @ManyToOne
    @JsonIgnore
    private Job job;
    @ManyToOne
    private Tool tool;
    private int seq;
    private String name;
    private String description;
    private int duration;
}
