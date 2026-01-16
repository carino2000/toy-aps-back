package org.example.toyaps.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.toyaps.domain.entity.Job;
import org.example.toyaps.domain.entity.Tool;

import java.util.List;

@Getter
@Setter
@Builder
public class TaskResponse {
    List<?> tasks;

    @Getter
    @Setter
    @Builder
    public static class Item {
        private String id;
        private String jobId;
        private Tool tool;
        private int seq;
        private String name;
        private String description;
        private int duration;
    }
}
