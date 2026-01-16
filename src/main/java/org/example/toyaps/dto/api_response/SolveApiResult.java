package org.example.toyaps.dto.api_response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.example.toyaps.domain.entity.ScenarioSchedule;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SolveApiResult {
    @JsonProperty("elapsed")
    private int makespan;
    private String status;
    private List<TaskSchedule> timeline;

//    public List<ScenarioSchedule> toScenarioSchedule() {
//        List<ScenarioSchedule> scenarioSchedule = new ArrayList<>();
//
//    }

    @Getter
    @Setter
    public static class TaskSchedule{
        @JsonProperty("job_id")
        private String jobId;
        @JsonProperty("task_id")
        private String taskId;
        @JsonProperty("tool_id")
        private String toolId;
        private int start;
        private int end;
        private int duration;
    }
}
