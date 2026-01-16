package org.example.toyaps.dto.response;

import jakarta.persistence.Id;
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
public class TaskParseResponse {
    List<Item> items;

    @Getter
    @Setter
    @Builder
    public static class Item{
        private String id;
        private String jobId;
        private String toolId;
        private int seq;
        private String name;
        private String description;
        private int duration;
    }
}
