package org.example.toyaps.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ToolBulkUpsertRequest {

    private List<ToolUpsertItem> tools;


    @Getter
    @Setter
    public static class ToolUpsertItem{
        String id;
        String name;
        String description;
    }
}
