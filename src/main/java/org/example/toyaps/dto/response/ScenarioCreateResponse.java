package org.example.toyaps.dto.response;

import lombok.Getter;
import lombok.Setter;
import org.example.toyaps.domain.entity.Job;
import org.example.toyaps.domain.entity.Scenario;

import java.util.List;

@Getter
@Setter
public class ScenarioCreateResponse {
    Scenario scenario;
    List<Job> jobs;
}
