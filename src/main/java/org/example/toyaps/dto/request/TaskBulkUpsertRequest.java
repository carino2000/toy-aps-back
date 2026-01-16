package org.example.toyaps.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TaskBulkUpsertRequest {

    private List<TaskUpsertItem> tasks;


    @Getter
    @Setter
    public static class TaskUpsertItem{
        private String id;
        private String jobId;
        private String toolId;
        private int seq;
        private String name;
        private String description;
        private int duration;
    }
}
