package org.example.toyaps.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.example.toyaps.domain.entity.Scenario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ScenarioCreateRequest {
    String description;
    LocalDateTime scheduleAt;
    List<String> jobIds;

    public Scenario toScenario() {
        Scenario scenario = new Scenario();
        scenario.setDescription(this.description);
        scenario.setScheduleAt(this.scheduleAt);
        return scenario;
    }
}
