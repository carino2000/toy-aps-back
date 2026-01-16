package org.example.toyaps.dto.response;

import lombok.*;
import org.example.toyaps.domain.entity.Scenario;

import java.util.List;

@Getter
@Setter
@Builder
public class ScenarioListResponse {
    private List<Item> scenarios;

    @Getter
    @Setter
    @Builder
    public static class Item{
        String id;
        String description;
        String status;
        int jobCount;
    }
}
