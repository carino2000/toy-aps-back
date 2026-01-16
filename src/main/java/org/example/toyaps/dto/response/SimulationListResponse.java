package org.example.toyaps.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.toyaps.domain.entity.ScenarioSchedule;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class SimulationListResponse {

    List<Item> scenarioItems;

    @Getter
    @Setter
    @Builder
    public static class Item {
        private long id;
        private String taskId;
        private String taskName;
        private int taskSeq;
        private String taskDescription;
        private String jobId;
        private String jobName;
        private String jobDescription;
        private String toolId;
        private String toolName;
        private LocalDateTime startAt;
        private LocalDateTime endAt;

        public static Item fromEntity(ScenarioSchedule s) {
            return Item.builder()
                    .id(s.getId())
                    .taskId(s.getTask().getId())
                    .taskName(s.getTask().getName())
                    .taskSeq(s.getTask().getSeq())
                    .taskDescription(s.getTask().getDescription())
                    .jobId(s.getTask().getJob().getId())
                    .jobName(s.getTask().getJob().getName())
                    .jobDescription(s.getTask().getJob().getDescription())
                    .toolId(s.getTask().getTool() == null ? "" : s.getTask().getTool().getId())
                    .toolName(s.getTask().getTool() == null ? "" : s.getTask().getTool().getName())
                    .startAt(s.getStartAt())
                    .endAt(s.getEndAt())
                    .build();
        }
    }
}
